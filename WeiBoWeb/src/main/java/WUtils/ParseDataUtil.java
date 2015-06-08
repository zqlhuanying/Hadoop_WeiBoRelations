package WUtils;

import Beans.ForceBeans.Edge;
import Beans.ForceBeans.Node;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.*;

/**
 * Created by root on 15-5-18.
 */
public class ParseDataUtil {

    // 法一：转化数据
    /*public static String parseForceData(String name, Iterator iterator){
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

    // 法二：
    public static String parseForceData(String name, Iterator iterator){
        Map<String, List> data = new HashMap<String, List>();
        List<Object> nodes = new ArrayList<Object>();
        List<Object> edges = new ArrayList<Object>();

        Node firstNode = new Node();
        firstNode.setName(name);
        nodes.add(firstNode);
        while (iterator.hasNext()){
            Node node = new Node();
            node.setName((String)iterator.next());
            //node.setName(Bytes.toString(((Result)iterator.next()).getValue(Bytes.toBytes("followers"), Bytes.toBytes("f"))));
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
