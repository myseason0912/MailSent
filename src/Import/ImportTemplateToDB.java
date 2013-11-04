package Import;

import java.io.BufferedReader;
import java.io.FileReader;

import DB.MailDB;
import Mail.MailTemplate;
import Account.AccountUtil;

public class ImportTemplateToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			FileReader fr = new FileReader("data\\email-20130320_163.html");
			BufferedReader br = new BufferedReader(fr);
			int s;
			StringBuffer buffer = new StringBuffer ();
			while((s = br.read())!=-1)
			{
				buffer.append((char)s);
			}
			MailTemplate t = new MailTemplate();
			t.setContent(buffer.toString());
			t.setType(AccountUtil.mail163_account);
			MailDB.insetTemplateToDB(t);
			br.close();
			System.out.println("邮件模板入库结束.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
