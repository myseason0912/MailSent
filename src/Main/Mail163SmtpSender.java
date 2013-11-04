package Main;

import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import DB.AccountDB;
import DB.MailSentDB;
import Mail.Mail163ReceiverManager;
import Mail.Mail163TemplateManager;
import Mail.MailReceiver;
import Mail.MailTemplate;
import Mail.MailTitle;
import Mail.MailTitleManager;

public class Mail163SmtpSender implements Runnable{
	
	protected MailAccount ma = null;
	protected boolean isRunning = false;
	protected int period = 60*1000*5;
	protected int gap = 60*1000*180;
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
			mr = Mail163ReceiverManager.getNextReceiver(AccountUtil.mail163_account);
			mt = MailTitleManager.getNextTitle(AccountUtil.mail163_account);
			mtmp = Mail163TemplateManager.getNextMail(mr.getAddr(),AccountUtil.mail163_account);
			ret = ma.sendMail(mr,mt,mtmp);
			Mail163ReceiverManager.updateSentCount();
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
				case AccountUtil.mailBox_not_found:
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
