package cn.com.burgerking.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 供应商表
 * @author Administrator
 *
 */
@Entity
@Table(name="sys_supplier")
public class Supplier {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)   
	@Column(name="id",length=20, nullable=false)
	private int id;    				 //编号
	
	@Column(name="supplierName", length=100, nullable=true)
	private String supplierName;    	   //供应商名称
	
	@Column(name="ename", length=100, nullable=true)
	private String ename;    	   //英文名
	
	@Column(name="comName", length=100, nullable=false)
	private String comName;    	   //公司名称
	
	@Column(name="linkName", length=100, nullable=true)
	private String  linkName;			// 联系人
	
	@Column(name="linkWay", length=50, nullable=true)
	private String linkWay;			//联系方式
	
	@Column(name="email", length=1000, nullable=true)
	private String email;			//邮箱
	
	@Column(name="address", length=500, nullable=true)
	private String address;			//地址
	
	@Column(name="createTime", nullable=true)
	private Date createTime;      	//创建时间
	 
	@Column(name="updateTime", nullable=true)
	private Date updateTime;      	//最后修改时间

	@Column(name="createMan", length=100, nullable=true)
	private String createMan;			//创建人
	
	@Column(name="updateMan", length=100, nullable=true)
	private String updateMan;			//跟新人
	
	@Column(name="sscompanyName", length=100, nullable=true)
	private String sscompanyName;			//所属公司
	
	@Column(name="ssStore", length=100, nullable=true)
	private String ssStore;			//所属门店
	
	
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkWay() {
		return linkWay;
	}

	public void setLinkWay(String linkWay) {
		this.linkWay = linkWay;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getSscompanyName() {
		return sscompanyName;
	}

	public void setSscompanyName(String sscompanyName) {
		this.sscompanyName = sscompanyName;
	}

	public String getSsStore() {
		return ssStore;
	}

	public void setSsStore(String ssStore) {
		this.ssStore = ssStore;
	}
	
}
