package cn.com.burgerking.client.service;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.client.entity.ExcelData;

public interface ExcelDataService {
	
	/**
	 * 保存excel数据（文件名称、门店ID、门店名称、公司名称、创建时间、操作人）
	 * @param fileNames	文件名  用|分割
	 * @param userName 操作人
	 */
	public void saveExcelData(String fileNames, String userName) throws Exception;
	
	/**
	 * 根据文件名称、未转换PDF状态查找门店及对应印章
	 * 盖章流程：转PDF-->盖章
	 * @param fileName
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @return
	 */
	public List<ExcelData> findExcelDataByFileName(String corporateName);
	
	/**
	 * 根据文件名称查找总记录数
	 * @param fileName
	 * @return
	 */
	public Integer countExcelDataByFileName(String fileName);
	
	/**
	 * 根据报表ID、印章ID查找印章
	 * @param ids
	 * @return
	 */
	public List findEsealById(String id, String esealId);
	
	/**
	 * 根据报表ID更新状态（单条）
	 * @param id
	 * @param key
	 * @param vaule
	 */
	public boolean updateExcelDataById(String id, String key, String userName, String esealName);
	
	/**
	 * 根据月份、门店查找报表
	 * @param month
	 * @param corporateName
	 * @param key
	 * @param value
	 * @return
	 */
	public List<ExcelData> findExcelData(String month, String corporateName);
	
	public List<ExcelData> findPdfData(String month, String corporateName);
	
	public List<ExcelData> findEmailData(String month, String corporateName);
	
	/**
	 * 根据id更新ExcelData状态标识（多个ID）
	 * @param ids
	 * @param userName
	 * @return
	 */
	public boolean updateExcelDataByIds(String ids, String userName, String key);
	
	/**
	 * 根据id更新ExcelData
	 * @param id
	 * @param recEmails
	 * @param userName
	 * @return
	 */
	public boolean updateExcelDataIsEmail(String id, String recEmails, String userName);
	
	/**
	 * 查找邮件主题数据
	 * @param month
	 * @param corporateName
	 * @return
	 */
	public List<ExcelData> findExcelDataIsSubjectEmailData(String month, String corporateName);
	
	/**
	 * 查找核对数据
	 * @param month
	 * @param corporateName
	 * @return
	 */
	public List<ExcelData> findAuditData(String month, String corporateName,String storeName);
	
	/**
	 * 查找发件人
	 * @param id
	 * @return
	 */
	public Map findSendEmail(String id);

}
