package LoadIntoHbase;

import Dao.HbaseDao;
import Dao.HbaseDaoImpl;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 15-4-27.
 */
public class LoadMap extends Mapper<LongWritable, Text, Text, IntWritable> {

    HbaseDao hbaseDao = new HbaseDaoImpl();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
        Map<String, Object> map = new ObjectMapper().readValue(value.toString(), Map.class);
        String follow = String.valueOf(map.get("id"));
        hbaseDao.getOrCreate(follow);

        // 遍历关注的人集合
        List<Object> followers = (ArrayList<Object>)map.get("ids");
        for(Object follower : followers){
            String strFollower = String.valueOf(follower);
            String followerName = hbaseDao.getOrCreate(strFollower);
            Put put = new Put(Bytes.toBytes(follow + strFollower));
            put.add(Bytes.toBytes("followers"), Bytes.toBytes("f"), Bytes.toBytes(followerName));
            hbaseDao.save("followsTall", put);
        }
    }
}
