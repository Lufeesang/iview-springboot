//package com.bootdo.system.service.impl;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.formula.functions.Count;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.bootdo.system.domain.MenuDomain;
//import com.bootdo.system.service.UploadService;
//import com.bootdo.system.util.ExcelImportUtils;
//import com.fasterxml.jackson.annotation.JsonProperty;
//
////import io.netty.handler.codec.CorruptedFrameException;
//
//
///**
// * 思路，逐行读取数据，
// * 判断每一个列中的数据是否符合要求，为null 或者是空字符串 ，则isNullRow ++
// * 当isNullRow == 文件列数的时候，标明是空行，则 correct 不变，但是 insertflag = false；
// * 当correct = insertflag = true 时候，才插入，
// * 当出现某一行中某个cell单元格中数据缺失时候，correct = false；
// * @author zcchen
// *
// */
//@Service
//public class UploadServiceImpl implements UploadService{
//	@Autowired
//	MenuServiceImpl menuServiceImpl;
//	ExcelImportUtils excelImportUtils = new ExcelImportUtils();
//	
//	//查找父节点是否存在与数据库
//	public boolean  isExiting(long menuId) {
//		int result = menuServiceImpl.isExiting(menuId);
//		if(result == 0) return false;
//		return true;
//		
//	}
//	
//	//查找父节点是否存在于上传的文件
//	
////	public boolean isExitingOnFile(Long parentId,List<Map<String, Long>> menuMap) {
////		
////		return true;
////		
////	}
//	
//	//遍历menuId parentId 对，查看是否有循环出现
//	
//	@Transactional(rollbackFor = {RuntimeException.class,Exception.class}
// 	, propagation= Propagation.REQUIRED)
//	public Object doUploadFile(MultipartFile file) throws Exception{
//		List<MenuDomain> importMenuDomainList = new ArrayList<>();
//		
//		//存放导入的菜单menuIds
//		List<Long> menuIds = new ArrayList<>();
//		
//		//存放导入的菜单parentIds
//		List<Long> parentIds =  new ArrayList<>();
//		
//		//存放读取到的 menuId parentId 对
//		List<Map<String, Long>> menuList= new ArrayList<>();
//		
//		//存放反馈信息
//		Map<String, Object> map = new HashMap<>();
//		String emptyWarn = "";
//		boolean insertflag = true ;
//		//是否空行
//		int isNullRow = 0;
//		//统计合法数据条目
//		int count = 0;
//		// 父菜单ID，一级菜单为0
//		Long parentId = 0L;
//		//菜单ID
//		Long menuId = 0L;
//		// 菜单名称
//		String name = "";
//		// 菜单URL
//		String url = "";
//		// 授权(多个用逗号分隔，如：user:list,user:create)
//		String perms = "";
//		Date gmt_create;
//		String  errorTips = "";
//		String uploadReslut = "";
//		boolean correctFlag = true;
//		String fileName = file.getOriginalFilename();
//		boolean notNull = false;
//		boolean isExcel2003 = true;
//		boolean isExcel2007 = true;
//		boolean notCicle = true;
//		MenuDomain menuDomain;
//		System.out.println(fileName);
//			if(!excelImportUtils.validateExcel(fileName)) {
//				System.out.println( "输入的文件后缀不正确，请重新选择");
//				errorTips = errorTips +  "输入的文件后缀不正确，请重新选择";
//				
//			}else {
//				if (excelImportUtils.isExcel2003(fileName)) {
//					isExcel2007 = false;
//					System.out.println("判断为2003");
//				}else if(excelImportUtils.isExcel2007(fileName)){
//					isExcel2003 = false;
//					System.out.println("判断为2007");
//				}
//				InputStream inputStream = file.getInputStream();
//				Workbook workbook = null;
//				if(isExcel2003) {
//					workbook = new HSSFWorkbook(inputStream);
//					System.out.println("HSSFWorkbook(inputStream)");
//				}
//				else if(isExcel2007) {
//					workbook =  new XSSFWorkbook(inputStream);
//					System.out.println("XSSFWorkbook(inputStream)");
//				}
//				Sheet sheet = workbook.getSheetAt(0);
//				if(sheet != null) {
//					notNull = true;
//					System.out.println("notNUll is ok");
//					System.out.println(sheet);
//					System.out.println("总共有多少行"+sheet.getLastRowNum() +sheet.getLastRowNum());
//					for (int r = 1; r <= sheet.getLastRowNum(); r++) {
//						isNullRow = 0;
//						//和correctflat 同时为 1 时候，才进行插入
//						insertflag = true ; 
//						Row row = sheet.getRow(r);
//						if (row == null ) {
//							emptyWarn = emptyWarn + "第"+(r+1)+"行出现空行，请尽量不要出现空行"+"\n";
//							System.out.println("出现空行");
//							continue;
//						}
//	//					StringBuilder stringBuilder = new StringBuilder();
//	//					stringBuilder.indexOf(str);
//						System.out.println("type "+(row.getCell(0).getCellType()));
//						//通过 type 来判断单元格内的字符类型，如果全是数字，则type =0 包含空格也是0
//						if(row.getCell(0).getCellType() != 1) {
//							System.out.println("等于1");
//							if( row.getCell(0)!=null) {
//								//读取第一列
//								row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
//								if(row.getCell(0).getStringCellValue() == "" || (row.getCell(0).getStringCellValue().trim().length()==0)) {
//									errorTips = errorTips + "(第"+(r+1)+"行,菜单ID为空字符串"+"\n";
//									
//									isNullRow++;
//									correctFlag = false;
//								}else {
//									menuId = Long.parseLong(row.getCell(0).getStringCellValue().trim());
//									System.out.println("第"+(r+1)+"menuId"+menuId);
//									if(menuIds.contains(menuId)) {
//										errorTips = errorTips + "第"+(r+1)+"行前菜单Id出现重复"+"\n";
//										correctFlag = false;
//									}
//									menuIds.add(menuId);
//									if(menuId == null || (menuId < 0) ){
//										errorTips = errorTips + "(第"+(r+1)+"行,菜单ID未填写)"+"\n";
//										if(menuId == null) isNullRow++;
//										correctFlag = false;
//										//throw new Exception("导入失败(第"+(r+1)+"行,姓名未填写)");
//									}
//								}
//							}else {
//								errorTips = errorTips + "(第"+(r+1)+"行,菜单ID未填写)"+"\n";
//								isNullRow++;
//								correctFlag = false;
//							}
//						}else {
//							System.out.println("菜ID包含了非数字字符");
//							errorTips = errorTips + "(第"+(r+1)+"行,菜ID包含了非数字字符)"+"\n";
//							correctFlag = false;
//						}
//						System.out.println("menuId isNullRow"+isNullRow);
//						if(row.getCell(1).getCellType() != 1) {
//							if(row.getCell(1)!=null) {
//								//读取第二列
//								//强行把excel中的第一列中内容parentId变化为String 来读取
//								row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
//								if(row.getCell(1).getStringCellValue() == ""|| (row.getCell(1).getStringCellValue().trim().length()==0)) {
//									errorTips = errorTips + "(第"+(r+1)+"行,父菜单为空字符串"+"\n";
//									isNullRow++;
//									correctFlag = false;
//								}else {
//									parentId = Long.parseLong(row.getCell(1).getStringCellValue().trim());
//									System.out.println("第"+(r+1)+"parentId"+parentId);
//									parentIds.add(parentId);
//									if(parentId == null || (parentId < 0) ){
//										errorTips = errorTips + "(第"+(r+1)+"行,父菜单ID未填写)"+"\n";
//										if(parentId == null) isNullRow++;
//										correctFlag = false;
//										//throw new Exception("导入失败(第"+(r+1)+"行,姓名未填写)");
//									}
//			//						else {
//			//							if(isExiting(parentId) == 0 && parentId != 0 && !isExitingOnFile(parentId,)) {
//			//								errorTips = errorTips + "(第"+(r+1)+"行,父菜单不存在)";
//			//								correctFlag = false;
//			//							}
//			//						}
//								}
//							}else {
//								errorTips = errorTips + "(第"+(r+1)+"行,父菜单ID未填写)"+"\n";
//								isNullRow++;
//								correctFlag = false;
//							}
//						}else {
//							System.out.println("父菜单ID包含了非数字字符");
//							errorTips = errorTips + "(第"+(r+1)+"行,父菜单ID包含了非数字字符)"+"\n";
//							correctFlag = false;
//						}
//						System.out.println("parentID isNullRow"+isNullRow);
//		
//						if(row.getCell(2)!=null) {
//							row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
//							name = row.getCell(2).getStringCellValue();
//							if (name == "" || (row.getCell(2).getStringCellValue().trim().length()==0)) isNullRow++;
//							if( name == null || name.isEmpty()){
//								correctFlag = false;
//								errorTips = errorTips + "(第"+(r+1)+"行,菜单名称未填写)"+"\n";
//								isNullRow++;
//								//throw new Exception("导入失败(第"+(r+1)+"行,密码未填写)");
//						}
//						}else {
//							errorTips = errorTips + "(第"+(r+1)+"行,菜单名称未填写)"+"\n";
//							isNullRow++;
//							correctFlag = false;
//						}
//						
//						System.out.println("现在是第"+ (r+1) +"行");
//						//row.getCell(3).getStringCellValue().trim().length()>0 判断不是空格组成
//						if(row.getCell(3) != null ) {
//		
//							row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
//							url = row.getCell(3).getStringCellValue();
//							if (url == "" || url.trim().length() == 0) isNullRow++;
//							System.out.println("URL"+url);
//						}else {
//							System.out.println("URL现在是空");
//							isNullRow++;
//						}
//						if(row.getCell(4) != null && (row.getCell(4).getStringCellValue().trim().length()>0)) {
//							row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
//							perms = row.getCell(4).getStringCellValue();
//							if (perms == "" || perms.trim().length() == 0) isNullRow++;
//							System.out.println("bug");
//						}else {
//							System.out.println("Perm现在是空");
//							isNullRow++;
//						}
//						System.out.println("现在NullROw  "+isNullRow);
//						if(isNullRow >= 5) {
//							System.out.println("第"+(r+1)+"行出现空行，请尽量不要出现空行");
//							emptyWarn = emptyWarn + "第"+(r+1)+"行出现空行，请尽量不要出现空行"+"\n";
//							correctFlag = true;
//							insertflag = false;
//						}
//						if(correctFlag && insertflag) {
//							menuDomain = new MenuDomain();
//							//顺利读取到文档中正确 menuId 和 parentId  存入 menuList
//							Map<String , Long > menuMap = new HashMap<>();
//							menuMap.put("menuId", menuId);
//							menuMap.put("parentId", parentId);
//							menuList.add(menuMap);
//							System.out.println("Map:"+menuMap);
//							menuDomain.setMenuId(menuId);
//							menuDomain.setParentId(parentId);
//							menuDomain.setName(name);
//							menuDomain.setPerms(perms);
//							menuDomain.setUrl(url);
//							Date date =  new Date();
//							menuDomain.setGmtCreate(date);
//							System.out.println("menuDomain"+menuDomain.toString());
//							importMenuDomainList.add(menuDomain);
//		            	
//						}else {
//							errorTips = errorTips + "\n";
//						}
//						
//						System.out.println("现在是第"+(r+1)+"行"+menuId+" "+parentId +" "+ name);
//					}//for 遍历结束
//				  }else {
//					System.out.println("sheet  出错");
//					errorTips = errorTips + "页码（sheet） 不能为零"+"\n";
//				  }
//			}
//			
//			if(correctFlag) {
//				boolean checkParentId = true;
//				//判断上传的菜单中是否存在 循环，即 A-》B，B-》C，C-》A
//				notCicle = true;
//				//查看数据
//				System.out.println("menuList<Map>"+menuList);
//				
//				//增加校验，用于检查上传文件=》 菜单信息中parentId是否存在
//				for(Long parent_id : parentIds) {
//					if(!menuIds.contains(parent_id) && !isExiting(parent_id) && parent_id !=0) {
//						errorTips = errorTips + "菜单Id"+parent_id+"数据库中不存在，文件中也不存在，不能作为父菜单Id"+"\n";
//						checkParentId = false;
//					}
//				}
//				List<Long> cicleLIst  = new ArrayList<>();
//				for(Map<String, Long> object : menuList ) {
//					//遍历 文档中的 menuId parentId 对，存放menuId
//					List<Long> okMenuId = new ArrayList<>();
//					System.out.println(object.toString()+object.getClass());
//					okMenuId.add(object.get("menuId"));
//					System.out.println("刚开始的时候的okmenuId"+okMenuId);
//					//System.out.println("主循环："+chekCicle(okMenuId,object.get("menuId"), object.get("parentId"),menuList));
//					cicleLIst = chekCicle(okMenuId,object.get("menuId"), object.get("parentId"),menuList);
//					if(cicleLIst != null && cicleLIst.size() >0) {
//						System.out.println("以下菜单陷入循环"+cicleLIst);
//						errorTips = errorTips +"以下菜单陷入循环"+cicleLIst+"\n";
//						notCicle = false;
//						break;
//					}
//				}
//				if(checkParentId && notCicle) {
//					//进行插入保存数据
//					count = 0;
//					for(MenuDomain sysMenuDomain : importMenuDomainList) {
//						System.out.println(sysMenuDomain.toString());
//						menuServiceImpl.saveMenuDomain(sysMenuDomain);
//						count++;
//					}
//				 }
//				 uploadReslut = "总共插入了"+count+"条数据"+"\n";
//				 map.put("result", count);
//				 map.put("DomainList", importMenuDomainList);
//				 map.put("warn", emptyWarn);
//				 map.put("errorTips", "");
//			}
//			//System.out.println("插入结果，upload Result"+uploadResult);
//			if(!correctFlag || !notCicle)  {
//				map.put("errorTips", errorTips);
//				map.put("result", 0);
//			}
//			return map;
//	
//	}
//
//		//递归查找父节点
//	private List<Long> chekCicle(List<Long> okMenuId,Long menuid, Long parentid , List<Map<String, Long>> menuList) {
//		//定义List<Long> 用来返回
//		List<Long> cirleList = new ArrayList<>();
//		// TODO Auto-generated method stub
//		//A->B,B->C,C-A; 表示A的父节点时B，类推
//		//okMenuId 会依此存如A，然后调用 checkCicle(okMenuId, A的parentId)
//		//然后 判断 A的parentId 是否已经在 okMenuId ，中，如果在，则表示时一个循环，此时返回fale;
//		System.out.println("okMenId "+okMenuId);
//		System.out.println("parentId"+parentid);
//		System.out.println("menuLIst"+menuList.toString());
//
//		for(Map<String ,Long > object : menuList) {
//			//传入的父类Id，能在 传入menuIdLIst里找到
//			//System.out.print("   "+object.get("menuId")+"  "+object.get("menuId").getClass()+"   ");
//			if(parentid.equals(object.get("menuId"))) {
//				//这个父Id没有出现在okMenuId中，表示当前追溯没有出现循环
//				System.out.println("父节点在文件数据中");
//				if(!okMenuId.contains(parentid)) {
//					okMenuId.add(parentid);
//					//System.out.println("okMenuId没有"+okMenuId);
////					return chekCicle(okMenuId, parentid, object.get("parentId"), menuList);
//					if(chekCicle(okMenuId, parentid, object.get("parentId"), menuList) != null) {
//						//System.out.println("********循环树********"+menuList);
//						cirleList.addAll(okMenuId);
//						//System.out.println("循环树后面 cirleList"+cirleList);
//						return cirleList;
//					}
//				}else{
//					return cirleList;
//				}
//			}
//		}
//		
//		//传进来的parentId 不是文件中的一个menuId，树没有比闭合
//		System.out.println("父节点在数据库");
//		List<Long> circleList2 = new ArrayList<>();
//		circleList2 = null;
//		if(circleList2 == null) System.out.println("是空的得到  ");
//		return circleList2;
//	}
//	
//}

package com.bootdo.system.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Count;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bootdo.system.domain.MenuDomain;
import com.bootdo.system.service.UploadService;
import com.bootdo.system.util.ExcelImportUtils;
import com.fasterxml.jackson.annotation.JsonProperty;




/**
 * 思路，逐行读取数据，
 * 判断每一个列中的数据是否符合要求，为null 或者是空字符串 ，则isNullRow ++
 * 当isNullRow == 文件列数的时候，标明是空行，则 correct 不变，但是 insertflag = false；
 * 当correct = insertflag = true 时候，才插入，
 * 当出现某一行中某个cell单元格中数据缺失时候，correct = false；
 * @author zcchen
 *
 */
@Service
public class UploadServiceImpl implements UploadService{
	@Autowired
	MenuServiceImpl menuServiceImpl;
	ExcelImportUtils excelImportUtils = new ExcelImportUtils();
	
	//查找父节点是否存在与数据库
	public boolean  isExiting(long menuId) {
		int result = menuServiceImpl.isExiting(menuId);
		if(result == 0) return false;
		return true;
		
	}
	
	//查找父节点是否存在于上传的文件
	
//	public boolean isExitingOnFile(Long parentId,List<Map<String, Long>> menuMap) {
//		
//		return true;
//		
//	}
	
	//遍历menuId parentId 对，查看是否有循环出现
	
	@Transactional(rollbackFor = {RuntimeException.class,Exception.class}
 	, propagation= Propagation.REQUIRED)
	public Map<String, Object> doUploadFile(MultipartFile file) throws Exception{
		List<MenuDomain> importMenuDomainList = new ArrayList<>();
		
		//存放导入的菜单menuIds
		List<Long> menuIds = new ArrayList<>();
		
		//存放导入的菜单parentIds
		List<Long> parentIds =  new ArrayList<>();
		
		
		//存放读取到的 menuId parentId 对
		List<Map<String, Long>> menuList= new ArrayList<>();
		
		//存放反馈信息
		Map<String, Object> map = new HashMap<>();
		String emptyWarn = "";
		boolean insertflag = true ;
		//是否空行
		int isNullRow = 0;
		//统计合法数据条目
		int count = 0;
		// 父菜单ID，一级菜单为0
		Long parentId = 0L;
		//菜单ID
		Long menuId = 0L;
		// 菜单名称
		String name = "";
		// 菜单URL
		String url = "";
		//菜单类型
		Integer type = 0;
		// 授权(多个用逗号分隔，如：user:list,user:create)
		String perms = "";
		Date gmt_create;
		String  errorTips = "";
		String uploadReslut = "";
		boolean correctFlag = true;
		String fileName = file.getOriginalFilename();
		boolean notNull = false;
		boolean isExcel2003 = true;
		boolean isExcel2007 = true;
		boolean notCicle = true;
		MenuDomain menuDomain;
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
				System.out.println("总共有多少行"+sheet.getLastRowNum() +sheet.getLastRowNum());
				for (int r = 1; r <= sheet.getLastRowNum(); r++) {
					isNullRow = 0;
					//和correctflat 同时为 1 时候，才进行插入
					insertflag = true ; 
					Row row = sheet.getRow(r);
					if (row == null ) {
						emptyWarn = emptyWarn + "第"+(r+1)+"行出现空行，请尽量不要出现空行"+"\n";
						System.out.println("出现空行");
						continue;
					}
//					StringBuilder stringBuilder = new StringBuilder();
//					stringBuilder.indexOf(str);
					System.out.println("type "+(row.getCell(0).getCellType()));
					//通过 type 来判断单元格内的字符类型，如果全是数字，则type =0 包含空格也是0
					if(row.getCell(0).getCellType() != 1) {
						System.out.println("等于1");
						if( row.getCell(0)!=null) {
							//读取第一列
							row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
							if(row.getCell(0).getStringCellValue() == "" || (row.getCell(0).getStringCellValue().trim().length()==0)) {
								errorTips = errorTips + "(第"+(r+1)+"行,菜单ID为空字符串"+"\n";
								
								isNullRow++;
								correctFlag = false;
							}else {
								menuId = Long.parseLong(row.getCell(0).getStringCellValue().trim());
								System.out.println("第"+(r+1)+"menuId"+menuId);
								if(menuIds.contains(menuId)) {
									errorTips = errorTips + "第"+(r+1)+"行前菜单Id出现重复"+"\n";
									correctFlag = false;
								}
								if(isExiting(menuId)) {
									errorTips = errorTips + "第"+(r+1)+"行前菜单Id 在数据库中已经存在，禁止重复插入"+"\n";
									break;
								}
								menuIds.add(menuId);
								if(menuId == null || (menuId < 0) ){
									errorTips = errorTips + "(第"+(r+1)+"行,菜单ID未填写)"+"\n";
									if(menuId == null) isNullRow++;
									correctFlag = false;
									//throw new Exception("导入失败(第"+(r+1)+"行,姓名未填写)");
								}
							}
						}else {
							errorTips = errorTips + "(第"+(r+1)+"行,菜单ID未填写)"+"\n";
							isNullRow++;
							correctFlag = false;
						}
					}else {
						System.out.println("菜ID包含了非数字字符");
						errorTips = errorTips + "(第"+(r+1)+"行,菜ID包含了非数字字符)"+"\n";
						correctFlag = false;
					}
					System.out.println("menuId isNullRow"+isNullRow);
					if(row.getCell(1).getCellType() != 1) {
						if(row.getCell(1)!=null) {
							//读取第二列
							//强行把excel中的第一列中内容parentId变化为String 来读取
							row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
							if(row.getCell(1).getStringCellValue() == ""|| (row.getCell(1).getStringCellValue().trim().length()==0)) {
								errorTips = errorTips + "(第"+(r+1)+"行,父菜单为空字符串"+"\n";
								isNullRow++;
								correctFlag = false;
							}else {
								parentId = Long.parseLong(row.getCell(1).getStringCellValue().trim());
								System.out.println("第"+(r+1)+"parentId"+parentId);
								parentIds.add(parentId);
								if(parentId == null || (parentId < 0) ){
									errorTips = errorTips + "(第"+(r+1)+"行,父菜单ID未填写)"+"\n";
									if(parentId == null) isNullRow++;
									correctFlag = false;
									//throw new Exception("导入失败(第"+(r+1)+"行,姓名未填写)");
								}
		//						else {
		//							if(isExiting(parentId) == 0 && parentId != 0 && !isExitingOnFile(parentId,)) {
		//								errorTips = errorTips + "(第"+(r+1)+"行,父菜单不存在)";
		//								correctFlag = false;
		//							}
		//						}
							}
						}else {
							errorTips = errorTips + "(第"+(r+1)+"行,父菜单ID未填写)"+"\n";
							isNullRow++;
							correctFlag = false;
						}
					}else {
						System.out.println("父菜单ID包含了非数字字符");
						errorTips = errorTips + "(第"+(r+1)+"行,父菜单ID包含了非数字字符)"+"\n";
						correctFlag = false;
					}
					System.out.println("parentID isNullRow"+isNullRow);
					
					//读取菜单名字
					if(row.getCell(2)!=null) {
						row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
						name = row.getCell(2).getStringCellValue();
						if (name == "" || (row.getCell(2).getStringCellValue().trim().length()==0)) isNullRow++;
						if( name == null || name.isEmpty()){
							correctFlag = false;
							errorTips = errorTips + "(第"+(r+1)+"行,菜单名称未填写)"+"\n";
							isNullRow++;
							//throw new Exception("导入失败(第"+(r+1)+"行,密码未填写)");
		                
						}
					}else {
						errorTips = errorTips + "(第"+(r+1)+"行,菜单名称未填写)"+"\n";
						isNullRow++;
						correctFlag = false;
					}
					
					//读取菜单类型
					if(row.getCell(3)!=null) {
						row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
						if(row.getCell(3).getStringCellValue().trim().length()==0) {
							isNullRow++;
							errorTips = errorTips + "(第"+(r+1)+"行,菜单类型未填写)"+"\n";
						}else {
							type = Integer.valueOf(row.getCell(3).getStringCellValue());
							System.out.println("***************************"+type);
							if(type <0 || type >2) {
								errorTips = errorTips + "(第"+(r+1)+"行,菜单类型 只能是 0/1/2 )"+"\n";
							}
						}
					}else {
						errorTips = errorTips + "(第"+(r+1)+"行,菜单类型未填写)"+"\n";
						isNullRow++;
						correctFlag = false;
					}
					System.out.println("现在是第"+ (r+1) +"行");
					//row.getCell(3).getStringCellValue().trim().length()>0 判断不是空格组成
					if(row.getCell(4) != null ) {
	
						row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
						url = row.getCell(4).getStringCellValue();
						if (url == "" || url.trim().length() == 0) isNullRow++;
						System.out.println("URL"+url);
					}else {
						System.out.println("URL现在是空");
						isNullRow++;
					}
					if(row.getCell(5) != null && (row.getCell(5).getStringCellValue().trim().length()>0)) {
						row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
						perms = row.getCell(5).getStringCellValue();
						if (perms == "" || perms.trim().length() == 0) isNullRow++;
						System.out.println("bug");
					}else {
						System.out.println("Perm现在是空");
						isNullRow++;
					}
					System.out.println("现在NullROw  "+isNullRow);
					if(isNullRow >= 6) {
						System.out.println("第"+(r+1)+"行出现空行，请尽量不要出现空行");
						emptyWarn = emptyWarn + "第"+(r+1)+"行出现空行，请尽量不要出现空行"+"\n";
						correctFlag = true;
						insertflag = false;
					}
					if(correctFlag && insertflag) {
						menuDomain = new MenuDomain();
						//顺利读取到文档中正确 menuId 和 parentId  存入 menuList
						Map<String , Long > menuMap = new HashMap<>();
						menuMap.put("menuId", menuId);
						menuMap.put("parentId", parentId);
						menuList.add(menuMap);
						System.out.println("Map:"+menuMap);
						menuDomain.setMenuId(menuId);
						menuDomain.setParentId(parentId);
						menuDomain.setName(name);
						menuDomain.setPerms(perms);
						menuDomain.setUrl(url);
						menuDomain.setType(type);
						Date date =  new Date();
						menuDomain.setGmtCreate(date);
						System.out.println("menuDomain"+menuDomain.toString());
						importMenuDomainList.add(menuDomain);
	            	
					}else {
						errorTips = errorTips + "\n";
					}
					
					System.out.println("现在是第"+(r+1)+"行"+menuId+" "+parentId +" "+ name);
				}//for 遍历结束
			  }else {
				System.out.println("sheet  出错");
				errorTips = errorTips + "页码（sheet） 不能为零"+"\n";
			  }
		}
		
		if(correctFlag) {
			boolean checkParentId = true;
			//判断上传的菜单中是否存在 循环，即 A-》B，B-》C，C-》A
			notCicle = true;
			//查看数据
			System.out.println("menuList<Map>"+menuList);
			
			//增加校验，用于检查上传文件=》 菜单信息中parentId是否存在
			for(Long parent_id : parentIds) {
				if(!menuIds.contains(parent_id) && !isExiting(parent_id) && parent_id !=0) {
					errorTips = errorTips + "菜单Id"+parent_id+"数据库中不存在，文件中也不存在，不能作为父菜单Id"+"\n";
					System.out.println(errorTips);
					checkParentId = false;
				}
			}
			List<Long> cicleLIst  = new ArrayList<>();
			for(Map<String, Long> object : menuList ) {
				//遍历 文档中的 menuId parentId 对，存放menuId
				List<Long> okMenuId = new ArrayList<>();
				System.out.println(object.toString()+object.getClass());
				okMenuId.add(object.get("menuId"));
				System.out.println("刚开始的时候的okmenuId"+okMenuId);
				//System.out.println("主循环："+chekCicle(okMenuId,object.get("menuId"), object.get("parentId"),menuList));
				cicleLIst = chekCicle(okMenuId,object.get("menuId"), object.get("parentId"),menuList);
				if(cicleLIst != null && cicleLIst.size() >0) {
					System.out.println("以下菜单陷入循环"+cicleLIst);
					errorTips = errorTips +"以下菜单陷入循环"+cicleLIst+"\n";
					notCicle = false;
					break;
				}
			}
			System.out.println(""+checkParentId  +  notCicle);
			
			if(checkParentId && notCicle) {
				//进行插入保存数据
				count = 0;
				for(MenuDomain sysMenuDomain : importMenuDomainList) {
					System.out.println(sysMenuDomain.toString());
					menuServiceImpl.saveMenuDomain(sysMenuDomain);
					count++;
				}
			 }
			 uploadReslut = "总共插入了"+count+"条数据"+"\n";
			 System.out.println(uploadReslut);
			 
		}
		//System.out.println("插入结果，upload Result"+uploadResult);
//		if(!correctFlag || !notCicle) return errorTips;
		 map.put("result", count);
		 map.put("DomainList", importMenuDomainList);
		 map.put("warn", emptyWarn);
		 map.put("errorTips",errorTips);
		 return map;
	}

		//递归查找父节点
	private List<Long> chekCicle(List<Long> okMenuId,Long menuid, Long parentid , List<Map<String, Long>> menuList) {
		//定义List<Long> 用来返回
		List<Long> cirleList = new ArrayList<>();
		// TODO Auto-generated method stub
		//A->B,B->C,C-A; 表示A的父节点时B，类推
		//okMenuId 会依此存如A，然后调用 checkCicle(okMenuId, A的parentId)
		//然后 判断 A的parentId 是否已经在 okMenuId ，中，如果在，则表示时一个循环，此时返回fale;
		System.out.println("okMenId "+okMenuId);
		System.out.println("parentId"+parentid);
		System.out.println("menuLIst"+menuList.toString());

		for(Map<String ,Long > object : menuList) {
			//传入的父类Id，能在 传入menuIdLIst里找到
			System.out.print("   "+object.get("menuId")+"  "+object.get("menuId").getClass()+"   ");
			if(parentid.equals(object.get("menuId"))) {
				//这个父Id没有出现在okMenuId中，表示当前追溯没有出现循环
				System.out.println("父节点在文件数据中");
				if(!okMenuId.contains(parentid)) {
					okMenuId.add(parentid);
					System.out.println("okMenuId没有"+okMenuId);
//					return chekCicle(okMenuId, parentid, object.get("parentId"), menuList);
					if(chekCicle(okMenuId, parentid, object.get("parentId"), menuList) != null) {
						System.out.println("********循环树********"+menuList);
						cirleList.addAll(okMenuId);
						System.out.println("循环树后面 cirleList"+cirleList);
						return cirleList;
					}
				}else{
					return cirleList;
				}
			}
		}
		
		//传进来的parentId 不是文件中的一个menuId，树没有比闭合
		System.out.println("父节点在数据库");
		List<Long> circleList2 = new ArrayList<>();
		circleList2 = null;
		if(circleList2 == null) System.out.println("是空的得到  ");
		return circleList2;
	}
	
}

