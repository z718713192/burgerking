package cn.com.burgerking.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 门店-公司关联表
 * @author Administrator
 *
 */
@Entity
@Table(name="sys_company_store")
public class CompanyStore{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)   
	@Column(name="id",length=20, nullable=false)
	private int id;    				 //编号
	
	@Column(name="storeId", length=20, nullable=false)
	private String storeId;    	// 门店编号
	
	@Column(name="comStoreId", length=20, nullable=false)
	private String comStoreId;			//公司编号
	
	@Column(name="updateTime", nullable=true)
	private Date updateTime;      	//创建时间
	
	@Column(name="createMan", length=20, nullable=true)
	private String createMan;			//创建人
	
	@Column(name="createTime", nullable=true)
	private Date createTime;      	//创建时间

	@Column(name="updateMan", length=20, nullable=true)
	private String updateMan;			//跟新人
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getComStoreId() {
		return comStoreId;
	}

	public void setComStoreId(String comStoreId) {
		this.comStoreId = comStoreId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}

}
