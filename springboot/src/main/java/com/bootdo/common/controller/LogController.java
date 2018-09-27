package com.bootdo.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.annotation.Log;
import com.bootdo.common.domain.LogDO;
import com.bootdo.common.domain.PageDO;
import com.bootdo.common.service.LogService;
import com.bootdo.common.service.impl.LogServiceImpl;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.bootdo.system.domain.MenuSearchLimit;


//@RequestMapping("/common/log")
@Controller
public class LogController {
	@Autowired
	LogService logService;
	
	@Autowired
	LogServiceImpl logServiceImpl;
	String prefix = "common/log";

	@GetMapping("/common/log/")
	String log() {
		return prefix + "/log";
	}
	@Log("查询-查看日志")
	@ResponseBody
	@GetMapping("/common/log/list")
	PageDO<LogDO> list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);
		PageDO<LogDO> page = logService.queryList(query);
		return page;
	}
	
	@Log("删除-删除日志")
	@ResponseBody
	@PostMapping("/common/log/remove")
	R remove(Long id) {
		if (logService.remove(id)>0) {
			return R.ok();
		}
		return R.error();
	}

	@Log("删除-批量删除日志")
	@DeleteMapping("/batchdeleteLog")
	@ResponseBody
	public Map<String , Object> batchdeleteLog(@RequestBody JSONObject data){
		List<Long> LogIdList = new ArrayList<>();
		String message = "";
		int result;
		LogIdList = (List<Long>) data.get("deleteMenuList");
		System.out.println(LogIdList);
		Map map = new HashMap<>();
		int size = LogIdList.size();
		//把List<Long> 转化为 long[] ,但是这里从List<Long>中取出来的是 Integer ,所以不能直接转化
		//long[] ids = LogIdList.stream().mapToLong(t->t.longValue()).toArray();
		long[] ids = new long[size];
		String middle;
		int count = 0;
		//下面先把从LogIdList 中取出来的 interger 转化为String，再转化为long
		System.out.print("ids=");
		for(Object id : LogIdList) {
			middle = id.toString();
			ids[count] = Long.parseLong(middle);
			System.out.print(ids[count]+" , ");
			count++;
		}
		
		result = logServiceImpl.batchRemove(ids);
		System.out.println(result);
		map.put("result", result);
		if(result>0) {
			message = "删除成功";
		}else message = "删除失败";
		map.put("message", message);
		System.out.println(map);
		return map;
	}
	
	@Log("查询-查看日志")
	@PostMapping("/getLoglist")
	@ResponseBody
	public Map<String, Object>  getLimitMenuList(@RequestBody MenuSearchLimit menuSearchLimit){
		Map map = new HashMap<>();
		System.out.println("LogSearchLImit"+menuSearchLimit.toString());
		if (menuSearchLimit.getPage() > 0) {
			menuSearchLimit.setOffset((menuSearchLimit.getPage() - 1) * 10);
			menuSearchLimit.setLimit(10);
		}
		List<LogDO> result = logServiceImpl.listMenuByLimit(menuSearchLimit);
		if(result != null) {
			map.put("result", result);
			map.put("message", "查询成功");
		}else {
			map.put("message", "无满足条件的数据，请重新查询");
		}
//		System.out.println("map"+map);
		return map;
	}
	
	@Log("获得日志总数")
	@PostMapping("/logCount")
	@ResponseBody
	public R  count(@RequestBody MenuSearchLimit menuSearchLimit){
		Map map = new HashMap<>();
		R r = R.ok();
		List<LogDO> result = logServiceImpl.listMenuByLimit(menuSearchLimit);
		System.out.println("LogSearchLImit"+menuSearchLimit.toString());
		if(result != null) {
			map.put("result", result);
			map.put("message", "查询成功");
			r.put("count", result.size());
		}else {
			map.put("message", "无满足条件的数据，请重新查询");
			r.put("count", 0);
		}
		return r;
	}
}
