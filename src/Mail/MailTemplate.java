package Mail;

public class MailTemplate {
	protected int id ;
	protected String content ;
	protected int type;
	
	public MailTemplate()
	{
		
	}
	
	public MailTemplate(MailTemplate m)
	{
		this.id = m.getId();
		this.content = m.getContent();
		this.type = m.getType();
	}
	
	
	public MailTemplate(int id,String content,int type)
	{
		this.id = id;
		this.content = content;
		this.type = type;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String ToString()
	{
		return this.content;
	}
}
