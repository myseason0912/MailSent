package Main;

import java.util.ArrayList;
import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import Mail.HiddenInfoManager;
import Mail.Mail163ReceiverManager;
import Mail.Mail163TemplateManager;
import Mail.MailTitleManager;

public class Mail163SmtpSendMail {
	public static void main(String[] args) 
	{
		int mail163_count = 150;
		/*发送间隔*/
		int period = 60 * 1000 * 40;//40min
		int gap = 60*1000*60; //1h
		int start_gap = 1000 * 60;//1 min
		int sent_count = 10;
		int type = 3;
		
		Mail163ReceiverManager.init();
		MailTitleManager.init();
		Mail163TemplateManager.init();
		HiddenInfoManager.init();
		
		List<MailAccount> mail163_account_list = AccountUtil.getUserListByType(mail163_count,type);
		mail163_count =  mail163_account_list.size();
		Mail163SmtpSender [] mailSenders = new Mail163SmtpSender[mail163_count];
		
		MailTemplateUpdater mailUpdater = new MailTemplateUpdater();
		mailUpdater.setGap(gap);
		mailUpdater.setIsRunning(true);
		mailUpdater.setType(AccountUtil.mail163_account);
		
		
		for(int i = 0; i < mail163_count; i++){
			
			mailSenders[i] = new Mail163SmtpSender();
			mailSenders[i].setAccount(mail163_account_list.get(i));
			mailSenders[i].setPeriod(period);
			mailSenders[i].setGap(gap);
			mailSenders[i].setSentPerOnce(sent_count);
		}
		
		try
		{
			List<Thread> threads=new ArrayList<Thread>();
			int i = 0;
			for(i = 0; i < mail163_count; i++)
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
