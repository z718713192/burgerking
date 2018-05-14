package cn.com.burgerking.client.utils;

public class WordToPdfThread implements Runnable {

	private String inputFile;
	private String savePdfPath;
	private String fileName;
	
	public WordToPdfThread(String inputFile, String savePdfPath,String fileName){
		this.inputFile = inputFile;
		this.savePdfPath = savePdfPath;
		this.fileName = fileName;
	}
	
	@Override
	public void run() {
		WordUtils.word2pdf(inputFile, savePdfPath,fileName);
	}

}
