package com.bootdo.system.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bootdo.system.domain.UserDO;
import com.bootdo.system.service.PersonUploadService;
import com.bootdo.system.service.UserService;
import com.bootdo.system.util.ExcelImportUtils;

@Service
public class PersonUploadServiceImpl implements PersonUploadService{
	
	@Autowired
	UserService userService;
	
	ExcelImportUtils excelImportUtils = new ExcelImportUtils();
	
	List<UserDO> importUserDOList = new ArrayList<>();
	@Override
	public int isExiting(long userId) {
		return userService.isExiting(userId);
	}

	@Override
	public Object doUploadFile(MultipartFile file) throws Exception {
		
		importUserDOList.clear();
		// TODO Auto-generated method stub
		// 导入的数据变量
		String username;
		String name;
		Long deptId;
		String email;
		
		//
		String  errorTips = "";
		String uploadReslut = "";
		boolean correctFlag = true;
		String fileName = file.getOriginalFilename();
		boolean notNull = false;
		boolean isExcel2003 = true;
		boolean isExcel2007 = true;
		
		//
		UserDO userDO;
		
		//
		System.out.println(fileName);
		if(!excelImportUtils.validateExcel(fileName)) {
			System.out.println( "输入的文件后缀不正确，请重新选择");
			errorTips = errorTips +  "输入的文件后缀不正确，请重新选择";
			
		}else {
			if (excelImportUtils.isExcel2003(fileName)) {
				isExcel2007 = false;
				System.out.println("判断为2003");
			}else if(excelImportUtils.isExcel2007(fileName)){
				isExcel2003 = false;
				System.out.println("判断为2007");
			}
			InputStream inputStream = file.getInputStream();
			Workbook workbook = null;
			if(isExcel2003) {
				workbook = new HSSFWorkbook(inputStream);
				System.out.println("HSSFWorkbook(inputStream)");
			}
			else if(isExcel2007) {
				workbook =  new XSSFWorkbook(inputStream);
				System.out.println("XSSFWorkbook(inputStream)");
			}
			Sheet sheet = workbook.getSheetAt(0);
			if(sheet != null) {
				notNull = true;
				System.out.println("notNUll is ok");
				System.out.println(sheet);
				System.out.println("总共有多少行"+sheet.getLastRowNum());
				for (int r = 1; r <= sheet.getLastRowNum(); r++) {
					Row row = sheet.getRow(r);
					if (row == null ) {
						System.out.println("出现空行");
						continue;
					}
					userDO = new UserDO();
					
					row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					username = row.getCell(0).getStringCellValue();
					
					if(username == null){
						errorTips = errorTips + "(第"+(r+1)+"行,用户名未填写)";
						correctFlag = false;
						
					}
					
					row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
					name = row.getCell(1).getStringCellValue();
				 
					if( name == null || name.isEmpty()){
						correctFlag = false;
						errorTips = errorTips + "(第"+(r+1)+"行,姓名未填写)";
					}
					
					row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
					deptId = Long.parseLong(row.getCell(2).getStringCellValue());
					
					row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
					email = row.getCell(3).getStringCellValue();
					
					
					if(correctFlag) {
						userDO.setUsername(username);
						userDO.setName(name);
						userDO.setDeptId(deptId);
						userDO.setEmail(email);
						importUserDOList.add(userDO);
					}else {
						errorTips = errorTips + "<br>";
					}
				}//for 遍历结束
			  }else {
				System.out.println("sheet  出错");
				errorTips = errorTips + "页码（sheet） 不能为零";
			  }
		}
		if(correctFlag) {
			//进行插入保存数据
			int count = 0;
			for(UserDO sysUserDomain : importUserDOList) {
				userService.userSave(sysUserDomain);
				count++;
			}
			 uploadReslut = "总共插入了"+count+"条数据";
		}
		if(!correctFlag) return errorTips;
		return uploadReslut;
	}
}
