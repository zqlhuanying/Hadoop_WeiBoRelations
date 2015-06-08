package SearchRelationsByName;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by root on 15-5-17.
 */
public class SearchRelationsByName extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception{
        try{
            // 存放中间结果的目录
            String staging = args[1] + "/staging";
            // Job : Find UserId By Name
            Configuration conf1 = getConf();
            Job job1 = Job.getInstance(conf1,"FindUserIdByName");
            job1.setJarByClass(SearchRelationsByName.class);
            job1.setNumReduceTasks(0);
            job1.setOutputFormatClass(TextOutputFormat.class);
            TextOutputFormat.setOutputPath(job1, new Path(staging));

            Scan scan1 = new Scan();
            FilterList lists = new FilterList();
            lists.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("name"),
                    CompareFilter.CompareOp.EQUAL, Bytes.toBytes(args[0])));
            lists.addFilter(new PageFilter(1));
            scan1.setFilter(lists);

            TableMapReduceUtil.initTableMapperJob("users", scan1, FindUserId.class,
                    NullWritable.class, Text.class, job1);

            //System.exit(job1.waitForCompletion(true) ? 0 : 1);
            // Job : Find Relations By Id
            Configuration conf2 = getConf();
            conf2.set("inputPath", staging + "/part-m-00000");
            Job job2 = Job.getInstance(conf2,"FindRelationsById");
            job2.setJarByClass(SearchRelationsByName.class);
            job2.setNumReduceTasks(1);

            // 这样设置自定义的TableInputFormat无效，必须在initTableMapperJob中设置
            //job2.setInputFormatClass(MyTableInputFormat.class);
            //TextInputFormat.addInputPath(job2, new Path(staging + "/part-m-00000"));
            job2.setOutputFormatClass(TextOutputFormat.class);
            TextOutputFormat.setOutputPath(job2, new Path(args[1] + "/result"));

            Scan scan2 = new Scan();
            //lists.addFilter(new PageFilter(100));
            //scan2.setFilter(new PageFilter(100));   //限制Scan行数的正确写法
            //scan2.setMaxResultSize(100);

            TableMapReduceUtil.initTableMapperJob("followsTall", scan2, FindRelations.class,
                    NullWritable.class, Text.class, job2, true, MyTableInputFormat.class);

            // ControlledJob
            ControlledJob cJob1 = new ControlledJob(job1, null);
            ControlledJob cJob2 = new ControlledJob(job2, null);
            cJob2.addDependingJob(cJob1);

            JobControl jobControl = new JobControl("SearchRelationsByName");
            jobControl.addJob(cJob1);
            jobControl.addJob(cJob2);

            // Run the job
            Thread thread = new Thread(jobControl);
            thread.start();
            while(true){
                if(jobControl.allFinished()){
                    System.out.println(jobControl.getSuccessfulJobList());
                    jobControl.stop();
                    return 0;
                }
                if(jobControl.getFailedJobList().size() > 0){
                    System.out.println(jobControl.getFailedJobList());
                    jobControl.stop();
                    return 1;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        ToolRunner.run(conf, new SearchRelationsByName(), args);
    }
}
