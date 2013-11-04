package Main;
import java.util.List;

import Account.MailAccount;
import Mail.MailQQReceiverManager;
import Mail.MailTitleManager;
import Mail.MailQQTemplateManager;
import Mail.MailReceiver;
import Mail.MailTemplate;
import Mail.MailTitle;
import Account.AccountUtil;

import DB.MailSentDB;
import DB.AccountDB;

public class QQSmtpMailSender implements Runnable{

	protected MailAccount ma = null;
	protected boolean isRunning = false;
	protected int period = 60*1000*5;
	protected int gap = 60*1000*180;
	protected int gender = 2;
	protected int sent_per_once = 10;
	
	public void run()
	{
		int ret = 0;
		List<MailReceiver> mrs = null;
		MailReceiver mr = null;
		MailTitle mt = null;
		MailTemplate mtmp = null;

		while (isRunning)
		{
			mr = MailQQReceiverManager.getNextReceiver(gender,AccountUtil.qq_account);
			mt = MailTitleManager.getNextTitle(AccountUtil.qq_account);
			mtmp = MailQQTemplateManager.getNextMail(mr.getAddr(),AccountUtil.qq_account);
			if (ma.getIsSmtp() == 1)
			{
				ret = ma.sendMail(mr,mt,mtmp);
			}
			else
			{
				break;
			}
			MailQQReceiverManager.updateSentCount();
			switch(ret)
			{
				case AccountUtil.sent_success:
					MailSentDB.insertSentRecord(ma, mr, mt, mtmp);
					break;
				case AccountUtil.sent_error:
					break;
				case AccountUtil.password_error:
					AccountDB.updateStatus(ma);
					break;
				case AccountUtil.smtp_not_open:
					AccountDB.updateSmtp(ma);
					return;
				case AccountUtil.mailBox_not_found:
					break;
				case AccountUtil.account_abnormal:
					AccountDB.updateStatus(ma);
					break;
				default:
				break;
			}

    		try 
    		{
				Thread.sleep(period);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}	
		}
	
	}
	
	public MailAccount getAccount()
	{
		return this.ma;
	}
	
	public void setAccount(MailAccount m)
	{
		this.ma = m;
	}
	
	
	public int getPeriod()
	{
		return this.period;
	}
	
	public void setPeriod(int p)
	{
		this.period = p;
	}
	
	public int getGender()
	{
		return this.gender;
	}
	
	public void setGender(int g)
	{
		this.gender = g;
	}
	
	public int getSentPerOnce()
	{
		return this.sent_per_once;
	}
	
	public void setSentPerOnce(int s)
	{
		this.sent_per_once = s;
	}
	
	
	public boolean isRunning() 
	{
		return isRunning;
	}

	public void setRunning(boolean isRunning) 
	{
		this.isRunning = isRunning;
	}
	
	public int getGap()
	{
		return this.gap;
	}
	
	public void setGap(int g)
	{
		this.gap = g;
	}
	
}
