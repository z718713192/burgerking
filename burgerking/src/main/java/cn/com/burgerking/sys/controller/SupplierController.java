package cn.com.burgerking.sys.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.burgerking.sys.entity.Supplier;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;
import cn.com.burgerking.sys.service.SupplierService;

/**
 * Handles requests for the application home page.
 */ 

@Controller
@RequestMapping(value = "/sys")
public class SupplierController {
	
	private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Autowired
	private SupplierService supplierService;

	/**
	 * 分页查询印章
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @param sEcho
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findSuppliers", method = RequestMethod.GET)
	@ResponseBody
	public Object findSuppliers(@RequestParam(value="iDisplayStart", required=true) Integer iDisplayStart, 
								@RequestParam(value="iDisplayLength", required=true) Integer iDisplayLength,
								@RequestParam(value="sEcho", required=true) String sEcho,
								@RequestParam(value="supplierName", required=false) String supplierName,
								@RequestParam(value="supplierCom", required=false) String supplierCom,
								HttpServletRequest request,ModelMap model){
		
		List<Map<Object,Object>> list = supplierService.findSuppliers(iDisplayStart, iDisplayLength,supplierName,supplierCom);
		int count = supplierService.findSupplierCount(supplierName,supplierCom);
		
		model.addAttribute("aaData", list);
		model.addAttribute("iTotalRecords", count);
		model.addAttribute("iTotalDisplayRecords", count);
		model.addAttribute("sEcho", sEcho);
		return model;
	}
	
	
	
	@RequestMapping(value = "/findSupplierById", method = RequestMethod.POST)
	@ResponseBody
	public Object findSupplierById(@RequestParam(value="id", required=true) Integer id, ModelMap model){
		
		Supplier supplier= supplierService.findSupplierById(id);
		List<Map<Object,Object>> list = supplierService.findSupplierCompanys(id); 
		List<Map<Object,Object>> list2 = supplierService.findSupplierStores(id); 
		model.addAttribute("supplier", supplier);
		model.addAttribute("companyIds", list);
		model.addAttribute("storeIds", list2);
		return model;
	}
	
	
	@RequestMapping(value = "/findAllSupplier", method = RequestMethod.POST)
	@ResponseBody
	public Object findAllSupplier(ModelMap model){
		
		List<Supplier> suppliers= supplierService.findAllSupplier();
		model.addAttribute("supplier", suppliers);
		return model;
	}
	
	@RequestMapping(value = "/savaSupplier", method = RequestMethod.POST)
	@ResponseBody
	public Object savaSupplier(
						@RequestParam(value="supplierName", required=true) String supplierName,
						@RequestParam(value="ename", required=true) String ename,
						@RequestParam(value="comName", required=true) String comName,
						@RequestParam(value="linkWay", required=true) String linkWay,
						@RequestParam(value="linkMan", required=true) String linkMan,
						@RequestParam(value="email", required=true) String email,
						@RequestParam(value="address", required=true) String address,
						@RequestParam(value="storeId", required=true) List<String> storeIds,
						@RequestParam(value="companyId", required=true)List<String> companyIds,
						@RequestParam(value="id", required=false) Integer id,HttpServletRequest request,
						HttpServletResponse response,
						Model model) {
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		Supplier supplier= supplierService.findSupplierById(id);
		if(supplier==null){
			supplier= new Supplier();
			supplier.setCreateTime(new Date());
			supplier.setUpdateTime(new Date());
		}
		supplier.setAddress(address);
		supplier.setComName(comName);
		supplier.setEmail(email);
		supplier.setEname(ename);
		supplier.setSupplierName(supplierName);
		supplier.setLinkName(linkMan);
		supplier.setLinkWay(linkWay);
		supplier.setCreateMan(username.toString());
		supplier.setUpdateMan(username.toString());
		
		List<SupplierStore> supplierStores=new ArrayList<SupplierStore>();
		for (int i = 0; i < storeIds.size(); i++) {
			SupplierStore supplierStore=new SupplierStore(); 
			supplierStore.setStoreId(storeIds.get(i));
			supplierStore.setCreateMan(username.toString());
			supplierStore.setCreateTime(new Date());
			supplierStores.add(supplierStore);
		}
		
		List<SupplierCompany> companies=new ArrayList<SupplierCompany>();
		for (int i = 0; i < companyIds.size(); i++) {
			SupplierCompany company=new SupplierCompany();
			company.setCompanyId(companyIds.get(i));
			company.setCreateMan(username.toString());
			company.setCreateTime(new Date());
			companies.add(company);
		}
		
		boolean falg =false;
		if(id==null){
			falg=supplierService.saveSupplier(supplier,supplierStores,companies);
		}else{
			falg=supplierService.updateSupplier(supplier,supplierStores,companies);
		}
		model.addAttribute("res", falg);
		return model;
	}
	
	@RequestMapping(value = "/delSupplier", method = RequestMethod.POST)
	@ResponseBody
	public Object delSupplier(@RequestParam(value="id", required=true) Integer id,ModelMap model,HttpServletRequest request){
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		boolean flag = supplierService.delSupplier(id);
		model.addAttribute("res", flag);
		
		return model;
	}
	
	@RequestMapping(value = "/findSupplierByStoreId", method = RequestMethod.GET)
	@ResponseBody
	public Object findSuppliersByStoreId(@RequestParam(value="id", required=true) String storeId,ModelMap model,HttpServletRequest request){
		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		if(username==null){
			model.addAttribute("code","400");
			return model;
		}
		model.addAttribute("supplier", supplierService.findSuppliersByStoreId(storeId));
		return model;
	}
	
}