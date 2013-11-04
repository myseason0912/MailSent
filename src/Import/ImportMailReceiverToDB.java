package Import;

import java.io.BufferedReader;
import java.io.FileReader;

import DB.MailDB;
import Mail.MailReceiver;

public class ImportMailReceiverToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			String cat  = "女装1";
			FileReader fr = new FileReader("data\\" + cat + ".txt");
			BufferedReader br = new BufferedReader(fr);
			String s = "";
			while((s = br.readLine())!= null)
			{
				MailReceiver t = new MailReceiver();
				t.setAddr(s.trim());
				t.setGender(2);
				t.setInterest("女装");
				MailDB.insetMailReceiverToDB(t);
			}
			br.close();
			System.out.println("接受者入库结束.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
