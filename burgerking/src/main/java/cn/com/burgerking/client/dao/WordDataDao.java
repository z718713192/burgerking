package cn.com.burgerking.client.dao;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.client.entity.WordData;

public interface WordDataDao {
	
	public void saveWordData(WordData excelData);
	
	public List<WordData> findWordDataByFileName(String corporateName);
	
	public Integer countWordDataByFileName(String fileName);
	
	public List findEsealById(String id, String esealId);
	
	public boolean updateWordDataById(String id, String key, String userName, String esealName);
	
	public List<WordData> findWordData(String month, String corporateName, String key);
	
	public boolean updateWordDataByIds(String ids, String userName);

	public List findEmailData(String month, String csName);

	public List findWordDataIsSubjectEmailData(String month, String csName);

	public Map findSendEmail(String id);

	public boolean updateWordDataIsEmail(String id, String recEmails, String userName);

	public List findAuditData(String month, String csName);

}
