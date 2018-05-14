package cn.com.burgerking.client.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_word_data")
public class WordData {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)   
	@Column(name="id",length=20, nullable=false)
	private Integer id;						//主键
	
	@Column(name="fileName", length=200, nullable=true)
	private String fileName;				//文件名称
	
	@Column(name="storeId")
	private String storeId;				//门店ID
	
	@Column(name="storeName" , length=200, nullable=true)
	private String storeName;				//门店名称
	
	@Column(name="corporateName" , length=200, nullable=true)
	private String corporateName;			//公司名称
	
	@Column(name="reportDate" , length=200, nullable=true)
	private String reportDate;				//报表日期 
	
	@Column(name="esealFlag", insertable=false)
	private String esealFlag;				//0-未盖章  1-已盖章
	
	@Column(name="pdfFlag", insertable=false)
	private String pdfFlag;					//0-未转换PDF  1-已转换PDF
	
	@Column(name="emailFlag", insertable=false)
	private String emailFlag;				//0-未发送  1-已发送
	
	@Column(name="signEseal", insertable=false)
	private String signEseal;				//印章名称
	
	@Column(name="emailStatus", insertable=false)
	private String emailStatus;				//0-未回复  1-已回复
	
	@Column(name="keyword", length=200, nullable=true)
	private String keyword;					//关键字
	
	@Column(name="pdfName", length=200, nullable=true)
	private String pdfName;					//转成pdf后的地址
	
	@Column(name="createTime")
	private Date createTime;				//添加时间
	
	@Column(name="operator")
	private String operator;				//操作人

	@Column(name="receiveEmail", insertable=false)
	private String receiveEmail;			//收件箱地址
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getCorporateName() {
		return corporateName;
	}
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	public String getPdfFlag() {
		return pdfFlag;
	}
	public void setPdfFlag(String pdfFlag) {
		this.pdfFlag = pdfFlag;
	}
	public String getEmailFlag() {
		return emailFlag;
	}
	public void setEmailFlag(String emailFlag) {
		this.emailFlag = emailFlag;
	}
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getPdfName() {
		return pdfName;
	}
	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}
	public String getEsealFlag() {
		return esealFlag;
	}
	public void setEsealFlag(String esealFlag) {
		this.esealFlag = esealFlag;
	}
	public String getSignEseal() {
		return signEseal;
	}
	public void setSignEseal(String signEseal) {
		this.signEseal = signEseal;
	}
	public String getReceiveEmail() {
		return receiveEmail;
	}
	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}
	

}
