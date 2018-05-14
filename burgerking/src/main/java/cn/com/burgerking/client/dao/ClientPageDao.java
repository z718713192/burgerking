package cn.com.burgerking.client.dao;

import java.util.List;
import java.util.Map;

public interface ClientPageDao {

	List<Map<String,Object>> forBarChar(String userId, String month);

	Map<String, Object> allclients(String trim, String month);
}
