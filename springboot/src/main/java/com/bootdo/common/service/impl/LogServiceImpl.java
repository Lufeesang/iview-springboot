package com.bootdo.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bootdo.common.dao.LogDao;
import com.bootdo.common.domain.LogDO;
import com.bootdo.common.domain.PageDO;
import com.bootdo.common.service.LogService;
import com.bootdo.common.utils.Query;
import com.bootdo.system.domain.MenuSearchLimit;

@CacheConfig(cacheNames = "log-cache")
@Service
@Transactional(rollbackFor = {RuntimeException.class,Exception.class}
	, propagation= Propagation.REQUIRED)
public class LogServiceImpl implements LogService {
	@Autowired
	LogDao logMapper;

	@Async
	@Override
	public void save(LogDO logDO) {
		 logMapper.save(logDO);
	}

	@Override
	public PageDO<LogDO> queryList(Query query) {
		int total = logMapper.count(query);
		List<LogDO> logs = logMapper.list(query);
		PageDO<LogDO> page = new PageDO<>();
		page.setTotal(total);
		page.setRows(logs);
		return page;
	}
	@CacheEvict(value = "log-cache" , allEntries = true)
	@Override
	public int remove(Long id) {
		int count = logMapper.remove(id);
		return count;
	}

	
	@Override
	@CacheEvict(value = "log-cache" , allEntries = true)
	public int batchRemove(long[] ids){
		return logMapper.batchRemove(ids);
	}
	@Cacheable
	public LogDO get(Long id){
		return logMapper.get(id);
	}
	
	@Cacheable
	public List<LogDO> listMenuByLimit(MenuSearchLimit menuSearchLimit){
		System.out.println("listLogDo"+logMapper.listLogByLimit(menuSearchLimit));
		return logMapper.listLogByLimit(menuSearchLimit);
	}

	@Override
	public int count(Map<String, Object> map) {
		return logMapper.count(map);
	}
}
