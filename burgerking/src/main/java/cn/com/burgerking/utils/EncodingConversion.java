package cn.com.burgerking.utils;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncodingConversion {
	
	private static final Logger logger = LoggerFactory.getLogger(EncodingConversion.class);
	
	public static String convertEncoding(String convertString){
		String str = convertString;
		if(convertString!=null){
			try {
				//判断当前字符串的编码格式
				if(convertString.equals(new String(convertString.getBytes("ISO8859-1"), "ISO8859-1")))
				{
					str = new String(convertString.getBytes("ISO-8859-1"),"utf-8");
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(e.toString(), e);
				e.printStackTrace();
			}
		}
		return str;
	}
}
