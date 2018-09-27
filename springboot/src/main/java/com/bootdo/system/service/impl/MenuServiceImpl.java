



package com.bootdo.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
//import org.hibernate.validator.internal.xml.binding.GetterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bootdo.system.common.BuildTree;
import com.bootdo.system.common.Tree;
import com.bootdo.system.dao.MenuDao;
import com.bootdo.system.dao.UserMenuDao;
import com.bootdo.system.dao.UserRoleDao;
import com.bootdo.system.domain.MenuDomain;
import com.bootdo.system.domain.MenuSearchLimit;
import com.bootdo.system.domain.RoleDO;
import com.bootdo.system.service.MenuService;
import com.bootdo.system.service.RoleService;


@CacheConfig(cacheNames = "menu-cache")
@Service
public class MenuServiceImpl implements MenuService{
	
	@Autowired
	MenuDao menuDao;
	@Autowired
	RoleService roleService;
	@Autowired
	UserRoleDao userRoleMapper;
	@Autowired
	UserMenuDao userMenuMapper;
	/**
	 * 用于返回菜单树形list
	 * @return
	 */
	///**********
	public List<Tree<MenuDomain>> getTree() {
		List<Tree<MenuDomain>> trees = new ArrayList<Tree<MenuDomain>>();
		System.out.println("3333333333333333333333333333333333%%%%%%%%");
		List<MenuDomain> menuDOs = menuDao.list(new HashMap<>(16));
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println(menuDOs.toString());
		for (MenuDomain sysMenuDO : menuDOs) {
			Tree<MenuDomain> tree = new Tree<MenuDomain>();
			System.out.println(sysMenuDO.getMenuId().toString());
			tree.setMenuId(sysMenuDO.getMenuId().toString());
			tree.setParentId(sysMenuDO.getParentId().toString());
			tree.setTitle(sysMenuDO.getName());
			System.out.println(tree.toString());
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		//Tree<MenuDomain> t = BuildTree.build(trees);
		List<Tree<MenuDomain>> t = BuildTree.buildList(trees);
		System.out.println("一棵树"+t.toString()+t.size());
		for(Object object : t) {
			System.out.println(object.toString());
		}
		return t;
		
	}
	/**
	 * 返回菜单List
	 * @return
	 */

	 public List<MenuDomain> getMenuListPage(int page){
		int limit = 10;
		int offset = page * 10;
		Map map = new HashMap<>();
		map.put("limit", limit);
		map.put("offset", offset);
		List<MenuDomain> menuDOmeins = menuDao.list(map);
		System.out.println(menuDOmeins.toString());
		return menuDOmeins;
	}
	
	@Cacheable
	 public List<MenuDomain> getMenuList(){
		List<MenuDomain> menuDOmeins = menuDao.list(new HashMap<>(16));
		System.out.println(menuDOmeins.toString());
		return menuDOmeins;
	}
	
	/**
	 * 更新菜单信息
	 * @param menuDomain
	 * @return
	 */
	 @Transactional(rollbackFor = {RuntimeException.class,Exception.class}
	 	, propagation= Propagation.REQUIRED)
	 @CacheEvict(value = "menu-cache", allEntries = true)
	 public int updateMenu(MenuDomain menuDomain) {
		 int result = menuDao.update(menuDomain);
		System.out.println("更新结果"+result);
//		if(result > 0 ) throw new RuntimeException("加入事务，运行回滚检测");
		return result;
		
	}
	 @Cacheable
	 public MenuDomain get(Long menuId) {
		 return menuDao.get(menuId);
	 }
	
	//用于存放需要被删除的菜单的所有子菜单menuid，包括自己
	public List<Long> childMenu = new ArrayList<>();
	
	/**
	 * 用来递归查找某一个菜单下，所有子菜单的编号
	 * @param menuDos
	 * @param parentId
	 * @return
	 */
	@Cacheable
	 public List<Long> treeMenuList(List<MenuDomain> menuDos, Long parentId) {
		for (MenuDomain sysMenuDO : menuDos) {
            long menuId = sysMenuDO.getMenuId();
            long pid = sysMenuDO.getParentId();
            if (parentId == pid) {
                List<Long> c_node = treeMenuList(menuDos, menuId);
                childMenu.add(menuId);
            }
        }
		return childMenu;
    }
	
	//清空childMenu
	public void  clearChildMenu() {
		childMenu.clear();
	}

	/**
	 * 
	 * 根据menuId删除menu
	 * 同时递归删除，该菜单下的所有子菜单
	 * @param meunId
	 * @return
	 */
	 @CacheEvict(value = "menu-cache", allEntries = true)
	 @Transactional(rollbackFor = {RuntimeException.class,Exception.class}
	 	, propagation= Propagation.REQUIRED)
	 public Map<String, Object> deleteMenu(List<Long> menuIdList) {
			childMenu.clear();
			List<MenuDomain> menuDomains = menuDao.list(new HashMap<>(16));
			List<Long> delete_menu_id_list = new ArrayList<>();
			System.out.println("删除正在进行");
			int flag = 0;
			long menuId;
			for(Object OmenuId : menuIdList) {
				System.out.println("for"+OmenuId.getClass());
				String mi = OmenuId.toString();
				System.out.println(mi);
				menuId = Long.parseLong(mi);
				System.out.println("LOng"+menuId);
				childMenu.add(menuId);
				delete_menu_id_list = treeMenuList(menuDomains, menuId);
				System.out.println("$$$$$$$$$"+delete_menu_id_list);
			}
			System.out.println("最后的数组是"+delete_menu_id_list);
			int reslut =  menuDao.deleteMenu(delete_menu_id_list);
			Map map = new HashMap<>();
			map.put("result", reslut);
			map.put("deleteArray", delete_menu_id_list);
			return map;
			//return 1;
		}
//	 public int deleteMenu(Long menuId) {
//		childMenu.clear();
//		System.out.println("删除正在进行");
//		int flag = 0;
//		childMenu.add(menuId);
//		List<MenuDomain> menuDomains = menuDao.list(new HashMap<>(16));
//		List<Long> delete_menu_id_list = treeMenuList(menuDomains, menuId);
//		System.out.println("最后的数组是"+delete_menu_id_list);
//		return menuDao.deleteMenu(delete_menu_id_list);
//	}
	/**
	 * 根据menuId查询 menu 是否存在
	 * @param menuId
	 * @return
	 */
	 public int isExiting(Long  menuId) {
		 return menuDao.isExisting(menuId);
	 }
	 
	 /**
	  * 保存/新增 menu
	  * @param menuDomain
	  * @return
	  */
	 @CacheEvict(value = "menu-cache" ,allEntries = true)
	 @Transactional(rollbackFor = {RuntimeException.class,Exception.class}
	 	, propagation= Propagation.REQUIRED)
	 public int saveMenuDomain(MenuDomain menuDomain) {
		 return menuDao.saveMenu(menuDomain);
	 }
	 @Cacheable
	 public List<MenuDomain> getLimitMenuList(MenuSearchLimit menuSearchLimit){
		 System.out.println("menuSearchLimit"+menuSearchLimit);
		 List<MenuDomain> limitMenuDomain = menuDao.listMenuByLimit(menuSearchLimit);
		 System.out.println(limitMenuDomain);
		 return limitMenuDomain;
	 }
	@Override
	public Set<String> listPerms(Long userId) {
//		Map<String, Object> m = new HashMap<>();
//		m.put("userId", userId);
//		List<RoleDO> allRoles = roleService.parentsList(userRoleMapper.list(m));
//		RoleDO r = new RoleDO();
//		if (allRoles.isEmpty()) {
//			allRoles = new ArrayList<RoleDO>();
//			r = new RoleDO();
//			r.setRoleId(new Long(-1));
//			allRoles.add(r);
//		} else{
//		}
//		Long[] menuIds = userMenuMapper.cascadeMenuIdList(userId,allRoles);
//		List<MenuDomain> menus = menuDao.listInArray(menuIds);
		List<MenuDomain> menus = listMenusByUserId(userId);
		System.out.println(menus.toString());
		Set<String> permsSet = new HashSet<>();
		for (MenuDomain menu : menus) {
			if (StringUtils.isNotBlank(menu.getPerms())) {
				permsSet.addAll(Arrays.asList(menu.getPerms().trim().split(",")));
			}
		}
		return permsSet;
//		return null;
	}
	
	public List<MenuDomain> list(Map<String, Object> params) {
		List<MenuDomain> menus = menuDao.list(params);
		return menus;
	}
	@Override
	public List<MenuDomain> listMenusByUserId(Long userId) {
		Map<String, Object> m = new HashMap<>();
		m.put("userId", userId);
		List<RoleDO> allRoles = roleService.parentsList(userRoleMapper.list(m));
		RoleDO r = new RoleDO();
		if (allRoles.isEmpty()) {
			allRoles = new ArrayList<RoleDO>();
			r = new RoleDO();
			r.setRoleId(new Long(-1));
			allRoles.add(r);
		} else{
		}
		Long[] menuIds = userMenuMapper.cascadeMenuIdList(userId,allRoles);
		List<MenuDomain> menus = menuDao.listInArray(menuIds);
		return menus;
	}
}


