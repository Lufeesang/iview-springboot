////package com.bootdo.system.dao;
////
////import com.bootdo.system.domain.MenuDO;
////import com.bootdo.system.domain.MenuDomain;
////import com.bootdo.system.domain.MenuSearchLimit;
////
////import java.util.List;
////import java.util.Map;
////
////import org.apache.ibatis.annotations.Mapper;
////
/////**
//// * 菜单管理
//// * @author chglee
//// * @email 1992lcg@163.com
//// * @date 2017-10-03 09:45:09
//// */
////@Mapper
////public interface MenuDao {
////
////	MenuDO get(Long menuId);
////	
////	List<MenuDO> list(Map<String,Object> map);
////	
////	int count(Map<String,Object> map);
////	
////	int save(MenuDO menu);
////	
////	int update(MenuDO menu);
////	
////	int remove(Long menuId);
////	
////	int batchRemove(Long[] menuIds);
////	
////	List<MenuDO> listMenuByUserId(Long id);
////	
////	List<String> listUserPerms(Long id);
////}
////
////
//
//
//package com.bootdo.system.dao;
//
//import java.lang.reflect.Array;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.ibatis.annotations.Mapper;
//
//import com.bootdo.system.domain.MenuDomain;
//import com.bootdo.system.domain.MenuSearchLimit;
//
//
//@Mapper
//public interface MenuDao {
//	/**
//	 * 通过id，查询menu是否存在
//	 * @param menuId
//	 * @return
//	 */
//	int isExisting(Long menuId);
//	/**
//	 * 通过id,获得menu 实体类
//	 * @param menuId
//	 * @return
//	 */
//	MenuDomain get(Long menuId);
//	
//	/**
//	 * 返回 menu List
//	 * @param map
//	 * @return
//	 */
//	List<MenuDomain> list(Map<String,Object> map);
//	
//	int count(Map<String,Object> map);
//	
//	/**
//	 * 保存 menu
//	 * @param menu
//	 * @return
//	 */
//	int saveMenu(MenuDomain menu);
//	
//	/**
//	 * 更新menu
//	 * @param menu
//	 * @return
//	 */
//	int update(MenuDomain menu);
//	
//	/**
//	 * 删除menu
//	 * @param menu
//	 * @return
//	 */
//	int deleteMenu(List<Long> list);
//	
//	/**
//	 * 批量删除menu
//	 * @param menu
//	 * @return
//	 */
//	int batchRemove(Long[] menuIds);
//	
//	/**
//	 * 通过用户Id查找，该用户拥有的menu
//	 * @param menu
//	 * @return
//	 */
//	List<MenuDomain> listMenuByUserId(Long id);
//	
//	
//	List<String> listUserPerms(Long id);
//	
//	/**
//	 * 通过查询条件，进行查询，返回符合条件的menu List
//	 * @param menu
//	 * @return
//	 */
//	List<MenuDomain> listMenuByLimit(MenuSearchLimit menuSearchLimit);
//	
//	List<MenuDomain> listInArray (Long[] menuIds);
//}
//
//
//
//

package com.bootdo.system.dao;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bootdo.system.domain.MenuDomain;
import com.bootdo.system.domain.MenuSearchLimit;


@Mapper
public interface MenuDao {
	/**
	 * 通过id，查询menu是否存在
	 * @param menuId
	 * @return
	 */
	int isExisting(Long menuId);
	/**
	 * 通过id,获得menu 实体类
	 * @param menuId
	 * @return
	 */
	MenuDomain get(Long menuId);
	
	/**
	 * 返回 menu List
	 * @param map
	 * @return
	 */
	List<MenuDomain> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	/**
	 * 保存 menu
	 * @param menu
	 * @return
	 */
	int saveMenu(MenuDomain menu);
	
	/**
	 * 更新menu
	 * @param menu
	 * @return
	 */
	int update(MenuDomain menu);
	
	/**
	 * 删除menu
	 * @param menu
	 * @return
	 */
	int deleteMenu(List<Long> list);
	
	/**
	 * 批量删除menu
	 * @param menu
	 * @return
	 */
	int batchRemove(Long[] menuIds);
	
	/**
	 * 通过用户Id查找，该用户拥有的menu
	 * @param menu
	 * @return
	 */
	List<MenuDomain> listMenuByUserId(Long id);
	
	
	List<String> listUserPerms(Long id);
	
	/**
	 * 通过查询条件，进行查询，返回符合条件的menu List
	 * @param menu
	 * @return
	 */
	List<MenuDomain> listMenuByLimit(MenuSearchLimit menuSearchLimit);
	List<MenuDomain> listInArray (Long[] menuIds);
}

