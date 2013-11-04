package Mail;

import java.util.List;
import java.util.Date;

public class MailReceived {
	
	protected String from;
	protected List<String> to;
	protected List<String> cc;
	protected List<String> bcc;
	protected String content;
	protected Date receive_time;
	protected String title;
	protected boolean is_read;
	protected boolean need_reply;
	protected boolean is_contain_attach;
	
	public String getFrom()
	{
		return this.from;
	}
	
	public void setFrom(String from)
	{
		this.from = from;
	}
	
	public List<String> getTo()
	{
		return this.to;
	}
	
	public void setTo(List<String> to)
	{
		this.to = to;
	}
	
	public List<String> getCC()
	{
		return this.cc;
	}
	
	public void setCC(List<String> cc)
	{
		this.cc = cc;
	}
	
	public List<String> getBcc()
	{
		return this.bcc;
	}
	
	public void setBcc(List<String> bcc)
	{
		this.bcc = bcc;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}

	public  Date getReceiveTime ()
	{
		return this.receive_time;
	}
	
	public void setReceiveTime (Date time)
	{
		this.receive_time = time;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public boolean getIsRead()
	{
		return this.is_read;
	}
	
	public void setIsRead(boolean is_read)
	{
		this.is_read = is_read;
	}
	
	public boolean getNeedReply()
	{
		return this.need_reply;
	}
	
	public void setNeedReply(boolean is_need)
	{
		this.need_reply = is_need;
	}
	
	public boolean getIsContainAttach()
	{
		return this.is_contain_attach;
	}
	
	public void setIsContainAttach(boolean is_contain)
	{
		this.is_contain_attach = is_contain;
	}
	
	public String ToString()
	{
		StringBuffer buffer = new StringBuffer();
		String enter = "\n";
		String gap = ",";
		if (this.is_read)
		{
			buffer.append("ÒÑ¶Á");
		}
		else
		{
			buffer.append("Î´¶Á");
		}
		buffer.append(enter);
		if (this.is_contain_attach)
		{
			buffer.append("´ø¸½¼þ");
			buffer.append(enter);
		}
		buffer.append(this.from);
		buffer.append(enter);
		int i = 0;
		for (i = 0;i < to.size();i++)
		{
			buffer.append(to.get(i));
			if (i != to.size() - 1)
			{
				buffer.append(gap);
			}
		}
		buffer.append(enter);
		for (i = 0;i < cc.size();i++)
		{
			buffer.append(cc.get(i));
			if (i != cc.size() - 1)
			{
				buffer.append(gap);
			}
		}
		if (cc.size() > 0)
		{
			buffer.append(enter);
		}
		for (i = 0;i < bcc.size();i++)
		{
			buffer.append(bcc.get(i));
			if (i != bcc.size() - 1)
			{
				buffer.append(gap);
			}
		}
		if (bcc.size() > 0)
		{
			buffer.append(enter);
		}
		buffer.append(this.receive_time);
		buffer.append(enter);
		buffer.append(this.title);
		if (title != "")
		{
			buffer.append(enter);
		}
		buffer.append(this.content);
		buffer.append(enter);
		return buffer.toString();
	}
}
