package cn.com.burgerking.client.utils;

public class ExcelToPdfThread implements Runnable {

	private String inputFile;
	private String savePdfPath;
	
	public ExcelToPdfThread(String inputFile, String savePdfPath){
		this.inputFile = inputFile;
		this.savePdfPath = savePdfPath;
	}
	
	@Override
	public void run() {
		ExcelToPdf.excelToPdfAll(inputFile, savePdfPath);
	}

}
