package cn.com.burgerking.utils;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertString {

	private static final Logger logger = LoggerFactory.getLogger(ConvertString.class);
	
	/**
	 * 字符串编码解析方法
	 * @param convertString
	 * @return String
	 */
	public static String convert(String convertString){
		String str=null;
		try {
			str = new String(convertString.getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString(), e);
			e.printStackTrace();
		}
		return str;
	}
}
