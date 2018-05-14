package cn.com.burgerking.utils;

import java.util.Map;

public class MsgInfoEntity {
	
    private MsgInfoEntity(){
        
    }
    private static class SingletonHolder{
        private final static MsgInfoEntity instance=new MsgInfoEntity();
    }
    public static MsgInfoEntity getInstanceNew(){
    	return new MsgInfoEntity();
    }
    public static MsgInfoEntity getInstance(){
        return SingletonHolder.instance;
    }
    public static MsgInfoEntity getInstance(String msgInf,String status){
    	SingletonHolder.instance.msgInf = msgInf;
    	SingletonHolder.instance.status = status;
    	return SingletonHolder.instance;
    }
    public static MsgInfoEntity getInstance(String msgInf,String status,Map<String, Object> data){
    	SingletonHolder.instance.msgInf = msgInf;
    	SingletonHolder.instance.status = status;
    	SingletonHolder.instance.data = data;
    	return SingletonHolder.instance;
    }
	 
    private Map<String, Object> data;
    
    private Object data2;
    
	private String msgInf;
	
	private String status;

	public String getMsgInf() {
		return msgInf;
	}

	public void setMsgInf(String msgInf) {
		this.msgInf = msgInf;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public Object getData2() {
		return data2;
	}
	public void setData2(Object data2) {
		this.data2 = data2;
	}

}
