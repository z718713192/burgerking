package cn.com.burgerking.client.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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

import org.apache.commons.lang.StringUtils;
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

import cn.com.burgerking.client.service.ClientPageService;
import cn.com.burgerking.client.service.ExcelDataService;
import cn.com.burgerking.client.service.impl.ExcelDataServiceImpl;
import cn.com.burgerking.client.utils.ExcelToPdfThread;
import cn.com.burgerking.client.utils.PropertiesConfig;
import cn.com.burgerking.client.utils.SendEmailUtil;
import cn.com.burgerking.client.utils.SignPdf;
import cn.com.burgerking.sys.controller.LoginController;
import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.User;
import cn.com.burgerking.sys.service.CompanyStoreService;

@Controller
@RequestMapping(value = "/client")
public class ClientController {

	private static Logger log = LoggerFactory.getLogger(ClientController.class);
	
	@Autowired
	ExcelDataService excelDataService;
	
	@Autowired
	ClientPageService clientPageService;	
	
	@Autowired
	private CompanyStoreService storeService;
	
	@RequestMapping(value = "/findExcelDatas", method = RequestMethod.GET)
	@ResponseBody
	public Object findExcelDatas( 
								@RequestParam(value="month", required=false) String month,
								@RequestParam(value="companyId", required=false) Integer companyId,
								HttpServletRequest request,ModelMap model){
		
		log.info("--------------------------------------> findExcelDatas");
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
		
//		List<ExcelData> list = excelDataService.findExcelDataByFileName(comStore.getCsName());
		List list = excelDataService.findExcelData(month,comStore.getCsName());
		log.info("--------------------------------------> 待签名数据："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
	
	/**
	 * 上传文件
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	@ResponseBody
	public Object fileUpload(HttpServletRequest request,ModelMap model){
		log.info("--------------------------------------> fileUpload");
		String res = "false";
		String msg = "";
		String fileNames = "";
		String sqlfileNames = "";
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
		List<MultipartFile> file = multiValueMap.get("excelfile");
		
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
			Integer count = excelDataService.countExcelDataByFileName(sqlfileNames);
			if(count != 0){
				model.addAttribute("res", res);
				model.addAttribute("msg", "不能重复上传！");
				log.info("--------------------------------------> 上传失败，不能重复上传！");
				return model;
			}
		}
		
		String uploadPath = PropertiesConfig.getProperties().getProperty("saveExcelPath");
		
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
	
	/**
	 * 保存Excel数据
	 * @param fileName
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveExcelData", method = RequestMethod.POST)
	@ResponseBody
	public Object saveExcelData(@RequestParam(value="fileName", required=true) String fileNames,
			HttpServletRequest request,
			ModelMap model){
		log.info("--------------------------------------> saveExcelData");
		String res = "false";
		
		try {
			HttpSession session = request.getSession();
			String userName = session.getAttribute("username")+"";
			
			if(fileNames != null && fileNames.length() > 0){
				excelDataService.saveExcelData(fileNames, userName);
				log.info("--------------------------------------> 保存Excel数据成功："+fileNames);
				res = "success";
				String saveExcelPath = PropertiesConfig.getProperties().getProperty("saveExcelPath");
				String savePdfPath = PropertiesConfig.getProperties().getProperty("savePdfPath");
				savePdfPath += ExcelDataServiceImpl.reportDate+"\\";
				
				String[] fileName = fileNames.split("\\|");
				for(int i=0; i<fileName.length; i++){
					ExcelToPdfThread excel1 = new ExcelToPdfThread(saveExcelPath+fileName[i], savePdfPath);
					Thread t1 = new Thread(excel1);
					t1.start();
				}
			}
		} catch (Exception e) {
			res = "false";
			model.addAttribute("msg", e.getMessage());
			e.printStackTrace();
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
	@RequestMapping(value = "/signEseal", method = RequestMethod.POST)
	@ResponseBody
	public Object signEseal(@RequestParam(value="chkEsealValue", required=true) List chkEsealValue,
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
				List list = excelDataService.findEsealById(id,esealId);
				Object []obj = (Object[]) list.get(0);
				String sourcePdfPath = savePdfPath+obj[1]+"\\"+obj[0]+".pdf";
				
				String esealPath = saveStampFilePatch+obj[2];
				File file = new File(saveAsPdfPath+obj[1]);
				if(!file.exists()){
					file.mkdirs();
				}
				boolean isSuc = SignPdf.signPdf(sourcePdfPath, saveAsPdfPath+obj[1]+"\\"+obj[0]+".pdf", esealPath);
				if(isSuc)
					isSuc = excelDataService.updateExcelDataById(id, "esealFlag", userName, obj[3]+"");
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

	
	@RequestMapping(value = "/findCompanyMessgeByMonth", method = RequestMethod.GET)
	@ResponseBody
	public Object findCompanyMessgeByMonth(@RequestParam(value="month", required=true) String month,
							HttpServletRequest request,ModelMap model){
		List<Map<String,Object>> rtnList = new ArrayList<Map<String,Object>>();
		HttpSession session = request.getSession();
		User usr = (User) session.getAttribute(LoginController.CURRENT_LOGIN_USER);
		if(usr==null){
			model.addAttribute("code","400");
			return model;
		}
		List<Map<String,Object>> dlis = clientPageService.forBarChar(usr.getUserId(), month);
		if(usr.getOfficeOrg()!=null){
			List<Map<String,Object>> lis = usr.getOfficeOrg();
			for (Map<String, Object> mp : lis) {
				String companyId = mp.get("id").toString().trim();
				Map<String,Object> _map=new HashMap<String,Object>();
				_map.put("id", companyId);
				_map.put("csName", mp.get("csName"));
				
				for (Map<String, Object> mp2 : dlis) {
					if(mp2.get("companyId")!=null){
						String _companyId = mp2.get("companyId").toString().trim();
						if(_companyId.equals(companyId)){
							Map<String,Object> map=clientPageService.allclients(mp2.get("companyId").toString().trim(),month);
							String[] _nb ={"","","",""};
							_nb[0]=map.get("emailStatus")==null?"0":map.get("emailStatus").toString();
							_nb[1]=map.get("emailFlag")==null?"0":map.get("emailFlag").toString();
							_nb[2]=map.get("pdfFlag")==null?"0":map.get("pdfFlag").toString();
							_nb[3]=map.get("inputCount").toString();
							_map.put("data", _nb);
						}
					}
				}
				
				rtnList.add(_map);
			}
		}
		 
		
		model.addAttribute("aaData", rtnList);
		return model;
	}
	
	/**
	 * 查找签章数据
	 * @param month
	 * @param companyId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findPdfData", method = RequestMethod.POST)
	@ResponseBody
	public Object findPdfData(@RequestParam(value="month", required=false) String month,
			@RequestParam(value="companyId", required=false) Integer companyId,
			ModelMap model){
		log.info("--------------------------------------> findPdfData");
		
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
		List list = excelDataService.findPdfData(month,comStore.getCsName());
		log.info("--------------------------------------> findPdfData Size："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
	
	/**
	 * 转成PDF-->发送邮件
	 * @param chkEsealValue
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toPdf", method = RequestMethod.POST)
	@ResponseBody
	public Object toPdf(@RequestParam(value="chkEsealValue", required=true) List chkEsealValue,
			HttpServletRequest request,
			ModelMap model){
		log.info("--------------------------------------> toPdf");
		
		HttpSession session = request.getSession();
		String userName = session.getAttribute("username")+"";
		String ids = StringUtils.join(chkEsealValue, ",");
		boolean isSuc = excelDataService.updateExcelDataByIds(ids, userName, "pdf");
		log.info("--------------------------------------> toPdf结束， 结果："+isSuc);
		
		return model;
	}
	
	/**
	 * 查找待发送邮箱报表
	 * @param month
	 * @param companyId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findExcelEmailData", method = RequestMethod.POST)
	@ResponseBody
	public Object findExcelEmailData(@RequestParam(value="month", required=false) String month,
			@RequestParam(value="companyId", required=false) Integer companyId,
			ModelMap model){
		log.info("--------------------------------------> findExcelEmailData");
		
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
		List list = excelDataService.findEmailData(month,comStore.getCsName());
		log.info("--------------------------------------> findExcelEmailData Size："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
	
	/**
	 * 发送邮箱-->邮件主题
	 * @param chkEsealValue
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
	@ResponseBody
	public Object sendEmail(@RequestParam(value="chkEsealValue", required=true) List chkEsealValue,
			HttpServletRequest request,
			ModelMap model){
		log.info("--------------------------------------> sendEmail");
		String res = "true";
		
		if(chkEsealValue.size() > 0){
			HttpSession session = request.getSession();
			String userName = session.getAttribute("username")+"";
			
			String ids = StringUtils.join(chkEsealValue, ",");
			boolean isSuc = excelDataService.updateExcelDataByIds(ids, userName, "email");
			log.info("--------------------------------------> sendEmail 处理结果："+isSuc);
			if(!isSuc)
				res = "false";
		}
		
		/**
		String resFail = " ";
		int iFail = 0;
		for(int i=0; i<chkEsealValue.size(); i++){
			String id = chkEsealValue.get(i).toString().split("\\|")[0];
			String recEmails = chkEsealValue.get(i).toString().split("\\|")[1];
			boolean isSuc = excelDataService.updateExcelDataIsEmail(id, recEmails, userName);
			if(!isSuc){
//				resFail += obj[0]+",";
				res = "false";
				iFail++;
			}
			log.info("--------------------------------------> id "+id+" 处理结果："+isSuc);
		}
		*/
		
		model.addAttribute("res", res);
//		model.addAttribute("msg", iFail);
		
		return model;
	}
	
	/**
	 * 查找邮件主题数据
	 * @param month
	 * @param companyId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findSubjectEmailData", method = RequestMethod.POST)
	@ResponseBody
	public Object findSubjectEmailData(@RequestParam(value="month", required=false) String month,
			@RequestParam(value="companyId", required=false) Integer companyId,
			ModelMap model){
		log.info("--------------------------------------> findSubjectEmailData");
		
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
		List list = excelDataService.findExcelDataIsSubjectEmailData(month, comStore.getCsName());
		log.info("--------------------------------------> findSubjectEmailData Size："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
	
	@RequestMapping(value = "/sendSubjectEmail", method = RequestMethod.POST)
	@ResponseBody
	public Object sendSubjectEmail(@RequestParam(value="chkEsealValue", required=true) List chkEsealValue,
			HttpServletRequest request,
			ModelMap model){
		log.info("--------------------------------------> sendSubjectEmail");
		String res = "true";
		String reportDate = null;
		String subject = "";
		
		String sendEmail = null;
		String storeId = null;
		String smtpHostName = PropertiesConfig.getProperties().getProperty("smtpHostName");
		String saveSignPdfPath = PropertiesConfig.getProperties().getProperty("saveSignPdfPath");
		int iFail = chkEsealValue.size();
		
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
					Map map = excelDataService.findSendEmail(id);
					sendEmail = (String) map.get("email");
					storeId = map.get("storeId")+"";
					reportDate = (String) map.get("reportDate");
					subject = "汉堡王月度报表 - "+reportDate;
//				}
				Multipart multipart = new MimeMultipart();
					
				//正文
				BodyPart contentPart = new MimeBodyPart(); 
			    contentPart.setContent("月度报表", "text/html;charset=UTF-8"); 
			    multipart.addBodyPart(contentPart);
			    contentPart = null;
			    
			    //附件
			    File file = new File(saveSignPdfPath+reportDate+File.separatorChar+storeId+".pdf");
			    BodyPart attachmentBodyPart = new MimeBodyPart();
			    DataSource source = new FileDataSource(file);
			    attachmentBodyPart.setDataHandler(new DataHandler(source));
			    attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
			    multipart.addBodyPart(attachmentBodyPart);
			    source = null;
			    attachmentBodyPart = null;
				
			    String pw = PropertiesConfig.getProperties().getProperty(sendEmail.split("@")[0]);
				SendEmailUtil.init(sendEmail, pw, smtpHostName);
				boolean isSuc = SendEmailUtil.sendEmail(recipients, subject, multipart);

				recipients = null;
				subject = null;
				multipart = null;
//				multipart.removeBodyPart(index);
				
				if(isSuc){
					isSuc = excelDataService.updateExcelDataIsEmail(id, recEmails, userName);
					--iFail;
				}
				if(!isSuc){
					res = "false";
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
	@RequestMapping(value = "/findAuditData", method = RequestMethod.POST)
	@ResponseBody
	public Object findAuditData(@RequestParam(value="month", required=false) String month,
			@RequestParam(value="companyId", required=false) Integer companyId,
			@RequestParam(value="companyName", required=false) String companyName,
			ModelMap model){
		log.info("--------------------------------------> findAuditData");
		
		ComStore comStore= storeService.findCompanyStoreById(companyId);
		log.info("--------------------------------------> 月份："+month);
		log.info("--------------------------------------> 门店名称："+comStore.getCsName());
//		List list = excelDataService.findExcelData(month,comStore.getCsName(), "email");
		List list = excelDataService.findAuditData(month, comStore.getCsName(),companyName);
		log.info("--------------------------------------> findAuditData Size："+list.size());
		
		model.addAttribute("dataList", list);
		return model;
	}
	
}
