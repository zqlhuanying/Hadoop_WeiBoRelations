package Dao;

import CommonUtils.RandomUtils;
import Utils.HTableUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by root on 15-4-28.
 */
public class HbaseDaoImpl implements HbaseDao {

    private HTableInterface hTable;

    @Override
    public String getOrCreate(String userid){
        Get get = new Get(Bytes.toBytes(userid));
        Result result = find("users", get);
        // 若HBase中已存在，则直接返回，否则需要将数据插入HBase
        if(result != null && !result.isEmpty()) return result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")).toString();
        String username = RandomUtils.random();
        Put put = new Put(Bytes.toBytes(userid));
        put.add(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(username));
        save("users", put);
        return username;
    }

    @Override
    public void save(String tableName, Put put){
        hTable = HTableUtils.getTable(tableName);
        try{
            hTable.put(put);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Result find(String tableName, Get get){
        hTable = HTableUtils.getTable(tableName);
        try{
            return hTable.get(get);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
