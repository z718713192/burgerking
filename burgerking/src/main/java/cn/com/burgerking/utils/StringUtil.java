package cn.com.burgerking.utils;

import java.util.Random;

public class StringUtil {
	/**
	 * 获取六位随机数
	 * @return
	 */
	public static String getSixRandomCode(){
		Random ran = new Random();
		String str = ran.nextInt(1000000) + "";
		int len = str.length();
		for(int i = len; i < 6; i++){
			str = "0" + str;
		}
		return str;
	}
}
