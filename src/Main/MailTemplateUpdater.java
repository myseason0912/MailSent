package Main;

import Account.AccountUtil;
import Mail.MailQQTemplateManager;
import Mail.Mail163TemplateManager;
public class MailTemplateUpdater implements Runnable{
	
	protected int gap = 1000 * 60 * 60;
	protected int send_type = AccountUtil.qq_account;
	protected boolean is_running = true;
	
	public void run()
	{
		while(is_running)
		{
			try
			{
				Thread.sleep(gap);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			switch(send_type)
			{	
				case AccountUtil.qq_account:
					MailQQTemplateManager.init();
					break;
				case AccountUtil.mail163_account:
					Mail163TemplateManager.init();
					break;
				case AccountUtil.sina_account:
				case AccountUtil.like_account:
					Mail163TemplateManager.init();
					break;
				default:
					break;
			}
		}
	}
	
	public int getGap()
	{
		return this.gap;
	}
	
	public void setGap(int gap)
	{
		this.gap = gap;
	}
	
	public int getType()
	{
		return this.send_type;
	}
	
	public void setType(int type)
	{
		this.send_type = type;
	}

	public boolean getIsRunning()
	{
		return this.is_running;
	}
	
	public void setIsRunning(boolean running)
	{
		this.is_running = running;
	}
}
