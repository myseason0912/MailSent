package Main;

import java.util.ArrayList;
import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import Mail.HiddenInfoManager;
import Mail.MailQQReceiverManager;
import Mail.MailQQTemplateManager;
import Mail.MailTitleManager;

public class QQSmtpSendMail {
	public static void main(String[] args) 
	{
		int qq_count = 50;
		int period = 60 * 1000 * 40;//1h
		int gap = 60*1000*60; //1h
		int start_gap = 1000 * 60 ;//1 min
		int sent_count = 10;
		int type = AccountUtil.qq_account;
		
		MailQQReceiverManager.init();
		MailTitleManager.init();
		MailQQTemplateManager.init();
		HiddenInfoManager.init();
		
		List<MailAccount> qq_account_list = AccountUtil.getUserListByTypeAndSmtp(qq_count,type,1);
		qq_count =  qq_account_list.size();
		QQSmtpMailSender [] mailSenders = new QQSmtpMailSender[qq_count];
		
		MailTemplateUpdater mailUpdater = new MailTemplateUpdater();
		mailUpdater.setGap(gap);
		mailUpdater.setIsRunning(true);
		mailUpdater.setType(AccountUtil.qq_account);
		
		for(int i = 0; i < qq_count; i++){
			
			mailSenders[i] = new QQSmtpMailSender();
			mailSenders[i].setAccount(qq_account_list.get(i));
			mailSenders[i].setPeriod(period);
			mailSenders[i].setGap(gap);
			mailSenders[i].setSentPerOnce(sent_count);
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
