package com.bootdo.system.dao;

import com.bootdo.system.domain.TransferUser;
import com.bootdo.system.domain.UserDO;
import com.bootdo.system.domain.UserSearchLimit;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 09:45:11
 */
@Mapper
public interface UserDao {

	UserDO get(Long userId);
	
	List<UserDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UserDO user);
	
	int update(UserDO user);
	
	int remove(Long userId);
	
	int batchRemove(Long[] userIds);
	
	Long[] listAllDept();
	
//	Long[] listMenuIds(@Param("userId") Long userId, @Param("idss") List<Long> ids);
	
	List<TransferUser> listUser(UserSearchLimit userSearchLimit);
}
