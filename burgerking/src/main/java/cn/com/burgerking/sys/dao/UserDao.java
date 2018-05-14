package cn.com.burgerking.sys.dao;

import java.util.Map;

import cn.com.burgerking.sys.entity.User;

public interface UserDao<T> {

	User findMUser(String username);
	
	User findMUser(String columnName,String columnValue);
	
	boolean deleteMUser(String userId);
	
	boolean saveMUser(T manager);
	
	/**
	 * l:loginPassword
	 * e:send email password
	 * @param usr
	 * @param flagType
	 * @return
	 */
	boolean updatePasswd(String oPasswd,String nPasswd,String remail,String userId,String flagType);

	boolean updateMUser(T manager);
	
	boolean updateMUser(User manager,String columnWhere,Object columnWhereValue);

	boolean updateMUserPassaword(T manager);

	/**
	 * query list of users
	 * @param manager
	 * @return
	 */
	Map<String, Object> findMUsersInfo(T user, Integer iDisplayStart, Integer iDisplayLength);
	
	Map<String, Object> findMUsersPageInfo(User user,Integer iDisplayStart, Integer iDisplayLength,String userNum,String userName);
	
	Object getAllSeals();

	Object getAllcompanys();

	Object getAllstores();

	Object getAllusers();
}
