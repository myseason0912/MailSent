package Mail;

import java.util.Date;

public class MailReceiver {
	
	protected long id;
	protected String addr;
	protected String interest;
	protected int gender = 0;
	
	public MailReceiver()
	{
		
	}
	
	public MailReceiver(long id,String addr)
	{
		this.id = id;
		this.addr = addr;
	}
	
	public long getId()
	{
		return this.id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getAddr()
	{
		return this.addr;
	}
	
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
	
	public String getInterest()
	{
		return this.interest;
	}
	
	public void setInterest(String interest)
	{
		this.interest = interest;
	}
	
	public int getGender()
	{
		return this.gender;
	}
	
	public void setGender(int gender)
	{
		this.gender = gender;
	}
}
