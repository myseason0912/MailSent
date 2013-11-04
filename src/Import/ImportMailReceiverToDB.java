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
			String cat  = "Ůװ1";
			FileReader fr = new FileReader("data\\" + cat + ".txt");
			BufferedReader br = new BufferedReader(fr);
			String s = "";
			while((s = br.readLine())!= null)
			{
				MailReceiver t = new MailReceiver();
				t.setAddr(s.trim());
				t.setGender(2);
				t.setInterest("Ůװ");
				MailDB.insetMailReceiverToDB(t);
			}
			br.close();
			System.out.println("������������.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
