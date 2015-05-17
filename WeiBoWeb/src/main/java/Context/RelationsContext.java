package Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 15-5-12.
 * 整个Web请求的上下文
 */
public class RelationsContext {

    private static ThreadLocal<Map<String, String>> weiBoPostMap = new ThreadLocal<Map<String, String>>();

    public static Map<String, String> getWeiBoPostMap() {
        Map<String, String> res = weiBoPostMap.get();
        if(res == null){
            return new HashMap<String, String>();
        }
        return res;
    }

    public static void setWeiBoPostMap(Map<String, String> postMap) {
        weiBoPostMap.set(postMap);
    }
}
