package Test;

import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import Mail.HiddenInfoManager;
import Mail.MailQQReceiverManager;
import Mail.MailQQTemplateManager;
import Mail.MailReceiver;
import Mail.MailTemplate;
import Mail.MailTitle;
import Mail.MailTitleManager;

public class TestSinaWebMail {

	public static void main(String[] args) 
	{
		List<MailAccount> qas = AccountUtil.getUserListByType(1, 1);
		MailAccount qa = qas.get(0);
		int gap = 1000 * 60 * 5;
		int error_count = 0;
		MailReceiver r = new MailReceiver();
		r.setAddr("345366940@qq.com");
		MailQQReceiverManager.init();
		MailTitleManager.init();
		MailQQTemplateManager.init();
		HiddenInfoManager.init();
		MailTemplate temp = MailQQTemplateManager.getNextMail(r.getAddr(),AccountUtil.sina_account);
		MailTitle t = MailTitleManager.getNextTitle(AccountUtil.sina_account);
		while(true)
		{
			int ret = qa.sendMail(r, t, temp);
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
		}
			

	}
}
