package Import;

import java.io.BufferedReader;
import java.io.FileReader;

import DB.MailDB;
import Mail.MailTitle;

public class ImportTitleToDB {
	public static void main(String[] args) 
	{
		try
		{
			FileReader fr = new FileReader("data\\title");
			BufferedReader br = new BufferedReader(fr);
			String str = null;
			while ((str = br.readLine()) != null)
			{
				MailTitle t = new MailTitle();
				t.setTitle(str);
				MailDB.insetTitleToDB(t);
			}
			br.close();
			System.out.println("邮件标题入库结束.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
