package cn.com.burgerking.sys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.burgerking.sys.dao.CompanyStoreDao;
import cn.com.burgerking.sys.dao.SealDao;
import cn.com.burgerking.sys.dao.UserDao;
import cn.com.burgerking.sys.entity.User;
import cn.com.burgerking.sys.entity.UserCompany;
import cn.com.burgerking.sys.entity.UserSeal;
import cn.com.burgerking.sys.service.UserService;
import cn.com.burgerking.utils.ConstGlobal;
import cn.com.burgerking.utils.DesEncryptAES;
import cn.com.burgerking.utils.MD5;
import cn.com.burgerking.utils.MsgInfoEntity;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao<User> userDao;

	@Autowired
	private SealDao sealDao;
	
	@Autowired
	private CompanyStoreDao companyStoreDao;
	
	@Override
	public User findMUser(String username) {
		return userDao.findMUser(username);
	}

	@Override
	public boolean saveMUser(User manager) {
		return userDao.saveMUser(manager);
	}

	@Override
	public boolean updateMUser(User manager) {
		return userDao.updateMUser(manager);
	}

	@Override
	public boolean updateMUserPassaword(User manager) {
		return userDao.updateMUserPassaword(manager);
	}

	@Override
	public Map<String, Object> findMUsers(User user, Integer iDisplayStart, Integer iDisplayLength,String userNum,String userName) {
		return userDao.findMUsersPageInfo(user, iDisplayStart, iDisplayLength,userNum,userName);
	}

	@Override
	public MsgInfoEntity saveMgrUser(User user) {
		StringBuffer _sbf = chechUpUser(user);		
		MsgInfoEntity _rst = MsgInfoEntity.getInstanceNew();
		if(_sbf!=null){
			_rst.setStatus("0");
			_rst.setMsgInf(_sbf.toString());
			return _rst;
		}
		String userId = null;
		
		if(StringUtils.isBlank(user.getUserId())){
			//save 
			User usr = userDao.findMUser("userSerial", user.getUserSerial()+"");
			if(usr != null){
				_rst.setStatus("0");
				_rst.setMsgInf("该工号已经存在！");
				return _rst;
			}
			usr = userDao.findMUser("username", user.getUsername());
			if(usr != null){
				_rst.setStatus("0");
				_rst.setMsgInf("该用户名已经存在！");
				return _rst;
			}
			userId = UUID.randomUUID().toString().replaceAll("-", "");
			user.setPassword(MD5.GetMD5Code(user.getPassword()));
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			user.setUserId(userId);
			user.setRole("2");
			userDao.saveMUser(user);
			
		}else{
			//update
			userId = user.getUserId();
			user.setUpdateTime(new Date());
			if(user.getPassword() !=null && user.getPassword().equals(ConstGlobal.DEFAULT_PSWD)){
				//查出来的密码送至前台均是无意义的字符，真正密码均不送至前段
				user.setPassword(null);
			}else{
				user.setPassword(MD5.GetMD5Code(user.getPassword()));
			}
			 
			userDao.updateMUser(user, "userId", "'"+user.getUserId()+"'");
			 
			companyStoreDao.deleteUserCompanyRef(userId);
			sealDao.deleteUserSealRef(userId);
		}
		//insert user_company
		List<String> _coms = user.getCompanyIds();
		for (String _str : _coms) {
			if(StringUtils.isNotBlank(_str)){
				UserCompany uc = new UserCompany();
				uc.setCreateMan(user.getUsername());
				uc.setCreateTime(new Date());
				uc.setUserId(userId);
				uc.setCompanyId(_str);
				companyStoreDao.saveUserCompanyRef(uc);
			}
		}
		//insert user_seal
		List<String> _ss = user.getSealIds();
		for (String _str : _ss) {
			if(StringUtils.isNotBlank(_str)){
				UserSeal us = new UserSeal();
				us.setCreateMan(user.getUsername());
				us.setCreateTime(new Date());
				us.setUserId(userId);
				us.setSealId(_str);
				sealDao.saveUserSealRef(us);
			}
		}
		_rst.setMsgInf("用户新增成功！");
		_rst.setStatus("1");
		return _rst;
	}


	private StringBuffer chechUpUser(User user){
		StringBuffer sbf = null;
		if(StringUtils.isBlank(user.getName())){
			sbf =sbf==null? new StringBuffer():sbf;
			sbf.append("用户名不能为空！");
		}
		if(StringUtils.isBlank(user.getUserId()) && StringUtils.isBlank(user.getUsername())){
			sbf =sbf==null? new StringBuffer():sbf;
			sbf.append("用户姓名不能为空！");
		}
		if(StringUtils.isBlank(user.getEmail())){
			sbf =sbf==null? new StringBuffer():sbf;
			sbf.append("用户邮箱不能为空！");
		}
		if(StringUtils.isBlank(user.getPassword())){
			sbf =sbf==null? new StringBuffer():sbf;
			sbf.append("用户密码不能为空！");
		}
		if(null ==user.getUserSerial() || user.getUserSerial() <=0){
			sbf =sbf==null? new StringBuffer():sbf;
			sbf.append("用户工号不能为空且数量有效数字！");
		}
		return sbf;
	}
	
	@Override
	public Object getAllSeals() {
		return userDao.getAllSeals();
	}

	@Override
	public Object getAllcompanys() {
		return userDao.getAllcompanys();
	}

	@Override
	public Object getAllstores() {
		return userDao.getAllstores();
	}

	@Override
	public Object getAllusers() {
		return userDao.getAllusers();
	}

	@Override
	public MsgInfoEntity deleteMgrUser(String userId) {
		if(userId == null){
			return MsgInfoEntity.getInstance("删除用户数据传输异常！", "0");
		}
		userDao.deleteMUser(userId);
		companyStoreDao.deleteUserCompanyRef(userId);
		sealDao.deleteUserSealRef(userId);
		return MsgInfoEntity.getInstance("用户删除成功！", "1");
	}

	@Override
	public MsgInfoEntity findMUsersInfo(String userId) {
		if(userId==null){
			return MsgInfoEntity.getInstance("数据传输异常！", "0");
		}
		User ur = new User();
		ur.setUserId(userId);
		MsgInfoEntity rtn = MsgInfoEntity.getInstance();
		rtn.setData(userDao.findMUsersInfo(ur, null, null));
		rtn.setStatus("0");
		return rtn;
	}

	@Override
	public MsgInfoEntity updateUserPasswd(String oPasswd, String nPasswd,String remail ,String userId, String flagType) {
		if(oPasswd==null || nPasswd==null ||  flagType==null){
			return MsgInfoEntity.getInstance("数据不全无法完成操作！", "0");
		}else if(userId==null){
			return MsgInfoEntity.getInstance("用户登陆超时！", "400");
		}
		if(flagType!=null && flagType.equals("l")){
			oPasswd = MD5.GetMD5Code(oPasswd);
			nPasswd = MD5.GetMD5Code(nPasswd);
			boolean _rt = userDao.updatePasswd(oPasswd, nPasswd, remail,userId, flagType);
			if(_rt){
				return MsgInfoEntity.getInstance("修改登陆密码成功！", "1");				
			}else{
				return MsgInfoEntity.getInstance("修改登陆密码失败，请输入正确的原登陆密码！", "0");				
			}
		}else if(flagType!=null && flagType.equals("e")){
	        DesEncryptAES des1 = new DesEncryptAES();  
			oPasswd = des1.encrypt(oPasswd);
			nPasswd = des1.encrypt(nPasswd);
			boolean _rt = userDao.updatePasswd(oPasswd, nPasswd,remail, userId, flagType);
			if(_rt){
				return MsgInfoEntity.getInstance("修改发件箱密码成功！", "1");				
			}else{
				return MsgInfoEntity.getInstance("修改发件箱密码失败，请输入正确的原发件箱密码！", "0");				
			}
		}else{
			return MsgInfoEntity.getInstance("用户登陆超时！", "400");
		}
		
	}
 
	
}
