package Service;

import WUtils.ClassPathUtil;
import WUtils.DateUtil;
import WUtils.FileUtil;
import WUtils.ParseDataUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 15-5-18.
 */
@Service
public class SearchRelationsByMRImpl implements SearchRelations {

    private static final Logger LOGGER = Logger.getLogger(SearchRelationsByMRImpl.class);

    @Override
    public String search(String name){
        LOGGER.debug(name + "发起查询粉丝的请求...........");
        LOGGER.debug("使用HBase MapReduce查询开始于：" + DateUtil.date2String(new Date()));

        String mapReduceMainClass="SearchRelationsByName.SearchRelationsByName";
        String mapReduceJar = ClassPathUtil.getClassPath(mapReduceMainClass);
        String outputPath = "/WeiBoRelations/output";
        String searchRelationsShell = "/static/shell/searchRelationsShell.sh";
        String shellPath = ClassPathUtil.getResourcePath(searchRelationsShell);
        //String shellPath = "/home/zhuang/IdeaProjects/Com.Mvn.Hadoop/WeiBoRelations/WeiBoWeb/src/main/resources/static/shell/searchRelationsShell.sh";

        execShell(shellPath, name, mapReduceJar, mapReduceMainClass, outputPath);

        LOGGER.debug("使用HBase MapReduce查询终止于：" + DateUtil.date2String(new Date()));

        // 从HDFS上拷贝文件
        String copyFileShell = "/static/shell/copyFileFromHdfs.sh";
        String copyFilePath = ClassPathUtil.getResourcePath(copyFileShell);
        //String copyFilePath = "/home/zhuang/IdeaProjects/Com.Mvn.Hadoop/WeiBoRelations/WeiBoWeb/src/main/resources/static/shell/copyFileFromHdfs.sh";
        String filename = "part-r-00000";
        String fromPath = outputPath + "/result/" + filename;
        String destPath = FileUtil.getParent(this.getClass().getResource("/").getPath()) + "/download";
        //String destPath = "/home/zhuang/IdeaProjects/Com.Mvn.Hadoop/WeiBoRelations/WeiBoWeb/download";

        FileUtil.mkdirs(destPath);

        execShell(copyFilePath, fromPath, destPath);

        // 读取文件内容
        BufferedReader in = null;
        Set<String> relations = new HashSet<String>();
        try{
            FileInputStream fin = new FileInputStream(destPath + "/" + filename);
            in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String line = new String();
            while ((line = in.readLine()) != null){
                relations.add(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                in.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        String forceJson = ParseDataUtil.parseForceData(name, relations.iterator());
        return forceJson;
    }

    private void execShell(String shellPath, String... args){
        sureExecPermission(shellPath);

        String[] cmd = new String[args.length + 1];
        cmd[0] = shellPath;
        for(int i = 1; i <= args.length; i++){
            cmd[i] = args[i - 1];
        }

        try{
            Process p = Runtime.getRuntime().exec(cmd);
            // 阻塞当前进程
            p.waitFor();
        } catch (Exception ex){
            ex.printStackTrace();
            LOGGER.debug("执行位于" + shellPath + "处的脚本有误.....");
        }
    }

    // 确保shell文件具有执行权限
    private void sureExecPermission(String shellPath){
        String cmd = "chmod 744 " + shellPath;

        try{
            Process p = Runtime.getRuntime().exec(cmd);
            // 阻塞当前进程
            p.waitFor();
        } catch (Exception ex){
            ex.printStackTrace();
            LOGGER.debug("修改" + shellPath + "文件权限有误.....");
        }
    }
}
