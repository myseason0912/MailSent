package Account;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.http.impl.client.BasicCookieStore;

import Account.MailAccount;
import DB.AccountDB;

public class AccountUtil {

	public static final int sina_account = 1;
	public static final int qq_account = 2;
	public static final int  mail163_account = 3;
	public static final int like_account = 4;
	
	
	public static final int sent_success = 1;
	public static final int need_captcha = 2;
	public static final int time_out = 3;
	public static final int sent_error = 4;
	public static final int too_many_receiver = 5;
	public static final int password_error = 6;
	public static final int smtp_not_open = 7;
	public static final int mailBox_not_found = 8;
	public static final int account_abnormal= 9;

	
	public static final int status_normal = 0;
	public static final int status_password_error = 1;
	public static final int status_abnormal = 2;

	
	public static String qq_smtp_host = "smtp.qq.com";
	public static String sina_smtp_host = "smtp.sina.com";
	public static String mail163_smtp_host = "smtp.163.com";
	public static String like_smtp_host = "mail.like-buy.com";
	
	public static String qq_pop3_host = "pop3.qq.com";
	public static String sina_pop3_host = "pop3.sina.com";
	public static String mail163_pop3_host = "pop3.163.com";
	
	public static String like_imap_host = "mail.like-buy.com";

	
	public static List<MailAccount> getUserListByType(int num,int type){
		
		AccountDB account_db = new AccountDB();
		List<MailAccount> account_list = account_db.getAccount(num,type);
		return account_list;
	}
	
	public static List<MailAccount> getUserListByTypeAndSmtp(int num,int type,int smtp){
		
		AccountDB account_db = new AccountDB();
		List<MailAccount> account_list = account_db.getAccount(num,type,smtp);
		return account_list;
	}
	
	public static List<MailAccount> getUserList(int num){
		
		AccountDB account_db = new AccountDB();
		List<MailAccount> account_list = account_db.getAccount(num);
		return account_list;
	}
	
	public static MailAccount loginAccount(MailAccount mc)
	{
		mc.loginWithCookie();
		if (mc.getIsLogin() == 1)
		{
		}
		else
		{
			mc.login();
		}
		AccountDB.updateCookie(mc);
		AccountDB.updateStatus(mc);
		return mc;
	}
}
