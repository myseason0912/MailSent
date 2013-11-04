package Mail;

import java.util.List;
import java.util.ArrayList;

import DB.ReceiverDB;
import DB.SentTaskDB;
import Account.AccountUtil;

public class MailQQReceiverManager extends MailReceiverManager{

	
	public synchronized static void getFromDB(int gender,int type)
	{

		receivers = ReceiverDB.getQQReceiverFromDB(total_count,count_per_get,gender);
		if (receivers.size() < count_per_get)
		{
			/*到最后了*/
			SentTaskDB.setIndex("user_index", 0, type);
		}
		else
		{
			SentTaskDB.updateIndex("user_index", count_per_get, type);
		}
	}
	
	public static void init()
	{
		test = ReceiverDB.getTestAccountFromDB();
		test_index = 0;
		index = 0;
		sent_count = 0;
	}
	
	public synchronized static MailReceiver getNextReceiver(int gender,int type)
	{
		synchronized(MailReceiverManager.class)
		{
			if (sent_count % count_per_get == 0)
			{
				if (test == null || test_index >= test.size())
				{
					test_index = 0;
				}
				return test.get(test_index++);
			}
			else
			{
				if (receivers == null || index >= getReceiverCount())
				{
					total_count = SentTaskDB.getIndexFromDB("user_index",type);
					getFromDB(gender,type);
					index = 0;
				}
				total_count += 1;
				return receivers.get(index++);
			}
			
		}
	}
	
	public synchronized static void updateSentCount()
	{
		sent_count += 1;
	}
	
	public synchronized static List<MailReceiver>getReceivers(int num,int gender,int type)
	{
		List<MailReceiver> mrs = new ArrayList<MailReceiver>();
		for (int i = 0;i < num;i++)
		{
			mrs.add(getNextReceiver(gender,type));
		}
		return mrs;
	}
}
