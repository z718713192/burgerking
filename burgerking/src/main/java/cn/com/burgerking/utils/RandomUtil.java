package cn.com.burgerking.utils;

import java.util.Random;


/**
 * 随机数工具类
 * 
 * @author luheng
 * @version v01.00.00 $Revision$
 * @date 2015年4月7日
 * @time 下午7:58:49
 */
public class RandomUtil {
	/**
	 * 获取指定位数随机数
	 * 
	 * @author luheng
	 * @param num
	 * @return
	 */
	public static String getRandomNumStr(int num) {
		Random random = new Random();
		String result = "";
		for (int i = 0; i < num; i++) {
			result += random.nextInt(10);
		}
		return result;
	}

	/**
	 * 返回指定长度的字母加数字随机数
	 * 
	 * @param int
	 * @return String
	 */
	public static String randomChars(int length) {
		String val = "";

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

			if ("char".equalsIgnoreCase(charOrNum)) // 字符串
			{
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) // 数字
			{
				val += String.valueOf(random.nextInt(10));
			}
		}

		return val;
	}
}
