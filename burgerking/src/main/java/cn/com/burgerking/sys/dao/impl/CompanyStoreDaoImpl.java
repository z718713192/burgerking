package cn.com.burgerking.sys.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.burgerking.sys.dao.CompanyStoreDao;
import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.CompanyStore;
import cn.com.burgerking.sys.entity.SealComStore;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;
import cn.com.burgerking.sys.entity.UserCompany;

@Repository
public class CompanyStoreDaoImpl  extends BaseDaoImpl<ComStore> implements CompanyStoreDao{
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyStoreDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ComStore findCompanyStoreById(Integer id) {
		String sql = "SELECT * FROM sys_org WHERE id='"+id+"'";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(ComStore.class);
		List<ComStore> list = query.list();
		if(list != null && list.size()>0) return list.get(0);
		return null;
	}


	@Override
	public boolean saveCompanyStore(ComStore comStore) {
		try {
			this.sessionFactory.getCurrentSession().save(comStore);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}


	@Override
	public boolean updateCompanyStore(ComStore comStore) {
		String sql="UPDATE sys_org SET csName = '" + comStore.getCsName()
		+ "',identifyNumber = '" + comStore.getIdentifyNumber()
		+ "',email = '" + comStore.getEmail()
		+ "',emailPassword = '" + comStore.getEmailPassword()
		+ "',address = '" + comStore.getAddress()
		+ "',updateMan = '" + comStore.getUpdateMan()
		+ "',phone = '" + comStore.getPhone()+"',updateTime=NOW() WHERE id=" + comStore.getId();
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1) return true;
		return false;
	}


	@Override
	public List<Map<String, Object>> findCompanys(Integer iDisplayStart, Integer iDisplayLength,String branchOffice) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id,a.csName,COUNT(DISTINCT b.sealId) sealCount,COUNT(DISTINCT c.userId) userCount,COUNT(DISTINCT d.storeId) storeCount from sys_org a ");
		sb.append("LEFT JOIN sys_seal_comstore b on a.id =b.companyId ");
		sb.append("LEFT JOIN sys_user_company c on a.id =c.companyId  ");
		sb.append("LEFT JOIN sys_company_store d on a.id =d.comStoreId ");
		
		sb.append("where 1=1 ");
		if(branchOffice!=null && branchOffice!=""){
			sb.append(" and a.csName like '%"+branchOffice+"%' " );
		}
		sb.append(" and a.type=1 GROUP BY a.id  ORDER BY a.updateTime desc  ");
		List<Map<String, Object>> list  = findListMap(sb.toString(), iDisplayStart, iDisplayLength);
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<String, Object>>();
	}


	@Override
	public int findCompanyCount(String branchOffice) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(1) from sys_org t WHERE type='1' ");
		if(branchOffice!=null && branchOffice!=""){
			sb.append(" and t.csName like '%"+branchOffice+"%' " );
		}
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
	public boolean delCompany(Integer id) {
		String sql="DELETE FROM sys_org WHERE id="+id;
		int  num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1){
			//查询印章与公司关联表
			sql="select * from sys_seal_comstore where companyId='"+id+"'";
			Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(SealComStore.class);
			List<SealComStore> list = query.list();
			for (SealComStore sealComStore : list) {
				//查询印章与用户关联表
				sql="DELETE FROM sys_user_seal WHERE sealId="+sealComStore.getSealId();
				this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			}
			//查询门店与公司关联信息
			sql="select * from sys_company_store where comStoreId='"+id+"'";
			query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(CompanyStore.class);
			List<CompanyStore> list1 = query.list();
			for (CompanyStore companyStore : list1) {
				//删除供应商与门店关联信息
				sql="DELETE FROM sys_supplier_store WHERE storeId='"+companyStore.getStoreId()+"'";
				this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			}
			//删除公司于印章关联信息
			sql="DELETE FROM sys_seal_comstore WHERE companyId='"+id+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			//删除公司于用户关联信息
			sql="DELETE FROM sys_user_company WHERE companyId='"+id+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			//删除公司于门店关联信息
			sql="DELETE FROM sys_company_store WHERE comStoreId='"+id+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			//删除公司于供应商关联信息
			sql="DELETE FROM sys_supplier_company WHERE companyId='"+id+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			return true;
		}
		return false;
	}

	/**
	 * 门店管理查询
	 */
	@Override
	public List findStores(Integer iDisplayStart, Integer iDisplayLength,String storeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id, a.storeId,a.address,a.openTime,a.`status`,c.csName from sys_org a  ");
		sb.append("left JOIN sys_company_store b  on a.storeId =b.storeId ");
		sb.append("LEFT JOIN sys_org c on c.id =b.comStoreId ");
		sb.append("where a.type='2'  and c.type='1'  ");
		if(storeId!=null && storeId!=""){
			sb.append("and a.storeId like '%"+storeId+"%' " );
		}
		sb.append("order BY a.updateTime Desc  ");
		
		List<Map<String, Object>> list  = findListMap(sb.toString(), iDisplayStart, iDisplayLength);
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<String, Object>>();
		
	}


	@Override
	public int findStoreCount(String storeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(id) from sys_org where type='2' ");
		if(storeId!=null && storeId!=""){
			sb.append("and storeId like '%"+storeId+"%' ");
		}
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
	public List findAllCompany() {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from sys_org where type='1' ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(ComStore.class);
		List<ComStore> list = query.list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<ComStore>();
	}


	@Override
	public int findMaxCSId() {
		StringBuffer sb = new StringBuffer();
		sb.append("select Max(id) from sys_org ");
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
	public boolean saveCompanyStore(ComStore comStore, CompanyStore companyStore) {
		try {
			this.sessionFactory.getCurrentSession().save(comStore);
			this.sessionFactory.getCurrentSession().save(companyStore);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}


	@Override
	public boolean updateCompanyStore(ComStore comStore, CompanyStore companyStore) {
		String sql="select * from sys_org where id="+comStore.getId();
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(ComStore.class);
		ComStore store2 = (ComStore) query.list().get(0);	
		String oldDeSId=store2.getDefautSupplier();
		
		sql="UPDATE sys_org SET csName = '" + comStore.getCsName()
		+ "',email = '" + comStore.getEmail()
		+ "',openTime = '" + comStore.getOpenTime()
		+ "',closeTime = '" + comStore.getCloseTime()
		+ "',linkman = '" + comStore.getLinkman()
		+ "',status = '" + comStore.getStatus()
		+ "',address = '" + comStore.getAddress()
		+ "',defautSupplier = '" + comStore.getDefautSupplier()
		+ "',updateMan = '" + comStore.getUpdateMan()
		+ "',phone = '" + comStore.getPhone()+"',updateTime=NOW() WHERE id=" + comStore.getId();
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1){
			sql="select comStoreId from sys_company_store where storeId='"+companyStore.getStoreId()+"' ";
			query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
			List list = query.setResultTransformer(Transformers.TO_LIST).list();
			if(list != null && list.size()>0){
				sql="UPDATE sys_company_store SET comStoreId='"+companyStore.getComStoreId()+"',updateMan = '" + companyStore.getUpdateMan()+"',updateTime=NOW() where storeId='"+companyStore.getStoreId()+"'";
				this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			}else{
				this.sessionFactory.getCurrentSession().save(companyStore);
			}
			return true;
		}
		return false;
	}


	@Override
	public String findCompanyIdByStoreId(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("select comStoreId from sys_company_store where storeId='"+id+"' ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0){
			Object obj = list.get(0);
			if(obj != null){
				return obj.toString().replace("[", "").replace("]", "");
			}
		}
		return "";
	}


	@Override
	public boolean delStore(Integer id) {
		String sql="select * from sys_org where id="+id;
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(ComStore.class);
		List<ComStore> list = query.list();
		String oldStoreId=list.get(0).getStoreId();
		sql="DELETE FROM sys_org WHERE id="+id;
		int  num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1){
			
			sql="DELETE FROM sys_seal_comStore WHERE storeId='"+oldStoreId+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			//删除公司于门店关联信息
			sql="DELETE FROM sys_company_store WHERE storeId='"+oldStoreId+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			sql="DELETE FROM sys_supplier_store WHERE storeId='"+oldStoreId+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			return true;
		}
		return false;
	}


	@Override
	public List<Map<Object,Object>> findStoresByCId(Integer id) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.storeId,a.csName from  sys_org  a ");
		sb.append("where a.storeId in (SELECT b.storeId from sys_company_store b where b.comStoreId='"+id+"')");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object,Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<Object,Object>>();
	}


	@Override
	public boolean saveUserCompanyRef(UserCompany userCompany) {
		try {
			this.sessionFactory.getCurrentSession().save(userCompany);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true; 
	}


	@Override
	public boolean deleteUserCompanyRef(String userId) {
		try {
			String sql=" delete from sys_user_company   where  userId ='"+userId+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}


	@Override
	public boolean deleteUserCompanyRef(String userId, String companyId) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean validateStoreId(String storeId) {
		String sql = "SELECT * FROM sys_org WHERE storeId ='" + storeId + "'";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(ComStore.class);
		List<ComStore> list = query.list();
		if(list!=null && list.size()>0 ) return true;
		return false;
	}


	@Override
	public List<Map<String, Object>> companysByUserId(String userId) {
		String sql="select org.*  from sys_org org where EXISTS( select * from sys_user_company  t where t.userId = '"+userId+"' and org.id=t.companyId) and org.type =1";
		return  findListMap(sql, null, null);
				
	}


	@Override
	public ComStore findStoreByStoreId(String storeId) {
		String sql = "SELECT * FROM sys_org WHERE storeId='"+storeId+"'";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(ComStore.class);
		List<ComStore> list = query.list();
		if(list != null && list.size()>0) return list.get(0);
		return new ComStore();
	}


	@Override
	public ComStore findCompanyByStoreId(String storeId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select b.* from sys_org a ");
		sb.append("LEFT JOIN sys_company_store c on a.storeId=c.storeId ");
		sb.append("LEFT JOIN  sys_org  b  on c.comStoreId=b.id ");
		sb.append("where a.storeId='"+storeId+"'");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(ComStore.class);
		List<ComStore> list = query.list();
		if(list != null && list.size()>0) return list.get(0);
		return new ComStore();
	}



}