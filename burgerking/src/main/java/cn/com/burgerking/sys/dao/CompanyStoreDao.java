package cn.com.burgerking.sys.dao;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.CompanyStore;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;
import cn.com.burgerking.sys.entity.UserCompany;

public interface CompanyStoreDao {
	ComStore findCompanyStoreById(Integer id);
	
	boolean saveCompanyStore(ComStore comStore);
	
	boolean saveCompanyStore(ComStore comStore,CompanyStore companyStore);
	
	boolean updateCompanyStore(ComStore comStore);
	
	boolean updateCompanyStore(ComStore comStore,CompanyStore companyStore);
	
	List<Map<String, Object>> findCompanys(Integer iDisplayStart, Integer iDisplayLength,String branchOffice);
	
	int findCompanyCount(String branchOffice);
	
	boolean delCompany(Integer id);

	List<Map<String, Object>> findStores(Integer iDisplayStart, Integer iDisplayLength, String storeId);
	
	int findStoreCount(String storeId);
	
	List findAllCompany();
	
	int findMaxCSId();

	String findCompanyIdByStoreId(String id);

	boolean delStore(Integer id);

	List<Map<Object, Object>> findStoresByCId(Integer id);
	
	/**
	 * save UserCompany
	 * @param userCompany
	 * @return
	 */
	boolean saveUserCompanyRef(UserCompany userCompany);
	
	boolean deleteUserCompanyRef(String userId);
	
	boolean deleteUserCompanyRef(String userId,String companyId);

	boolean validateStoreId(String storeId);
	
	List<Map<String, Object>> companysByUserId(String userId);
	
	ComStore findStoreByStoreId(String storeId);
	
	ComStore findCompanyByStoreId(String storeId);
}

