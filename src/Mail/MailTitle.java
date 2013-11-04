package Mail;

public class MailTitle {
	
	protected int id;
	protected String title;
	
	public MailTitle()
	{
		
	}
	
	public MailTitle(int id,String title)
	{
		this.id = id;
		this.title = title;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle(String t)
	{
		this.title = t;
	}

	public String ToString()
	{
		return  this.title;
	}
}
