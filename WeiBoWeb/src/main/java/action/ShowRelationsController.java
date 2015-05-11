package action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/showRelations")
public class ShowRelationsController {

    @RequestMapping(value = "/find")
    @ResponseBody
    public String search(){
        return "1";
    }
}
