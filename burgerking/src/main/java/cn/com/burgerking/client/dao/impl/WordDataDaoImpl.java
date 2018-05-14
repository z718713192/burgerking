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

import cn.com.burgerking.client.dao.WordDataDao;
import cn.com.burgerking.client.entity.ExcelData;
import cn.com.burgerking.client.entity.WordData;
import cn.com.burgerking.sys.dao.impl.BaseDaoImpl;

@Repository
public class WordDataDaoImpl extends BaseDaoImpl<WordData> implements WordDataDao {
	private Logger log = LoggerFactory.getLogger(WordDataDaoImpl.class);

	@Override
	public void saveWordData(WordData wordData) {
		try {
			this.sessionFactory.getCurrentSession().save(wordData);
		} catch (HibernateException e) {
			log.error(e.toString(), e);
		}
	}
	
	@Override
	public List<WordData> findWordDataByFileName(String corporateName) {
 
		return new ArrayList<WordData>();
	}

	@Override
	public Integer countWordDataByFileName(String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(id) from client_word_data ");
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
		sb.append("select a.storeId,a.reportDate,d.img,d.sealName from client_word_data a ");
		sb.append("LEFT JOIN sys_org b on a.corporateName=b.csName ");
		sb.append("LEFT JOIN sys_seal_comstore c on c.companyId=b.id ");
		sb.append("LEFT JOIN sys_seal d on c.sealId=d.id ");
		sb.append("where a.id="+id+" and d.id = "+esealId+" ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
//		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(WordData.class);
//		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addScalar("storeId").addScalar("reportDate").addScalar("img").setResultTransformer(Transformers.aliasToBean(WordData.class));
		List<WordData> list = query.list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<WordData>();
	}

	@Override
	public boolean updateWordDataById(String id, String key, String userName, String esealName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update client_word_data ");
		if("esealFlag".equals(key)){
			sb.append(" set esealFlag = '1' ");
			sb.append(" , signEseal = '"+esealName+"' ");
		}else if("pdfFlag".equals(key)){
			sb.append(" set pdfFlag = '1' ");
		}else if("emailFlag".equals(key)){
			sb.append(" set emailFlag = '1' ");
		}else if("emailStatus".equals(key)){
			sb.append(" set emailStatus = '1' ");
		}
		sb.append(" , operator = '"+userName+"' ");
		sb.append(" where id="+id+"");
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
		if(num==1) return true;
		return false;
	}

	@Override
	public List<WordData> findWordData(String month, String corporateName, String key) {
		StringBuffer sb = new StringBuffer();
//		sb.append(" select t1.id,t1.fileName ");
////		if("pdf".equals(key)){
////			sb.append(",t1.signEseal ");
////		}else if("email".equals(key)){
////			sb.append(",GROUP_CONCAT(CONCAT(t3.id,'|',t3.sealName) SEPARATOR ':') as esealS ");
////		}
//		sb.append(" from client_word_data t1 ");
////		sb.append(" where corporateName='"+corporateName+"' and t1.storeId = t2.storeId and t2.sealId = t3.id ");
//		sb.append(" where reportDate='"+month+"' ");
//		
		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.corporateName,GROUP_CONCAT(CONCAT(t3.id,'|',t3.sealName) SEPARATOR ':') as esealS ");
		sb.append(" from client_word_data t1, sys_seal_comstore t2, sys_seal t3 ");
		sb.append(" where corporateName='"+corporateName+"' and t1.storeId = t2.storeId and t2.sealId = t3.id ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		if("seal".equals(key)){
			sb.append(" and esealFlag = '0' ");
		}else if("pdf".equals(key)){
			sb.append(" and esealFlag = '1' ");	
			sb.append(" and pdfFlag = '0' ");
		}else if("email".equals(key)){
			sb.append(" and pdfFlag = '1' ");
			sb.append(" and emailFlag = '0' ");
		}else if("emailSubject".equals(key)){
			sb.append(" and emailFlag = '1' ");
			sb.append(" and emailStatus = '0' ");
		}
		sb.append(" group by t1.id order by t1.id desc ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<WordData> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<WordData>();
	}

	@Override
	public boolean updateWordDataByIds(String ids, String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update client_word_data ");
		sb.append(" set pdfFlag = '1',operator = '"+userName+"' ");
		sb.append(" where id in("+ids+")");
		
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
		if(num==1) return true;
		return false;
		
	}

	@Override
	public List findEmailData(String month, String csName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.corporateName,t2.email ");
		sb.append(" from client_word_data t1, sys_org t2 ");
		sb.append(" where corporateName='"+csName+"' and t1.storeId = t2.storeId ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		sb.append(" and pdfFlag = '1' ");
		sb.append(" and emailFlag = '0' ");
		sb.append(" order by t1.id desc ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object, Object>> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<Object, Object>>();
	}

	@Override
	public List findWordDataIsSubjectEmailData(String month, String csName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.fileName,t1.storeId,t1.storeName,t2.email ");
		sb.append(" from client_word_data t1, sys_org t2 ");
		sb.append(" where t1.corporateName='"+csName+"' and t1.storeId = t2.storeId ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		sb.append(" and emailFlag = '1' and emailStatus = '0' ");
		sb.append(" group by t1.id,t1.fileName,t1.storeId,t1.storeName,t2.email order by t1.id desc ");
		
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object, Object>> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<Object, Object>>();
	}

	@Override
	public Map findSendEmail(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.storeId,t1.corporateName,t1.reportDate,t2.email,t1.fileName ");
		sb.append(" from client_word_data t1, sys_org t2 ");
		sb.append(" where t1.corporateName = t2.csName ");
		sb.append(" and t2.type = '1' and t1.storeId = '"+id+"'");
		
		return findOne4Map(sb.toString());
	}

	@Override
	public boolean updateWordDataIsEmail(String id, String recEmails, String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update client_word_data ");
		sb.append(" set receiveEmail = '"+recEmails+"', emailStatus = '1', operator = '"+userName+"' ");
		sb.append(" where id ="+id+"");
		
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
		if(num==1) return true;
		return false;
	}

	@Override
	public List findAuditData(String month, String csName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select t1.id,t1.fileName,t1.storeName,t1.signEseal,t1.receiveEmail,t1.emailStatus ");
		sb.append(" from client_word_data t1 ");
		sb.append(" where t1.corporateName='"+csName+"' ");
		if(month != null && !"".equals(month)){
			sb.append(" and reportDate='"+month+"' ");
		}
		sb.append(" and emailFlag = '1' and emailStatus != '0' ");
		sb.append(" order by t1.id desc ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object, Object>> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<Object, Object>>();
	}

}
