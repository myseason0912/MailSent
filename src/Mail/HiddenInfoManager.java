package Mail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

import DB.SentTaskDB;
public class HiddenInfoManager {
	
	protected static final String hidden_info_path = "data\\hidden_info.txt";
	protected static List<String> hiddens = new ArrayList<String>();
	protected static int index = 0;
	
	
	public static String getNextInfo(int type)
	{
		synchronized(HiddenInfoManager.class)
		{
			index = SentTaskDB.getIndexFromDB("hidden_index", type);
			if (index >= hiddens.size())
			{
				index = 0;
				SentTaskDB.setIndex("hidden_index", 0,type);
			}
			else
			{
				SentTaskDB.updateIndex("hidden_index", 1,type);
			}
			
			return hiddens.get(index++);
		}
	}
	
	public static void init()
	{
		try
		{
			FileReader fr = new FileReader(hidden_info_path);
			BufferedReader br = new BufferedReader(fr);
			String str = null;
			while ((str = br.readLine()) != null)
			{
				hiddens.add(str);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

}
