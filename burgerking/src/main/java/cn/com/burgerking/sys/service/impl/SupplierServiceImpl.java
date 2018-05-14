package cn.com.burgerking.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.burgerking.sys.dao.CompanyStoreDao;
import cn.com.burgerking.sys.dao.SealDao;
import cn.com.burgerking.sys.dao.SupplierDao;
import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.CompanyStore;
import cn.com.burgerking.sys.entity.Seal;
import cn.com.burgerking.sys.entity.SealComStore;
import cn.com.burgerking.sys.entity.Supplier;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;
import cn.com.burgerking.sys.service.CompanyStoreService;
import cn.com.burgerking.sys.service.SealService;
import cn.com.burgerking.sys.service.SupplierService;

@Service
@Transactional
public class SupplierServiceImpl implements  SupplierService{
	
	@Autowired
	private SupplierDao supplierDao;

	@Override
	public List<Map<Object, Object>> findSuppliers(Integer iDisplayStart, Integer iDisplayLength,String supplierName,String supplierCom) {
		return supplierDao.findSuppliers(iDisplayStart, iDisplayLength,supplierName,supplierCom);
	}

	@Override
	public int findSupplierCount(String supplierName,String supplierCom) {
		return supplierDao.findSupplierCount(supplierName,supplierCom);
	}

	@Override
	public Supplier findSupplierById(Integer id) {
		return supplierDao.findSupplierById(id);
	}

	@Override
	public boolean saveSupplier(Supplier supplier, List<SupplierStore> supplierStores, List<SupplierCompany> companies) {
		return supplierDao.saveSupplier(supplier,supplierStores,companies);
	}

	@Override
	public boolean updateSupplier(Supplier supplier, List<SupplierStore> supplierStores, List<SupplierCompany> companies) {
		return supplierDao.updateSupplier(supplier,supplierStores,companies);
	}

	@Override
	public List<Map<Object, Object>> findSupplierCompanys(Integer id) {
		return supplierDao.findSupplierCompanys(id);
	}

	@Override
	public List<Map<Object, Object>> findSupplierStores(Integer id) {
		return supplierDao.findSupplierStores(id);
	}

	@Override
	public boolean delSupplier(Integer id) {
		return supplierDao.delSupplier(id);
	}

	@Override
	public List<Supplier> findAllSupplier() {
		return supplierDao.findAllSupplier();
	}

	@Override
	public List<Map<String, Object>> findSuppliersByStoreId(String storeId) {
		return supplierDao.findSuppliersByStoreId(storeId);
	}

	@Override
	public Supplier findSupplierByStoreId(int storeId) {
		return supplierDao.findSupplierByStoreId(storeId);
	}

	 
 
}
