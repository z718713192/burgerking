package cn.com.burgerking.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解析Excel
 * @author hub
 *
 */
public class AnalysisExcel {
	
	private static Logger log = LoggerFactory.getLogger(AnalysisExcel.class);
	
	/**
	 * 读取门店信息（sheet1）
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	public static Map readExcelByStoreInfo(File file) throws Exception{
		Map map = new HashMap();
		String keyValue = null;
		String value = null;
		
		if(file.exists()){
			InputStream in = null;
			Workbook workBook = null;
			
			try {
				in = new FileInputStream(file);
				workBook = new XSSFWorkbook(in);
				int sheetNumber = workBook.getNumberOfSheets();
				
				Sheet sheet = workBook.getSheetAt(0);		//读取"Store ID" sheet
				int rNumber = sheet.getLastRowNum();
				Iterator<Row> rws = sheet.iterator();
				int r = 0;
				while (rws.hasNext()) {
					r++;
					Row rw = rws.next();
					if(r < 4 && r != 1)
						continue;
					
					Iterator<Cell> cs = rw.cellIterator();
					List list = new ArrayList();
					int c = 0;
					while (cs.hasNext()) {
						c++;
						Cell cell = cs.next();
						if(c>2)
							break;
						
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC: // 数字
							// 如果为时间格式的内容
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
								value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
								break;
							} else {
								value = new DecimalFormat("0").format(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_STRING: // 字符串
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
							value = cell.getBooleanCellValue() + "";
							break;
						case HSSFCell.CELL_TYPE_FORMULA: // 公式
							try {
								value = cell.getNumericCellValue() + "";
							} catch (Exception e) {
								value = cell.getRichStringCellValue() + "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK: // 空值
							value = "";
							break;
						case HSSFCell.CELL_TYPE_ERROR: // 故障
//							value = "非法字符";
//							System.out.print(value);
//							break;
							throw new Exception("非法字符!");
						default:
//							value = "未知类型";
//							System.out.print(value);
//							break;
							throw new Exception("未知类型!");
						}
						list.add(value.trim());
						if (c == 1){
							if(r==1)
								keyValue = "reportDate";
							else
								keyValue = value;
						}
					}
					if(keyValue != "")
						map.put(keyValue, list);
					list = null;
				}
			} catch (IOException e){
				log.error("------------> 读取文件失败："+e.getMessage());
				e.printStackTrace();
				throw new Exception("读取文件失败！");
			}catch (IllegalArgumentException e1) {
				log.error("------------> 解析文件失败："+e1.getMessage());
				e1.printStackTrace();
				throw new Exception("解析文件失败！");
			} catch (Exception e2){
				log.error("------------> 解析文件异常："+e2.getMessage());
				e2.printStackTrace();
				throw new Exception("解析文件异常！");
			}finally {
				try {
					if(in != null)
						in.close();
					if(workBook != null)
						workBook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return map;
	}	
	
	/**
	 * 读取门店信息（sheet1\sheet2\sheet3）
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	public static Map readExcelByStoreInfo_bak(File file) throws Exception{
		Map map = new HashMap();
		String keyValue = null;
		String value = null;
		
		if(file.exists()){
			InputStream in = null;
			Workbook workBook = null;
			
			try {
				in = new FileInputStream(file);
				workBook = new XSSFWorkbook(in);
				int sheetNumber = workBook.getNumberOfSheets();
				
//				String [] sheetName = {"Store ID","计租营业额"};
				
				for(int i=0; i<3; i++){
					if(i==1)
						continue;
					Sheet sheet = workBook.getSheetAt(i);
					int rNumber = sheet.getLastRowNum();

					int r = 0;
					int startR = 0;			//开始获取行
					int endC = 0;			//结束获取列
					if(i==0){
						startR = 0;
						endC = 1;
					}else if(i==2){
						startR = 2;
						endC = 5;
					}
					
					Iterator<Row> rws = sheet.iterator();
					while (rws.hasNext()) {
						r++;
						Row rw = rws.next();
						if (r < startR)
							continue;

						List list = new ArrayList();
						Iterator<Cell> cs = rw.cellIterator();
						int c = 0;
						while (cs.hasNext()) {
							c++;
							Cell cell = cs.next();
							
							if (c > endC)
								break;
							if(i==2 && c!=2 && c!=3 && c!=5){
								continue;
							}

							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC: // 数字
								// 如果为时间格式的内容
								if (HSSFDateUtil.isCellDateFormatted(cell)) {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
									value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
									break;
								} else {
									value = new DecimalFormat("0").format(cell.getNumericCellValue());
								}
								break;
							case HSSFCell.CELL_TYPE_STRING: // 字符串
								value = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
								value = cell.getBooleanCellValue() + "";
								break;
							case HSSFCell.CELL_TYPE_FORMULA: // 公式
//								value = cell.getCellFormula() + "";
								try {
									value = cell.getNumericCellValue() + "";
								} catch (Exception e) {
									value = cell.getRichStringCellValue() + "";
								}
								break;
							case HSSFCell.CELL_TYPE_BLANK: // 空值
								value = "";
								break;
							case HSSFCell.CELL_TYPE_ERROR: // 故障
								value = "非法字符";
								System.out.print(value);
								break;
							default:
								value = "未知类型";
								System.out.print(value);
								break;
							}
							list.add(value);							
							if ((i==0 && c == 1) || (i==2 && c ==3)){
								if(i==0 && r==1){
									keyValue = "reportDate";
									map.put(keyValue, list);
								}
								keyValue = value;
							}
						}
						if(i==0){
							map.put(keyValue, list);
						}else{
							Object obj = map.get(keyValue);
							if(obj != null){
								List list1 = (List) obj;
								list1.addAll(list);
								map.put(keyValue,list1);
							}
						}
						
						//目的是"Store ID" sheet 只取第一行第一列的日期，取完再从第4行开始, 第2列结束
						if(i == 0 && startR == 0){
							startR = 4;
							endC = 2;
						}
						
					}
				}
			} catch (IOException e){
				log.error("------------> 读取文件失败："+e.getMessage());
				e.printStackTrace();
				throw new Exception("读取文件失败！");
			}catch (IllegalArgumentException e1) {
				log.error("------------> 解析文件失败："+e1.getMessage());
				e1.printStackTrace();
				throw new Exception("解析文件失败！");
			} catch (Exception e2){
				log.error("------------> 解析文件失败："+e2.getMessage());
				e2.printStackTrace();
				throw new Exception("解析文件失败！");
			}finally {
				try {
					if(in != null)
						in.close();
					if(workBook != null)
						workBook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return map;
	}
	

	/**
	 * 指定sheet读取
	 * @param file	excel绝对路径
	 * @param startSheet	指定读取sheet下标
	 * @param startR		开始读取行	0-所有行
	 * @param endC			结束读取列	0-所有列
	 * @return
	 */
	public static Map readExcelBySheet(File file, int startSheet, int startR, int endC){
		Map map = new HashMap();
		String keyValue = null;
		String value = null;
		
		log.info("---->读取Excel开始："+file);
		if(file.exists()){
			InputStream in = null;
			Workbook workBook = null;
			try {
				in = new FileInputStream(file);
				workBook = new XSSFWorkbook(in);
				int sheetNumber = workBook.getNumberOfSheets();
				log.info("---->sheet个数："+sheetNumber);
				if(startSheet > 0)
					startSheet = startSheet-1;
				
				Sheet sheet = workBook.getSheetAt(startSheet);
				Sheet sheet1 = workBook.getSheet("计租营业额");
				int rows = sheet1.getLastRowNum();
				int rNumber = sheet.getLastRowNum();
				log.info("---->第" + (startSheet + 1) + "个sheet行数：" + rNumber);

				Iterator<Row> rws = sheet.iterator();
				int r = 0;
				while (rws.hasNext()) {
					r++;
					Row rw = rws.next();
					if (r < startR)
						continue;

					List list = new ArrayList();
					Iterator<Cell> cs = rw.cellIterator();
					int c = 0;
					while (cs.hasNext()) {
						// if(sheet.isColumnHidden(c)){
						// c++;
						// continue;
						// }
						c++;
						Cell cell = cs.next();
						if (c > endC)
							continue;

						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC: // 数字
							// 如果为时间格式的内容
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
								value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
								break;
							} else {
								value = new DecimalFormat("0").format(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_STRING: // 字符串
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
							value = cell.getBooleanCellValue() + "";
							break;
						case HSSFCell.CELL_TYPE_FORMULA: // 公式
							// value = cell.getCellFormula() + "";
							try {
								value = cell.getNumericCellValue() + "";
							} catch (Exception e) {
								value = cell.getRichStringCellValue() + "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK: // 空值
							value = "";
							break;
						case HSSFCell.CELL_TYPE_ERROR: // 故障
							value = "非法字符";
							System.out.print(value);
							break;
						default:
							value = "未知类型";
							break;
						}
						list.add(value);
						if (c == 1)
							keyValue = value;
					}
					map.put(keyValue, list);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(in != null)
						in.close();
					if(workBook != null)
						workBook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	public static void main(String [] agre){
		
		String url = "D:\\Users\\hub\\Desktop\\汉堡王\\";
		String fileName = "Sales.xlsx";
		File file = new File(url+fileName);
//		readExcelBySheet(file, 0, 0, 4);
		
		try {
			Map map = readExcelByStoreInfo(file);
			System.out.println(map.size());			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Map map = AnalysisExcel.readExcelBySheet(file, 1, 4, 2);
//		List list = (List) map.get("22915");
//		System.out.println(list.get(0));
//		System.out.println(list.get(1));
	}
}
