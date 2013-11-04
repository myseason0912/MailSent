package Account;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Store;
import javax.mail.Folder;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import Mail.MailTemplate;
import Mail.MailTitle;
import Mail.MailReceiver;
import Mail.MailReceived;
import Mail.PraseMimeMessage;

public class MailAccount {
	protected int id;
	protected String username;
	protected String password;
	protected int type;
	protected int status;
	protected String uid;
	protected int is_login = 0;
	protected BasicCookieStore cookies;
	protected DefaultHttpClient client = new DefaultHttpClient();
	protected String smtp_host;
	protected String pop3_host;
	protected int is_smtp;
	protected static final int connect_time_out = 10000;
	
	public MailAccount()
	{
		
	}
	
	public MailAccount(String username,String pass,int type,String smtp_host,String pop3_host)
	{
		this.username = username;
		this.password = pass;
		this.type = type;
		this.smtp_host = smtp_host;
		this.pop3_host = pop3_host;
		this.status = AccountUtil.status_normal;
	}
	
	public DefaultHttpClient getClient() {
		return client;
	}
	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public BasicCookieStore getCookies() {
		return cookies;
	}
	
	public void setCookies(BasicCookieStore cookies) {
		this.cookies = cookies;
	}
	
	public int getIsLogin()
	{
		return this.is_login;
	}
	
	public void setIsLogin(int login)
	{
		this.is_login = login;
	}
	
	public String getSmtpHost()
	{
		return this.smtp_host;
	}
	
	public void setSmtpHost(String host)
	{
		this.smtp_host = host;
	}
	
	public String getPop3Host()
	{
		return this.pop3_host;
	}
	
	public void setPop3Host(String pop3_host)
	{
		this.pop3_host = pop3_host;
	}
	
	public int getIsSmtp()
	{
		return this.is_smtp;
	}
	
	public void setIsSmtp(int smtp)
	{
		this.is_smtp = smtp;
	}
	
	public int getStatus()
	{
		return this.status;
	}
	
	public void setStatus(int s)
	{
		this.status = s;
	}
	
	/**
	 * �����½
	 * @return �Ƿ�ɹ���½
	 */
	public boolean login(){return false;}
	
	
	/*
	 * ʹ��cookie��¼��qq��Ҫ��ȡһЩ�ֶ�
	 */
	public boolean loginWithCookie(){return false;}
	
	
	/**
	 * �����ʼ�
	 * @return ����״̬
	 */
	public int sendWebMail(MailReceiver receiver,MailTitle title,MailTemplate template) {return 0;}
	
	/*
	 * �����ʼ���������
	 * @return ����״̬
	 */
	public int sendWebMail(List<MailReceiver> receiver,MailTitle title,MailTemplate template){return 0;}
	
	/**
	 * ʹ��smtp�����ʼ�
	 * @return ����״̬
	 */
	public int sendMail(List<MailReceiver> receivers,MailTitle title,MailTemplate template)
	{
		int result = 0;
		Properties props = new Properties();
		props.put("mail.smtp.host", this.smtp_host);
		props.put("mail.smtp.auth", "true");
		props.put("username", this.username);
		props.put("password", this.password);
		
		MyAuthenticator authenticator = new MyAuthenticator(this.username,this.password);
		
		Session session = Session.getInstance(props, authenticator);
		MailReceiver mr = receivers.get(0);
	    try {
	        MimeMessage mimeMessage = new MimeMessage(session);
	        mimeMessage.setFrom(new InternetAddress(this.username));
	        InternetAddress to = new InternetAddress(mr.getAddr());
	        InternetAddress[] receiver = new InternetAddress[receivers.size() - 1];
	        
	        for (int i=0; i<receivers.size() - 1; i++) {
	        	receiver[i] = new InternetAddress(receivers.get(i+1).getAddr());
	        }
	        
	        mimeMessage.setRecipients(Message.RecipientType.BCC, receiver);  
	        mimeMessage.setRecipient(Message.RecipientType.TO, to);
	        mimeMessage.setSubject(title.getTitle(), "UTF-8");
	        mimeMessage.setSentDate(new Date());
	       
	        Multipart multipart = new MimeMultipart();
	        MimeBodyPart body = new MimeBodyPart();
	        body.setContent(template.getContent(), "text/html; charset=utf-8");
	        multipart.addBodyPart(body);
	        mimeMessage.setContent(multipart);
	        
	        //�����ʼ�
	        Transport transport = session.getTransport("smtp");
	        transport.send(mimeMessage);
	        result =AccountUtil.sent_success;
	        System.out.println("�����ˣ� " + this.username + " �����ˣ� " + mr.getAddr() + " " + title.ToString() + " ���ͳɹ�.");
	    } catch (Exception e) {
	    	result =AccountUtil.sent_error;
	    	String err = e.toString();
	    	if (err.contains("User not found"))
	    	{
	    		err = " �û�������";
	    	}
	        System.out.println("�����ˣ� " +  this.username + " �����ˣ� " + mr.getAddr() + " " + title.ToString() + " ����ʧ��. "  + err);
	    }

		return result;
	}
	
	public int sendMail(MailReceiver receiver,MailTitle title,MailTemplate template)
	{
		int result = 0;
		Properties props = new Properties();
		props.put("mail.smtp.host", this.smtp_host);
		props.put("mail.smtp.auth", "true");
		props.put("username", this.username);
		props.put("password", this.password);
		
		MyAuthenticator authenticator = new MyAuthenticator(this.username,this.password);
		
		Session session = Session.getInstance(props, authenticator);
	    try {
	        MimeMessage mimeMessage = new MimeMessage(session);
	        mimeMessage.setFrom(new InternetAddress(this.username));
	        InternetAddress mail_receiver = new InternetAddress(receiver.getAddr());
	        mimeMessage.setRecipient(Message.RecipientType.TO, mail_receiver);
	        mimeMessage.setSubject(title.getTitle(), "UTF-8");
	        mimeMessage.setSentDate(new Date());
	       
	        Multipart multipart = new MimeMultipart();
	        MimeBodyPart body = new MimeBodyPart();
	        body.setContent(template.getContent(), "text/html; charset=utf-8");
	        multipart.addBodyPart(body);
	        mimeMessage.setContent(multipart);
	        
	        //�����ʼ�
	        Transport transport = session.getTransport("smtp");
	        transport.send(mimeMessage);
	        result =AccountUtil.sent_success;
	        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy��MM��dd��   HH:mm:ss     ");     
			Date   curDate   =   new   Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
			String   time   =   formatter.format(curDate);  
			System.out.println("�����ˣ� " + this.username + " �����ˣ� " + receiver.getAddr() + " " + title.ToString() + " ���ͳɹ�." + " " + time);
	    } catch (Exception e) {
	    	result =AccountUtil.sent_error;
	    	String err = e.toString();
	    	if (err.contains("User not found"))
	    	{
	    		err = " �û�������";
	    	}
	    	if (err.contains("535 Error"))
	    	{
	    		err = "�û��������벻��ȷ";
	    		result = AccountUtil.password_error;
	    		this.status = AccountUtil.password_error;
	    	}
	    	if(err.contains("please open smtp flag first"))
	    	{
	    		err = "δ��ͨsmtp����";
	    		this.is_smtp = 0;
	    		result = AccountUtil.smtp_not_open;
	    	}
	    	if (err.contains("550 Mailbox not found"))
	    	{
	    		err = "�ռ��䲻����";
	    		result = AccountUtil.mailBox_not_found;
	    	}
	    	if (err.contains("535 Authentication failed"))
	    	{
	    		err = "�˺��쳣";
	    		result = AccountUtil.account_abnormal;
	    	}
	        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy��MM��dd��   HH:mm:ss     ");     
			Date   curDate   =   new   Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
			String   time   =   formatter.format(curDate);  
	        System.out.println("�����ˣ� " +  this.username + " �����ˣ� " + receiver.getAddr() + " " + title.ToString() + " ����ʧ��. "  + err + " " + time);
	    }

		return result;
	}
	
	public MailReceived getRecentMail()
	{
		MailReceived mr = new MailReceived();
		try
		{
			Properties props = new Properties();
			Session session = Session.getInstance(props,null);
			Store store = session.getStore("pop3");
			store.connect(this.pop3_host, this.username, this.password);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			Message[] message = folder.getMessages();
			//System.out.println(this.username + " �ռ������ʼ�����:��" + message.length);
			PraseMimeMessage pmm = null;
			for (int i = message.length - 1 ;i < message.length;i++)
			{
				pmm = new PraseMimeMessage((MimeMessage)message[i]);
				mr.setFrom(pmm.getFrom());
				mr.setReceiveTime(pmm.getReceiveTime());
				mr.setTo(pmm.getMailAddress("TO"));
				mr.setCC(pmm.getMailAddress("CC"));
				mr.setBcc(pmm.getMailAddress("BCC"));
				mr.setTitle(pmm.getTitle());
				mr.setContent(pmm.getBodyText());
				mr.setIsRead(pmm.getIsRead());
				mr.setIsContainAttach(pmm.getIsContainAttach());
				mr.setNeedReply(pmm.getNeedRelay());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return mr;
	}
	
	/*
	 * ��ͨsmtp���ܣ�������������Ҫ�ֶ���ͨsmtp���ܣ���ʵ�ָú���
	 */
	public boolean openSmtp(){ return false;}
}
