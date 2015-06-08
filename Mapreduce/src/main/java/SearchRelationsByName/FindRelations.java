package SearchRelationsByName;

import HUtils.Common;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 15-5-17.
 */
public class FindRelations extends TableMapper<NullWritable, Text> {


    @Override
    public void setup(Context context) throws IOException, InterruptedException{

        /*Configuration conf = context.getConfiguration();
        String path = conf.get("inputPath");

        FileSystem fs = FileSystem.get(conf);
        FSDataInputStream fin = fs.open(new Path(path));
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
        String rowId = in.readLine();
        in.close();*/

        //System.out.println("rowid:" + rowId);
        //System.out.println("path:" + path);
        /*while(context.nextKeyValue()) {
            System.out.println("key:" + context.getCurrentKey());
            System.out.println("value:" + new Text(context.getCurrentValue().getRow()));
        }*/

        // 在执行setup之前，已经对table进行scan，并将值放入到Context
        // 所以在setup内修改scan值 无效
        /*Scan scan = Common.convertStringToScan(conf.get("hbase.mapreduce.scan"));
        FilterList lists = new FilterList();
        lists.addFilter(new PageFilter(100));
        lists.addFilter(new PrefixFilter(Bytes.toBytes(rowId)));
        scan.setFilter(new PrefixFilter(Bytes.toBytes(rowId)));
        //scan.setMaxResultSize(100);*/
    }

    @Override
    public void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException{
        Text text = new Text(value.getValue(Bytes.toBytes("followers"), Bytes.toBytes("f")));
        context.write(NullWritable.get(), text);
    }

    @Override
    public void cleanup(Context context) throws IOException, InterruptedException{

    }
}
