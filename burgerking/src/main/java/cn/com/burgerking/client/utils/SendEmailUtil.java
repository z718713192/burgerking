package cn.com.burgerking.client.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmailUtil {
	
	private static Logger log = LoggerFactory.getLogger(SendEmailUtil.class);
	
	private static Properties props = System.getProperties();
	
	private static MailAuthenticator authenticator;
	
	private static Session session;
	
	public static void init(String username, String password, String smtpHostName) {
		if(smtpHostName.isEmpty()){
			smtpHostName = "smtp." + username.split("@")[1];
		}
        // 初始化props
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHostName);
        
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        
        // 验证
        authenticator = new MailAuthenticator(username, password);
        // 创建session
        session = Session.getInstance(props, authenticator);
    }
	
	public static boolean sendEmail(List<String> recipients, String subject, Multipart multipart){
		boolean b = true;
		try {
			int num = recipients.size();
			InternetAddress[] addresses = new InternetAddress[num];
			for (int i = 0; i < num; i++) {
			    addresses[i] = new InternetAddress(recipients.get(i));
			}
			// 创建mime类型邮件
			MimeMessage message = new MimeMessage(session);
			// 设置发信人
			message.setFrom(new InternetAddress(authenticator.getUsername()));
			// 设置收件人
			message.setRecipients(RecipientType.TO, addresses);
			// 设置主题
			message.setSubject(subject);
			// 设置邮件内容
			message.setContent(multipart);
			// 发送
			Transport.send(message);
		} catch (Exception e) {
			log.error("------------> 邮件发送异常："+e.getMessage());
			e.printStackTrace();
			b =false;
		}
		return b;
	}
	
	public static void sendEmail2() {

		MailAuthenticator authenticator = new MailAuthenticator("bkeastrent@bkchina.cn", "Whopper2012");
		Properties props = System.getProperties();
//		props.put("mail.smtp.host", "smtp.qq.com");
		props.put("mail.smtp.host", "smtp.bkchina.cn");
		props.put("mail.smtp.auth", "true");
		
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465"); 

		Session session = Session.getInstance(props, authenticator);

		List<String> recipients = new ArrayList();
		recipients.add("13917036442@qq.com");
		recipients.add("ctianhua@live.com");
		
		try {
			
			int num = recipients.size();
			InternetAddress[] addresses = new InternetAddress[num];
			for (int i = 0; i < num; i++) {
	            addresses[i] = new InternetAddress(recipients.get(i));
	        }
			
			// 创建mime类型邮件
			MimeMessage message = new MimeMessage(session);
			// 设置发信人
			message.setFrom(new InternetAddress("bkeastrent@bkchina.cn"));
			// 设置收件人
//			message.setRecipient(RecipientType.TO, new InternetAddress(
//					"13917036442@qq.com"));
			
			message.setRecipients(RecipientType.TO, addresses);
			// 设置主题
			message.setSubject("Test 汉堡王月报表");
			// 设置邮件内容			
			Multipart multipart = new MimeMultipart();
			//正文
			BodyPart contentPart = new MimeBodyPart(); 
            contentPart.setContent("测试", "text/html;charset=UTF-8"); 
            multipart.addBodyPart(contentPart);
            //附件
            File file = new File("D:\\Users\\hub\\Desktop\\汉堡王\\PDF\\22841.pdf");
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
            multipart.addBodyPart(attachmentBodyPart);
            
            message.setContent(multipart);
			// 发送
			Transport.send(message);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
