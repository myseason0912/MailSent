package Import;

import java.io.BufferedReader;
import java.io.FileReader;

import DB.AccountDB;
import Account.MailAccount;
import Account.AccountUtil;

public class ImportMailSenderToDB {
	public static void main(String[] args) {
		try
		{
			String cat  = "likeMailSender";
			String gap = " ";
			int has_open_smtp = 1;
			FileReader fr = new FileReader("data\\" + cat );
			BufferedReader br = new BufferedReader(fr);
			String s = "";
			int type = 0;
			if (cat.contains("163"))
			{
				type = AccountUtil.mail163_account;
			}
			if (cat.contains("sina"))
			{
				type = AccountUtil.sina_account;
			}
			if (cat.contains("qq"))
			{
				type = AccountUtil.qq_account;
			}
			if (cat.contains("like"))
			{
				type = AccountUtil.like_account;
			}
			
			while((s = br.readLine())!= null)
			{
				String[] content = s.split(gap);
				if (content.length == 2)
				{
					MailAccount t = new MailAccount();
					t.setUsername(content[0].trim());
					t.setPassword(content[1].trim());
					t.setType(type);
					t.setIsSmtp(has_open_smtp);
					AccountDB.insetMailSenderToDB(t);
				}

			}
			br.close();
			System.out.println("发送者入库结束.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
