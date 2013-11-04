package Account;

public class LikeBuyMailAccount extends MailAccount{
	public LikeBuyMailAccount()
	{
		
	}
	
	public LikeBuyMailAccount(String username,String pass,int type,String smtp_host,String pop3_host)
	{
		this.username = username;
		this.password = pass;
		this.type = type;
		this.smtp_host = smtp_host;
		this.pop3_host = pop3_host;
		this.status = AccountUtil.status_normal;
	}
}
