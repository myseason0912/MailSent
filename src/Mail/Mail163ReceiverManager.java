package Mail;

import java.util.ArrayList;
import java.util.List;

import DB.ReceiverDB;
import DB.SentTaskDB;
import Account.AccountUtil;

public class Mail163ReceiverManager extends MailReceiverManager{
		
	public synchronized static void getFromDB(int type)
	{
		receivers = ReceiverDB.getOtherReceiverFromDB(total_count,count_per_get);
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
	
	public synchronized static void init()
	{
		test = ReceiverDB.getTestAccountFromDB();
		test_index = 0;
		index = 0;
		sent_count = 0;
	}

	public synchronized static MailReceiver getNextReceiver(int type)
	{
		synchronized(MailReceiverManager.class)
		{
			if (sent_count % count_per_get == 0)
			{
				if (test == null || test_index >= test.size())
				{
					test_index = 0;
				}
				sent_count += 1;
				return test.get(test_index++);
			}
			else
			{
				if (receivers == null || index >= getReceiverCount())
				{
					total_count =  SentTaskDB.getIndexFromDB("user_index",type);
					getFromDB(type);
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
	
	public synchronized static List<MailReceiver>getReceivers(int num,int type)
	{
		List<MailReceiver> mrs = new ArrayList<MailReceiver>();
		for (int i = 0;i < num;i++)
		{
			mrs.add(getNextReceiver(type));
		}
		return mrs;
	}
}
