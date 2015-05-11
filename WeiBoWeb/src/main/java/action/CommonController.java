package action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by root on 15-5-11.
 */
@Controller
public class CommonController {

    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }
}
