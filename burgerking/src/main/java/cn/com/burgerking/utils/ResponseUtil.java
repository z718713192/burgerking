package cn.com.burgerking.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 响应客户请求
 * @author yaomingyang
 *
 */
public class ResponseUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ResponseUtil.class);
	
	public static void responseReq(HttpServletResponse response, String resStr){
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.write(resStr);
		} catch (IOException e) {
			logger.error("响应客户端出现异常！");
			logger.error(e.toString(), e);
		} finally{
			if(null != out) out.close();
		}
		
	}
	/**
	 * 跳转页面
	 * @param response
	 * @param url
	 */
	public static void redirectUrl(HttpServletResponse response, String url){
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			logger.error("响应客户端时出现异常！");
			logger.error(e.toString(), e);
		}
	}
	
	public static String getApMac(HttpServletRequest request){
		String apMac = "";
		try {
			Map<String, String[]> map = request.getParameterMap();
			JSONObject json = new JSONObject();
			for(Iterator<String> it=map.keySet().iterator();it.hasNext();){
				String key=it.next();
				String[] str=map.get(key);
				String value="";
				for(int i=0;i<str.length;i++){
					value=value+str[i];
				}
				value=ConvertString.convert(value);
				json.put(key, value);
			}
			String nasid = json.getString("nasid");
			if(null == nasid || "".equals(nasid)){
				return apMac;
			}else{
				apMac = nasid.replace("-", "");
				logger.info("APMAC=" + apMac);
				return apMac;
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return apMac;
	}
	
}