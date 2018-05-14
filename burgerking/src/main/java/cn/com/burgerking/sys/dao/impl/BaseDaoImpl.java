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

@Repository
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> {

	public static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

	@Autowired
	public SessionFactory sessionFactory;
	
	/**
	 * 完整的SQL
	 * 
	 * @param full_sql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int count(String full_sql) {
		String _sql = "select count(1) from (" + full_sql + ") tb";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(_sql);
		List list = query.setResultTransformer(Transformers.TO_LIST).list();
		if (list != null && list.size() > 0) {
			_sql = null;
			full_sql = null;
			Object obj = list.get(0);
			if (obj != null) {
				return Integer.parseInt(obj.toString().replace("[", "").replace("]", ""));
			}
		}
		return 0;
	}

	/**
	 * 查询数据列表 Json， （iDisplayStart ==null or iDisplayLength ==null 不分页查询）
	 * 
	 * @param full_sql
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @return
	 */
	public List<Map<String, Object>> findListMap(String full_sql, Integer iDisplayStart, Integer iDisplayLength) {
		if (iDisplayStart != null && iDisplayLength != null) {
			full_sql += " LIMIT " + iDisplayStart + "," + iDisplayLength;
		}
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(full_sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		full_sql = null;
		if (list != null && list.size() > 0)
			return list;
		return new ArrayList<Map<String, Object>>();
	}
	
	/**
	 * 查询数据列表 Json， （iDisplayStart ==null or iDisplayLength ==null 不分页查询）
	 * 
	 * @param full_sql
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @return
	 */
	public Map<String, Object> findOne4Map(String full_sql) {
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(full_sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		full_sql = null;
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 查询获取对象
	 * 
	 * @param full_sql
	 * @param t
	 * @return
	 */
	public T findOne(String full_sql, Class<T> cls) {
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(full_sql).addEntity(cls);
		List<T> list = query.list();
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 查询获取对象
	 * 
	 * @param full_sql
	 * @param t
	 * @return
	 */
	public List<T> findList(String full_sql, Class<T> cls) {
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(full_sql).addEntity(cls);
		List<T> list = query.list();
		if (list != null && list.size() > 0)
			return list;
		return null;
	}
	
	/**
	 * 查询获取对象
	 * 
	 * @param full_sql
	 * @param t
	 * @return
	 */
	public List<T> findListWithPagination(String full_sql, Class<T> cls, Integer iDisplayStart, Integer iDisplayLength) {
		if (iDisplayStart != null && iDisplayLength != null) {
			full_sql += " LIMIT " + iDisplayStart + "," + iDisplayLength;
		}
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(full_sql).addEntity(cls);
		List<T> list = query.list();
		if (list != null && list.size() > 0)
			return list;
		return null;
	}

}
