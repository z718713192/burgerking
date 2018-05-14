package cn.com.burgerking.client.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.com.burgerking.client.service.ExcelDataService;
import cn.com.burgerking.client.service.WordDataService;
import cn.com.burgerking.client.service.impl.ExcelDataServiceImpl;
import cn.com.burgerking.client.utils.ExcelToPdfThread;
import cn.com.burgerking.client.utils.PropertiesConfig;
import cn.com.burgerking.client.utils.SendEmailUtil;
import cn.com.burgerking.client.utils.SignPdf;
import cn.com.burgerking.client.utils.WordToPdfThread;
import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.service.CompanyStoreService;

@Controller
@RequestMapping(value = "/client")
public class WordController {

	private static Logger log = LoggerFactory.getLogger(ClientController.class);
	
	@Autowired
	WordDataService wordDataService;
	
	
	@Autowired
	private CompanyStoreService storeService;
	@RequestMapping(value = "/wordFileUpload", method = RequestMethod.POST)
	@ResponseBody
	public Object wordFileUpload(HttpServletRequest request,ModelMap model){
		log.info("--------------------------------------> fileUpload");
		String res = "false";
		String fileNames = "";
		String sqlfileNames = "";
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
		List<MultipartFile> file = multiValueMap.get("wordfile");
		
		Iterator<MultipartFile> it = file.iterator();
		while (it.hasNext()) {
			MultipartFile mf = it.next();
		    String fileName = mf.getOriginalFilename();
		    if(fileName != null)
		    	sqlfileNames += "'"+fileName+"',";
		}
		log.info("--------------------------------------> 待上传文件名："+sqlfileNames);
		
		if(sqlfileNames != null && sqlfileNames.length() > 0){
			sqlfileNames = sqlfileNames.substring(0,sqlfileNames.length()-1);
			Integer count = wordDataService.countWordDataByFileName(sqlfileNames);
			if(count != 0){
				model.addAttribute("res", res);
				model.addAttribute("msg", "不能重复上传！");
				log.info("--------------------------------------> 上传失败，不能重复上传！");
				return model;
			}
		}
		
		String uploadPath = PropertiesConfig.getProperties().getProperty("saveWordPath");
		
		try {
			it = file.iterator();
			while (it.hasNext()) {
				MultipartFile mf = it.next();
				String fileName = mf.getOriginalFilename();
				File savedFile = new File(uploadPath, fileName);
				if (!savedFile.exists()) {
					savedFile.mkdirs();
				}
				mf.transferTo(savedFile);
				log.info("--------------------------------------> 上传成功："+savedFile.getAbsolutePath());
				fileNames += fileName + "|";
				res = "success";
			}
			fileNames = fileNames.substring(0, fileNames.length() - 1);
			model.addAttribute("fileName", fileNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("res", res);
		return model;
	}
	
	
	@RequestMapping(value = "/findSealWordDatas", method = RequestMethod.GET)
	@ResponseBody
	public Object findSealWordDatas( 
								@RequestParam(value="month", required=false) String month,
								@RequestParam(value="companyId", required=false) Integer companyId,
								HttpServletRequest request,ModelMap model){
		
		log.info("--------------------------------------> findExcelDatas");
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
		
//		List<ExcelData> list = excelDataService.findExcelDataByFileName(comStore.getCsName());
		List list = wordDataService.findWordData(month,comStore.getCsName(),"seal");
		log.info("--------------------------------------> 待签名数据："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
	/**
	 * 保存Word数据
	 * @param fileName
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveWordData", method = RequestMethod.POST)
	@ResponseBody
	public Object saveWordData(@RequestParam(value="fileName", required=true) String fileNames,
			@RequestParam(value="month", required=true) String month,
			HttpServletRequest request,
			ModelMap model){
		log.info("--------------------------------------> saveExcelData");
		String res = "false";
		
		try {
			HttpSession session = request.getSession();
			String userName = session.getAttribute("username")+"";
			
			if(fileNames != null && fileNames.length() > 0){
				wordDataService.saveWordData(fileNames, userName,month);
				log.info("--------------------------------------> 保存Word数据成功："+fileNames);
				res = "success";
				String saveExcelPath = PropertiesConfig.getProperties().getProperty("saveWordPath");
				String savePdfPath = PropertiesConfig.getProperties().getProperty("savePdfPath");
				savePdfPath += month+"\\";
				
				String[] fileName = fileNames.split("\\|");
				for(int i=0; i<fileName.length; i++){
					WordToPdfThread pdfThread = new WordToPdfThread(saveExcelPath+fileName[i], savePdfPath,fileName[i]);
					Thread t1 = new Thread(pdfThread);
					t1.start();
				}
			}
		} catch (Exception e) {
			model.addAttribute("msg", e.getMessage());
		}
		
		model.addAttribute("res", res);
		return model;
	}
	
	/**
	 * 电子签章
	 * 流程：先转PDF-->PDF加图片
	 * @param chkValue
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/signWseal", method = RequestMethod.POST)
	@ResponseBody
	public Object signWseal(@RequestParam(value="chkEsealValue", required=true) List chkEsealValue,
			HttpServletRequest request,
			ModelMap model){
		log.info("--------------------------------------> signEseal");
		String res = "true";
		
		if(chkEsealValue != null && chkEsealValue.size() > 0){
			HttpSession session = request.getSession();
			String userName = session.getAttribute("username")+"";
			String savePdfPath = PropertiesConfig.getProperties().getProperty("savePdfPath");
			String saveAsPdfPath = PropertiesConfig.getProperties().getProperty("saveSignPdfPath");
			String saveStampFilePatch = PropertiesConfig.getProperties().getProperty("saveStampFilePatch");
			String id = "";
			String esealId = "";
			String resFail = " ";
			for(int i=0; i<chkEsealValue.size(); i++){
				id = chkEsealValue.get(i).toString().split("\\|")[0];
				esealId = chkEsealValue.get(i).toString().split("\\|")[1];
				List list = wordDataService.findWsealById(id,esealId);
				Object []obj = (Object[]) list.get(0);
				String sourcePdfPath = savePdfPath+obj[1]+"\\"+(obj[0].toString()).split("\\.")[0]+".pdf";
				String esealPath = saveStampFilePatch+obj[2];
				File file = new File(saveAsPdfPath+obj[1]);
				if(!file.exists()){
					file.mkdirs();
				}
				boolean isSuc = SignPdf.signWPdf(sourcePdfPath, saveAsPdfPath+obj[1]+"\\"+(obj[0].toString()).split("\\.")[0]+".pdf", esealPath,obj[4].toString());
				if(isSuc)
					isSuc = wordDataService.updateWordDataById(id, "esealFlag", userName, obj[3]+"");
				else{
					resFail += obj[0]+",";
					res = "false";
				}
				log.info("--------------------------------------> 电子签章结束："+obj[0]+".pdf， 签章结果："+isSuc);
			}
			resFail = resFail.substring(0, resFail.length()-1);
			model.addAttribute("msg", resFail);
		}
		
		model.addAttribute("res", res);
		return model;
	}
	
	
	/**
	 * 查看Word pdf
	 * @param month
	 * @param companyId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findPDFWordDatas", method = RequestMethod.GET)
	@ResponseBody
	public Object findPDFWordDatas( 
								@RequestParam(value="month", required=false) String month,
								@RequestParam(value="companyId", required=false) Integer companyId,
								HttpServletRequest request,ModelMap model){
		
		log.info("--------------------------------------> findExcelDatas");
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
		
//		List<ExcelData> list = excelDataService.findExcelDataByFileName(comStore.getCsName());
		List list = wordDataService.findWordData(month,comStore.getCsName(), "pdf");
		log.info("--------------------------------------> 待签名数据："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}

	@RequestMapping(value = "/WchangePDF", method = RequestMethod.POST)
	@ResponseBody
	public Object WchangePDF( 
								@RequestParam(value="fileId", required=true) String fileId,
								HttpServletRequest request,ModelMap model){
		
		log.info("--------------------------------------> WchangePDF");
		String []id=fileId.split(",");
		HttpSession session = request.getSession();
		String userName = session.getAttribute("username")+"";
		boolean b=false;
		for (int i = 0; i < id.length; i++) {
			b=wordDataService.updateWordDataById(id[i],"pdfFlag",userName,null);
		}
		
		model.addAttribute("res",b );
		return model;
	}
	
	@RequestMapping(value = "/findOlreadyWordToPDF", method = RequestMethod.GET)
	@ResponseBody
	public Object findOlreadyWordToPDF( 
								@RequestParam(value="month", required=false) String month,
								@RequestParam(value="companyId", required=false) Integer companyId,
								HttpServletRequest request,ModelMap model){
		
		log.info("--------------------------------------> findExcelDatas");
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
		
//		List<ExcelData> list = excelDataService.findExcelDataByFileName(comStore.getCsName());
		List list = wordDataService.findEmailData(month,comStore.getCsName());
		log.info("--------------------------------------> 待签名数据："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
	
	@RequestMapping(value = "/WWsendEmail", method = RequestMethod.POST)
	@ResponseBody
	public Object WWsendEmail( 
								@RequestParam(value="fileId", required=true) String fileId,
								HttpServletRequest request,ModelMap model){
		
		log.info("--------------------------------------> WchangePDF");
		String []id=fileId.split(",");
		HttpSession session = request.getSession();
		String userName = session.getAttribute("username")+"";
		boolean b=false;
		for (int i = 0; i < id.length; i++) {
			b=wordDataService.updateWordDataById(id[i],"emailFlag",userName,null);
		}
		
		model.addAttribute("res",b );
		return model;
	}
	

	@RequestMapping(value = "/findOlreadyPDFtoEmail", method = RequestMethod.GET)
	@ResponseBody
	public Object findOlreadyPDFtoEmail( 
								@RequestParam(value="month", required=false) String month,
								@RequestParam(value="companyId", required=false) Integer companyId,
								HttpServletRequest request,ModelMap model){
		
		log.info("--------------------------------------> findExcelDatas");
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
		
//		List<ExcelData> list = excelDataService.findExcelDataByFileName(comStore.getCsName());
		List list = wordDataService.findWordDataIsSubjectEmailData(month,comStore.getCsName());
		log.info("--------------------------------------> 待签名数据："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
	
	
	@RequestMapping(value = "/sendWordSubjectEmail", method = RequestMethod.POST)
	@ResponseBody
	public Object sendSubjectEmail(@RequestParam(value="chkEsealValue", required=true) List chkEsealValue,
			HttpServletRequest request,
			ModelMap model){
		log.info("--------------------------------------> sendSubjectEmail");
		String res = "true";
		String reportDate = null;
		String subject = "汉堡王月度报表 - ";
		Multipart multipart = new MimeMultipart();
		String sendEmail = null;
		String storeId = null;
		String smtpHostName = PropertiesConfig.getProperties().getProperty("smtpHostName");
		String saveSignPdfPath = PropertiesConfig.getProperties().getProperty("saveSignPdfPath");
		int iFail = 0;
		
		try {
			HttpSession session = request.getSession();
			String userName = session.getAttribute("username")+"";
			
			for(int i=0; i<chkEsealValue.size(); i++){
				String id = chkEsealValue.get(i).toString().split("\\|")[0];
				String recEmails = chkEsealValue.get(i).toString().split("\\|")[1];
				String [] recs = recEmails.split("，");
				int num = recs.length;
				List<String> recipients = new ArrayList();
				for (int j = 0; j < num; j++) {
					recipients.add(recs[j]);
			    }

//				if(i==0){
					Map map = wordDataService.findSendEmail(id.split("\\_")[0]);
					sendEmail = (String) map.get("email");
					storeId = map.get("fileName").toString().split("\\.")[0];
					reportDate = (String) map.get("reportDate");
					subject = subject+reportDate;
					id=map.get("id").toString();
//				}
				//正文
				BodyPart contentPart = new MimeBodyPart(); 
			    contentPart.setContent("月度报表", "text/html;charset=UTF-8"); 
			    multipart.addBodyPart(contentPart);
			    //附件
			    File file = new File(saveSignPdfPath+reportDate+File.separatorChar+storeId+".pdf");
			    BodyPart attachmentBodyPart = new MimeBodyPart();
			    DataSource source = new FileDataSource(file);
			    attachmentBodyPart.setDataHandler(new DataHandler(source));
			    attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
			    multipart.addBodyPart(attachmentBodyPart);
				
			    String pw = PropertiesConfig.getProperties().getProperty(sendEmail.split("@")[0]);
				SendEmailUtil.init(sendEmail, pw, smtpHostName);
				boolean isSuc = SendEmailUtil.sendEmail(recipients, subject, multipart);
				if(isSuc)
					isSuc = wordDataService.updateWordDataIsEmail(id, recEmails, userName);
				if(!isSuc){
					res = "false";
					iFail++;
				}
				log.info("--------------------------------------> id "+id+" 处理结果："+isSuc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("res", res);
		model.addAttribute("msg", iFail);
		return model;
	}
	
	
	/**
	 * 审核数据查找
	 * @param month
	 * @param companyId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findWordAuditData", method = RequestMethod.POST)
	@ResponseBody
	public Object findAuditData(@RequestParam(value="month", required=false) String month,
			@RequestParam(value="companyId", required=false) Integer companyId,
			ModelMap model){
		log.info("--------------------------------------> findAuditData");
		
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
//		List list = excelDataService.findExcelData(month,comStore.getCsName(), "email");
		List list = wordDataService.findAuditData(month, comStore.getCsName());
		log.info("--------------------------------------> findAuditData Size："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
}
