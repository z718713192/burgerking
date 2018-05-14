package cn.com.burgerking.client.dao;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.client.entity.ExcelData;

public interface ExcelDataDao {
	
	public void saveExcelData(ExcelData excelData);
	
	public List<ExcelData> findExcelDataByFileName(String corporateName);
	
	public Integer countExcelDataByFileName(String fileName);
	
	public List findEsealById(String id, String esealId);
	
	public boolean updateExcelDataById(String id, String key, String userName, String esealName);
	
	public List<ExcelData> findExcelData(String month, String corporateName);
	
	public List<ExcelData> findPdfData(String month, String corporateName);
	
	public List<ExcelData> findEmailData(String month, String corporateName);
	
	public boolean updateExcelDataByIds(String ids, String userName, String key);
	
	public boolean updateExcelDataIsEmail(String id, String recEmails, String userName);
	
	public List<ExcelData> findExcelDataIsSubjectEmailData(String month, String corporateName);
	
	public List<ExcelData> findAuditData(String month, String corporateName,String storeName);
	
	public Map findSendEmail(String id);

}
