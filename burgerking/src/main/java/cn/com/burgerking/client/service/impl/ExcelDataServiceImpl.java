package cn.com.burgerking.client.service.impl;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.burgerking.client.dao.ExcelDataDao;
import cn.com.burgerking.client.entity.ExcelData;
import cn.com.burgerking.client.service.ExcelDataService;
import cn.com.burgerking.client.utils.AnalysisExcel;
import cn.com.burgerking.client.utils.PropertiesConfig;
import cn.com.burgerking.sys.dao.CompanyStoreDao;
import cn.com.burgerking.sys.dao.SupplierDao;
import cn.com.burgerking.sys.entity.Supplier;

@Service
@Transactional
public class ExcelDataServiceImpl implements ExcelDataService {
	
	public static String reportDate;
	
	@Autowired
	ExcelDataDao excelDataDao;
	
	@Autowired
	SupplierDao supplierDao;
	
	@Autowired
	private CompanyStoreDao storeDao;

	@Override
	public void saveExcelData(String fileNames, String userName) throws Exception {
		String uploadPath = PropertiesConfig.getProperties().getProperty("saveExcelPath");
		
		String[] fileName = fileNames.split("\\|");
		for (int i = 0; i < fileName.length; i++) {
			String filePath = uploadPath + fileName[i];
			File file = new File(filePath);
			Map map = AnalysisExcel.readExcelByStoreInfo(file);
			Set<String> keySet = map.keySet();
			Iterator<String> it = keySet.iterator();
			//获取reportDate
			List rdList =  (List) map.get("reportDate");
			reportDate = (String) rdList.get(0);
			reportDate = reportDate.replace("/", "");
			reportDate = reportDate.substring(0, 6);
			int tmp = 1;
			String corporateName = "";
			
			while (it.hasNext()) {
				String keyValue = it.next();
				if("reportDate".equals(keyValue))
					continue;
				
				List list = (List) map.get(keyValue);
				//if(tmp==1){
//					Supplier sup = supplierDao.findSupplierByStoreId(new Integer(list.get(0).toString()));
					corporateName = storeDao.findCompanyByStoreId(list.get(0).toString()).getCsName();
				//}
				ExcelData ed = new ExcelData();
				ed.setFileName(fileName[i]);
				ed.setStoreId(new Integer(list.get(0).toString()));
				ed.setStoreName(list.get(1) + "");
				ed.setCorporateName(corporateName);
				ed.setReportDate(reportDate);
				ed.setCreateTime(new Date());
				ed.setOperator(userName + "");
				excelDataDao.saveExcelData(ed);
				//tmp=2;
			}
		}
	}

	@Override
	public List<ExcelData> findExcelDataByFileName(String corporateName) {
		return excelDataDao.findExcelDataByFileName(corporateName);
	}

	@Override
	public Integer countExcelDataByFileName(String fileName) {
		return excelDataDao.countExcelDataByFileName(fileName);
	}

	public String getReportDate() {
		return reportDate;
	}

	@Override
	public List findEsealById(String id, String esealId) {
		return excelDataDao.findEsealById(id,esealId);
	}

	@Override
	public boolean updateExcelDataById(String id, String key, String userName, String esealName) {
		return excelDataDao.updateExcelDataById(id, key,userName,esealName);
	}

	@Override
	public List<ExcelData> findExcelData(String month, String corporateName) {
		return excelDataDao.findExcelData(month,corporateName);
	}
	
	@Override
	public List<ExcelData> findPdfData(String month, String corporateName) {
		return excelDataDao.findPdfData(month,corporateName);
	}
	
	@Override
	public List<ExcelData> findEmailData(String month, String corporateName) {
		return excelDataDao.findEmailData(month,corporateName);
	}

	@Override
	public boolean updateExcelDataByIds(String ids, String userName, String key) {
		return excelDataDao.updateExcelDataByIds(ids,userName,key);
	}

	@Override
	public boolean updateExcelDataIsEmail(String id, String recEmails, String userName) {
		return excelDataDao.updateExcelDataIsEmail(id, recEmails, userName);
	}

	@Override
	public List<ExcelData> findExcelDataIsSubjectEmailData(String month, String corporateName) {
		return excelDataDao.findExcelDataIsSubjectEmailData(month,corporateName);
	}

	@Override
	public List<ExcelData> findAuditData(String month, String corporateName,String storeName) {
		return excelDataDao.findAuditData(month,corporateName,storeName);
	}

	@Override
	public Map findSendEmail(String id) {
		return excelDataDao.findSendEmail(id);
	}

	

}
