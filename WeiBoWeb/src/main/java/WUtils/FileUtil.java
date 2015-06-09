package WUtils;

import java.io.File;

/**
 * Created by root on 15-6-8.
 */
public class FileUtil {

    public static void mkdirs(String path){
        File file = new File(path);
        if(!file.exists() || file.isFile()){
            file.mkdirs();
        }
    }

    public static String getParent(String path){
        File file = new File(path);
        return file.getParent();
    }
}
