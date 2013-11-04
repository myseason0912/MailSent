package Test;

import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import Mail.HiddenInfoManager;
import Mail.Mail163TemplateManager;
import Mail.Mail163ReceiverManager;
import Mail.MailReceiver;
import Mail.MailTemplate;
import Mail.MailTitle;
import Mail.MailTitleManager;

public class Test163SmtpMail {
	public static void main(String[] args) 
	{
		List<MailAccount> qas = AccountUtil.getUserListByType(4, 3);
		int gap = 1000 * 60 * 5;
		int error_count = 0;
		Mail163ReceiverManager.init();
		MailTitleManager.init();
		Mail163TemplateManager.init();
		HiddenInfoManager.init();
		MailTemplate temp = new MailTemplate();
		MailTitle t = new MailTitle();
		int i = 0;
		
		while(true && i < 3)
		{
			MailAccount qa = qas.get(i);
			MailReceiver r = Mail163ReceiverManager.getNextReceiver(AccountUtil.mail163_account);
			t = MailTitleManager.getNextTitle(AccountUtil.mail163_account);
			temp = Mail163TemplateManager.getNextMail(r.getAddr(),AccountUtil.mail163_account);
			int ret = 0;
			ret = qa.sendMail(r, t, temp);
			//ret =  AccountUtil.sent_success;
			if (ret == AccountUtil.sent_success)
			{

			}
			else
			{
				error_count++;
				if (error_count > 3)
				{
					break;
				}
			}
    		try 
    		{
				Thread.sleep(gap + (int)Math.random() * 2000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}	
			i++;

		}
	}
}
