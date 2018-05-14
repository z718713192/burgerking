package cn.com.burgerking.client.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;

/**
 * Excel转pdf
 * 
 * @author hub
 *
 */
public class ExcelToPdf {

	private static Logger log = LoggerFactory.getLogger(ExcelToPdf.class);
	private static final int XLTYPE_PDF = 0;

	/**
	 * Excel转pdf pdf文件名=sheet名g
	 * 
	 * @param inputFile
	 * @param savePdfPath
	 *            pdf存放目录
	 */
	public static void excelToPdfAll(String inputFile, String savePdfPath) {
		ComThread.InitSTA();
		ActiveXComponent activeXComponent = null;
		Dispatch excel = null;
		try {
			File file = new File(savePdfPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			long start = System.currentTimeMillis();
			log.info("--------------------------------------> Excel转PDF开始：" + inputFile);
			activeXComponent = new ActiveXComponent("Excel.Application");
			activeXComponent.setProperty("Visible", false); // word不可见

			Dispatch excels = activeXComponent.getProperty("Workbooks").toDispatch();
			excel = Dispatch.call(excels, "Open", inputFile, false, true).toDispatch();

			Dispatch sheets = Dispatch.get(excel, "Sheets").toDispatch();
			int count = Dispatch.get(sheets, "Count").getInt(); // sheet总数

			String pdfFileName = "";
			for (int i = 4; i <= count; i++) {
				String sheetname = null;
				try {
					Dispatch sheet = Dispatch.invoke(sheets, "Item", Dispatch.Get, new Object[] { new Integer(i) }, new int[1]).toDispatch();
					Dispatch.call(sheet, "Activate"); // 设为活动sheet
					Dispatch.call(sheet, "Select");
					sheetname = Dispatch.get(sheet, "name").toString();
					pdfFileName = sheetname + ".pdf";


					Dispatch.call(sheet, "ExportAsFixedFormat", XLTYPE_PDF, savePdfPath + pdfFileName);
				} catch (Exception e) {
					log.error(e.getMessage());
					log.info("sheetname:"+sheetname+" is error! ");
				}
			}
			// ComThread.Release();
			long end = System.currentTimeMillis();
			log.info("--------------------------------------> Excel转PDF结束：" + inputFile + ",  耗时："+ (end - start) / 1000 + " s");
		} finally {
			if(excel!=null){
				Dispatch.call(excel, "Close", false);
			}
			if(activeXComponent!= null){
				activeXComponent.invoke("Quit");
			}
			ComThread.Release();
		}

	}

	public static void main(String[] args) {
		excelToPdfAll("C:\\Users\\liuzhu.li\\Desktop\\China Daily Sales Report -East-10月.xlsx", "C:\\Users\\liuzhu.li\\Desktop\\dqw\\dqw");
	}
}
