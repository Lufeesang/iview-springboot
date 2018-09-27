package com.bootdo.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bootdo.common.annotation.Log;
import com.bootdo.system.service.PersonUploadService;


@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/userUpload")
public class PersonUploadController {
	
	@Autowired
	PersonUploadService personUploadService;
	@Log("操作-查询-获取所有部门信息")
	@ResponseBody
	@RequestMapping(value="/doUpload", method=RequestMethod.POST)
	public Object doUpload(MultipartFile file) throws Exception {	
		Object doUploadResult =  personUploadService.doUploadFile(file);
		System.out.println("doUploadResult"+doUploadResult);
		return doUploadResult;		
	}
}
