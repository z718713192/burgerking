package cn.com.burgerking.client.utils;

import java.io.File;
import java.io.IOException;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;

public class WordUtils {
    static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。  
    static final int wdFormatPDF = 17;// word转PDF 格式  

    public static boolean word2pdf(String source, String target,String fileName) { 
    	ComThread.InitSTA();
        System.out.println("Word转PDF开始启动...");  
        long start = System.currentTimeMillis();  
        ActiveXComponent app = null;  
        try {  
        	File file = new File(target);
			if (!file.exists()) {
				file.mkdirs();
			}
            app = new ActiveXComponent("Word.Application");  
            app.setProperty("Visible", false);  
            Dispatch docs = app.getProperty("Documents").toDispatch();  
            System.out.println("打开文档：" + source);  
            Dispatch doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();  
            System.out.println("转换文档到PDF：" + target);  
            
            target+=fileName.substring(0,fileName.indexOf('.'))+".pdf";
            Dispatch.call(doc, "SaveAs", target, wdFormatPDF);  
            Dispatch.call(doc, "Close", false);  
            long end = System.currentTimeMillis();  
            System.out.println("转换完成，用时：" + (end - start) + "ms");  
            return true;  
        } catch (Exception e) {  
            System.out.println("Word转PDF出错：" + e.getMessage());  
            return false;  
        } finally {  
            if (app != null) {  
                app.invoke("Quit", wdDoNotSaveChanges);  
            }  
            ComThread.Release();
        }  
    }  
  
}
