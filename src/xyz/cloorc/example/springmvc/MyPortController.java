package xyz.cloorc.example.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "")
public class MyPortController  {

    @ResponseBody
    @RequestMapping(value="/getSearchList",method= RequestMethod.GET)
    public String searchbyid() {
        return ("hello, we r in java");
    }
}