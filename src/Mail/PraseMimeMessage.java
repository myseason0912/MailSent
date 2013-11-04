package Mail;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.InternetAddress;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Message;
import javax.mail.Flags;
import javax.mail.BodyPart;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 

public class PraseMimeMessage {
	protected MimeMessage mimeMessage = null;
	protected StringBuffer bodytext = new StringBuffer();
	
	public PraseMimeMessage(MimeMessage m)
	{
		this.mimeMessage = m; 
	}
	
	public void setMimeMessage(MimeMessage mimeMessage)
	{
		this.mimeMessage = mimeMessage;
	}
	
	public String getFrom()
	{
		String from = null;
		try
		{
			InternetAddress[] address = (InternetAddress[]) mimeMessage.getFrom();
			from = address[0].getAddress();
			if (from == null)
			{
				from = "";
			}
			else
			{
				int locl = from.indexOf("CONTENTTYPE");
				if (locl != -1)
				{
					from = from.substring(0,locl);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return from;
	}
	
	public List<String> getMailAddress(String type)
	{
		String add_type = type.toUpperCase();
		InternetAddress[] address = null;
		List<String> addr = new ArrayList<String>();
		try
		{
			if (add_type.equals("TO") || add_type.equals("CC") || add_type.equals("BCC"))
			{
				if (add_type.equals("TO"))
				{
					address = (InternetAddress[])mimeMessage.getRecipients(MimeMessage.RecipientType.TO);
				}
				if (add_type.equals("CC"))
				{
					address = (InternetAddress[])mimeMessage.getRecipients(MimeMessage.RecipientType.CC);
				}
				if (add_type.equals("BCC"))
				{
					address = (InternetAddress[])mimeMessage.getRecipients(MimeMessage.RecipientType.BCC);
				}
				if (address != null)
				{
					for (int i = 0;i < address.length;i++)
					{
						String email = address[i].getAddress();
						if (email == null)
						{
							email = "";
						}
						else
						{
							email = MimeUtility.decodeText(email);
						}
						addr.add(email);
					}
				}
			}
			else
			{
				System.out.println("获取邮件地址有误");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return addr;
	}
	
	public String getTitle()
	{
		String title = null;
		try
		{
			title = MimeUtility.decodeText(mimeMessage.getSubject());
			if (title == null)
			{
				title = "";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return title;
	}
	
	public Date getReceiveTime()
	{
		Date sentdate = null;  
		try
		{
			sentdate = mimeMessage.getSentDate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return sentdate;
	}
	
	public String getBodyText()
	{
		getMailContent((Part)mimeMessage);
		return bodytext.toString();
	}
	
    public void getMailContent(Part part)
    {
    	try
    	{
	        String contenttype = part.getContentType();   
	        int nameindex = contenttype.indexOf("name");   
	        boolean conname = false;   
	        if (nameindex != -1)   
	        {
	            conname = true;
	        }
	        if (part.isMimeType("text/plain") && !conname) 
	        {   
	            bodytext.append((String) part.getContent());   
	        } 
	        else
	        {
	        	if (part.isMimeType("text/html") && !conname)
	        	{   
	        		bodytext.append((String) part.getContent());
	        	} 
	        	else
	        	{
	        		if (part.isMimeType("multipart/*")) 
	        		{
	        			Multipart multipart = (Multipart) part.getContent();   
	                    int counts = multipart.getCount();   
	                    for (int i = 0; i < counts; i++) 
	                    {   
	                        getMailContent(multipart.getBodyPart(i));   
	                    }   
	        		}
	        		else 
	        		{
	        			if (part.isMimeType("message/rfc822")) 
	        			{   
	        				getMailContent((Part) part.getContent());   
	        			}
	        			else
	        			{
	        				
	        			}
	                } 
	        	}
	            
	        }   
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }   
    
    public boolean getNeedRelay()
    {
    	 boolean replysign = false;   
    	 try
    	 {
	         String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");   
	         if (needreply != null) 
	         {   
	             replysign = true;   
	         }
    	 }
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
         return replysign;
    }
    
    public boolean getIsRead()
    {
        boolean is_read = false;   
        try
        {
            Flags flags = ((Message) mimeMessage).getFlags();   
            Flags.Flag[] flag = flags.getSystemFlags();   
            for (int i = 0; i < flag.length; i++) 
            {   
                if (flag[i] == Flags.Flag.SEEN) 
                {   
                	is_read = true;   
                    break;   
                }   
            } 
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
  
        return is_read;   
    }   
    
    public boolean getIsContainAttach()
    {
    	return isContainAttach((Part)mimeMessage);
    }
    
    public boolean isContainAttach(Part part)
    {   
        boolean attachflag = false;   
        try
        {
	        if (part.isMimeType("multipart/*")) 
	        {   
	            Multipart mp = (Multipart) part.getContent();   
	            for (int i = 0; i < mp.getCount(); i++) 
	            {   
	                BodyPart mpart = mp.getBodyPart(i);   
	                String disposition = mpart.getDisposition();   
	                if ((disposition != null)&& 
	                	((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))))
	                {
	                    attachflag = true;
	                }
	                else
	                {
	                	if (mpart.isMimeType("multipart/*"))
	                	{
		                    attachflag = isContainAttach((Part) mpart);      
	                	}
	                	else 
	                	{   
		                    String contype = mpart.getContentType();   
		                    if (contype.toLowerCase().indexOf("application") != -1)   
		                    {
		                        attachflag = true;
		                    }
		                    if (contype.toLowerCase().indexOf("name") != -1)   
		                    {
		                        attachflag = true;
		                    }
	                	}
	                }

	            }   
	        } 
	        else
	        {
	        	if (part.isMimeType("message/rfc822"))
	        	{   
	        		attachflag = isContainAttach((Part) part.getContent());  
	        	}
	        }   
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        return attachflag;   
    }   
}
