package com.bootdo.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bootdo.common.domain.LogDO;
import com.bootdo.common.domain.PageDO;
import com.bootdo.common.utils.Query;
import com.bootdo.system.domain.MenuSearchLimit;

@Service
public interface LogService {
	void save(LogDO logDO);

	PageDO<LogDO> queryList(Query query);

	int remove(Long id);

	int batchRemove(long[] ids);

	List<LogDO> listMenuByLimit(MenuSearchLimit menuSearchLimit);

	public LogDO get(Long id);
	
	int count(Map<String,Object> map);
}
