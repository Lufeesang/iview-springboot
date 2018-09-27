//package com.bootdo.system.dao;
//
//import java.util.List;
//import java.util.Map;
//
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//
//import com.bootdo.system.domain.RoleDO;
//import com.bootdo.system.domain.UserMenuDO;
//import com.bootdo.system.domain.UserRoleDO;
//
//@Mapper
//public interface UserMenuDao {
//	List<UserMenuDO> list(Map<String, Object> map);
//	
//	int count(Map<String, Object> map);
//
//	int save(UserMenuDO userRole);
//
//	int update(UserMenuDO userRole);
//
//	int remove(Long id);
//
//	int batchRemove(Long[] ids);
//
//	int removeByUserId(Long userId);
//	
//	int removeByMenuId(Long menuId);
//
//	int batchSave(List<UserMenuDO> list);
//	
//	Long[] cascadeMenuIdList (@Param("userId") Long userId, @Param("roles") List roles);	
//}
package com.bootdo.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bootdo.system.domain.TransferUser;
import com.bootdo.system.domain.UserMenuDO;

@Mapper
public interface UserMenuDao {
	List<UserMenuDO> list(Map<String, Object> map);
		
	int count(Map<String, Object> map);

	int save(UserMenuDO userRole);

	int update(UserMenuDO userRole);

	int remove(Long id);

	int batchRemove(Long[] ids);

	int removeByUserId(Long userId);
	
	int removeByMenuId(Long menuId);

	int batchSave(List<UserMenuDO> list);
	
	Long[] cascadeMenuIdList (@Param("userId") Long userId, @Param("roles") List roles);	

	Long[] hasUserMenuPerms(@Param("menuId")long menuId, @Param("userIds")List<Long> userIds);
	
	
	int batchRemoveBymenuIdAnduserIds(@Param("menuId")Long menuId, @Param("alluserIds")List<Long> allUserIds);
}
