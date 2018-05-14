package cn.com.burgerking.sys.service;

import java.util.Map;

import cn.com.burgerking.sys.entity.User;
import cn.com.burgerking.utils.MsgInfoEntity;

public interface UserService {

	User findMUser(String username);

	Map<String,Object> findMUsers(User user,Integer iDisplayStart,Integer iDisplayLength,String userNum,String userName);

	MsgInfoEntity findMUsersInfo(String userId);
	
	MsgInfoEntity saveMgrUser(User user);
	
	MsgInfoEntity deleteMgrUser(String userId);
	
	boolean saveMUser(User user);

	MsgInfoEntity updateUserPasswd(String oPasswd,String nPasswd,String remail,String userId,String flagType);
	

	boolean updateMUser(User user);

	boolean updateMUserPassaword(User user);
	
	Object getAllSeals();

	Object getAllcompanys();

	Object getAllstores();

	Object getAllusers();

}
