package cn.com.burgerking.sys.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.CompanyStore;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;
import cn.com.burgerking.sys.service.CompanyStoreService;

@Controller
@RequestMapping(value = "/sys")
public class CompanyStoreController {
	@Autowired
	private CompanyStoreService storeService;
	
	
	/**
	 * 新增或修改分公司
	 * @param csName
	 * @param identifyNumber
	 * @param phone
	 * @param address
	 * @param email
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/savaCompany", method = RequestMethod.POST)
	@ResponseBody
	public Object savaCompany(@RequestParam(value="csName", required=true) String csName,
						@RequestParam(value="identifyNumber", required=true) String identifyNumber,
						@RequestParam(value="phone", required=true) String phone,
						@RequestParam(value="address", required=true) String address,
						@RequestParam(value="email", required=true) String email,
						@RequestParam(value="emailPassword", required=true) String emailPassword,
						@RequestParam(value="id", required=false) Integer id,HttpServletRequest request,
						HttpServletResponse response,
						Model model) {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		ComStore comStore= storeService.findCompanyStoreById(id);
		if(comStore==null){
			comStore=new ComStore();
			comStore.setUpdateTime(new Date());
			comStore.setCreateTime(new Date());
		}
		comStore.setCsName(csName);
		comStore.setIdentifyNumber(identifyNumber);
		comStore.setPhone(phone);
		comStore.setAddress(address);
		comStore.setEmail(email);
		comStore.setEmailPassword(emailPassword);
		comStore.setType("1");
		comStore.setCreateMan(username.toString());
		comStore.setUpdateMan(username.toString());
		boolean falg =false;
		if(id==null){
			falg=storeService.saveCompanyStore(comStore);
		}else{
			falg=storeService.updateCompanyStore(comStore);
		}
		model.addAttribute("res", falg);
		return model;
	}
	
	/**
	 * 分页查询分公司
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @param sEcho
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findCompanys", method = RequestMethod.GET)
	@ResponseBody
	public Object findCompanys(@RequestParam(value="iDisplayStart", required=true) Integer iDisplayStart, 
								@RequestParam(value="iDisplayLength", required=true) Integer iDisplayLength,
								@RequestParam(value="sEcho", required=true) String sEcho,
								@RequestParam(value="branchOffice", required=false) String branchOffice,
								HttpServletRequest request,ModelMap model){
		List<Map<Object,Object>> list = storeService.findCompanys(iDisplayStart, iDisplayLength,branchOffice);
		int count = storeService.findCompanyCount(branchOffice);
		model.addAttribute("aaData", list);
		model.addAttribute("iTotalRecords", count);
		model.addAttribute("iTotalDisplayRecords", count);
		model.addAttribute("sEcho", sEcho);
		return model;
	}
	
	/**
	 * 删除分公司
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/delCompany", method = RequestMethod.POST)
	@ResponseBody
	public Object delCompany(@RequestParam(value="id", required=true) Integer id,ModelMap model,HttpServletRequest request){
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		boolean flag = storeService.delCompany(id);
		model.addAttribute("res", flag);
		
		return model;
	}
	
	@RequestMapping(value = "/findCompany", method = RequestMethod.POST)
	@ResponseBody
	public Object findCompany(@RequestParam(value="id", required=true) Integer id, ModelMap model){
		
		ComStore comStore= storeService.findCompanyStoreById(id);
		
		model.addAttribute("comStore", comStore);
		
		return model;
	}
	
	/**
	 * 分页查询门店信息
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @param sEcho
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findCStores", method = RequestMethod.GET)
	@ResponseBody
	public Object findCStores(@RequestParam(value="iDisplayStart", required=true) Integer iDisplayStart, 
								@RequestParam(value="iDisplayLength", required=true) Integer iDisplayLength,
								@RequestParam(value="sEcho", required=true) String sEcho,
								@RequestParam(value="storeId", required=false) String storeId,
								HttpServletRequest request,ModelMap model){
		
		List<Map<String, Object>> list = storeService.findStores(iDisplayStart, iDisplayLength,storeId);
		int count = storeService.findStoreCount(storeId);
		
		model.addAttribute("aaData", list);
		model.addAttribute("iTotalRecords", count);
		model.addAttribute("iTotalDisplayRecords", count);
		model.addAttribute("sEcho", sEcho);
		return model;
	}
	
	/**
	 * 查询所有分公司
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findAllCompany", method = RequestMethod.GET)
	@ResponseBody
	public Object findAllCompany(HttpServletRequest request,ModelMap model){
		
		List<ComStore> list = storeService.findAllCompany();
		
		model.addAttribute("data", list);
		return model;
	}
	
	@RequestMapping(value = "/findMaxCSId", method = RequestMethod.GET)
	@ResponseBody
	public Object findMaxCSId(HttpServletRequest request,ModelMap model){
		
		int maxCSId = storeService.findMaxCSId();
		
		model.addAttribute("maxCSId", maxCSId);
		return model;
	}
	
	@RequestMapping(value = "/savaStore", method = RequestMethod.POST)
	@ResponseBody
	public Object savaStore(
						@RequestParam(value="storeId", required=true) String storeId,
						@RequestParam(value="csName", required=true) String csName,
						@RequestParam(value="address", required=true) String address,
						@RequestParam(value="openTime", required=true) String openTime,
						@RequestParam(value="status", required=true) String status,
						@RequestParam(value="closeTime", required=true) String closeTime,
						@RequestParam(value="linkMan", required=true) String linkman,
						@RequestParam(value="phone", required=true) String phone,
						@RequestParam(value="companyId", required=true) String companyId,
						@RequestParam(value="email", required=true) String email,
						@RequestParam(value="id", required=false) Integer id,
						Model model,HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		ComStore comStore= storeService.findCompanyStoreById(id);
		if(comStore==null){
			comStore=new ComStore();
			comStore.setUpdateTime(new Date());
			comStore.setCreateTime(new Date());
		}
		comStore.setStoreId(storeId);
		comStore.setCsName(csName);
		comStore.setPhone(phone);
		comStore.setAddress(address);
		comStore.setEmail(email);
		comStore.setLinkman(linkman);
		comStore.setStatus(status);
		comStore.setOpenTime(openTime);
		comStore.setCloseTime(closeTime);
//		comStore.setDefautSupplier(supplier);
		comStore.setType("2");
		comStore.setCreateMan(username.toString());
		comStore.setUpdateMan(username.toString());
		
		CompanyStore companyStore=new CompanyStore();
		companyStore.setStoreId(storeId);
		companyStore.setComStoreId(companyId);
		companyStore.setCreateTime(new Date());
		companyStore.setUpdateTime(new Date());
		companyStore.setCreateMan(username.toString());
		companyStore.setUpdateMan(username.toString());
		
//		SupplierCompany company=new SupplierCompany();
//		company.setCompanyId(companyId);
//		company.setSuppId(supplier);
//		company.setCreateMan(username.toString());
//		company.setCreateTime(new Date());
//		
//		SupplierStore store=new SupplierStore(); 
//		store.setStoreId(storeId);
//		store.setSupplierId(supplier);
//		store.setCreateMan(username.toString());
//		store.setCreateTime(new Date());
		boolean falg =false;
		if(id==null){
			falg=storeService.saveCompanyStore(comStore,companyStore);
		}else{
			falg=storeService.updateCompanyStore(comStore,companyStore);
		}
		model.addAttribute("res", falg);
		return model;
	}
	
	
	@RequestMapping(value = "/findStoreById", method = RequestMethod.POST)
	@ResponseBody
	public Object findStoreById(@RequestParam(value="id", required=true) Integer id, ModelMap model){
		
		ComStore comStore= storeService.findCompanyStoreById(id);
		String companyId=storeService.findCompanyIdByStoreId(comStore.getStoreId());
		model.addAttribute("comStore", comStore);
		model.addAttribute("companyId", companyId);
		return model;
	}
	
	
	
	@RequestMapping(value = "/delStore", method = RequestMethod.POST)
	@ResponseBody
	public Object delStore(@RequestParam(value="id", required=true) Integer id,ModelMap model,HttpServletRequest request){
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		boolean flag = storeService.delStore(id);
		model.addAttribute("res", flag);
		
		return model;
	}
	
	@RequestMapping(value = "/findStoresByCId", method = RequestMethod.GET)
	@ResponseBody
	public Object findStoresByCId(@RequestParam(value="id", required=true) Integer id, ModelMap model){
		
		List<Map<Object,Object>> comStores= storeService.findStoresByCId(id);
		model.addAttribute("comStores", comStores);
		return model;
	}
	
	@RequestMapping(value = "/findCompanysAndStores", method = RequestMethod.POST)
	@ResponseBody
	public Object findCompanysAndStores(ModelMap model){
		List<Map<Object,Object>> maps=new ArrayList<Map<Object,Object>>();
		List<ComStore>  comStore= storeService.findAllCompany();
		for (ComStore comStore2 : comStore) {
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("id", comStore2.getId());
			map.put("name", comStore2.getCsName());
			map.put("pid",0);
			maps.add(map);
			List<Map<Object,Object>> comStores= storeService.findStoresByCId(comStore2.getId());
			for (Map<Object, Object> map2 : comStores) {
				Map<Object,Object> map3=new HashMap<Object,Object>();
				map3.put("id", map2.get("storeId").toString());
				map3.put("name",map2.get("csName").toString());
				map3.put("pid",comStore2.getId());
				maps.add(map3);
			}
		}
		model.addAttribute("maps", maps);
		return model;
	}
	
	@RequestMapping(value = "/validateStoreId", method = RequestMethod.GET)
	@ResponseBody
	public Object validateStoreId(@RequestParam(value="storeId", required=true) String storeId,ModelMap model){
		boolean flag = storeService.validateStoreId(storeId);
		model.addAttribute("res",flag);
		return model;
	}
	
}