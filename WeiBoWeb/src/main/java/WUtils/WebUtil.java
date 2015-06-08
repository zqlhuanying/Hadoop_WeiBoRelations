package WUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by root on 15-5-12.
 */
public final class WebUtil {

    public static Map<String, String> getRequestMap(HttpServletRequest request){

        Map<String, String> reqMap = new HashMap<String, String>();

        if(request != null){
            Set<String> paramsKey = request.getParameterMap().keySet();
            for(String paramKey : paramsKey){
                reqMap.put(paramKey, request.getParameter(paramKey));
            }
        }
        return reqMap;
    }
}
