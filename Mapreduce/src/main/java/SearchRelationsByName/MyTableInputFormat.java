package SearchRelationsByName;

import HUtils.Common;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by root on 15-6-7.
 */
public class MyTableInputFormat extends TableInputFormat {

    @Override
    public void setConf(Configuration configuration){
        try{
            String path = configuration.get("inputPath");

            FileSystem fs = FileSystem.get(configuration);
            FSDataInputStream fin = fs.open(new Path(path));
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String rowId = in.readLine();
            in.close();

            //System.out.println("rowid1:" + rowId);

            Scan scan = Common.convertStringToScan(configuration.get("hbase.mapreduce.scan"));
            FilterList lists = new FilterList();
            lists.addFilter(new PageFilter(100));
            lists.addFilter(new PrefixFilter(Bytes.toBytes(rowId)));
            scan.setFilter(lists);

            configuration.set("hbase.mapreduce.scan", Common.convertScanToString(scan));
        } catch (IOException e){
            e.printStackTrace();
        }

        super.setConf(configuration);
    }
}
