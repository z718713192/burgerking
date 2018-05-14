package cn.com.burgerking.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 印章表
 * @author Administrator
 *
 */
@Entity
@Table(name="sys_seal")
public class Seal {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)   
	@Column(name="id",length=20, nullable=false)
	private int id;    				 //编号
	
	@Column(name="sealName", length=100, nullable=true)
	private String sealName;    	//印章名称
	
	@Column(name="img", length=200, nullable=true)
	private String img;			//印章图片
	

	@Column(name="imgName", length=200, nullable=true)
	private String imgName;			//印章图片名称
	
	
	@Column(name="createMan", length=100, nullable=true)
	private String createMan;			//创建人
	
	@Column(name="updateMan", length=100, nullable=true)
	private String updateMan;			//跟新人
	
	@Column(name="createTime", nullable=true)
	private Date createTime;      	//创建时间
	 
	@Column(name="updateTime", nullable=true)
	private Date updateTime;      	//最后修改时间

	@Column(name="sscompanyName", length=100,  nullable=true)
	private String sscompanyName;      	//所属公司
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSealName() {
		return sealName;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
}
