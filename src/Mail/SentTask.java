package Mail;

public class SentTask {
	protected static int hidden_info_index = 0;
	protected static int user_index = 0;
	protected static int title_index = 0;
	protected static int template_index = 0;
	protected static int type = 0;
	
	public int getHiddenIndex()
	{
		return this.hidden_info_index;
	}
	
	public void setHiddenIndex(int hidden)
	{
		this.hidden_info_index = hidden;
	}
	
	public int getUserIndex()
	{
		return this.user_index;
	}
	
	public void setUserIndex(int user)
	{
		this.user_index = user;
	}
	
	public int getTitleIndex()
	{
		return this.title_index;
	}
	
	public void setTitelIndex(int title)
	{
		this.title_index = title;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getTemplateIndex()
	{
		return this.template_index;
	}
	
	public void setTemplateIndex(int template)
	{
		this.template_index = template;
	}
}
