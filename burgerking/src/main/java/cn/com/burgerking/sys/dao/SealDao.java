package cn.com.burgerking.sys.dao;

import java.util.List;
import java.util.Map;

import cn.com.burgerking.sys.entity.ComStore;
import cn.com.burgerking.sys.entity.CompanyStore;
import cn.com.burgerking.sys.entity.Seal;
import cn.com.burgerking.sys.entity.SealComStore;
import cn.com.burgerking.sys.entity.UserSeal;

public interface SealDao {

	List findSeals(Integer iDisplayStart, Integer iDisplayLength,String sealName,String sealCom);

	int findSealCount(String sealName,String sealCom);

	boolean savaSeal(Seal seal, List<SealComStore> comStors);

	Seal findASealById(Integer id);

	List<SealComStore> findSealComStoreBySealId(int id);

	boolean updateSeal(Seal seal, List<SealComStore> comStors);

	boolean delSealById(Integer id);

	List<Map<String,Object>> companyWithSealsTree(Long id);
	
	boolean saveUserSealRef(UserSeal userSeal);
	
	boolean deleteUserSealRef(String userId);
	
	boolean deleteUserSealRef(String userId,String companyId);
	
	List<Map<String, Object>> findAllCompanySealByStoreId(long storeId);

}
