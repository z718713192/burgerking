package cn.com.burgerking.client.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.com.burgerking.client.dao.ExcelDataDao;
import cn.com.burgerking.client.entity.ExcelData;
import cn.com.burgerking.sys.dao.impl.BaseDaoImpl;

@Repository
public class ExcelDataDaoImpl extends BaseDaoImpl<ExcelData> implements ExcelDataDao {
	private Logger log = LoggerFactory.getLogger(ExcelDataDaoImpl.class);

	@Override
	public void saveExcelData(ExcelData excelData) {
		try {
			this.sessionFactory.getCurrentSession().save(excelData);
		} catch (HibernateException e) {
			log.error(e.toString(), e);
		}
	}
	
	@Override
	public List<ExcelData> findExcelDataByFileName(String corporateName) {
		/**
		StringBuffer sb = new StringBuffer();
		sb.append("select id,fileName,storeId,corporateName from client_excel_data ");
//		sb.append("select * from client_excel_data ");
		sb.append("where fileName = '"+fileName+"' ");
		sb.append("LIMIT "+iDisplayStart+","+iDisplayLength);
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<ExcelData> list = query.setResultTransformer(Transformers.TO_LIST).list();
//		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(ExcelData.class);
//		List<ExcelData> list = query.list();
 * 
 */
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.corporateName,GROUP_CONCAT(CONCAT(t3.id,'|',t3.sealName) SEPARATOR ':') as esealS  ");
		sb.append(" from client_excel_data t1, sys_seal_comstore t2, sys_seal t3 ");
		sb.append(" where t1.esealFlag = '0' and corporateName='"+corporateName+"' and t1.storeId = t2.storeId and t2.sealId = t3.id ");
		sb.append(" group by t1.id,t1.storeId order by t1.id ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<ExcelData> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<ExcelData>();
	}

	@Override
	public Integer countExcelDataByFileName(String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(id) from client_excel_data ");
		sb.append("where fileName in ("+fileName+")");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0){
			Object obj = list.get(0);
			if(obj != null){
				return Integer.parseInt(obj.toString().replace("[", "").replace("]", ""));
			}
		}
		return 0;
	}

	@Override
	public List findEsealById(String id, String esealId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.storeId,a.reportDate,d.imgName,d.sealName from client_excel_data a ");
		sb.append("LEFT JOIN sys_org b on a.corporateName=b.csName ");
		sb.append("LEFT JOIN sys_seal_comstore c on c.companyId=b.id ");
		sb.append("LEFT JOIN sys_seal d on c.sealId=d.id ");
		sb.append("where a.id="+id+" and d.id = "+esealId+" ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
//		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(ExcelData.class);
//		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addScalar("storeId").addScalar("reportDate").addScalar("img").setResultTransformer(Transformers.aliasToBean(ExcelData.class));
		List<ExcelData> list = query.list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<ExcelData>();
	}

	@Override
	public boolean updateExcelDataById(String id, String key, String userName, String esealName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update client_excel_data ");
		if("esealFlag".equals(key)){
			sb.append(" set esealFlag = '1', ");
			sb.append(" signEseal = '"+esealName+"', ");
		}else if("pdfFlag".equals(key)){
			sb.append(" set pdfFlag = '1', ");
		}else if("emailFlag".equals(key)){
			sb.append(" set emailFlag = '1', ");
		}else if("emailStatus".equals(key)){
			sb.append(" set emailStatus = '1', ");
		}
		sb.append(" operator = '"+userName+"' ");
		sb.append(" where id="+id+"");
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
		if(num==1) return true;
		return false;
	}
	
	@Override
	public List<ExcelData> findExcelData(String month, String corporateName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.corporateName,GROUP_CONCAT(CONCAT(t3.id,'|',t3.sealName) SEPARATOR ':') as esealS ");
		sb.append(" from client_excel_data t1, sys_seal_comstore t2, sys_seal t3 ");
		sb.append(" where corporateName='"+corporateName+"' and t1.storeId = t2.storeId and t2.sealId = t3.id ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		sb.append(" and t1.esealFlag = '0' ");
		sb.append(" group by t1.id,t1.storeId order by t1.id desc ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<ExcelData> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<ExcelData>();
	}

	@Override
	public List<ExcelData> findPdfData(String month, String corporateName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.corporateName,t1.signEseal ");
		sb.append(" from client_excel_data t1, sys_seal_comstore t2 ");
		sb.append(" where corporateName='"+corporateName+"' and t1.storeId = t2.storeId ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		sb.append(" and esealFlag = '1' ");
		sb.append(" and pdfFlag = '0' ");
		sb.append(" group by t1.id,t1.storeId order by t1.id desc ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<ExcelData> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<ExcelData>();
	}
	
	@Override
	public List<ExcelData> findEmailData(String month, String corporateName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.corporateName,t2.email ");
		sb.append(" from client_excel_data t1, sys_org t2 ");
		sb.append(" where corporateName='"+corporateName+"' and t1.storeId = t2.storeId ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		sb.append(" and pdfFlag = '1' ");
		sb.append(" and emailFlag = '0' ");
		sb.append(" order by t1.id desc ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<ExcelData> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<ExcelData>();
	}

	@Override
	public boolean updateExcelDataByIds(String ids, String userName, String key) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update client_excel_data ");
		if("pdf".equals(key)){
			sb.append(" set pdfFlag = '1',operator = '"+userName+"' ");
		}else if("email".equals(key)){
			sb.append(" set emailFlag = '1',operator = '"+userName+"' ");
		}
		sb.append(" where id in("+ids+")");
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
		if(num==1) return true;
		return false;
		
	}

	@Override
	public boolean updateExcelDataIsEmail(String id, String recEmails, String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update client_excel_data ");
		sb.append(" set receiveEmail = '"+recEmails+"', emailStatus = '1', operator = '"+userName+"' ");
		sb.append(" where id ="+id+"");
		
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
		if(num==1) return true;
		return false;
	}

	@Override
	public List<ExcelData> findExcelDataIsSubjectEmailData(String month, String corporateName) {
		StringBuffer sb = new StringBuffer();
//		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.storeName,t4.email,t1.receiveEmail ");
//		sb.append(" from client_excel_data t1, sys_seal_comstore t2, sys_org t3, sys_supplier t4 ");
//		sb.append(" where t1.corporateName='"+corporateName+"' and t1.storeId = t2.storeId and t1.storeId = t3.storeId and t3.defautSupplier = t4.id ");
//		if(month != null && !"".equals(month)){
//			sb.append(" and reportDate='"+month+"' ");
//		}
//		sb.append(" and emailFlag = '1' and emailStatus = '0' ");
//		sb.append(" group by t1.id,t1.fileName,t1.storeId,t1.storeName,t4.email,t1.receiveEmail order by t1.id desc ");
		
		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.storeName,t2.email ");
		sb.append(" from client_excel_data t1, sys_org t2 ");
		sb.append(" where t1.corporateName='"+corporateName+"' and t1.storeId = t2.storeId ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		sb.append(" and emailFlag = '1' and emailStatus = '0' ");
		sb.append(" group by t1.id,t1.fileName,t1.storeId,t1.storeName,t2.email order by t1.id desc ");
		
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<ExcelData> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<ExcelData>();
	}

	@Override
	public List<ExcelData> findAuditData(String month, String corporateName,String storeName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.fileName,t1.storeName,t1.signEseal,t1.receiveEmail,t1.emailStatus,t1.storeId ");
		sb.append(" from client_excel_data t1 ");
		sb.append(" where t1.corporateName='"+corporateName+"' ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		if(storeName!=null && !storeName.equals("")){
			sb.append(" and t1.storeName like '%"+storeName+"%' ");
		}
		sb.append(" and emailFlag = '1' and emailStatus != '0' ");
		sb.append(" order by t1.id desc ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<ExcelData> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<ExcelData>();
	}

	@Override
	public Map findSendEmail(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.storeId,t1.corporateName,t1.reportDate,t2.email ");
		sb.append(" from client_excel_data t1, sys_org t2 ");
		sb.append(" where t1.corporateName = t2.csName ");
		sb.append(" and t2.type = '1' and t1.id = "+id);
		
		return findOne4Map(sb.toString());
	}

}
