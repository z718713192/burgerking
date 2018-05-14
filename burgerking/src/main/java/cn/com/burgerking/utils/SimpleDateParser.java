package cn.com.burgerking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDateParser {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleDateParser.class);
	
	public static Date parse(String dateStr) throws ParseException {
		Date				ret = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			ret = sdf.parse(dateStr);
		}
		catch (ParseException e) {
			logger.error(e.toString(), e);
			throw e;
		}
		
		return ret;
	}
	
	/**
	 * yyyy-MM-dd
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parser1(String dateStr) throws ParseException {
		Date				ret = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			ret = sdf.parse(dateStr);
		}
		catch (ParseException e) {
			logger.error(e.toString(), e);
			throw e;
		}
		
		return ret;
	}
	
	public static Date parse3(String dateStr) throws ParseException {
		Date				ret = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyyMMdd");
		
		try {
			ret = sdf.parse(dateStr);
		}
		catch (ParseException e) {
			logger.error(e.toString(), e);
			throw e;
		}
		
		return ret;
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param dt
	 * @return
	 */
	public static String format(Date dt) {
		String				dateStr = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (dt != null) {
			dateStr = sdf.format(dt);
		}
		
		return dateStr;
	}
	
	public static String formatYear(Date dt)
	{
		String				dateStr = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyy");
		
		if (dt != null) 
		{
			dateStr = sdf.format(dt);
		}
		
		return dateStr;
	}
	
	public static String formatYearMonth(Date dt)
	{
		String				dateStr = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyy-MM");
		
		if (dt != null) 
		{
			dateStr = sdf.format(dt);
		}
		
		return dateStr;
	}
	
	public static String formatYearMonthDay(Date dt)
	{
		String				dateStr = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if (dt != null) 
		{
			dateStr = sdf.format(dt);
		}
		
		return dateStr;
	}

	public static String format1(Date dt) {
		String				dateStr = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if (dt != null) {
			dateStr = sdf.format(dt);
		}

		return dateStr;
		
	}

	public static String format2(Date dt) {
		String				dateStr = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("HH:mm:ss");
		
		if (dt != null) {
			dateStr = sdf.format(dt);
		}

		return dateStr;
		
	}

	public static String format3(Date dt) {
		String				dateStr = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("yyyyMMdd");
		
		if (dt != null) {
			dateStr = sdf.format(dt);
		}

		return dateStr;
		
	}
	
	public static String format4(Date dt) {
		String				dateStr = null;
		SimpleDateFormat	sdf = new SimpleDateFormat("HHmmss");
		
		if (dt != null) {
			dateStr = sdf.format(dt);
		}

		return dateStr;
		
	}
	
	public static void main (String args[]) {
		
		try {
			System.out.println(parser1("2012-1-01"));
		} catch (ParseException e) {
			logger.error(e.toString(), e);
			e.printStackTrace();
		}
	}
}
