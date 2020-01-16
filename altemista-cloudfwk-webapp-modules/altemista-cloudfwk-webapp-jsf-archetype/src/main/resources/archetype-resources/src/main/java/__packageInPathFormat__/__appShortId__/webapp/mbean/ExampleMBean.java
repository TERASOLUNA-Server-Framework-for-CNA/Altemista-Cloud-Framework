package ${groupId}.${appShortId}.webapp.mbean;

import java.util.Arrays;
import java.util.List;
import javax.faces.bean.SessionScoped;
import org.springframework.stereotype.Controller;

@Controller("jsfExampleMBean")
@SessionScoped
public class ExampleMBean {

	private List<String> exampleList;
	
	public ExampleMBean() {
		super();
		this.exampleList = Arrays.asList("foo", "bar", "baz");
	}

	public List<String> getExampleList() {
		return this.exampleList;
	}
}
