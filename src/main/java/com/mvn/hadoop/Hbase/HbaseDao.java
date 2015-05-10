package com.mvn.hadoop.Hbase;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

/**
 * Created by root on 15-4-28.
 */
public interface HbaseDao {

    public String getOrCreate(String userid);

    public void save(String tableName, Put put);

    public Result find(String tableName, Get get);
}
