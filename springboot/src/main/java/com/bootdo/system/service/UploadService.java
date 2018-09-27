package com.bootdo.system.service;

import java.util.List;
import java.util.Map;

import org.apache.catalina.startup.Tomcat.ExistingStandardWrapper;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.PublicImpl;
import org.springframework.web.multipart.MultipartFile;

import com.bootdo.system.domain.MenuDomain;

public interface UploadService {
	public boolean isExiting(long menuId);
	public Map<String,Object> doUploadFile(MultipartFile file) throws Exception;
	
}
