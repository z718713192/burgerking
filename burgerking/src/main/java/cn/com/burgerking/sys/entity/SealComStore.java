package cn.com.burgerking.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 印章—公司-门店关联表
 * @author Administrator
 *
 */
@Entity
@Table(name="sys_seal_comstore")
public class SealComStore {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)   
	@Column(name="id",length=20, nullable=false)
	private int id;    				 //编号
	
	@Column(name="sealId", length=20, nullable=false)
	private String sealId;    	// 印章编号
	
	@Column(name="companyId", length=20, nullable=false)
	private String companyId;			//公司编号
	
	@Column(name="storeId", length=20, nullable=false)
	private String storeId;			//门店编号
	
	@Column(name="createTime", nullable=true)
	private Date createTime;      	//创建时间
 
	@Column(name="createMan", length=20, nullable=true)
	private String createMan;			//创建人
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSealId() {
		return sealId;
	}

	public void setSealId(String sealId) {
		this.sealId = sealId;
	}


	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
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
	
	
}
