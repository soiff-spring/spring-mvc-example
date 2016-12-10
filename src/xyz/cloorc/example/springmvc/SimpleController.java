package xyz.cloorc.example.springmvc;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.view.RedirectView;
import xyz.cloorc.example.springmvc.utils.HttpHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class SimpleController {

    @ResponseBody
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return ("hello, we r in java");
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ModelAndView redirect(@PathVariable long id) {
        return new ModelAndView(new RedirectView("https://github.com"));
    }

    @ResponseBody
    @RequestMapping(value = "/proxy", method = RequestMethod.GET)
    public String proxy(@RequestParam(value = "target") String target) {
        return HttpHelper.get(target, null, null);
    }

    @ResponseBody
    @RequestMapping(value = "/login")
    public String login(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "password", required = false) String password) {
        final Map headers = new HashMap(request.getParameterMap());
        Enumeration enumeration = request.getHeaderNames();
        Object name;
        while (enumeration.hasMoreElements()) {
            name = enumeration.nextElement();
            headers.put(name, request.getHeader((String) name));
        }
        headers.put("username", Collections.singletonList(username));
        headers.put("password", Collections.singletonList(password));
        return headers.toString();
    }
}
