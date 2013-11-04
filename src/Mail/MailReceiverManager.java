package Mail;

import java.util.List;

import DB.ReceiverDB;

public class MailReceiverManager {
	
	protected static List<MailReceiver> receivers = null;
	protected static List<MailReceiver> test = null;
	protected static int total_count = 0;
	protected static int count_per_get = 100;
	protected static int index = 0;
	protected static int test_index = 0;
	protected static int sent_count = 0;
	
	
	public static int getReceiverCount()
	{
		return receivers.size();
	}
	
	public static int getTotalCount()
	{
		return total_count;
	}
	
	public static void init(){};
	
	
}
