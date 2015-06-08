package SearchRelationsByName;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;


import java.io.IOException;

/**
 * Created by root on 15-5-17.
 */
public class FindUserId extends TableMapper<NullWritable, Text> {

    @Override
    public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException{
        Text text = new Text();
        if(value != null && !value.isEmpty()){
            text = new Text(value.getRow());
        }
        context.write(NullWritable.get(), text);
    }
}
