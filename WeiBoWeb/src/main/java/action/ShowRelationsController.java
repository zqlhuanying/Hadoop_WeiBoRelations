package action;

import Beans.Edge;
import Beans.Node;
import Context.RelationsContext;
import Dao.HbaseDao;

import Utils.DateUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;


import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "/showRelations")
public class ShowRelationsController {

    @Autowired
    private HbaseDao hbaseDao;

    private static final Logger LOGGER = Logger.getLogger(ShowRelationsController.class);

    @RequestMapping(value = "/find")
    @ResponseBody
    public String search(){
        Map<String, String> reqMap = RelationsContext.getWeiBoPostMap();
        String name = reqMap.get("name");

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
        scan.setFilter(new PrefixFilter(r.getRow()));
        ResultScanner relationsRes = hbaseDao.find("followsTall", scan);
        LOGGER.debug("使用HBase API查询终止于：" + DateUtil.date2String(new Date()));

        if(relationsRes == null) return "";
        return parseData(name, relationsRes.iterator());

    }

    // 法一：转化数据
    /*private String parseData(String name, Iterator iterator){
        Map<String, List> data = new HashMap<String, List>();
        List<Map<String, String>> nodes = new ArrayList<Map<String, String>>();
        List<Map<Object, Object>> edges = new ArrayList<Map<Object, Object>>();
        Map<String, String> m = new HashMap<String, String>();
        m.put("name", name);
        nodes.add(m);
        while (iterator.hasNext()){
            Map<String, String> node = new HashMap<String, String>();
            node.put("name", Bytes.toString(((Result) iterator.next()).getValue(Bytes.toBytes("followers"), Bytes.toBytes("f"))));
            nodes.add(node);
        }

        for(int i = 0; i < nodes.size() - 1; i++){
            Map<Object, Object> edge = new LinkedHashMap<Object, Object>();
            edge.put("source", 0);
            edge.put("target", i + 1);
            edges.add(edge);
        }

        data.put("nodes", nodes);
        data.put("edges", edges);

        try{
            String json = new ObjectMapper().writeValueAsString(data);
            System.out.println(json);
            return json;
        } catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }*/

    // 法二：使用Java对象来转化数据
    private String parseData(String name, Iterator iterator){
        Map<String, List> data = new HashMap<String, List>();
        List<Object> nodes = new ArrayList<Object>();
        List<Object> edges = new ArrayList<Object>();

        Node firstNode = new Node();
        firstNode.setName(name);
        nodes.add(firstNode);
        while (iterator.hasNext()){
            Node node = new Node();
            node.setName(Bytes.toString(((Result) iterator.next()).getValue(Bytes.toBytes("followers"), Bytes.toBytes("f"))));
            nodes.add(node);
        }

        for(int i = 0; i < nodes.size() - 1; i++){
            Edge edge = new Edge();
            edge.setSource(0);
            edge.setTarget(i + 1);
            edges.add(edge);
        }

        data.put("nodes", nodes);
        data.put("edges", edges);

        try{
            String json = new ObjectMapper().writeValueAsString(data);
            System.out.println(json);
            return json;
        } catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }
}
