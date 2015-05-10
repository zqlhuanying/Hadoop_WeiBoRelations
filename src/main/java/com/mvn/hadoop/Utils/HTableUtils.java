package com.mvn.hadoop.Utils;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;

import java.io.IOException;

/**
 * Created by root on 15-4-28.
 */
public class HTableUtils {

    private static Configuration conf;
    private static HConnection connection;

    static {
        try{
            conf = HBaseConfiguration.create();
            connection = HConnectionManager.createConnection(conf);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static HTableInterface getTable(String tableName){
        try{
            return connection.getTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            /*try{
                connection.close();
            } catch (IOException e){
                e.printStackTrace();
            }*/
        }
    }
}
