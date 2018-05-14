package cn.com.burgerking.sys.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.com.burgerking.client.utils.PropertiesConfig;
import cn.com.burgerking.sys.entity.Seal;
import cn.com.burgerking.sys.entity.SealComStore;
import cn.com.burgerking.sys.entity.User;
import cn.com.burgerking.sys.service.SealService;
import cn.com.burgerking.utils.MsgInfoEntity;

/**
 * Handles requests for the application home page.
 */ 

@Controller
@RequestMapping(value = "/sys")
public class SealController {
	
	private static final Logger logger = LoggerFactory.getLogger(SealController.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Autowired
	private SealService sealService;

    @Autowired  
    private HttpServletRequest request; 
    
    private SimpleDateFormat sdfFolder = new SimpleDateFormat("yyMM");
	
	/**
	 * 分页查询印章
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @param sEcho
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findSeals", method = RequestMethod.GET)
	@ResponseBody
	public Object findSeals(@RequestParam(value="iDisplayStart", required=true) Integer iDisplayStart, 
								@RequestParam(value="iDisplayLength", required=true) Integer iDisplayLength,
								@RequestParam(value="sEcho", required=true) String sEcho,
								@RequestParam(value="sealName", required=false) String sealName,
								@RequestParam(value="sealCom", required=false) String sealCom,
								HttpServletRequest request,ModelMap model){
		
		List<Map<Object,Object>> list = sealService.findSeals(iDisplayStart, iDisplayLength,sealName,sealCom);
		int count = sealService.findSealCount(sealName,sealCom);
		
		model.addAttribute("aaData", list);
		model.addAttribute("iTotalRecords", count);
		model.addAttribute("iTotalDisplayRecords", count);
		model.addAttribute("sEcho", sEcho);
		return model;
	}
	
	
	@RequestMapping(value = "/savaSeal", method = RequestMethod.POST)
	@ResponseBody
	public Object savaSeal(
						@RequestParam(value="imgUrl", required=false) MultipartFile imgUrl,
						@RequestParam(value="sealName", required=true) String sealName,
						@RequestParam(value="companyId", required=true) String companyId,
						@RequestParam(value="storeId", required=true) String[] storeIds,
						@RequestParam(value="id", required=true) Integer id,
						Model model) {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		try {
			String suffix= imgUplode(imgUrl, model);
			Seal seal=new Seal();
			seal.setSealName(sealName);
			seal.setImgName(suffix);
			seal.setImg(suffix);
			seal.setCreateMan(username.toString());
			seal.setUpdateMan(username.toString());
			seal.setCreateTime(new Date());
			seal.setUpdateTime(new Date());
			
			List<SealComStore> comStors=new ArrayList<SealComStore>();
			for (int i = 0; i < storeIds.length; i++) {
				SealComStore comStore=new SealComStore();
				comStore.setCompanyId(companyId);
				comStore.setCreateTime(new Date());
				comStore.setCreateMan(username.toString());
				comStore.setStoreId(storeIds[i]);
				comStors.add(comStore);
			}
			boolean flag= sealService.savaSeal(seal,comStors);
			model.addAttribute("res", flag);
		} catch (IllegalStateException e) {
			logger.error(e.toString(), e);
			e.printStackTrace();
			model.addAttribute("res", "uploaderror");
		} catch (IOException e) {
			logger.error(e.toString(), e);
			e.printStackTrace();
			model.addAttribute("res", "uploaderror");
		}
         //
		return model;
	}
	
	
	@RequestMapping(value = "/updateSeal", method = RequestMethod.POST)
	@ResponseBody
	public Object updateSeal(
						@RequestParam(value="imgUrl", required=false) MultipartFile imgUrl,
						@RequestParam(value="sealName", required=true) String sealName,
						@RequestParam(value="companyId", required=true) String companyId,
						@RequestParam(value="storeId", required=true) String[] storeIds,
						@RequestParam(value="id", required=true) Integer id,
						@RequestParam(value="ismodify", required=true) boolean ismodify,
						Model model) {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		if(ismodify){
			try {
				String suffix= imgUplode(imgUrl, model);
				Seal seal=new Seal();
				seal.setId(id);
				seal.setSealName(sealName);
				seal.setImgName(suffix);
				seal.setImg(PropertiesConfig.getProperties().getProperty("saveStampFilePatch")+"/"+suffix);
				seal.setCreateMan(username.toString());
				seal.setUpdateMan(username.toString());
				seal.setCreateTime(new Date());
				seal.setUpdateTime(new Date());
				
				List<SealComStore> comStors=new ArrayList<SealComStore>();
				for (int i = 0; i < storeIds.length; i++) {
					SealComStore comStore=new SealComStore();
					comStore.setCompanyId(companyId);
					comStore.setCreateTime(new Date());
					comStore.setCreateMan(username.toString());
					comStore.setStoreId(storeIds[i]);
					comStore.setSealId(id+"");
					comStors.add(comStore);
				}
				boolean flag= sealService.updateSeal(seal,comStors);
				model.addAttribute("res", flag);
			} catch (IllegalStateException e) {
				logger.error(e.toString(), e);
				e.printStackTrace();
				model.addAttribute("res", "uploaderror");
			} catch (IOException e) {
				logger.error(e.toString(), e);
				e.printStackTrace();
				model.addAttribute("res", "uploaderror");
			}
		}else{
			Seal seal=new Seal();
			seal.setSealName(sealName);
			seal.setId(id);
			seal.setImg(null);
			seal.setUpdateMan(username.toString());
			seal.setUpdateTime(new Date());
			
			List<SealComStore> comStors=new ArrayList<SealComStore>();
			for (int i = 0; i < storeIds.length; i++) {
				SealComStore comStore=new SealComStore();
				comStore.setCompanyId(companyId);
				comStore.setCreateTime(new Date());
				comStore.setCreateMan(username.toString());
				comStore.setStoreId(storeIds[i]);
				comStore.setSealId(id+"");
				comStors.add(comStore);
			}
			boolean flag= sealService.updateSeal(seal,comStors);
			model.addAttribute("res", flag);
		}
		
         //
		return model;
	}
	
	@RequestMapping(value = "/findSealById", method = RequestMethod.POST)
	@ResponseBody
	public Object findSealById(@RequestParam(value="id", required=true) Integer id, ModelMap model){
		
		Seal seal= sealService.findASealById(id);
		
		List<SealComStore> list=sealService.findSealComStoreBySealId(seal.getId());
		
		model.addAttribute("seal", seal);
		model.addAttribute("list", list);
		return model;
	}
	
	
	@RequestMapping(value = "/delSealById", method = RequestMethod.POST)
	@ResponseBody
	public Object delSealById(@RequestParam(value="id", required=true) Integer id,ModelMap model,HttpServletRequest request){
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		boolean flag = sealService.delSealById(id);
		model.addAttribute("res", flag);
		
		return model;
	}
	
	public String imgUplode(MultipartFile imgUrl,Model model) throws IllegalStateException, IOException{
		String filename = imgUrl.getOriginalFilename();
        logger.info("文件名="+filename);
		// 获得容器中上传文件夹所在的物理路径
		Date date = new Date();
		String savePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "resources" + File.separator + "sealImgs" + File.separator + sdfFolder.format(date) + File.separator;
		logger.info("路径" + savePath);
		String url=PropertiesConfig.getProperties().getProperty("saveStampFilePatch");
		System.err.println(url);
        //上传文件目标
        File targetFile = new File(url, filename); 
        //上传文件重命名后的名字
        String suffix = String.valueOf(new Date().getTime())+"_"+filename;
        //重命名的目标文件
        File newFile = new File(url, suffix);
        //判断目标文件是否存在
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
         }
		imgUrl.transferTo(targetFile);//上传
		targetFile.renameTo(newFile);//重命名
		
		return suffix;
		
	}
	
	
	@RequestMapping(value = "/companyWithSealsTree", method = RequestMethod.POST)
	@ResponseBody
	public Object companyWithSealsTree(ModelMap model){
		List<Map<String,Object>> maps=sealService.companyWithSealsTree(null);
		if(maps == null){
			new ArrayList<Map<Object,Object>>();
		}
		model.addAttribute("maps", maps);
		return model;
	}
	
	@RequestMapping(value = "/findSealsByStoreId", method = RequestMethod.GET)
	@ResponseBody
	public MsgInfoEntity findSealsByStoreId(@RequestParam(value="storeId", required=true) long id,HttpServletRequest request) {
		MsgInfoEntity msi =null;
		try {
			 msi = MsgInfoEntity.getInstanceNew();
			 msi.setData2(sealService.findAllCompanySealByStoreId(id));
			 
		} catch (Exception e) {
			e.printStackTrace();
			msi.setStatus("0");
		}
	
		return msi;
	}
	
}