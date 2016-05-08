package xyz.cloorc.example.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.cloorc.example.springmvc.utils.HttpHelper;

@Controller
@RequestMapping(value = "")
public class SimpleController {

    @ResponseBody
    @RequestMapping(value="/hello",method= RequestMethod.GET)
    public String hello () {
        return ("hello, we r in java");
    }

    @ResponseBody
    @RequestMapping(value = "/proxy", method = RequestMethod.GET)
    public String proxy (@RequestParam(value = "target")String target) {
        return HttpHelper.get(target, null, null);
    }
}