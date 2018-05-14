package cn.com.burgerking.client.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.com.burgerking.client.dao.ClientPageDao;
import cn.com.burgerking.client.entity.ClientPageEntity;
import cn.com.burgerking.sys.dao.impl.BaseDaoImpl;

@Repository
public class ClientPageDaoImpl extends BaseDaoImpl<ClientPageEntity> implements ClientPageDao {

	/**
	 * esealFlag,pdfFlag,emailFlag,emailStatus
	 */
	public List<Map<String, Object>> forBarChar(String userId, String month) {
		StringBuffer sbf = new StringBuffer();
//		sbf.append(" select companyId, sum(emailStatus) 已回复文件数, sum(emailFlag) 已发送文件数, sum(esealFlag) 已转换文件数, count(1) 已导入 ");
		sbf.append("select companyId from sys_user_company  where userId ='").append(userId).append("'").append(" GROUP BY companyId");
		return findListMap(sbf.toString(), null, null);
	}

	@Override
	public Map<String, Object> allclients(String trim,String month) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("select sum(emailStatus) emailStatus, sum(emailFlag) emailFlag, sum(esealFlag) pdfFlag, count(1) inputCount  from client_excel_data a ");
		sbf.append("LEFT JOIN sys_company_store b on a.storeId=b.storeId where b.comStoreId='"+trim+"' and reportDate='"+month+"'");
		return findListMap(sbf.toString(), null, null).get(0);
	}

}
