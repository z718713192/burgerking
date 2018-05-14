package cn.com.burgerking.client.service;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.client.entity.ExcelData;
import cn.com.burgerking.client.entity.WordData;

public interface WordDataService {
	
	/**
	 * 保存excel数据（文件名称、门店ID、门店名称、公司名称、创建时间、操作人）
	 * @param fileNames	文件名  用|分割
	 * @param userName 操作人
	 * @param month 
	 */
	public void saveWordData(String fileNames, String userName, String month);
	
	/**
	 * 根据文件名称、未转换PDF状态查找门店及对应印章
	 * 盖章流程：转PDF-->盖章
	 * @param fileName
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @return
	 */
	public List<WordData> findExcelDataByFileName(String corporateName);
	
	/**
	 * 根据文件名称查找总记录数
	 * @param fileName
	 * @return
	 */
	public Integer countWordDataByFileName(String fileName);
	
	/**
	 * 根据报表ID、印章ID查找印章
	 * @param ids
	 * @return
	 */
	public List findWsealById(String id, String esealId);
	
	/**
	 * 根据报表ID更新状态
	 * @param id
	 * @param key
	 * @param vaule
	 */
	public boolean updateWordDataById(String id, String key, String userName, String esealName);
	
	/**
	 * 根据月份、门店查找报表
	 * @param month
	 * @param corporateName
	 * @param key
	 * @param value
	 * @return
	 */
	public List<WordData> findWordData(String month, String corporateName, String key);
	
	public boolean updateWordDataByIds(String ids, String userName);

	public List findEmailData(String month, String csName);

	public List findWordDataIsSubjectEmailData(String month, String csName);

	public Map findSendEmail(String id);

	public boolean updateWordDataIsEmail(String id, String recEmails, String userName);

	public List findAuditData(String month, String csName);

}
