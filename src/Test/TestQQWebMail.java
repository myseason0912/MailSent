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


public class TestQQWebMail {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<MailAccount> qas = AccountUtil.getUserListByType(1, 2);
		MailAccount qa = qas.get(0);
		qa = AccountUtil.loginAccount(qa);
		MailReceiver r = new MailReceiver();
		r.setAddr("345366940@qq.com");
		MailQQReceiverManager.init();
		MailTitleManager.init();
		MailQQTemplateManager.init();
		HiddenInfoManager.init();
	
//		MailTemplate temp = MailQQTemplateManager.getNextMail(r.getAddr(),AccountUtil.qq_account);
//		MailTitle t = MailTitleManager.getNextTitle(AccountUtil.qq_account);
//		int ret = qa.sendWebMail(r, t, temp);


	}

}
