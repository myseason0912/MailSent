package Main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import Account.MailAccount;
import Account.AccountUtil;

import Mail.MailQQTemplateManager;
import Mail.MailTitleManager;
import Mail.MailQQReceiverManager;
import Mail.HiddenInfoManager;

public class QQWebSendMail {
	private static int male = 1;
	private static int female = 2;
	public static void main(String[] args) 
	{
		int qq_count = 100;
		int type = 2; 
		int period = 60 * 1000 * 30;//30min
		int gap = 60*1000*60; //1h
		int start_gap = 1000 * 60;//1min
		int sent_count = 10;
		MailQQReceiverManager.init();
		MailTitleManager.init();
		MailQQTemplateManager.init();
		HiddenInfoManager.init();
		
		//List<MailAccount> qq_account_list = AccountUtil.getUserListByTypeAndSmtp(qq_count,type,0);
		List<MailAccount> qq_account_list = AccountUtil.getUserListByType(qq_count,type);
		
		qq_count = qq_account_list.size();
		
		QQWebMailSender [] mailSenders = new QQWebMailSender[qq_count];
		
		MailTemplateUpdater mailUpdater = new MailTemplateUpdater();
		mailUpdater.setGap(gap);
		mailUpdater.setIsRunning(true);
		mailUpdater.setType(AccountUtil.qq_account);
		
		for(int i = 0; i < qq_count; i++){
			
			mailSenders[i] = new QQWebMailSender();
			mailSenders[i].setAccount(AccountUtil.loginAccount(qq_account_list.get(i)));
			mailSenders[i].setPeriod(period);
			mailSenders[i].setGap(gap);
			mailSenders[i].setGender(female);
			mailSenders[i].setSentPerOnce(sent_count);
	        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   time   =   formatter.format(curDate);  
			if (mailSenders[i].getAccount().getIsLogin() == 1)
			{
				System.out.println("第" + i + "个" + "账户" + mailSenders[i].getAccount().getUsername() + "登陆成功." + " " + time);
			}
			else
			{
				System.out.println("第" + i + "个" + "账户" + mailSenders[i].getAccount().getUsername() + "登陆失败."+ " " + time);
			}
		}
		
		try
		{
			List<Thread> threads=new ArrayList<Thread>();
			int i = 0;
			for(i = 0; i < qq_count; i++)
			{
				mailSenders[i].setRunning(true);
				Thread thread = new Thread(mailSenders[i], String.valueOf(i));
				thread.start();
				thread.sleep(start_gap);
				threads.add(thread);
			}
			
			Thread thread = new Thread(mailUpdater, String.valueOf(i+1));
			thread.start();
			threads.add(thread);
			
		    for(i = 0; i < threads.size(); i++)
	        {
		    	threads.get(i).join();
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("所有线程结束，更新数据库");
	}
}
