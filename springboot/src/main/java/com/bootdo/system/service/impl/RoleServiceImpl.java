package com.bootdo.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bootdo.system.dao.RoleDao;
import com.bootdo.system.dao.RoleMenuDao;
import com.bootdo.system.dao.UserDao;
import com.bootdo.system.dao.UserMenuDao;
import com.bootdo.system.dao.UserRoleDao;
import com.bootdo.system.domain.RoleDO;
import com.bootdo.system.domain.RoleMenuDO;
import com.bootdo.system.domain.UserRoleDO;
import com.bootdo.system.service.RoleService;


@Service
@CacheConfig(cacheNames = "role-cache")
public class RoleServiceImpl implements RoleService {

    public static final String ROLE_ALL_KEY = "\"role_all\"";

    public static final String DEMO_CACHE_NAME = "role";

    @Autowired
    RoleDao roleMapper;
    @Autowired
    RoleMenuDao roleMenuMapper;
    @Autowired
    UserDao userMapper;
    @Autowired
    UserRoleDao userRoleMapper;
    @Autowired
    UserMenuDao userMenuMapper;

    @Cacheable
    @Override
    public List<RoleDO> list(HashMap<String, Object> params) {
        List<RoleDO> roles = roleMapper.list(params);
        return roles;
    }

    @Cacheable
    @Override
    public List<RoleDO> list(Long roleId) {
        List<Long> rolesIds = userRoleMapper.listRoleId(roleId);
        List<RoleDO> roles = roleMapper.list(new HashMap<>(16));
        for (RoleDO roleDO : roles) {
            roleDO.setRoleSign("false");
            for (Long id : rolesIds) {
                if (Objects.equals(roleDO.getRoleId(), id)) {
                    roleDO.setRoleSign("true");
                    break;
                }
            }
        }
        return roles;
    }
    
    @CacheEvict(value = "role-cache", allEntries = true)
    @Override
    public int save(RoleDO role) {
        int count = roleMapper.save(role);
        return count;
    }

    @CacheEvict(value = "role-cache", allEntries = true)
    @Override
    public int remove(Long id) {
//        userMenuMapper.removeCasecadeByRoleId(id);
        roleMenuMapper.removeByRoleId(id);
        userRoleMapper.removeByRoleId(id);
        int count = roleMapper.remove(id);
        return count;
    }

    @Override
    public RoleDO get(Long id) {
        RoleDO roleDO = roleMapper.get(id);
        return roleDO;
    }
    
    @CacheEvict(value = "role-cache", allEntries = true)
    @Override
    public int update(RoleDO role) {
        int r = roleMapper.update(role);
        return r;
    }

    @Transactional(rollbackFor = {RuntimeException.class,Exception.class}
 	, propagation= Propagation.REQUIRED)
    @CacheEvict(value = "role-cache", allEntries = true)
    @Override
    public int batchremove(Long[] ids) {
    	for (Long id : ids) {
    		remove(id);
    	}
        return 0;
    }

    @Cacheable
	@Override
	public List<RoleDO> parentsList(List<UserRoleDO> userRoleDOs) {
    	List<RoleDO> allList = roleMapper.list(null);
    	List<RoleDO> temp = new ArrayList<RoleDO>();
    	boolean flag = true;
    	Long tempL = null;
    	for (UserRoleDO userRoleDO : userRoleDOs) {
    		flag = true;
    		tempL = userRoleDO.getRoleId();
    		while(flag) {
    			flag = false;
    			for(RoleDO roleDO : allList) {
    				if(roleDO.getRoleId() == tempL) {
    					flag = true;
    					temp.add(roleDO);
    					tempL = roleDO.getParentId();
    				}
    			}
    		}
    	}
		return temp;
	}
	
    @CacheEvict(value = "role-cache", allEntries = true)
	@Transactional(rollbackFor = {RuntimeException.class,Exception.class}
 	, propagation= Propagation.REQUIRED)
	public int batchSave(List<RoleDO> list) {
		//check
		return roleMapper.batchSave(list);
	}
}
