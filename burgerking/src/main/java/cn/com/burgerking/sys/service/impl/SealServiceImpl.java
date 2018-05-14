package cn.com.burgerking.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.burgerking.sys.dao.CompanyStoreDao;
import cn.com.burgerking.sys.dao.SealDao;
import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.Seal;
import cn.com.burgerking.sys.entity.SealComStore;
import cn.com.burgerking.sys.service.SealService;

@Service
@Transactional
public class SealServiceImpl implements SealService {

	@Autowired
	private SealDao sealDao;
	
	@Autowired
	private CompanyStoreDao storeDao;


	@Override
	public List findSeals(Integer iDisplayStart, Integer iDisplayLength,String sealName,String sealCom) {
		return sealDao.findSeals(iDisplayStart, iDisplayLength,sealName,sealCom);
	}

	@Override
	public int findSealCount(String sealName,String sealCom) {
		return sealDao.findSealCount(sealName,sealCom);
	}

	@Override
	public boolean savaSeal(Seal seal, List<SealComStore> comStors) {
		return sealDao.savaSeal(seal, comStors);
	}

	@Override
	public Seal findASealById(Integer id) {
		return sealDao.findASealById(id);
	}

	@Override
	public List<SealComStore> findSealComStoreBySealId(int id) {
		return sealDao.findSealComStoreBySealId(id);
	}

	@Override
	public boolean updateSeal(Seal seal, List<SealComStore> comStors) {
		return sealDao.updateSeal(seal, comStors);
	}

	@Override
	public boolean delSealById(Integer id) {
		return sealDao.delSealById(id);
	}

	@Override
	public List<Map<String, Object>> companyWithSealsTree(Long companyId) {
		
		List<Map<String,Object>> maps=new ArrayList<Map<String,Object>>();
		List<ComStore>  comStore=  storeDao.findAllCompany();
		for (ComStore comStore2 : comStore) {
				Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", comStore2.getId());
					map.put("name", comStore2.getCsName());
					map.put("pid",0);
					maps.add(map);
					List<Map<String, Object>> comStores= sealDao.companyWithSealsTree(Long.parseLong(comStore2.getId()+""));
					for (Map<String, Object> map2 : comStores) {
						Map<String,Object> map3=new HashMap<String,Object>();
						map3.put("id", map2.get("sealId").toString());
						map3.put("name",map2.get("sealName").toString());
						map3.put("pid",comStore2.getId());
						maps.add(map3);
					}
				}		
				
				
		return maps;
	}

	@Override
	public List<Map<String, Object>> findAllCompanySealByStoreId(long storeId) {
		return sealDao.findAllCompanySealByStoreId(storeId);
	}

}
