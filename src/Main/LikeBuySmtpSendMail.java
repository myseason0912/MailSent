package Main;

import java.util.ArrayList;
import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import Mail.HiddenInfoManager;
import Mail.Mail163ReceiverManager;
import Mail.Mail163TemplateManager;
import Mail.MailTitleManager;

public class LikeBuySmtpSendMail {
	public static void main(String[] args) 
	{
		int like_count = 120;
		int period = 60 * 1000 * 50;//50min
		int gap = 60*1000*60; //1h
		int start_gap = 1000 * 30 ;//30s
		int threshold = 1000;//mail box sent threshold per day
		int sent_count = 10;
		int type = AccountUtil.like_account;
		
		Mail163ReceiverManager.init();
		MailTitleManager.init();
		Mail163TemplateManager.init();
		HiddenInfoManager.init();
		
		List<MailAccount> account_list = AccountUtil.getUserListByTypeAndSmtp(like_count,type,1);
		like_count =  account_list.size();
		LikeBuyMailSender [] mailSenders = new LikeBuyMailSender[like_count];
		MailTemplateUpdater mailUpdater = new MailTemplateUpdater();
		mailUpdater.setGap(gap);
		mailUpdater.setIsRunning(true);
		mailUpdater.setType(AccountUtil.like_account);
		
		for(int i = 0; i < like_count; i++){
			
			mailSenders[i] = new LikeBuyMailSender();
			mailSenders[i].setAccount(account_list.get(i));
			mailSenders[i].setPeriod(period);
			mailSenders[i].setGap(gap);
			mailSenders[i].setSentPerOnce(sent_count);
			mailSenders[i].setThreshold(threshold);
		}
		
		try
		{
			List<Thread> threads=new ArrayList<Thread>();
			int i = 0;
			for(i = 0; i < like_count; i++)
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
