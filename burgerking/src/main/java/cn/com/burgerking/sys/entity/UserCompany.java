package cn.com.burgerking.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 用户-公司关联表
 * @author Administrator
 *
 */
@Entity
@Table(name="sys_user_company")
public class UserCompany {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)   
	@Column(name="id",length=20, nullable=false)
	private int id;    				 //编号
	
	@Column(name="userId", length=36, nullable=false)
	private String userId;    	//用户编号
	
	@Column(name="companyId", length=20, nullable=false)
	private String companyId;			//公司编号
	
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
