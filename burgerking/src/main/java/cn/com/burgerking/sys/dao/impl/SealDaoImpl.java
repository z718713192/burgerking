package cn.com.burgerking.sys.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.com.burgerking.sys.dao.SealDao;
import cn.com.burgerking.sys.entity.Seal;
import cn.com.burgerking.sys.entity.SealComStore;
import cn.com.burgerking.sys.entity.UserSeal;

@Repository
public class SealDaoImpl extends BaseDaoImpl<Seal> implements SealDao{
	
	private static final Logger logger = LoggerFactory.getLogger(SealDaoImpl.class);
	
	@Override
	public List findSeals(Integer iDisplayStart, Integer iDisplayLength,String sealName,String sealCom) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select src.sealId ,sel.sealName,org.csName,IFNULL(userCout,0),IFNULL(storeCount,0)  from  ");
		sb.append(" ( ");
		sb.append(" select rtb.sealId, companyId , storeCount ,userCout from  ");
		sb.append(" ( select  td.sealId,td.companyId,count(1) storeCount from sys_seal_comstore td  where  EXISTS ( select * from (select * from sys_seal  LIMIT "+iDisplayStart+","+iDisplayLength+") ss where ss.id = td.sealId ) ");
		sb.append("  group by td.sealId,td.companyId) rtb left join  ");
		sb.append(" (select   ts.sealId sealId,count(1) userCout  from sys_user_seal ts  where  EXISTS ( select * from (select * from sys_seal  LIMIT "+iDisplayStart+","+iDisplayLength+") ss where ss.id = ts.sealId ) ");
		sb.append("  group by ts.sealId ) utb   on  rtb.sealId = utb.sealId ");
		sb.append(" ) src inner join sys_org org  on org.id =src.companyId  ");
		if(sealCom!=null && sealCom!=""){
			sb.append("and org.csName like '%"+sealCom+"%' " );
		}
		sb.append(" inner join sys_seal sel on sel.id = src.sealId ");
		if(sealName!=null && sealName!=""){
			sb.append("and sel.sealName like '%"+sealName+"%' " );
		}
		sb.append("  ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		List<Map<Object,Object>> list = query.setResultTransformer(Transformers.TO_LIST).list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<Map<Object,Object>>();
	}


	@Override
	public int findSealCount(String sealName,String sealCom) {
		StringBuffer sb = new StringBuffer();
		sb.append("select  COUNT(1)  from sys_seal a where 1=1 ");
		if(sealName!=null && sealName!=""){
			sb.append("and a.sealName like '%"+sealName+"%' " );
		}
		if(sealCom!=null && sealCom!=""){
			sb.append("and a.sscompanyName like '%"+sealCom+"%' " );
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
	public boolean savaSeal(Seal seal, List<SealComStore> comStors) {
		try {
			this.sessionFactory.getCurrentSession().save(seal);
			
			String sql = "SELECT * FROM sys_seal order by id desc ";
			Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(Seal.class);
			List<Seal> list = query.list();
			if(list!=null && list.size()>0){
				for (SealComStore comStore : comStors) {
					comStore.setSealId(list.get(0).getId()+"");
					this.sessionFactory.getCurrentSession().save(comStore);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}


	@Override
	public Seal findASealById(Integer id) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from sys_seal where id="+id+" ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(Seal.class);
		List<Seal> list = query.list();
		if(list != null && list.size()>0) return list.get(0);
		return new Seal();
	}


	@Override
	public List<SealComStore> findSealComStoreBySealId(int id) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from sys_seal_comstore where sealId='"+id+"' ");
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(SealComStore.class);
		List<SealComStore> list = query.list();
		if(list != null && list.size()>0) return list;
		return new ArrayList<SealComStore>();
	}


	@Override
	public boolean updateSeal(Seal seal, List<SealComStore> comStors) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE sys_seal SET sealName='"+seal.getSealName()+"' ");
			if(seal.getImg()!=null){
				sb.append(",img='"+seal.getImg()+"' ");
				sb.append(",imgName='"+seal.getImgName()+"' ");
			}
			sb.append(" where id="+seal.getId());
			int num = this.sessionFactory.getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
			if(num>=1){
					String sql="DELETE FROM sys_seal_comStore WHERE sealId='"+seal.getId()+"'";
					this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
					for (SealComStore comStore : comStors) {
						this.sessionFactory.getCurrentSession().save(comStore);
					}
			}
			
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}


	@Override
	public boolean delSealById(Integer id) {
		String sql="DELETE FROM sys_seal WHERE id="+id;
		int  num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num>=1){
			sql="DELETE  FROM sys_seal_comstore WHERE sealId='"+id+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
			
			sql="DELETE FROM sys_user_seal WHERE sealId='"+id+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		}
		return false;
	}


	@Override
	public List<Map<String, Object>> companyWithSealsTree(Long id) {
		StringBuffer _sbf = new StringBuffer();
		_sbf.append("select DISTINCT a.id sealId,a.sealName sealName from sys_seal a  ");
		_sbf.append("LEFT JOIN sys_seal_comstore b on a.id=b.sealId ");
		_sbf.append("where b.companyId='"+id+"' ");
		return findListMap(_sbf.toString(), null, null);
	}


	@Override
	public boolean saveUserSealRef(UserSeal userSeal) {
		try {
			this.sessionFactory.getCurrentSession().save(userSeal);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true; 
	}


	@Override
	public boolean deleteUserSealRef(String userId) {
		try {
			String sql="DELETE FROM sys_user_seal  where userId = '"+userId+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}


	@Override
	public boolean deleteUserSealRef(String userId, String companyId) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public List<Map<String, Object>> findAllCompanySealByStoreId(long storeId) {
		String sql = "select  sll.id,sll.sealName  from (select com.sealId  from sys_seal_comstore com  where exists ("
				+ " select * from sys_company_store t where t.storeId = "+storeId+" and t.comStoreId = com.companyId) group by com.sealId) src inner join sys_seal sll on src.sealId = sll.id";
		return findListMap(sql, null, null);
	}

	
}