//package com.bootdo.system.service;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.springframework.stereotype.Service;
//
//import com.bootdo.common.domain.Tree;
//import com.bootdo.system.domain.MenuDO;
//
//@Service
//public interface MenuService {
//	Tree<MenuDO> getSysMenuTree(Long id);
//
//	List<Tree<MenuDO>> listMenuTree(Long id);
//
//	Tree<MenuDO> getTree();
//
//	Tree<MenuDO> getTree(Long id);
//
//	List<MenuDO> list(Map<String, Object> params);
//
//	int remove(Long id);
//
//	int save(MenuDO menu);
//
//	int update(MenuDO menu);
//
//	MenuDO get(Long id);
//
//	Set<String> listPerms(Long userId);
//}

package com.bootdo.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bootdo.system.common.Tree;
import com.bootdo.system.domain.MenuDomain;
import com.bootdo.system.domain.MenuSearchLimit;

@Service
public interface MenuService {
	public List<Tree<MenuDomain>> getTree();

	public List<MenuDomain> getMenuList();

	public int updateMenu(MenuDomain menuDomain);

	public List<Long> childMenu = new ArrayList<>();

	public List<Long> treeMenuList(List<MenuDomain> menuDos, Long parentId);

	// public int deleteMenu(Long menuId);
	public Map<String, Object> deleteMenu(List<Long> menuIdList);

	public int isExiting(Long menuId);

	public int saveMenuDomain(MenuDomain menuDomain);

	public List<MenuDomain> getLimitMenuList(MenuSearchLimit menuSearchLimit);

	public MenuDomain get(Long menuId);

	Set<String> listPerms(Long userId);
	
	List<MenuDomain> listMenusByUserId(Long userId);
	
	List<MenuDomain> list(Map<String, Object> params);
}
