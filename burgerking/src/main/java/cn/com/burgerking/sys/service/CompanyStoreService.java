package cn.com.burgerking.sys.service;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.CompanyStore;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;

public interface CompanyStoreService {
	public ComStore findCompanyStoreById(Integer id);
	
	public boolean saveCompanyStore(ComStore comStore,CompanyStore companyStore);
	
	public boolean saveCompanyStore(ComStore comStore);
	
	public boolean updateCompanyStore(ComStore comStore);
	
	public boolean updateCompanyStore(ComStore comStore,CompanyStore companyStore);
	
	public List findCompanys(Integer iDisplayStart, Integer iDisplayLength,String branchOffice);
	
	public int findCompanyCount(String branchOffice);
	
	public boolean delCompany(Integer id);
	
	public List findStores(Integer iDisplayStart, Integer iDisplayLength, String storeId);
	
	public int findStoreCount(String storeId);

	public List findAllCompany();
	
	public int findMaxCSId();
	
	public String findCompanyIdByStoreId(String id);
	
	public boolean delStore(Integer id);

	public List<Map<Object, Object>> findStoresByCId(Integer id);

	public boolean validateStoreId(String storeId);
	
	List<Map<String, Object>> companysByUserId(String userId);
	
}
