package cn.com.burgerking.sys.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.com.burgerking.utils.DesEncryptAES;


/**
 * 用户表
 * @author Administrator
 *
 */
@Entity
@Table(name="sys_user")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)   
	@Column(name="id",length=20, nullable=false)
	private int id;    				 //编号
	
	@Column(name="userId", length=36, nullable=true)
	private String userId;
	
	@Column(name="userSerial", nullable=true)
	private Long userSerial;        //工号
	
	@Column(name="username", length=50, nullable=true)
	private String username;    	//
	
	@Column(name="name", length=50, nullable=true)
	private String name;			//姓名
	
	@Column(name="ename", length=50, nullable=true)
	private String  ename;			//英文名
	
	@Column(name="password", length=100, nullable=true)
	private String password;       	//密码

	@Column(name="email", length=50, nullable=true)
	private String email;           //邮箱

	@Column(name="epasswd", length=90, nullable=true)
	private String epasswd;           //邮箱
	
	@Column(name="role", length=50, nullable=true)
	private String role;           //1：系统管理员 2：公司  3：门店
	
	@Column(name="phone", length=20, nullable=true)
	private String phone;           // phone
	
	@Column(name="createTime", nullable=true)
	private Date createTime;      	//创建时间
	 
	@Column(name="updateTime", nullable=true)
	private Date updateTime;      	//最后修改时间
	
	@Column(name="createMan", length=20, nullable=true)
	private String createMan;			//创建人
	
	@Column(name="updateMan", length=20, nullable=true)
	private String updateMan;			//跟新人
	
	@Transient
	private String companyId;
	
	@Transient
	private String companyName;
	
	@Transient
	private List<String> companyIds;
	
	@Transient
	private List<String> sealIds;
	
	@Transient
	private List<Map<String,Object>> officeOrg;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateMan() {
		return createMan;
	}
	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
	public String getUpdateMan() {
		return updateMan;
	}
	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Long getUserSerial() {
		return userSerial;
	}
	public void setUserSerial(Long userSerial) {
		this.userSerial = userSerial;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<String> getCompanyIds() {
		return companyIds;
	}
	public void setCompanyIds(List<String> companyIds) {
		this.companyIds = companyIds;
	}
	public List<String> getSealIds() {
		return sealIds;
	}
	public void setSealIds(List<String> sealIds) {
		this.sealIds = sealIds;
	}
	public String getEpasswd() {
		return epasswd;
	}
	public void setEpasswd(String epasswd) {
		this.epasswd = epasswd;
	}
	
	public List<Map<String, Object>> getOfficeOrg() {
		return officeOrg;
	}
	public void setOfficeOrg(List<Map<String, Object>> officeOrg) {
		this.officeOrg = officeOrg;
	}
	/**
	 * 邮箱密码加密
	 */
	@Transient
	public void resetEpasswd(){
		if(epasswd!=null){
			this.epasswd = DesEncryptAES.encrypt(epasswd);
		}
	}
	
	/**
	 * 发送邮箱时候需要解密
	 * @return
	 */
	@Transient
	public String epasswd(){
		if(epasswd==null)
		return null;
		return DesEncryptAES.decrypt(epasswd);
	}
}
