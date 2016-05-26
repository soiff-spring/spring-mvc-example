package xyz.cloorc.example.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import xyz.cloorc.example.springmvc.utils.HttpHelper;

@Controller
public class SimpleController {

	@ResponseBody
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello() {
		return ("hello, we r in java");
	}

	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public ModelAndView redirect(@PathVariable long id) {
		return Redirect;
	}

	@ResponseBody
	@RequestMapping(value = "/proxy", method = RequestMethod.GET)
	public String proxy(@RequestParam(value = "target") String target) {
		return HttpHelper.get(target, null, null);
	}
}