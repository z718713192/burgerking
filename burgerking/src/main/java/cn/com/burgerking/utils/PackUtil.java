package cn.com.burgerking.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackUtil {

	private static final Logger logger = LoggerFactory.getLogger(PackUtil.class);
	
	/**
	 * 打包文件
	 * @param filePath 需要打包的文件的路径数组
	 * @param packagePathName 打包后存放的位置及包名 zip
	 */
	public static boolean pack(String[] filePath, String packagePathName){
		 byte[] buffer = new byte[1024];   
		  
		try {
			 ZipOutputStream out = new ZipOutputStream(new FileOutputStream(packagePathName));
			 
			 File[] file1 = new File[filePath.length];
			 
			   //需要同时下载的两个文件result.txt ，source.txt   
			  for(int i=0;i<filePath.length;i++){
				  file1[i] = new File(filePath[i]);
			  }
		  
		       for(int i=0;i<file1.length;i++) {   
		  
		           FileInputStream fis = new FileInputStream(file1[i]);   
		  
		           out.putNextEntry(new ZipEntry(file1[i].getName()));   
		  
		           int len=0;   
		  
		           //读入需要下载的文件的内容，打包到zip文件   
		  
		          while((len = fis.read(buffer))>0) {   
		  
		           out.write(buffer,0,len);    
		  
		          }   
		  
		           out.closeEntry();   
		  
		           fis.close();   
		  
		       }   
		  
		        out.close();   
		  
		        System.out.println("生成Demo.zip成功");  
		        
		        return true;
		} catch (FileNotFoundException e) {
			logger.error(e.toString(), e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString(), e);
			e.printStackTrace();
		}   
	  return false;
	}
}
