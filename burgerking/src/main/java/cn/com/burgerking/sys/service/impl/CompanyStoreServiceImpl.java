package cn.com.burgerking.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.burgerking.sys.dao.CompanyStoreDao;
import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.CompanyStore;
import cn.com.burgerking.sys.entity.SupplierCompany;
import cn.com.burgerking.sys.entity.SupplierStore;
import cn.com.burgerking.sys.service.CompanyStoreService;

@Service
@Transactional
public class CompanyStoreServiceImpl implements  CompanyStoreService{
	
	@Autowired
	private CompanyStoreDao storeDao;

	@Override
	public ComStore findCompanyStoreById(Integer id) {
		return storeDao.findCompanyStoreById(id);
	}

	@Override
	public boolean saveCompanyStore(ComStore comStore) {
		return storeDao.saveCompanyStore(comStore);
	}

	@Override
	public boolean updateCompanyStore(ComStore comStore) {
		return storeDao.updateCompanyStore(comStore);
	}

	@Override
	public List findCompanys(Integer iDisplayStart, Integer iDisplayLength,String branchOffice) {
		return storeDao.findCompanys(iDisplayStart, iDisplayLength,branchOffice);
	}

	@Override
	public int findCompanyCount(String branchOffice) {
		return storeDao.findCompanyCount(branchOffice);
	}

	@Override
	public boolean delCompany(Integer id) {
		return storeDao.delCompany(id);
	}

	@Override
	public List findStores(Integer iDisplayStart, Integer iDisplayLength,String storeId) {
		return storeDao.findStores(iDisplayStart,iDisplayLength,storeId);
	}

	@Override
	public int findStoreCount(String storeId) {
		return storeDao.findStoreCount(storeId);
	}

	@Override
	public List findAllCompany() {
		return storeDao.findAllCompany();
	}

	@Override
	public int findMaxCSId() {
		return storeDao.findMaxCSId();
	}

	@Override
	public boolean saveCompanyStore(ComStore comStore, CompanyStore companyStore){
		return storeDao.saveCompanyStore(comStore,companyStore);
	}

	@Override
	public boolean updateCompanyStore(ComStore comStore, CompanyStore companyStore ) {
		return storeDao.updateCompanyStore(comStore,companyStore);
	}

	@Override
	public String findCompanyIdByStoreId(String id) {
		return storeDao.findCompanyIdByStoreId(id);
	}

	@Override
	public boolean delStore(Integer id) {
		return storeDao.delStore(id);
	}

	@Override
	public List<Map<Object,Object>> findStoresByCId(Integer id) {
		return storeDao.findStoresByCId(id);
	}

	@Override
	public boolean validateStoreId(String storeId) {
		return storeDao.validateStoreId(storeId);
	}

	@Override
	public List<Map<String, Object>> companysByUserId(String userId) {
		return storeDao.companysByUserId(userId);
	}
		
	

}
