package cn.com.burgerking.client.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.burgerking.client.dao.ClientPageDao;
import cn.com.burgerking.client.service.ClientPageService;
import cn.com.burgerking.utils.DateUtil;

@Service
@Transactional
public class ClientPageServiceImpl implements ClientPageService {

	@Autowired
	private ClientPageDao clientPageDao;
	
	@Override
	public List<Map<String, Object>> forBarChar(String userId, String month) {
		if(StringUtils.isBlank(month)){
			month = DateUtil.dateToString(new Date(), "YYYYMM");
		}else{
			month = month.replace("-", "");
		}
		
		return clientPageDao.forBarChar(userId, month);
	}

	@Override
	public Map<String, Object> allclients(String trim,String month) {
		return clientPageDao.allclients(trim,month);
	}

	
	
	
	
	
	
}
