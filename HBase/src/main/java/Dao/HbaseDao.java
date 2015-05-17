package Dao;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;

/**
 * Created by root on 15-4-28.
 */
public interface HbaseDao {

    public String getOrCreate(String userid);

    public ResultScanner scanBySingleColumnValueFilter(String tablename, String family, String quali, CompareFilter.CompareOp compareOp, String value);

    public void save(String tableName, Put put);

    public Result find(String tableName, Get get);

    public ResultScanner find(String tableName, Scan scan);
}
