package Mail;

import java.util.List;

import DB.MailDB;
import DB.SentTaskDB;

public class MailTitleManager {

	protected static List<MailTitle> titles = null;
	protected static int index = 0;
	
	public static int getTitleCount()
	{
		return titles.size();
	}
	
	public synchronized static MailTitle getNextTitle(int type)
	{
		synchronized(MailTitle.class)
		{
			index = SentTaskDB.getIndexFromDB("title_index", type);
			if (index >= getTitleCount())
			{
				index = 0;
				SentTaskDB.setIndex("title_index", 0,type);
			}
			else
			{
				SentTaskDB.updateIndex("title_index", 1,type);
			}
			return titles.get(index++);
		}
	}
	
	public synchronized static void init()
	{
		titles = MailDB.getTitleFromDB();
	}
	
}
