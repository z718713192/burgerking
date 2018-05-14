package cn.com.burgerking.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import microsoft.exchange.webservices.data.BodyType;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.EmailMessageSchema;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.LogicalOperator;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;

/**
 * 
 * @author Administrator
 *
 */
public class ExchangeMail {

	private static String username = "liuzhu.li";
	private static String password = "123456p";
	private static String demand = "mail.sh-sapling.com";// 邮件服务器
	private static ExchangeService service;
	
	/**
	 * 初始化连接
	 * */
	private static void init(){
		service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
		ExchangeCredentials credentials = new WebCredentials(username,password);
		service.setCredentials(credentials);
		try {
			service.setUrl(new URI("https://" + demand + "/ews/exchange.asmx"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 使用Exchange协议发送
	 * @param subject 邮件主题
	 * @param to  收件人
	 * @param cc  抄送
	 * @param bobytext  正文
	 * @param realPath  附件
	 * 
	 * @throws Exception
	 */
	public static void doSend(String subject, List<String> to, List<String> cc,
			String bodyText, String realPath) throws Exception {
		if(service==null){
			init();
		}
		EmailMessage msg = new EmailMessage(service);
		msg.setSubject(subject);
		MessageBody body = MessageBody.getMessageBodyFromText(bodyText);
		body.setBodyType(BodyType.HTML);
		msg.setBody(body);
		for (String s : to) {
			msg.getToRecipients().add(s);
		}
		// for (String s : cc) {
		// msg.getCcRecipients().add(s);
		// }
		if (realPath != null && !"".equals(realPath)) {
			msg.getAttachments().addFileAttachment(realPath);
		}
		msg.send();
	}
	
	public static void send(String subject, List<String> to, List<String> cc,
			String bodyText) throws Exception {
		if(service==null){
			init();
		}
		doSend(subject, to, cc, bodyText, null);
	}
	
	/**
	 * 使用Exchange协议接收邮件
	 * 
	 * @throws Exception
	 */
	
	public static void GetUnreadEmails() throws Exception {
		ItemView view = new ItemView(Integer.MAX_VALUE);
		List<String> list = new ArrayList<String>();
		FindItemsResults<Item> unRead = Folder.bind(service,WellKnownFolderName.Inbox).findItems(SetFilter(), view);
		for (Item item : unRead.getItems()) {
			if (item.getSubject() != null) {
				list.add(item.getSubject().toString());
			} else {
				list.add("无标题");
			}
			// list.Add(item.DateTimeSent.ToString());
		}
		// int unRead=Folder.bind(service, WellKnownFolderName.Inbox).getUnreadCount();  //未读邮件数量
		// System.out.println(unRead);
	}

	/**
	 * 设置获取什么类型的邮件
	 * 
	 * @throws Exception
	 */
	
	private static SearchFilter SetFilter() {
		List<SearchFilter> searchFilterCollection = new ArrayList<SearchFilter>();
		searchFilterCollection.add(new SearchFilter.IsEqualTo(
				EmailMessageSchema.IsRead, false));
		SearchFilter s = new SearchFilter.SearchFilterCollection(
				LogicalOperator.And,searchFilterCollection);
		// 如果要获取所有邮件的话代码改成这样：
//		 searchFilterCollection.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
//		 searchFilterCollection.add(new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, true));
//		 SearchFilter s = new SearchFilter.SearchFilterCollection(LogicalOperator.Or,searchFilterCollection);
		return s;
	}
	

	public static void main(String[] args) throws Exception {
		init();
		List<String> to = new ArrayList<String>();
		to.add("718713192@qq.com");
		doSend("test",to,null,"hello","C:\\Users\\Administrator\\Desktop\\messge.txt");
//		GetUnreadEmails();
	}
}
