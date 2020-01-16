package ${groupId}.${appShortId}.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/example", method = RequestMethod.GET)
public class ExampleController {

	@RequestMapping
	public String example() {
		return "page/example";
	}

}
