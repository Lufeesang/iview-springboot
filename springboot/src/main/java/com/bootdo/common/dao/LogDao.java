package com.bootdo.common.dao;

import com.bootdo.common.domain.LogDO;
import com.bootdo.system.domain.MenuSearchLimit;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
public interface LogDao {

	LogDO get(Long id);
	
	List<LogDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(LogDO log);
	
	int update(LogDO log);
	
	int remove(Long id);
	
	
	int batchRemove(long[] ids);
	
	List<LogDO> listLogByLimit(MenuSearchLimit menuSearchLimit);

}
