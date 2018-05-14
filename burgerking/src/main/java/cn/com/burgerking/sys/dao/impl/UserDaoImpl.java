package cn.com.burgerking.sys.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.com.burgerking.sys.dao.UserDao;
import cn.com.burgerking.sys.entity.User;
import cn.com.burgerking.utils.ConstGlobal;
import cn.com.burgerking.sys.dao.impl.UserDaoImpl;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao<User> {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	@Override
	public User findMUser(String username) {
		String sql = "SELECT * FROM sys_user WHERE trim(username)='"+username+"'";
		User usr = findOne(sql, User.class);
		if(usr!=null){
			 sql = "select org.csName  from sys_user_company suc  left join sys_org org  on suc.companyId  =org.id where suc.userId ='"+usr.getUserId()+"'";
			 List<Map<String, Object>> lis = findListMap(sql, null, null);
			 int c =0;
			 for (Map<String, Object> map : lis) {
				 c++;
				if(c == lis.size()){
					usr.setCompanyName(usr.getCompanyName()==null?""+map.get("csName"):usr.getCompanyName()+"、"+map.get("csName"));
				}else{
					if(usr.getCompanyName() == null){
						usr.setCompanyName(map.get("csName")+"");
						continue;
					}
					usr.setCompanyName(usr.getCompanyName()+"、"+map.get("csName"));
					
				}
			}
		}
		
		return usr;
	}
	
	@Override
	public User findMUser(String columnName,String columnValue) {
		String sql = "SELECT * FROM sys_user WHERE "+columnName+"='"+columnValue+"'";
		return findOne(sql, User.class);
	}

	@Override
	public boolean saveMUser(User manager) {
		try{
			this.sessionFactory.getCurrentSession().save(manager);
		}catch(Exception e){
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean updateMUser(User manager) {
		String sql="UPDATE sys_user SET ename='"+manager.getEname()+"',"
				+ "email='"+manager.getEmail()+"',name='"+manager.getName()
				+ "',updateMan = '" + manager.getUpdateMan()
				+ "',updateTime=NOW() "
				+ "WHERE id="+manager.getId();
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1) return true;
		return false;
	}
	
	@Override
	public boolean updateMUser(User manager,String columnWhere,Object columnWhereValue) {
		String sql="UPDATE sys_user SET ename='"+manager.getEname()+"',"
				+ "email='"+manager.getEmail()+"',name='"+manager.getName()
				+ "',updateMan = '" + manager.getUpdateMan()
				+ "',updateTime=NOW() ,phone = '"+ manager.getPhone()+"'";
		if(manager.getPassword()!=null && !manager.getPassword().trim().equals(ConstGlobal.DEFAULT_PSWD)){
			sql+= ",password = '"+manager.getPassword()+"' ";
		}
		sql+= "WHERE "+columnWhere+"="+columnWhereValue ;
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1) return true;
		return false;
	}

	@Override
	public boolean updateMUserPassaword(User manager) {
		String sql="UPDATE sys_user SET password='"+manager.getPassword()
				+ "',updateMan = '" + manager.getUpdateMan()
				+ "',updateTime=NOW() "
				+ "WHERE id="+manager.getId();
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1) return true;
		return false;
	}

	@Override
	public Map<String, Object> findMUsersInfo(User user,Integer iDisplayStart, Integer iDisplayLength) {
		 
		String _usr = " select  id, createMan, createTime, email, ename, name, userId, username, userSerial, phone from sys_user ur    where ur.role != 1 and ur.userId = '"+user.getUserId().trim()+"'";
		  
		Map<String,Object> usr = findOne4Map(_usr);
		usr.put("pswd",ConstGlobal.DEFAULT_PSWD);
		String _sqlCo = " select  concat('C_',org.id) pid ,org.*,suc.userId from sys_user_company  suc "
				+ "left join sys_org org  on suc.companyId  = org.id where suc.userId ='"+user.getUserId().trim()+"'";
		
		List<Map<String, Object>> _orgLis = findListMap(_sqlCo, iDisplayStart, iDisplayLength);
		
		String _sqlUsr = "select   concat('S_',ssel.id) pid,  ssel.*,sus.userId from sys_user_seal sus "
				+ "left join sys_seal ssel on  ssel.id = sus.sealId where sus.userId='"+user.getUserId().trim()+"'";
		
		List<Map<String, Object>> _seaLis = findListMap(_sqlUsr, iDisplayStart, iDisplayLength);
		
		Map<String, Object> _rMap = new HashMap<String, Object>();
		_rMap.put("usr", usr);
		_rMap.put("orgs", _orgLis);
		_rMap.put("seals", _seaLis);
		return _rMap;
	}
	
	
	@Override
	public Map<String, Object> findMUsersPageInfo(User user,Integer iDisplayStart, Integer iDisplayLength,String userNum,String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select ur.userId,ur.userSerial,ur.name,ur.username,ur.id  from sys_user ur where  ur.role != 1 "); 
		if(user!= null && StringUtils.isNotBlank(user.getUserId())){
			sb.append(" and ur.userId = '").append(user.getUserId().trim()).append("'");
		}
		
		if(userNum!=null && userNum!=""){
			sb.append("and ur.userSerial like '%"+userNum+"%' " );
		}
		
		if(userName!=null && userName!=""){
			sb.append("and ur.name like '%"+userName+"%' " );
		}
		
		//查询总数
		int _countLine = count(sb.toString());
		
		StringBuffer sealb = new StringBuffer();
		sealb.append(sb.toString());

		List<Map<String, Object>> _lsm = findListMap(sb.toString(), iDisplayStart, iDisplayLength);
		
		
		//组装公司
		sb.insert(0, "  select org.*,scy.userId  from sys_user_company scy  left join sys_org org on org.id = scy.companyId  where exists ("
				+ " select userId from (");
		sb.append(" ) et where et.userId = scy.userId)");
		
		List<Map<String, Object>> _orgLis = findListMap(sb.toString(), null, null);
		//组装印章
		sealb.insert(0,"  SELECT sal.*,sus.userId from sys_user_seal sus  left join sys_seal sal on sal.id = sus.sealId"
				+ " where exists (   select * from (");
		sealb.append(") et where et.userId = sus.userId)");
		
		List<Map<String, Object>> _seaLis = findListMap(sealb.toString(), null, null);
		for (Map<String, Object> _etm : _lsm) {
			String userId = _etm.get("userId").toString();
			//公司
			for (Map<String, Object> _imp : _orgLis) {
				String _userId = _imp.get("userId").toString();
				if(userId.equals(_userId) && _imp.get("csName")!=null){
					if(_etm.get("companyNames")==null){
						StringBuffer _sbf = new StringBuffer();
						_sbf.append(_imp.get("csName").toString()).append(" 、 ");
						_etm.put("companyNames", _sbf);
					}else{
						StringBuffer _sbf = (StringBuffer) _etm.get("companyNames");
						_sbf.append(_imp.get("csName").toString()).append(" 、 ");
					}
				}
			}
			//印章
			for (Map<String, Object> _imp : _seaLis) {
				String _userId = _imp.get("userId").toString();
				if(userId.equals(_userId) && _imp.get("sealName")!=null){
					if(_etm.get("sealNames")==null){
						StringBuffer _sbf = new StringBuffer();
						_sbf.append(_imp.get("sealName").toString()).append(" 、 ");
						_etm.put("sealNames", _sbf);
					}else{
						StringBuffer _sbf = (StringBuffer) _etm.get("sealNames");
						_sbf.append(_imp.get("sealName").toString()).append(" 、 ");
					}
				}
			}
			if(_etm.get("companyNames")==null){
				_etm.put("companyNames", "无");
			}
			if(_etm.get("sealNames")==null){
				_etm.put("sealNames", "无");
			}
		}
		
		Map<String, Object> _rMap = new HashMap<String, Object>();
		_rMap.put("iTotalRecords", _countLine);
		_rMap.put("iTotalDisplayRecords", _countLine);
		_rMap.put("aaData", _lsm);
		
		return _rMap;
	}
	
	
	@Override
	public Object getAllSeals() {
		StringBuffer sb = new StringBuffer();
		sb.append("select COUNT(id) from sys_seal");
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
	public Object getAllcompanys() {
		StringBuffer sb = new StringBuffer();
		sb.append("select COUNT(id) from sys_org where type='1'");
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
	public Object getAllstores() {
		StringBuffer sb = new StringBuffer();
		sb.append("select COUNT(id) from sys_org where type='2'");
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
	public Object getAllusers() {
		StringBuffer sb = new StringBuffer();
		sb.append("select COUNT(id) from sys_user where role!=1");
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
	public boolean deleteMUser(String userId) {
		try {
			String sql="delete from   sys_user  where  userId = '"+userId+"'";
			this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean updatePasswd(String oPasswd,String nPasswd,String remail,String userId,String flagType) {
		String sql="update sys_user   set ";
		if(flagType.equals("l")){
			sql+=" password ='"+nPasswd+"' where password='"+oPasswd+"'";
		}else{
			if(StringUtils.isNotBlank(remail)){
				sql+=" email = '"+remail+"' ,";
			}
			sql+=" epasswd ='"+nPasswd+"' where epasswd='"+oPasswd+"'";
		}
		sql+=" and userId='"+userId+"'";
		int num = this.sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		if(num==1) return true;
		return false;
	}

}