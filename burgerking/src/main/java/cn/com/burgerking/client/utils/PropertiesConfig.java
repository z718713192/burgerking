package cn.com.burgerking.client.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesConfig {
	
	private static String filePath = "config.properties";
	private static Properties prop = null;
	
	public static Properties getProperties(){
		if(prop==null){
			prop = new Properties();
			try {
				prop.load(new InputStreamReader(PropertiesConfig.class.getClassLoader().getResourceAsStream(filePath),"UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}
	
	public static void main(String [] arge){
		String str = PropertiesConfig.getProperties().getProperty("saveExcelPathTemp");
		System.out.println(str);
	}

}
