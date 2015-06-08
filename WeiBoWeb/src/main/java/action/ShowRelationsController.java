package action;


import Context.RelationsContext;

import Service.SearchRelationsByApiImpl;
import Service.SearchRelationsByMRImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(value = "/showRelations")
public class ShowRelationsController {

    @Autowired
    SearchRelationsByApiImpl searchRelationsByApi;
    @Autowired
    SearchRelationsByMRImpl searchRelationsByMR;

    @RequestMapping(value = "/find")
    @ResponseBody
    public String search(){
        Map<String, String> reqMap = RelationsContext.getWeiBoPostMap();
        String name = reqMap.get("name");
        String isByMR = reqMap.get("isByMR");

        if("true".equals(isByMR)){
            return searchRelationsByMR.search(name);
        } else {
            return searchRelationsByApi.search(name);
        }
    }
}
