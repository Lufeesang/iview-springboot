package com.bootdo.system.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bootdo.common.annotation.Log;
import com.bootdo.system.service.impl.UploadServiceImpl;


@Controller
public class UploadController {
	
	@Autowired
	UploadServiceImpl uploadServiceImpl;
	
	@Log("操作-添加-上传菜单文件，批量导入")
	@ResponseBody
	@RequestMapping(value="/doUpload", method=RequestMethod.POST)
	//public void doUpload(@RequestParam("fileName") MultipartFile file) throws Exception {
	public Map<String,Object> doUpload( MultipartFile file) throws Exception {	
		Map<String,Object> doUploadResult = new HashMap<>();
		try {
			 doUploadResult =  uploadServiceImpl.doUploadFile(file);
		}catch(Exception e) {
			doUploadResult.put("msg", "文件中可能存在menuId 与 数据库中字段相同的情况，或者该文件缺少menuId/parentId字段，请仔细检查");
			doUploadResult.put("result", -1);
		}
		System.out.println("doUploadResult"+doUploadResult);
		return doUploadResult;
		
		
	}
}

