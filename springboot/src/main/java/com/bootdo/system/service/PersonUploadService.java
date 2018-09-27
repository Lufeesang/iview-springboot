package com.bootdo.system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface PersonUploadService {
	public int isExiting(long userId);
	public Object doUploadFile(MultipartFile file) throws Exception;
}
