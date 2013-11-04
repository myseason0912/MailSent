package Test;

import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import Mail.MailReceived;

public class Test163PopMail {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<MailAccount> qas = AccountUtil.getUserListByType(1, 3);
		MailAccount qa = qas.get(0);
		MailReceived mr = qa.getRecentMail();
		System.out.println(mr.ToString());
	}

}
