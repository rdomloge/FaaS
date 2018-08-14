
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.faas.common.AbstractFunction;

import catholicon.CodeToBeCalledByFunction;

public class Test extends AbstractFunction<String> { 

	private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);
	
	public String call() {
		if(null == params) throw new IllegalStateException();
		
		System.out.println("Hello functional world");
		LOGGER.debug("Hello functional world using libs");
		CodeToBeCalledByFunction c = new CodeToBeCalledByFunction();
		String msg = c.createMessage();
		System.out.println("Called external lib: "+msg);
		String msgParam = params.get("msg");
		LOGGER.debug("String from lib: {}, and the 'msg' param you used: {}", msg, msgParam);
		return msg;
	}
	
}