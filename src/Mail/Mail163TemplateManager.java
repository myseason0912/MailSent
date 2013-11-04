package Mail;


import java.util.List;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


import Account.AccountUtil;
import DB.MailDB;
import DB.SentTaskDB;

public class Mail163TemplateManager {
	protected static List<MailTemplate> temps = null;
	protected static final String hidden = "<TD>(.*?)</TD>";
	protected static final String email_addr = "m=(.*?)\"";
	protected static int sent_type = AccountUtil.mail163_account;
	
	protected static int index = 0;
	
	public static int getTemplateCount()
	{
		return temps.size();
	}

	public synchronized static MailTemplate getNextMail(String addr,int type)
	{
		synchronized(MailQQTemplateManager.class)
		{
			index = SentTaskDB.getIndexFromDB("template_index", type);
			if (index >= getTemplateCount())
				index = 0;
			MailTemplate tmp = new MailTemplate(temps.get(index++));
			String content = tmp.getContent();
			String str = content;
			try
			{
				str = content.replaceAll(hidden, "<TD>" + HiddenInfoManager.getNextInfo(type) + "</TD>");
				if (addr == "")
				{
					
				}
				else
				{
						String code  =  Base64.encode(addr.getBytes());
						str = str.replaceAll(email_addr, "m=" + code + "\"");
				
				}
			}
			catch(Exception e)
			{
				//e.printStackTrace();
			} 
			tmp.setContent(str);
			return tmp;
		}
	
	}
	
	
	public synchronized static void init()
	{
		temps = MailDB.getTemplateFromDB(sent_type);
	}
}
