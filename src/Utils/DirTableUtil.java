package Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

public class DirTableUtil {
    public static String buildBody(File[] files,String preURI)
    {
        StringBuffer res=new StringBuffer("");

        res.append(preLine);
        res.append(header);
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
        res.append(endLine);

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
        try {
            afs=new String(s.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return afs;
    }

    //HTML
    private static String preLine="<!DOCTYPE html>\n" +
            "<html lang=\"en\">";
    private static String header="<head>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Download</title>\n" +
            "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">\n" +
            "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>\n" +
            "    <style>\n" +
            "        body{\n" +
            "            padding-top: 50px;\n" +
            "            font-size: 18px;\n" +
            "        }\n" +
            "        .strter{\n" +
            "            padding: 20px 15px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>";
    private static String preBody="<body>\n" +
            "<div class=\"container\">\n" +
            "    <a class=\"navbar-brand\">\n" +
            "        <span>下载站</span>\n" +
            "    </a>\n" +
            "    <table id=\"list\" class=\"table\">\n" +
            "        <thead>\n" +
            "        <tr>\n" +
            "            <th style=\"width:55%\"><a href=\"?C=N&amp;O=A\">File Name</a>&nbsp;</th>\n" +
            "            <th style=\"width:20%\"><a href=\"?C=S&amp;O=A\">File Size</a>&nbsp;</th>\n" +
            "            <th style=\"width:25%\"><a href=\"?C=M&amp;O=A\">Date</a>&nbsp;</th>\n" +
            "        </tr>\n" +
            "        </thead>\n" +
            "        <tbody>";
    private static String endBody="</tbody>\n" +
            "    </table>\n" +
            "</div>\n" +
            "</body>";
    private static String endLine="</html>";

    static {
        preBody=encode(preBody);
    }
}
