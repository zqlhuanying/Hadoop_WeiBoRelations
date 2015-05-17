package Dao;

import CommonUtils.RandomUtils;
import Utils.HTableUtils;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by root on 15-4-28.
 */
@Repository
public class HbaseDaoImpl implements HbaseDao {

    private HTableInterface hTable;

    @Override
    public String getOrCreate(String userid){
        Get get = new Get(Bytes.toBytes(userid));
        Result result = find("users", get);
        // 若HBase中已存在，则直接返回，否则需要将数据插入HBase
        if(result != null && !result.isEmpty()) return new String(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));
        String username = RandomUtils.random();
        Put put = new Put(Bytes.toBytes(userid));
        put.add(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(username));
        save("users", put);
        return username;
    }

    @Override
    public ResultScanner scanBySingleColumnValueFilter(String tablename, String family, String qualifier, CompareFilter.CompareOp compareOp, String value){
        hTable = HTableUtils.getTable(tablename);
        FilterList filters = new FilterList();
        filters.addFilter(new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), compareOp, Bytes.toBytes(value)));
        Scan scan = new Scan();
        scan.setFilter(filters);
        try{
            return hTable.getScanner(scan);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
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

    @Override
    public ResultScanner find(String tableName, Scan scan){
        hTable = HTableUtils.getTable(tableName);
        try{
            return hTable.getScanner(scan);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
