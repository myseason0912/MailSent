package Test;

import java.util.List;

import Account.AccountUtil;
import Account.MailAccount;
import DB.AccountDB;

public class TestQQOpenSmtp {
	
	public static void main(String[] args) 
	{
		List<MailAccount> qas = AccountUtil.getUserListByTypeAndSmtp(50,2,0);
		for (int i = 0;i < qas.size();i++)
		{
			MailAccount qa = qas.get(i);
			if (qa.getPassword().equalsIgnoreCase( "os2008"))
			{
				qa.login();
				if (qa.openSmtp())
				{
					System.out.println(qa.getUsername() + " 开通smtp成功");
					AccountDB.updateSmtp(qa);
				}
				else
				{
					System.out.println(qa.getUsername() + " 开通smtp失败");
				}
			}

		}

	}

}
