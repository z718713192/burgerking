package cn.com.burgerking.client.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class SignPdf {
	
	private static Logger log = LoggerFactory.getLogger(SignPdf.class);
	
	private static float fy = 0;
	private static float fx = 0;
	
	 // 定义关键字
    private static String KEY_WORD = "";
    // 定义返回值
    private static float[] resu = null;
   // 定义返回页码
    private static int i = 0;
	
	/**
	 * 盖章
	 * @param sourcePdfPath		待盖章PDF路径
	 * @param saveAsPdfPath		盖章后的PDF保存路径
	 * @param esealPath			印章路径
	 * @throws Exception 
	 */
	public static boolean signPdf(String sourcePdfPath, String saveAsPdfPath, String esealPath) {
		boolean signSuc = true;
		// 创建一个pdf读入流
		PdfReader reader = null;
		// 根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
		PdfStamper stamper = null;

		try {
			// 删除原有文件
			deletePdf(saveAsPdfPath);
			
			reader = new PdfReader(sourcePdfPath);
			stamper = new PdfStamper(reader, new FileOutputStream(saveAsPdfPath));
			
			// 获取pdf页
			int pages = reader.getNumberOfPages();

			// 根据关键字查找坐标
			final String key = "盖";
			PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(reader);
			pdfReaderContentParser.processContent(pages, new RenderListener() {
				@Override
				public void renderText(TextRenderInfo textRenderInfo) {
					String text = textRenderInfo.getText();
					if (null != text && text.contains(key)) {
						com.itextpdf.awt.geom.Rectangle2D.Float boundingRectange = textRenderInfo.getBaseline()
								.getBoundingRectange();
						fy = boundingRectange.y;
						fx = boundingRectange.x;
					}
				}

				@Override
				public void renderImage(ImageRenderInfo arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void endTextBlock() {
					// TODO Auto-generated method stub

				}

				@Override
				public void beginTextBlock() {
					// TODO Auto-generated method stub

				}
			});

			// 编辑最后一页
			PdfContentByte over = stamper.getOverContent(pages);

			// 用pdfreader获得当前页字典对象.包含了该页的一些数据.比如该页的坐标轴信息
			PdfDictionary p = reader.getPageN(pages);

			// 拿到mediaBox 里面放着该页pdf的大小信息
			PdfObject po = p.get(new PdfName("MediaBox"));

			// po是一个数组对象.里面包含了该页pdf的坐标轴范围
			PdfArray pa = (PdfArray) po;

			// float fy = pa.getAsNumber(pa.size()-1).floatValue()-800;
			// float fx = pa.getAsNumber(pa.size()-2).floatValue()-200;

			Image image = Image.getInstance(esealPath);
//			image.setAbsolutePosition(fx - 60, fy - 30);
			//此处精度要求不高
			if(image.getWidth() > 80)
				image.scaleAbsolute(80, 75);
			image.setAbsolutePosition(fx-35,fy-30);
			over.addImage(image);
		} catch (Exception e) {
			log.error("------------> 盖章异常："+e.getMessage());
			signSuc = false;
			e.printStackTrace();
		} finally {
			try {
				if (stamper != null)
					stamper.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (reader != null)
				reader.close();
		}
		return signSuc;
	}
	
	/**
	 * 盖章
	 * @param sourcePdfPath		待盖章PDF路径
	 * @param saveAsPdfPath		盖章后的PDF保存路径
	 * @param esealPath			印章路径
	 * @throws Exception 
	 */
	public static boolean signWPdf(String sourcePdfPath, String saveAsPdfPath, String esealPath,String keyWord) {
		boolean signSuc = true;
		// 创建一个pdf读入流
		PdfReader reader = null;
		// 根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
		PdfStamper stamper = null;
		KEY_WORD=keyWord;
		try {
			// 删除原有文件
			deletePdf(saveAsPdfPath);
			
			reader = new PdfReader(sourcePdfPath);
			stamper = new PdfStamper(reader, new FileOutputStream(saveAsPdfPath));
			
			// 获取pdf页
			int pages = reader.getNumberOfPages();

			// 根据关键字查找坐标
			final String key =keyWord;
			PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(reader);
			pdfReaderContentParser.processContent(pages, new RenderListener() {
				@Override
				public void renderText(TextRenderInfo textRenderInfo) {
						com.itextpdf.awt.geom.Rectangle2D.Float boundingRectange = textRenderInfo.getBaseline()
								.getBoundingRectange();
						fy = boundingRectange.y;
						fx = boundingRectange.x;
				}

				@Override
				public void renderImage(ImageRenderInfo arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void endTextBlock() {
					// TODO Auto-generated method stub

				}

				@Override
				public void beginTextBlock() {
					// TODO Auto-generated method stub

				}
			});

			// 编辑最后一页
			PdfContentByte over = stamper.getOverContent(pages);

			// 用pdfreader获得当前页字典对象.包含了该页的一些数据.比如该页的坐标轴信息
			PdfDictionary p = reader.getPageN(pages);

			// 拿到mediaBox 里面放着该页pdf的大小信息
			PdfObject po = p.get(new PdfName("MediaBox"));

			// po是一个数组对象.里面包含了该页pdf的坐标轴范围
			PdfArray pa = (PdfArray) po;

			// float fy = pa.getAsNumber(pa.size()-1).floatValue()-800;
			// float fx = pa.getAsNumber(pa.size()-2).floatValue()-200;

			Image image = Image.getInstance(esealPath);
//			image.setAbsolutePosition(fx - 60, fy - 30);
			//此处精度要求不高
//			getKeyWords(sourcePdfPath);		
			image.setAbsolutePosition(fx-90,fy-50);
			over.addImage(image);
		} catch (Exception e) {
			log.error("------------> 盖章异常："+e.getMessage());
			signSuc = false;
			e.printStackTrace();
		} finally {
			try {
				if (stamper != null)
					stamper.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (reader != null)
				reader.close();
		}
		return signSuc;
	}
	
	 private static void getKeyWords(String filePath)
	    {
	        try
	        {
	             PdfReader pdfReader = new PdfReader(filePath);
	             int pageNum = pdfReader.getNumberOfPages();
	             PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
	                     pdfReader);
	 
	             // 下标从1开始
	             for (i = 1; i < pageNum; i++)
	             {
	                 pdfReaderContentParser.processContent(i, new RenderListener()
	                 {
	 
	                     @Override
	                     public void renderText(TextRenderInfo textRenderInfo)
	                     {
	                         String text = textRenderInfo.getText();
	                         if (null != text && text.contains(KEY_WORD))
	                         {
	                             Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
	                             fx = boundingRectange.x;
	                             fy= boundingRectange.y;
	                         }
	                     }
	 
	                     @Override
	                     public void renderImage(ImageRenderInfo arg0)
	                     {
	                         // TODO Auto-generated method stub
	 
	                     }
	 
	                     @Override
	                     public void endTextBlock()
	                     {
	                         // TODO Auto-generated method stub
	 
	                     }
	 
	                     @Override
	                     public void beginTextBlock()
	                     {
	                         // TODO Auto-generated method stub
	 
	                     }
	                 });
	             }
	         } catch (IOException e)
	         {
	             // TODO Auto-generated catch block
	             e.printStackTrace();
	         }
	     }
	private static void deletePdf(String pdfPath) {  
        File pdfFile = new File(pdfPath);  
        if (pdfFile.exists()) {  
            pdfFile.delete();  
        }  
    }
 
	
	public static void main(String[] args) {
		SignPdf pdf=new SignPdf();
		pdf.signPdf("C:\\Users\\liuzhu.li\\Desktop\\dqw\\dqw15694.pdf", "C:\\Users\\liuzhu.li\\Desktop\\qwdq\\dqw15694.pdf", "C:\\burgerking\\stamp\\HBWBJCYGLYXGSGZ0001.png");
	}
}
