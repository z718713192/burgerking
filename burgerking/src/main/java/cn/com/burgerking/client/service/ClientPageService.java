package cn.com.burgerking.client.service;

import java.util.List;
import java.util.Map;

public interface ClientPageService {

	List<Map<String,Object>> forBarChar(String userId, String month);

	Map<String, Object> allclients(String trim, String month);
	
	
	
}
