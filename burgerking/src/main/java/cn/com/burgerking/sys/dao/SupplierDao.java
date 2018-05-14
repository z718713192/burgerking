package cn.com.burgerking.sys.dao;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.sys.entity.Supplier;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;

public interface SupplierDao {
	
	public List<Map<Object, Object>> findSuppliers(Integer iDisplayStart, Integer iDisplayLength,String supplierName,String supplierCom);

	public int findSupplierCount(String supplierName,String supplierCom);

	public Supplier findSupplierById(Integer id);

	public boolean saveSupplier(Supplier supplier, List<SupplierStore> supplierStores, List<SupplierCompany> companies);

	public boolean updateSupplier(Supplier supplier, List<SupplierStore> supplierStores, List<SupplierCompany> companies);

	public List<Map<Object, Object>> findSupplierCompanys(Integer id);

	public List<Map<Object, Object>> findSupplierStores(Integer id);

	public boolean delSupplier(Integer id);

	public List<Supplier> findAllSupplier();
	
	List<Map<String,Object>> findSuppliersByStoreId(String storeId);
	
	public Supplier findSupplierByStoreId(int storeId);
}

