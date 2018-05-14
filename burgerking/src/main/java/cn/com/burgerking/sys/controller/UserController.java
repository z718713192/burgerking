package cn.com.burgerking.sys.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.burgerking.sys.entity.User;
import cn.com.burgerking.sys.service.UserService;
import cn.com.burgerking.utils.ConstGlobal;
import cn.com.burgerking.utils.MD5;
import cn.com.burgerking.utils.MsgInfoEntity;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/sys")
public class UserController {
	
	public final static String CURRENT_LOGIN_USER ="curUser";
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Autowired
	private UserService userService;
	
	
	
	
	
	/* 汉堡王登录页面
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/hbLogin", method = RequestMethod.GET)
	public String hbLogin(HttpServletRequest request,
						HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username!=null){
			session.setAttribute("username", null);
		}
		return "hbLogin";
	}
	
	/**
	 *  汉堡王登录
	 * @param managerNo
	 * @param password
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/hbIndex", method = RequestMethod.POST)
	public ModelAndView hbMchtIndex(@RequestParam(value="managerNo", required=true) String managerNo,
						@RequestParam(value="password", required=true) String password,
						HttpServletRequest request,
						HttpServletResponse response,
						ModelAndView model) {
		User user = userService.findMUser(managerNo);
		
		if(MD5.GetMD5Code(password).equals(user.getPassword())){
			HttpSession session = request.getSession();
			session.setAttribute("username", managerNo);
			session.setAttribute(CURRENT_LOGIN_USER, user);
			model.addObject("username", managerNo);
			model.addObject("time", sdf.format(user.getUpdateTime()));
			if(user.getRole() != null && "1".equals(user.getRole())){
				model.setViewName("hbIndex");
			}else{
				model.setViewName("hbFrontIndex");
			}
			
		}else{
			model.addObject("res", "error");
			model.setViewName("hbLogin");
		}
		return model;
	}
	
	/**
	 * 修改管理员信息
	 * @param managerNo
	 * @param password
	 * @param name
	 * @param ename
	 * @param email
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateManager", method = RequestMethod.POST)
	@ResponseBody
	public Object updateManager( @RequestParam(value="managerNo", required=true) String managerNo,
						@RequestParam(value="name", required=true) String name,
						@RequestParam(value="ename", required=true) String ename,
						@RequestParam(value="email", required=true) String email,
						@RequestParam(value="id", required=false) Integer id,HttpServletRequest request,
						Model model) {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		User user = new User();
		user.setUsername(managerNo);
		user.setEname(ename);
		user.setEmail(email);
		user.setName(name);
		user.setUpdateTime(new Date());
		user.setUpdateMan(username.toString());
		user.setId(id);
		boolean falg = userService.updateMUser(user);
		model.addAttribute("res", falg);
		return model;
	}
	
	
	@RequestMapping(value = "/findManager", method = RequestMethod.GET)
	@ResponseBody
	public ModelMap findManager(HttpServletRequest request,
			HttpServletResponse response,ModelMap model){
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		User manager = userService.findMUser(username.toString());
		model.addAttribute("code","200");
		model.addAttribute("manager",manager);
		return model;
	}
	
	/**
	 * 修改管理员信息
	 * @param managerNo
	 * @param password
	 * @param name
	 * @param ename
	 * @param email
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateManaPassaword", method = RequestMethod.POST)
	@ResponseBody
	public Object updateManaPassaword(@RequestParam(value="managerNo", required=true) String managerNo,
						@RequestParam(value="password", required=true) String password,
						@RequestParam(value="id", required=false) Integer id,HttpServletRequest request,
						Model model) {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		
		User manager = userService.findMUser(managerNo);
		if(manager.getUsername()!=null){
			manager.setPassword(MD5.GetMD5Code(password));
			manager.setUpdateMan(username.toString());
			manager.setUpdateTime(new Date());
		}
		
		boolean falg = userService.updateMUserPassaword(manager);
		model.addAttribute("res", falg);
		return model;
	}
	
	
	
	
	@RequestMapping(value = "/hbFrontLogin", method = RequestMethod.GET)
	public String hbFrontLogin(HttpServletRequest request,
						HttpServletResponse response, Model model) {
		HttpSession session = request.getSession();
		Object mchtId = session.getAttribute("username");
		if(mchtId!=null){
			session.setAttribute("username", null);
		}
		return "hbFrontLogin";
	}
	
	/**
	 * 
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @param sEcho
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/userMgrPage", method = RequestMethod.GET)
	@ResponseBody
	public Object userMgrPage(@RequestParam(value="iDisplayStart", required=true) Integer iDisplayStart, 
			@RequestParam(value="iDisplayLength", required=true) Integer iDisplayLength,
			@RequestParam(value="sEcho", required=true) String sEcho,
			@RequestParam(value="userNum", required=false) String userNum,
			@RequestParam(value="userName", required=false) String userName,
								HttpServletRequest request,ModelMap model){
		
		Map<String, Object> _mp = userService.findMUsers(null, iDisplayStart, iDisplayLength,userNum,userName);
		model.addAttribute("aaData", _mp.get("aaData"));
		model.addAttribute("asData", _mp.get("asData"));
		model.addAttribute("iTotalRecords", _mp.get("iTotalRecords"));
		model.addAttribute("iTotalDisplayRecords", _mp.get("iTotalDisplayRecords"));
		model.addAttribute("sEcho", sEcho);
		return model;
	}
	
	/**
	 * 
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @param sEcho
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/userMgrInfo", method = RequestMethod.POST)
	@ResponseBody
	public MsgInfoEntity userMgrInfo( @RequestParam(value="id", required=true) String userId,
			HttpServletRequest request,ModelMap model){
		MsgInfoEntity _rt = userService.findMUsersInfo(userId);
		return _rt;
	}
	
	
	@RequestMapping(value = "/userMgrSave", method = RequestMethod.POST)
	@ResponseBody
	public MsgInfoEntity savaStore(@ModelAttribute User user,HttpServletRequest request) {
		MsgInfoEntity msi =null;
		try {
			HttpSession session = request.getSession();
			Object username = session.getAttribute("username");
			if(username==null){
				msi =MsgInfoEntity.getInstanceNew();
				msi.setStatus("0");
				return msi;
			}
			user.setUpdateMan(username.toString());
			user.setCreateMan(username.toString());
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			msi = userService.saveMgrUser(user);
			
		} catch (Exception e) {
			e.printStackTrace();
			msi.setStatus("0");
		}
	
		return msi;
	}
	
	@RequestMapping(value = "/userMgrDelete", method = RequestMethod.POST)
	@ResponseBody
	public MsgInfoEntity userMgrDelete(@RequestParam(value="id", required=true) String userId,HttpServletRequest request) {
		MsgInfoEntity msi =null;
		try {
			if(StringUtils.isBlank(userId)){
				msi =MsgInfoEntity.getInstanceNew();
				msi.setStatus("0");
				msi.setMsgInf("数据传输错误！");
				return msi;
			}
			msi = userService.deleteMgrUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			msi.setStatus("0");
		}
		
		return msi;
	}
	

	@RequestMapping(value = "/findAllMessage", method = RequestMethod.GET)
	@ResponseBody
	public ModelMap findAllMessage(HttpServletRequest request,
			HttpServletResponse response,ModelMap model){
		Map<Object, Object> map=new HashMap<Object, Object>();
		map.put("sealNum", userService.getAllSeals());
		map.put("companyNum", userService.getAllcompanys());
		map.put("storeNum", userService.getAllstores());
		map.put("userNum", userService.getAllusers());
		model.addAttribute("map",map);
		return model;
	}
	
	@RequestMapping(value = "/hbFrontIndex", method = RequestMethod.POST)
	public ModelAndView hbFrontIndex(@RequestParam(value="username", required=true) String username,
						@RequestParam(value="password", required=true) String password,
						HttpServletRequest request,
						HttpServletResponse response,
						ModelAndView model) {
		User user = userService.findMUser(username);
		
		if(MD5.GetMD5Code(password).equals(user.getPassword())){
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute(CURRENT_LOGIN_USER, user);
			model.addObject("username", username);
			model.addObject("time", sdf.format(user.getUpdateTime()));
			model.setViewName("hbFrontIndex");
		}else{
			model.addObject("res", "error");
			model.setViewName("hbFrontLogin");
		}
		return model;
	}
	
	
	@RequestMapping(value = "/clt/usrUp", method = RequestMethod.POST)
	public ModelAndView updateUserInfo4Client(@RequestParam(value="username", required=true) String username,
						@RequestParam(value="password", required=true) String password,
						HttpServletRequest request,
						HttpServletResponse response,
						ModelAndView model) {
		User user = userService.findMUser(username);
		
		if(MD5.GetMD5Code(password).equals(user.getPassword())){
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute(CURRENT_LOGIN_USER, user);
			model.addObject("username", username);
			model.addObject("time", sdf.format(user.getUpdateTime()));
			model.setViewName("hbFrontIndex");
		}else{
			model.addObject("res", "error");
			model.setViewName("hbFrontLogin");
		}
		return model;
	}
	
	@RequestMapping(value = "/clt/usrPwd4c", method = RequestMethod.POST)
	@ResponseBody
	public MsgInfoEntity usrPwd4c(@RequestParam(value="od", required=true) String oPasswd,
			@RequestParam(value="nd", required=true) String nPasswd,
			@RequestParam(value="rd", required=true) String rPasswd,
			@RequestParam(value="re", required=true) String remail,
			@RequestParam(value="ft", required=true) String flagType,
			HttpServletRequest request) {
		MsgInfoEntity msi =null;
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(CURRENT_LOGIN_USER);
			if(user==null){
				msi =MsgInfoEntity.getInstanceNew();
				msi.setStatus("0");
				msi.setMsgInf("数据传输错误！");
				return msi;
			}else{
				MsgInfoEntity rt = userService.updateUserPasswd(oPasswd, nPasswd,remail, user.getUserId(), flagType);
				user = userService.findMUser(user.getUsername());
				session.setAttribute(CURRENT_LOGIN_USER, user);
				return rt;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msi.setStatus("0");
		}
		
		return msi;
	}
	
	@RequestMapping(value = "/curtUsrInfoPg", method = RequestMethod.POST)
	@ResponseBody
	public MsgInfoEntity curtUsrInfoPg2(HttpServletRequest request) {
		MsgInfoEntity msi =null;
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(CURRENT_LOGIN_USER);
			if(user==null){
				msi =MsgInfoEntity.getInstanceNew();
				msi.setStatus("0");
				msi.setMsgInf("数据传输错误！");
				return msi;
			}else{
				User rs = new User();
				BeanUtils.copyProperties(user, rs);
				rs.setPassword(ConstGlobal.DEFAULT_PSWD);
				rs.setCreateMan(null);
				rs.setCreateTime(null);
				rs.setUpdateMan(null);
				rs.setUpdateTime(null);
				rs.setId(0);
				rs.setRole(null);
				rs.setUserId(null);
				msi =MsgInfoEntity.getInstanceNew();
				msi.setStatus("0"); 
				msi.setData2(rs); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			msi.setStatus("0");
		}
		
		return msi;
	}
	
}