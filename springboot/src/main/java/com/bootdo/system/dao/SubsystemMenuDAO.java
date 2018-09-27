package com.bootdo.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bootdo.system.domain.SubsystemDO;
import com.bootdo.system.domain.SubsystemMenuDO;
import com.bootdo.system.domain.UserSubsystemSsuserDO;

/**
 * 
 * @author jxwang
 * @email 15jxwang@stu.edu.cn
 * @date 2018-7-25
 */
@Mapper
public interface SubsystemMenuDAO {
	SubsystemMenuDO get(Long id);

	List<SubsystemMenuDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(SubsystemMenuDO subsystemMenuDO);

	int update(SubsystemMenuDO subsystemMenuDO);

	int remove(Long id);

	int batchRemove(Long[] ids);
}
