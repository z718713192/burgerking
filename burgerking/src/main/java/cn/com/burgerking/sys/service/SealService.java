package cn.com.burgerking.sys.service;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.sys.entity.Seal;
import cn.com.burgerking.sys.entity.SealComStore;

public interface SealService {
	
	List findSeals(Integer iDisplayStart, Integer iDisplayLength,String sealName,String sealCom);
	
	int findSealCount(String sealName,String sealCom);

	boolean savaSeal(Seal seal, List<SealComStore> comStors);

	Seal findASealById(Integer id);

	List<SealComStore> findSealComStoreBySealId(int id);

	boolean updateSeal(Seal seal, List<SealComStore> comStors);

	boolean delSealById(Integer id);
	
	/**
	 * 公司与印章对于tree
	 * @param companyId canbe null
	 * @return
	 */
	List<Map<String,Object>> companyWithSealsTree(Long companyId);
	
	/**
	 * 
	 * @param storeId
	 * @return
	 */
	List<Map<String,Object>> findAllCompanySealByStoreId(long storeId);
	
	
}
