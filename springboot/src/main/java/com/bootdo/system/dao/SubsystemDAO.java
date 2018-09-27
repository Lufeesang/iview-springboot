package com.bootdo.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bootdo.system.domain.SubsystemDO;
import com.bootdo.system.domain.UserSubsystemSsuserDO;

/**
 * 
 * @author jxwang
 * @email 15jxwang@stu.edu.cn
 * @date 2018-7-25
 */
@Mapper
public interface SubsystemDAO {
	SubsystemDO get(Long id);

	List<SubsystemDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(SubsystemDO subsystemDO);

	int update(SubsystemDO subsystemDO);

	int remove(Long id);

	int batchRemove(Long[] ids);
	
	SubsystemDO getByMenuId (Long menuId);
}
