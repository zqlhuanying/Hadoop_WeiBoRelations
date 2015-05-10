package com.mvn.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by root on 15-4-27.
 */
public class WeiBoRelations extends Configured implements Tool{

    @Override
    public int run(String[] args) throws Exception{
        try{
            Configuration conf = getConf();
            Job job = Job.getInstance(conf,"WeiBoRelations");
            /** 不加下面这行代码，在伪分布式模式即mapreduce.framework.name='yarn'下会提示找不到Map类
             *  但是在单机模式或mapreduce.framework.name='local'模式下却是可以运行
             */
            job.setJarByClass(WeiBoRelations.class);
            job.setMapperClass(WeiBoMap.class);
            //job.setReducerClass(Reduce.class);
            job.setNumReduceTasks(0);
            job.setInputFormatClass(TextInputFormat.class);
            //job.setOutputFormatClass(TextOutputFormat.class);
            job.setOutputFormatClass(NullOutputFormat.class);
            /** 不加下面这两行代码，可能会出现类型不匹配到错误
             *
             */
            //job.setOutputKeyClass(Text.class);
            //job.setOutputValueClass(IntWritable.class);
            TextInputFormat.addInputPath(job,new Path(args[0]));
            //TextOutputFormat.setOutputPath(job,new Path(args[1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        ToolRunner.run(conf, new WeiBoRelations(), args);
    }
}
