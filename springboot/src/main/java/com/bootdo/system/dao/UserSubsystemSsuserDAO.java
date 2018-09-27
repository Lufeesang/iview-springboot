package com.bootdo.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bootdo.system.domain.UserSubsystemSsuserDO;

/**
 * 
 * @author jxwang
 * @email 15jxwang@stu.edu.cn
 * @date 2018-7-25
 */
@Mapper
public interface UserSubsystemSsuserDAO {
	UserSubsystemSsuserDO get(Long id);

	List<UserSubsystemSsuserDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(UserSubsystemSsuserDO userSubsystemSsuserDO);

	int update(UserSubsystemSsuserDO userSubsystemSsuserDO);

	int remove(Long id);

	int batchRemove(Long[] ids);
}
