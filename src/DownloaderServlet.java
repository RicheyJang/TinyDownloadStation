import Utils.DirTableUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@WebServlet(name = "IndexServlet")
public class DownloaderServlet extends HttpServlet {
    private static String rootPath="/";
    private static String uriBegin="download";

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Properties prop = System.getProperties();
            String os = prop.getProperty("os.name");
            int stIndex=0;
            //判断系统是否为WINDOWS
            if (os != null && os.toLowerCase().contains("win")) {
                stIndex=1;
            }
            String path = DownloaderServlet.class.getResource("/").getPath();
            path = path.substring(stIndex, path.indexOf("classes"));
            //读取配置文件
            File file=new File(path + "configs.properties");
            System.out.println("config path: "+file.getAbsolutePath());

            Properties pps = new Properties();
            pps.load(new FileInputStream(file));
            //读取下载地址根目录
            String nowPath=pps.getProperty("rootpath");
            if(nowPath!=null && nowPath.length()>0){
                rootPath=nowPath;
                if(rootPath.endsWith("\\") || rootPath.endsWith("/"))
                    rootPath=rootPath.substring(0,rootPath.length()-1);
                System.out.println("root path : "+rootPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //转换字符集
            uriBegin=new String(uriBegin.getBytes(StandardCharsets.UTF_8),"ISO8859-1");
            rootPath=new String(rootPath.getBytes(StandardCharsets.UTF_8),"ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri=new String(request.getRequestURI().getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        int index=uri.indexOf(uriBegin);
        if(index<0){
            response.setStatus(403);
            return;
        }
        index+=uriBegin.length()+1;
        String filepath=rootPath;
        if(index<uri.length())
            filepath=rootPath+"/"+uri.substring(index);

        //获得URI文件路径
        filepath=filepath.replaceAll("\\+","%2B");
        filepath=URLDecoder.decode(filepath, StandardCharsets.UTF_8);

        File file=new File(filepath);
        String nowPath=file.getAbsolutePath();
        //判断是否为根目录以下的目录或文件
        if(!nowPath.contains(rootPath)){
            response.setStatus(403);
            return ;
        }

        File[] files=file.listFiles();
        //文件夹：显示所有文件
        if(file.isDirectory() && files!=null)
            response.getWriter().write(DirTableUtil.buildBody(files,uri));
        //文件：开启下载
        else if(file.isFile())
            download(file,response);
        //其他：404
        else
            response.setStatus(404);
    }

    private void download(File file,HttpServletResponse response)//下载
    {
        try {
            //缓冲区大小
            int maxBufLength=100*1024;
            // 取得文件名
            String filename = file.getName();
            // 取得文件的后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + DirTableUtil.encode(filename));
            response.addHeader("Content-Length", "" + file.length());

            int length=maxBufLength;
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            // 开辟缓冲区
            byte[] buffer = new byte[maxBufLength];
            while(length>0)
            {
                length=fis.read(buffer);
                if(length>0)
                {
                    toClient.write(buffer);
                    toClient.flush();
                }
            }
            fis.close();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
