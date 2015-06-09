package WUtils;

import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Created by root on 15-5-18.
 */
public class ClassPathUtil {

    // 根据类名获取类所在的路径,className格式(包名.类名)
    public static String getClassPath(String className){
        try{
            Class<?> _class = Class.forName(className);
            return getMyPath(_class.getResource(""));
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    // 根据资源名获取资源所在的路径,resourceName格式(文件夹/文件名.后缀名)
    public static String getResourcePath(String resourceName){
        ClassPathUtil classPathUtil = new ClassPathUtil();
        return getMyPath(classPathUtil.getClass().getResource(resourceName));
    }

    private static String getMyPath(URL url){
        String path = url.getPath();

        /** 当程序依赖于自己所写好的jar包时，并一起发布到tomcat中时，getPath()获取到的值将形如
         *  file:/home/zhuang/apache-tomcat-7.0.61/webapps/WeiBoWeb/WEB-INF/lib/mapreduce-1.0-SNAPSHOT.jar!/SearchRelationsByName/，
         *  此时需要做进一步的处理
         */
        if(path.startsWith("file:")){
            path = path.substring("file:".length());
        }

        path = path.replaceAll("!.*$", "");

        try{
            return java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return path;
        }

    }


    public static void main(String[] args){
        System.out.println(ClassPathUtil.getClassPath("SearchRelationsByName.SearchRelationsByName"));
        System.out.println(ClassPathUtil.getResourcePath("/SearchRelationsByName/SearchRelationsByName.class"));
        System.out.println(ClassPathUtil.getResourcePath("/static/shell/searchRelationsShell.sh"));
        /*System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("java.library.path"));
        System.out.println(System.getProperty("java.io.tmpdir"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("user.home"));*/
    }
}
