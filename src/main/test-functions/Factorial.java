import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import com.example.faas.common.Function;
import com.example.faas.dto.JobRequest;

public class Factorial implements Function<String> { 

	private static final Logger LOGGER = LoggerFactory.getLogger(Factorial.class);
	
	private JobRequest request;

	public String call() {
		if(null == request) throw new IllegalStateException();
		if(null == request.getParams()) throw new IllegalStateException();
	
		Map<String, String> params = request.getParams();

		int n = Integer.parseInt(params.get("value"));


		String msg = String.format("%d! = %d\n", n, myFactorial(n));

		LOGGER.info(msg);

		return msg;
	}
	
	public void setRequest(JobRequest request) {
		this.request = request;
	}

	private int myFactorial(int n) {
		int answer = 1;
		if (n >= 2) {
			for (int i = 2; i <= n; i++) {
				answer *= i;
			}
		}
		return answer;
	}
}
