package Test;

import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import DB.AccountDB;
import Mail.HiddenInfoManager;
import Mail.MailQQReceiverManager;
import Mail.MailQQTemplateManager;
import Mail.MailReceiver;
import Mail.MailTemplate;
import Mail.MailTitle;
import Mail.MailTitleManager;

public class TestMailTemplate {
	public static void main(String[] args) 
	{
		List<MailAccount> qas = AccountUtil.getUserListByType(1,2);
		int gap = 1000 * 60 * 5;
		MailReceiver r = new MailReceiver();
		r.setAddr("345366940@qq.com");
		MailQQReceiverManager.init();
		MailTitleManager.init();
		MailQQTemplateManager.init();
		HiddenInfoManager.init();
		for (int i = 0;i < qas.size();i++)
		{
			MailAccount qa = qas.get(i);
			qa.setUsername("1563915947");
			qa.setPassword("season199024");
			MailTemplate temp = MailQQTemplateManager.getNextMail(r.getAddr(),AccountUtil.qq_account);
			MailTitle t = MailTitleManager.getNextTitle(AccountUtil.qq_account);
			int ret = qa.sendMail(r, t, temp);

			if (ret == AccountUtil.smtp_not_open)
			{
				AccountDB.updateSmtp(qa);
			}
			try
			{
				Thread.sleep(gap);
			}
			catch(Exception e)
			{
				
			}
		}
	}
}
