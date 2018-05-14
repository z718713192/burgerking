package cn.com.burgerking.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 公司-门店表
 * @author Administrator
 *
 */
@Entity
@Table(name="sys_org")
public class ComStore {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)   
	@Column(name="id",length=20, nullable=false)
	private int id;    				 	//编号
	
	@Column(name="storeId", length=20, nullable=true)
	private String storeId;    	   		//商店编号
	
	
	@Column(name="csName", length=200, nullable=true)
	private String csName;    	   		//名称
	
	@Column(name="identifyNumber", length=50, nullable=true)
	private String  identifyNumber;		//识别号
	
	@Column(name="address", length=200, nullable=true)
	private String address;				//地址
	
	@Column(name="email", length=50, nullable=true)
	private String email;				//邮箱
	
	@Column(name="emailPassword", length=50, nullable=true)
	private String emailPassword;				//邮箱密码
	
	@Column(name="openTime", length=20, nullable=true)
	private String openTime;			//开业时间
	
	@Column(name="status", length=50, nullable=true)
	private String status;				//状态
	
	@Column(name="type", length=20, nullable=true)
	private String type;				//类型（1：分公司2：门店）
	
	@Column(name="closeTime", length=20, nullable=true)
	private String closeTime;			//闭店时间
	
	@Column(name="linkman", length=100, nullable=true)
	private String linkman;				//联系人
	
	@Column(name="phone", length=20, nullable=true)
	private String phone;				// 电话
	
	@Column(name="createTime", nullable=true)
	private Date createTime;      		//创建时间
	 
	@Column(name="defautSupplier", length=20, nullable=true)
	private String defautSupplier;				// 默认供应商
	
	
	@Column(name="updateTime", nullable=true)
	private Date updateTime;      		//最后修改时间
	
	@Column(name="createMan", length=20, nullable=true)
	private String createMan;			//创建人
	
	@Column(name="updateMan", length=20, nullable=true)
	private String updateMan;			//跟新人

	@Column(name="defautSupplierName", length=100, nullable=true)
	private String defautSupplierName;				// 默认供应商
	
	
	@Column(name="sscompanyName", length=100, nullable=true)
	private String sscompanyName;				// 默认供应商
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCsName() {
		return csName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public void setCsName(String csName) {
		this.csName = csName;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefautSupplier() {
		return defautSupplier;
	}

	public void setDefautSupplier(String defautSupplier) {
		this.defautSupplier = defautSupplier;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public String getDefautSupplierName() {
		return defautSupplierName;
	}

	public void setDefautSupplierName(String defautSupplierName) {
		this.defautSupplierName = defautSupplierName;
	}

	public String getSscompanyName() {
		return sscompanyName;
	}

	public void setSscompanyName(String sscompanyName) {
		this.sscompanyName = sscompanyName;
	}
	
}
