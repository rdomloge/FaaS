
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.faas.common.Function;
import com.example.faas.dto.JobRequest;

import catholicon.CodeToBeCalledByFunction;

public class Test implements Function<String> { 

	private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);
	
	private JobRequest request;
	
	private Map<String, String> config;
	

	public String call() {
		if(null == request) throw new IllegalStateException();
		if(null == request.getParams()) throw new IllegalStateException();
		
		System.out.println("Hello functional world");
		LOGGER.debug("Hello functional world using libs");
		CodeToBeCalledByFunction c = new CodeToBeCalledByFunction();
		String msg = c.createMessage();
		System.out.println("Called external lib: "+msg);
		String msgParam = request.getParams().get("msg");
		LOGGER.debug("String from lib: {}, and the 'msg' param you used: {}", msg, msgParam);
		return msg;
	}
	
	public void setRequest(JobRequest newrequest) {
		this.request = newrequest;
	}
	
	public void setConfig(Map<String, String> config) {
		this.config = config;
	}
}