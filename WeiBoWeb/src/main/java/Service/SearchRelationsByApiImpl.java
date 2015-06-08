package Service;

import Dao.HbaseDao;
import WUtils.DateUtil;
import WUtils.ParseDataUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by root on 15-5-18.
 */
@Service
public class SearchRelationsByApiImpl implements SearchRelations {

    @Autowired
    private HbaseDao hbaseDao;

    private static final Logger LOGGER = Logger.getLogger(SearchRelationsByApiImpl.class);

    @Override
    public String search(String name){
        LOGGER.debug(name + "发起查询粉丝的请求...........");
        LOGGER.debug("使用HBase API查询开始于：" + DateUtil.date2String(new Date()));
        ResultScanner userRes = hbaseDao.scanBySingleColumnValueFilter("users", "info", "name", CompareFilter.CompareOp.EQUAL, name);
        if(userRes == null) return "";
        Iterator<Result> iterator = userRes.iterator();
        // 若无结果集，则直接返回
        if(!iterator.hasNext()) return "";
        Result r = iterator.next();
        Scan scan = new Scan();
        //String cc = Bytes.toString(r.getRow());  //获取rowkey的正确形式
        // scan的起止键设置有误，暂时不会设置，所以采用PrefixFilter
        //scan.setStartRow(r.getRow());
        //scan.setStopRow(Bytes.toBytes(Bytes.toString((r.getRow())) + 1));
        FilterList lists = new FilterList();
        lists.addFilter(new PageFilter(100));
        lists.addFilter(new PrefixFilter(r.getRow()));
        scan.setFilter(lists);
        ResultScanner relationsRes = hbaseDao.find("followsTall", scan);
        LOGGER.debug("使用HBase API查询终止于：" + DateUtil.date2String(new Date()));

        if(relationsRes == null) return "";
        Set<String> relations = new HashSet<String>();
        Iterator<Result> iterator1 = relationsRes.iterator();
        while (iterator1.hasNext()){
            relations.add(Bytes.toString(iterator1.next().getValue(Bytes.toBytes("followers"), Bytes.toBytes("f"))));
        }
        String forceJson = ParseDataUtil.parseForceData(name, relations.iterator());

        // 关闭ResultScanner
        userRes.close();
        relationsRes.close();

        return forceJson;
    }


}
