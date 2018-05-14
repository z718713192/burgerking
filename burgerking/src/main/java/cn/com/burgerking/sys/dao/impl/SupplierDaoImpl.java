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

import cn.com.burgerking.sys.dao.SupplierDao;
import cn.com.burgerking.sys.entity.Supplier;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;

@Repository
public class SupplierDaoImpl extends BaseDaoImpl<Supplier> implements SupplierDao{
	
	private static final Logger logger = LoggerFactory.getLogger(SupplierDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Map<Object, Object>> findSuppliers(Integer iDisplayStart, Integer iDisplayLength,String supplierName,String supplierCom) {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,supplierName,ename,linkWay,email,comName,address from sys_supplier where 1=1 ");
		if(supplierName!=null && supplierName!=""){
			sb.append("and supplierName like '%"+supplierName+"%' " );
		}
		if(supplierCom!=null && supplierCom!=""){
			sb.append("and comName like '%"+supplierCom+"%' " );
		}
		sb.append("order BY updateTime  LIMIT "+iDisplayStart+","+iDisplayLength);
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object,Object>> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<Object,Object>>();
	}


	@Override
	public int findSupplierCount(String supplierName,String supplierCom) {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,supplierName,ename,linkWay,email,comName,address from sys_supplier where 1=1 ");
		if(supplierName!=null && supplierName!=""){
			sb.append("and supplierName like '%"+supplierName+"%' " );
		}
		if(supplierCom!=null && supplierCom!=""){
			sb.append("and comName like '%"+supplierCom+"%' " );
		}
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object,Object>> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list.size();
		return 0;
	}


	@Override
	public Supplier findSupplierById(Integer id) {
		String sql = "SELECT * FROM sys_supplier WHERE id='"+id+"'";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(Supplier.class);
		List<Supplier> list = query.list();
		if(list != null && list.size()>0) return list.get(0);
		return null;
	}


	@Override
	public boolean saveSupplier(Supplier supplier, List<SupplierStore> supplierStores, List<SupplierCompany> companies) {
		try {
			this.sessionFactory.getCurrentSession().save(supplier);
			String sql = "SELECT * FROM sys_supplier order by id desc";
			Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(Supplier.class);
			List<Supplier> list = query.list();
			if(list != null && list.size()>0){
				for (SupplierStore supplierStore : supplierStores) {
					supplierStore.setSupplierId(list.get(0).getId()+"");
					this.sessionFactory.getCurrentSession().save(supplierStore);
				}
				for (SupplierCompany company : companies) {
					company.setSuppId(list.get(0).getId()+"");
					this.sessionFactory.getCurrentSession().save(company);
				}
			}

		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}


	@Override
	public boolean updateSupplier(Supplier supplier, List<SupplierStore> supplierStores, List<SupplierCompany> companies) {
		String sql="UPDATE sys_supplier SET comName = '" + supplier.getComName()
		+ "',supplierName = '" + supplier.getSupplierName()
		+ "',ename = '" + supplier.getEname()
		+ "', linkWay= '" + supplier.getLinkWay()
		+ "',linkName = '" + supplier.getLinkName()
		+ "',email = '" + supplier.getEmail()
		+ "',address = '" + supplier.getAddress()
		+ "',updateMan = '" + supplier.getUpdateMan()
		+ "',updateTime=NOW() WHERE id=" + supplier.getId();
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1){
			sql="DELETE FROM sys_supplier_company WHERE suppId='"+supplier.getId()+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			sql="DELETE FROM sys_supplier_store WHERE supplierId='"+supplier.getId()+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			for (SupplierStore supplierStore : supplierStores) {
				supplierStore.setSupplierId(supplier.getId()+"");
				this.sessionFactory.getCurrentSession().save(supplierStore);
			}
			for (SupplierCompany company : companies) {
				company.setSuppId(supplier.getId()+"");
				this.sessionFactory.getCurrentSession().save(company);
			}
			return true;
		}
		return false;
	}


	@Override
	public List<Map<Object, Object>> findSupplierCompanys(Integer id) {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,companyId from sys_supplier_company where suppId='"+id+"'");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object,Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<Object,Object>>();
	}


	@Override
	public List<Map<Object, Object>> findSupplierStores(Integer id) {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,storeId from sys_supplier_store where supplierId='"+id+"'");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object,Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<Object,Object>>();
	}


	@Override
	public boolean delSupplier(Integer id) {
		String sql="DELETE FROM sys_supplier WHERE id="+id;
		int  num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num>=1){
			sql="DELETE FROM sys_supplier_company WHERE suppId='"+id+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			sql="DELETE FROM sys_supplier_store WHERE supplierId='"+id+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			return true;
		}
		return false;
	}


	@Override
	public List<Supplier> findAllSupplier() {
		String sql = "SELECT * FROM sys_supplier";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(Supplier.class);
		List<Supplier> list = query.list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Supplier>();
	}


	@Override
	public List<Map<String, Object>> findSuppliersByStoreId(String storeId) {
		 StringBuffer sbf = new StringBuffer();
//		 sbf.append("  select  ssp.id,ssp.comName,ssp.email  from sys_supplier ssp where ssp.id in ( ")
//			 .append(" select cm.suppId  from sys_supplier_company cm where cm.companyId in ( ")
//			 .append(" select t.comStoreId  from sys_company_store  t where t.storeId =  ")
//			 .append(storeId)
//			 .append(" )  ) and ssp.email is not NULL ")
// 
//		 ;
		 sbf.append("select DISTINCT(a.id),a.supplierName,a.email from sys_supplier a  ");
		 sbf.append("LEFT JOIN sys_supplier_store b on a.id=b.supplierId ");
		 sbf.append("LEFT JOIN sys_supplier_company c on a.id=c.suppId ");
		 sbf.append("LEFT JOIN sys_org d on c.companyId=d.id ");
		 sbf.append(" where b.storeId = '"+storeId+"'");
		return findListMap(sbf.toString(), null, null);
	}


	@Override
	public Supplier findSupplierByStoreId(int storeId) {
		String sql = "select * from sys_supplier  a LEFT JOIN sys_supplier_store b on a.id =b.supplierId where b.storeId='"+storeId+"'";
		return findOne(sql, Supplier.class);
	}



}