package Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

public class DirTableUtil {
    public static String buildBody(File[] files,String preURI)
    {
        StringBuffer res=new StringBuffer("");
        res.append(preBody);

        try {
            res.append('\n');
            res.append(getFileLine(preURI,"..",-1,null,true));
            for(File file : files)
            {
                res.append('\n');
                res.append(getFileLine(file,preURI));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        res.append(endBody);
        return res.toString();
    }

    private static String getFileLine(File file,String preURI) throws UnsupportedEncodingException {
        String name=encode(file.getName());
        if(file.isDirectory())
            return getFileLine(preURI,name,-1,new Date(file.lastModified()),true);
        return getFileLine(preURI,name,file.length()/1024,new Date(file.lastModified()),false);
    }
    private static String getFileLine(String preURI, String name, long size, Date modifyDate,boolean isDir)
    {
        if(name==null) return "";
        StringBuffer buffer=new StringBuffer("            ");
        buffer.append("<tr><td class=\"link\"><a href=\"");
        buffer.append(preURI);
        if(preURI.charAt(preURI.length()-1)!='/')
            buffer.append('/');
        buffer.append(name);
        if(isDir)
            buffer.append('/');
        buffer.append("\" title=\"");
        buffer.append(name);
        buffer.append("\">");
        buffer.append(name);
        if(isDir)
            buffer.append('/');
        buffer.append("</a></td><td class=\"size\">");
        if(size>=0){
            buffer.append(size);
            buffer.append("KB");
        }
        else
            buffer.append('-');
        buffer.append("</td><td class=\"date\">");
        if(modifyDate!=null)
            buffer.append(modifyDate.toString());
        else
            buffer.append('-');
        buffer.append("</td></tr>");
        return buffer.toString();
    }

    public static String encode(String s)
    {
        String afs=s;
        afs=new String(s.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        return afs;
    }

    public static void setPreBody(String preBody) {
        DirTableUtil.preBody = encode(preBody);
    }
    public static void setEndBody(String endBody) {
        DirTableUtil.endBody = encode(endBody);
    }
    //HTML
    private static String preBody;
    private static String endBody;
}
