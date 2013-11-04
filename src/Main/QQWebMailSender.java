package Main;

import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import DB.MailSentDB;
import Mail.MailQQReceiverManager;
import Mail.MailQQTemplateManager;
import Mail.MailReceiver;
import Mail.MailTemplate;
import Mail.MailTitle;
import Mail.MailTitleManager;

public class QQWebMailSender implements Runnable{
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
			if (ma.getIsLogin() == 1)
			{
				mr = MailQQReceiverManager.getNextReceiver(gender,AccountUtil.qq_account);
				mt = MailTitleManager.getNextTitle(AccountUtil.qq_account);
				mtmp = MailQQTemplateManager.getNextMail(mr.getAddr(),AccountUtil.qq_account);
				ret = ma.sendWebMail(mr,mt,mtmp);
				MailQQReceiverManager.updateSentCount();
				switch(ret)
				{
					case AccountUtil.need_captcha:
			    		try 
			    		{
							Thread.sleep(gap);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						break;
					case AccountUtil.time_out:
						ma = AccountUtil.loginAccount(ma);
						break;
					case AccountUtil.sent_success:
						MailSentDB.insertSentRecord(ma, mr, mt, mtmp);
						break;
					case AccountUtil.sent_error:
						break;
					case AccountUtil.too_many_receiver:
						this.sent_per_once /= 2;
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
			else
			{
				break;
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
