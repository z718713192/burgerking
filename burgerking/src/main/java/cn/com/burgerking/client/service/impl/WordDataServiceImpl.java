package cn.com.burgerking.client.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.burgerking.client.dao.WordDataDao;
import cn.com.burgerking.client.entity.WordData;
import cn.com.burgerking.client.service.WordDataService;
import cn.com.burgerking.client.utils.PropertiesConfig;
import cn.com.burgerking.sys.dao.CompanyStoreDao;

@Service
@Transactional
public class WordDataServiceImpl implements WordDataService {
	
	public static String reportDate;
	 
	public SimpleDateFormat sad=new SimpleDateFormat("yyyyMM");
	
	@Autowired
	WordDataDao wordDataDao;
	
	@Autowired
	private CompanyStoreDao storeDao;

	@Override
	public void saveWordData(String fileNames, String userName,String month) {
		
		String[] fileName = fileNames.split("\\|");
		for (int i = 0; i < fileName.length; i++) {
			WordData ed = new WordData();
			ed.setFileName(fileName[i]);
			ed.setStoreId(fileName[i].split("_")[0]);
			ed.setStoreName(storeDao.findStoreByStoreId(fileName[i].split("_")[0]).getCsName());
			ed.setCorporateName(storeDao.findCompanyByStoreId(fileName[i].split("_")[0]).getCsName());
			ed.setReportDate(month);
			ed.setCreateTime(new Date());
			ed.setOperator(userName + "");
			wordDataDao.saveWordData(ed);
		}
		
	}

	@Override
	public List<WordData> findExcelDataByFileName(String corporateName) {
		return wordDataDao.findWordDataByFileName(corporateName);
	}

	@Override
	public Integer countWordDataByFileName(String fileName) {
		return wordDataDao.countWordDataByFileName(fileName);
	}

	@Override
	public List findWsealById(String id, String esealId) {
		return wordDataDao.findEsealById(id, esealId);
	}

	@Override
	public boolean updateWordDataById(String id, String key, String userName, String esealName) {
		return wordDataDao.updateWordDataById(id, key, userName, esealName);
	}

	@Override
	public List<WordData> findWordData(String month, String corporateName, String key) {
		return wordDataDao.findWordData(month, corporateName, key);
	}

	@Override
	public boolean updateWordDataByIds(String ids, String userName) {
		return wordDataDao.updateWordDataByIds(ids, userName);
	}

	@Override
	public List findEmailData(String month, String csName) {
		return wordDataDao.findEmailData(month,csName);
	}

	@Override
	public List findWordDataIsSubjectEmailData(String month, String csName) {
		return wordDataDao.findWordDataIsSubjectEmailData(month,csName);
	}

	@Override
	public Map findSendEmail(String id) {
		return wordDataDao.findSendEmail(id);
	}

	@Override
	public boolean updateWordDataIsEmail(String id, String recEmails, String userName) {
		return wordDataDao.updateWordDataIsEmail(id,recEmails,userName);
	}

	@Override
	public List findAuditData(String month, String csName) {
		return wordDataDao.findAuditData(month,csName);
	}


	

}
