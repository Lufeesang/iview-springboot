package com.bootdo.system.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.annotation.Log;
import com.bootdo.system.common.Tree;
import com.bootdo.system.domain.MenuDomain;
import com.bootdo.system.domain.MenuSearchLimit;
import com.bootdo.system.service.impl.MenuServiceImpl;
import com.mysql.fabric.xmlrpc.base.Array;

@Controller
public class MenuController {
	@Autowired
	MenuServiceImpl menuServiceImpl;
	
	
	
	@Log("操作-查询某菜单下的树")
	@GetMapping("/getTreeList")
	@ResponseBody
	public List<MenuDomain> getTreeList(
			@RequestParam(value="menuId" , required=true) Long menuId
			){
		System.out.println("******8"+menuId);
		List<MenuDomain> menuIdList = new ArrayList<>();
		List<Long> menuIdList2 = new ArrayList<>();
		menuIdList2.add(menuId);
		List<MenuDomain> menuDos = menuServiceImpl.list(new HashMap<>(16));
		if(menuId > 0) {
			menuIdList2 = menuServiceImpl.treeMenuList(menuDos, menuId);
		}
		menuIdList2.add(menuId);
		for(Long i : menuIdList2) {
			//menuIdList.add(i);
			MenuDomain menuDomain  = new MenuDomain();
			menuDomain = menuServiceImpl.get(i);
			menuIdList.add(menuDomain);
		}
		System.out.println(menuIdList2);
		//该方法清空了menuServiceImpl.treeMenuList(menuDos, menuId); 产生的结果
		menuServiceImpl.clearChildMenu();
		System.out.println(menuIdList2);
		System.out.println(menuIdList);
		return  menuIdList;
		
		
	}
	
	@Log("操作-修改菜单")
	@PutMapping("/updateMenu")
	@ResponseBody
	@CrossOrigin()
    int UpdateMenu(@RequestBody MenuDomain menuDomain) {
        /**
		System.out.println("this is Map"+map);
        System.out.println("数据"+map.get("menuId"));
        System.out.println("数据  dddddd"+map.get("name"));
        
        MenuDomain menuDomain = new MenuDomain();
        menuDomain.setMenuId(Long.parseLong(map.get("menuId").toString()));
        menuDomain.setParentId(Long.parseLong(map.get("parentId").toString()));
        menuDomain.setUrl(map.get("url").toString());
        menuDomain.setName(map.get("name").toString());
        menuDomain.setUrl(map.get("url").toString());
        **/
		int result;
        Date date = new Date();
        menuDomain.setGmtModified(date);
        System.out.println(menuDomain.toString());
        result = menuServiceImpl.updateMenu(menuDomain);
        return result;
    }
	
//	@DeleteMapping("/deleteMenu")
//	@ResponseBody
//	@CrossOrigin()
//	int deleteMenu() {
//		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//				.getRequestAttributes();
//		HttpServletRequest request = attributes.getRequest();
//		System.out.println("接收到参数是"+request.getParameter("menu_id"));
//		long deleteMenuId = Long.parseLong(request.getParameter("menu_id"));
//		System.out.println("deleteMenuId"+deleteMenuId);
//		return (menuServiceImpl.deleteMenu(deleteMenuId));
//		//return 1;
//	}
	
	@Log("操作-删除菜单")
	@DeleteMapping("/batchdeleteMenu")
	@ResponseBody
	Map<String, Object> batchDeleteMenu(@RequestBody JSONObject data) {
		List<Long> menuIdList = new ArrayList<>();
		menuIdList = (List<Long>) data.get("deleteMenuList");
		System.out.println(menuIdList);
		Map map = new HashMap<>();
		map = menuServiceImpl.deleteMenu(menuIdList);
		System.out.println(map);
		return map;
	}
	
	@GetMapping("/getMenuList")
	@ResponseBody
	public List<MenuDomain> getMenuList(){
		return menuServiceImpl.getMenuList();
	}
	
	@GetMapping("/getMenuList/page")
	@ResponseBody
	public Map<String , Object> getMenuListPage(
			@RequestParam(value="page" , required=true) int page){
		Map map = new HashMap<>();
		
		map.put("menuDomains", menuServiceImpl.getMenuListPage(page));
		map.put("total", menuServiceImpl.getMenuList().size());
		return map;
	}
	
	
	/**
	 * 返回多条件查询后的 menuList
	 * @param menuSearchLimit
	 * @return
	 */
	@Log("操作-条件查询菜单")
	@PostMapping("/getLimitMenuList")
	@ResponseBody
	public Map<String, Object>  getLimitMenuList(@RequestBody MenuSearchLimit menuSearchLimit){
		Map map = new HashMap<>();
		List<MenuDomain> result = menuServiceImpl.getLimitMenuList(menuSearchLimit);
		if(result.size() > 0) {
			map.put("result", result);
			map.put("message", "查询成功");
		}else {
			map.put("message", "无满足条件的数据，请重新查询");
		}
		System.out.println("map"+map);
		return map;
	}
	@InitBinder 
	public void initBinder(WebDataBinder binder) { 
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		dateFormat.setLenient(true); 
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); 
	}
	
	@Log("操作-添加菜单")
	@PostMapping("/addMenu")
	@ResponseBody
	public Map<String, Object> addMenu(@RequestBody MenuDomain menuDomain){
		
		Map map = new HashMap<>();
		Date date = new Date();
		menuDomain.setGmtCreate(date);
		menuServiceImpl.saveMenuDomain(menuDomain);
		MenuDomain newMenu = new MenuDomain();
		Long  result = menuDomain.getMenuId();
		newMenu = menuServiceImpl.get(result);
		System.out.println(newMenu.toString());
		System.out.println(result);
		map.put("result", result);
		if(result <= 0 ) {
			map.put("message", "保存失败");
		}
		map.put("message", "保存成功，请刷新页面查看");
		map.put("domain", newMenu);
		System.out.println(menuDomain.toString());
	
		return map;
	}
	
	@Log("操作-查询菜单树")
	@GetMapping("/")
	@ResponseBody
	public List<Tree<MenuDomain>>  getMenuTree() {

		return (menuServiceImpl.getTree());
	}

}
