package Account;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.HeaderIterator;

import Mail.MailReceiver;
import Mail.MailTemplate;
import Mail.MailTitle;
import Utils.QQUtil;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import Utils.WebClientDevWrapper;

public class QQMailAccount extends MailAccount{
	
	/*发信时用到*/
	protected String login_sid = "";
	protected String r = "";
	protected String qq_verify_img = "qq_verify_img.jpg";
	 String smtp_verify_img = "smtp_verify_img.jpg";
	protected String nick = "";
	protected String mail_send_para1 = "";
	protected String mail_send_para2 = "";
	
	protected ClientConnectionManager connManager = null;
	
	public QQMailAccount()
	{
		connManager = new PoolingClientConnectionManager();
        client = new DefaultHttpClient(connManager);
        this.client = WebClientDevWrapper.wrapClient(client);
		client.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, this.connect_time_out);
	}
	
	public QQMailAccount(String username,String pass,int type,String smtp_host,String pop3_host)
	{
		this.username = username;
		this.password = pass;
		this.type = type;
		this.uid = this.username.split("@")[0];
		this.smtp_host = smtp_host;
		this.pop3_host = pop3_host;
		this.status = AccountUtil.status_normal;
		connManager = new PoolingClientConnectionManager();
        client = new DefaultHttpClient(connManager);
        this.client = WebClientDevWrapper.wrapClient(client);
		client.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, this.connect_time_out);
	}
	
	public  boolean login()
	{
		boolean result = false;
		String urlv = "";
		String str = "";
		String verify_code = "";
		String uni = "";
	    Pattern p;
	    Matcher m;
		try
		{
			long begin = System.currentTimeMillis();
			/*获取验证码*/
			urlv = "https://ssl.ptlogin2.qq.com/check?uin=" + this.username
					+ "&appid=522005705&ptlang=2052&js_type=2&js_ver=10009&r="
					+ System.currentTimeMillis();
			HttpGet check = new HttpGet(urlv);
			HttpResponse response = client.execute(check);
			str = EntityUtils.toString(response.getEntity());
			
			/*数据格式:ptui_checkVC('0','!JWQ','\x00\x00\x00\x00\x5d\x37\x76\xab');*/
			p = Pattern. compile(QQUtil.login_msg);  
	        m = p. matcher(str);  

	        String[] content = new String[QQUtil.check_field_num];
	        int i = 0;
	        while (m.find())
	        {
	        	content[i] = m.group(1);
	        	i++;
	        }
	        String state =  content[0];
	        verify_code = content[1];
			if (state.equalsIgnoreCase("1"))
			{
				synchronized(QQMailAccount.class)
				{
					/*需要验证码*/
					urlv = "https://ssl.captcha.qq.com/getimage?aid=522005705&r=" 
							+ System.currentTimeMillis() + "&uin=" + this.username;
					HttpGet captcha = new HttpGet(urlv);;
					HttpEntity captchaEntity = client.execute(captcha).getEntity();
					InputStream is=captchaEntity.getContent();
			        File storeFile = new File(qq_verify_img);
			        FileOutputStream output = new FileOutputStream(storeFile); 
			        //得到网络资源的字节数组,并写入文件
		             byte b[] = new byte[1024];  
		             int j = 0;  
		             while( (j = is.read(b))!=-1){  
		                 output.write(b,0,j);  
		             }  
		            output.flush();
		            output.close(); 
		            System.out.println(uid + " 账号登录请输入 " + qq_verify_img + " 中的验证码");
		            verify_code = new BufferedReader(new InputStreamReader(System.in)).readLine();
				}
			}
			uni = content[2];
			
			/*登录*/
			String encodePass = QQUtil.getPass(password, verify_code,uni);
			StringBuffer loginUrl = new StringBuffer("https://ssl.ptlogin2.qq.com/login?");
			long end = System.currentTimeMillis();
			loginUrl.append("ptlang=2052&aid=522005705&daid=4&u1=")
					.append("https%3A%2F%2Fmail.qq.com%2Fcgi-bin%2Flogin%3Fvt%3D")
					.append("passport%26vm%3Dwpt%26ft%3Dptlogin%26ss%3D1%26validcnt%3D%26")
					.append("clientaddr%3D").append(this.uid).append("%40qq.com")
					.append("&from_ui=1&ptredirect=1&h=1&wording=%E5%BF%AB%E9%80%9F%E7%99%BB%E5%BD%95")
					.append("&css=https://mail.qq.com/zh_CN/htmledition/style/fast_login13ffb2.css")
					.append("&mibao_css=m_ptmail&u_domain=@qq.com&uin=").append(this.uid)
					.append("&u=").append(this.username).append("&p=").append(encodePass)
					.append("&verifycode=").append(verify_code).append("&fp=loginerroralert")
					.append("&action=2-17-").append(end-begin).append("&g=1&t=1&dummy=&js_type=2&js_ver=10009");
	
			HttpGet login = new HttpGet(loginUrl.toString());
			login.setHeader("Referer", "https://mail.qq.com/cgi-bin/loginpage");
			response = client.execute(login);
			str = EntityUtils.toString(response.getEntity());

			p = Pattern. compile(QQUtil.login_msg);  
	        m = p. matcher(str);  
	        String[] msg = new String[QQUtil.login_result_field_num];
	        i = 0;
	        while (m.find())
	        {
	        	msg[i] = m.group(1);
	        	i++;
	        }
	        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   time   =   formatter.format(curDate);  
			if(str.indexOf("登录成功！") == -1)
			{
		        System.out.print(this.username + " " + msg[QQUtil.login_err_field] + " " + time);
		        if (msg[QQUtil.login_err_field].contains("验证码不正确"))
		        {
		        	System.out.println(this.username + " 验证码不正确，重新登录。");
		        	return this.login();
		        }
		        if (msg[QQUtil.login_err_field].contains("您的帐号暂时无法登录，"))
		        {
		        	System.out.println(this.username + " 账号异常，需要验证。登录失败");
		        	this.status = AccountUtil.status_abnormal;
		        }
		        if (msg[QQUtil.login_err_field].contains("您输入的帐号或密码不正确"))
		        {
		        	System.out.println(this.username + " 帐号或密码不正确。登录失败");
		        	this.status = AccountUtil.status_password_error;
		        }
			}
			else
			{
				urlv = msg[QQUtil.login2_url_field];
				this.nick = msg[QQUtil.login_nick_field];
				/*检查sig*/
				HttpGet sig = new HttpGet(urlv);
				response = client.execute(sig);
				str = EntityUtils.toString(response.getEntity());
				
				if (str.contains("verify=true"))
				{
					/*需要二次输入验证码*/
					result = login2();
				}
				else
				{
					if (str.contains("container"))
					{
						result = true;
						this.is_login = 1;
					}
					else
					{
						if (str.contains("errcode"))
						{
							if (str.contains("-10"))
							{
								String regex = "/cgi-bin/login";
								int locl = str.lastIndexOf(regex);
								int locr = str.indexOf(';', locl);
								String new_url = "";
								new_url = str.substring(locl,locr);
								if (new_url != "")
								{
									new_url = new_url.replace("\\x22", "");
									new_url = new_url.replace("\\x26", "&");
									new_url = "https://mail.qq.com" + new_url;
									
						        	HttpGet get = new HttpGet(new_url);
									response = client.execute(get);
									str = EntityUtils.toString(response.getEntity());
									if (str.contains("container"))
									{
										result = true;
										this.is_login = 1;
									}
								}
								
							}
						}
						else
						{
							if (getKeyAfterLogin(str))
							{
								result = true;
								this.is_login = 1;
							}
							else
							{
	
								System.out.println(this.username + " 获得sid和r失败，服务器返回：  " + str);
							}
						}
					}
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			client.getConnectionManager().closeExpiredConnections();
		}
		
		return result;
	}
	
	public boolean login2()
	{
		boolean result = false;
		try
		{
			String urlv = "https://mail.qq.com/cgi-bin/getverifyimage?aid=23000101&f=html&ck=1&"
					+ System.currentTimeMillis();
			HttpGet captcha = new HttpGet(urlv);;
			HttpEntity captchaEntity = client.execute(captcha).getEntity();
			InputStream is=captchaEntity.getContent();
			File storeFile = new File(qq_verify_img);
			FileOutputStream output = new FileOutputStream(storeFile); 
			//得到网络资源的字节数组,并写入文件
			byte b[] = new byte[1024];  
			int j = 0;  
			while( (j = is.read(b))!=-1){  
				output.write(b,0,j);  
			}  
			output.flush();
			output.close(); 
			System.out.println(uid + " 账号登录请输入 " + qq_verify_img + " 中的验证码");
			String verify_code = new BufferedReader(new InputStreamReader(System.in)).readLine();
			
			/*再次登录*/
			urlv = "https://mail.qq.com/cgi-bin/login?sid=,2,zh_CN";
			HttpPost post = new HttpPost(urlv);
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	        nvps.add(new BasicNameValuePair("btlogin", "?"));
	        nvps.add(new BasicNameValuePair("delegate_url", ""));
	        nvps.add(new BasicNameValuePair("sid", ",2,zh_CN"));
	        nvps.add(new BasicNameValuePair("uin", this.uid));
	        nvps.add(new BasicNameValuePair("verifycode", verify_code));
	        nvps.add(new BasicNameValuePair("vt", "passport"));
     	 	post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
       	 	HttpResponse response = client.execute(post);
			String str = EntityUtils.toString(response.getEntity(),"UTF-8");
	        
			if (getKeyAfterLogin(str))
			{
				result = true;
				this.is_login = 1;
			}
			else
			{
				System.out.println(this.username + " 输入验证码错误，请重新登录");
				return login2();
			}
	        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	
	public boolean loginWithCookie()
	{
		boolean result = false;
		String urlv = "";
		String str = "";
		HttpResponse response = null;
		this.is_login = 0;
		try
		{
			urlv = "http://mail.qq.com/cgi-bin/login?fun=psaread&rand="
					+ System.currentTimeMillis() +"&delegate_url=";
			HttpGet login = new HttpGet(urlv);
			response = client.execute(login);
			str = EntityUtils.toString(response.getEntity());
			if (str.contains("container"))
			{
				result = true;
				this.is_login = 1;
			}
			else
			{
				if (str.contains("cgi exception"))
				{
					if (str.contains("-100"))
					{
						String regex = "/cgi-bin/login";
						int locl = str.lastIndexOf(regex);
						int locr = str.indexOf("\\x3c", locl);
						String new_url = "";
						new_url = str.substring(locl,locr);
						if (new_url != "")
						{
							new_url = new_url.replace("\\x22", "");
							new_url = new_url.replace("+", "");
							new_url = new_url.replace("\\x26", "&");
							new_url = "https://mail.qq.com" + new_url;
							
				        	HttpGet get = new HttpGet(new_url);
							response = client.execute(get);
							str = EntityUtils.toString(response.getEntity());
							//System.out.println(str);
							if(str.contains("document.cookie"))
							{
								result = false;
								this.is_login = 0;

								System.out.println(this.username + " cookie登陆失败");
							}
							else
							{
								if (str.contains("container"))
								{
									result = true;
									this.is_login = 1;
								}
							}
						}
					}
				}
				else
				{
					if (getKeyAfterLogin(str))
					{
						result = true;
						this.is_login = 1;
					}
					else
					{
						this.is_login = 0;
						System.out.println(this.username + " cookie登陆失败");
					}	
				}
			}

			
		}
		catch(Exception e)
		{
			e.printStackTrace();

		}
		finally
		{
			client.getConnectionManager().closeExpiredConnections();
		}
		
		return result;
	}
	
	public String getMailKeyFromCookie()
	{
		String key = "";
		String cookie = client.getCookieStore().toString();
		int locl = cookie.indexOf(QQUtil.login_mail_key) ;
		int locr = -1;
		if (locl != -1)
		{
			locr = cookie.indexOf(']',locl+QQUtil.login_mail_key.length());
			if (locr != -1)
			{
				String qm_antisky = cookie.substring(locl+QQUtil.login_mail_key.length(),locr);
				key = qm_antisky.split("&")[1];
			}
		}	
		return key;
	}
	
	public boolean getKeyAfterLogin(String str)
	{
		boolean sid_t = false;
		boolean r_t = false;
		if (str.contains("cgi exception"))
		{
			return false;
		}
		int locl = str.indexOf(QQUtil.login_sid) ;
		int locr = -1;
		if (locl != -1)
		{
			locr = str.indexOf('\"',locl+QQUtil.login_sid.length());
			if (locr != -1)
			{
				this.login_sid = str.substring(locl+QQUtil.login_sid.length(),locr);
				sid_t = true;
			}
		}
		locl = str.indexOf(QQUtil.login_r) ;
		locr = -1;
		if (locl != -1)
		{
			locr = str.indexOf('\"',locl+QQUtil.login_r.length());
			if (locr != -1)
			{
				this.r = str.substring(locl+QQUtil.login_r.length(),locr);
				r_t = true;
			}
		}
		return sid_t&&r_t;
	}
	
	public boolean getSendKey(String str)
	{
		boolean result = false;
		int locl = str.indexOf(QQUtil.mail_hidden_value) ;
		int locr = -1;
		if (locl != -1)
		{
			locr = str.indexOf('>',locl+QQUtil.mail_hidden_value.length());
			if (locr != -1)
			{
				String hidden = str.substring(locl+QQUtil.mail_hidden_value.length(),locr+1);
				Pattern p;
			    Matcher m;
				p = Pattern. compile(QQUtil.mail_para_value);  
		        m = p. matcher(hidden);  
		        if (m.find())
		        {
		        	this.mail_send_para1 = m.group(1);
		        	this.mail_send_para2 = m.group(2);
		        }
		       
			}
		}
		return result;
	}
	
	public int sendWebMail(MailReceiver receiver,MailTitle title,MailTemplate template)
	{

		int result = 0;
		String str = "";
        String urlv = "";
        String refer = "";
        try
        {
        	/*获得hidden参数*/
        	refer = "http://mail.qq.com/cgi-bin/frame_html?sid="
    			  + this.login_sid + "&r=" + this.r;
        	
        	urlv = "http://mail.qq.com/cgi-bin/readtemplate?sid=" + this.login_sid 
        			+ "&t=compose&s=cnew&loc=frame_html,,,21";
        	HttpGet mail_template = new HttpGet(urlv);
        	mail_template.setHeader("Referer", refer);
			HttpResponse response = client.execute(mail_template);
			str = EntityUtils.toString(response.getEntity());
			getSendKey(str);
        	 	
        	
        	urlv = "http://mail.qq.com/cgi-bin/compose_send?sid="
  			  + this.login_sid + "&sid=" + this.login_sid;

        	HttpPost post = new HttpPost(urlv);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair(this.mail_send_para1, this.mail_send_para2));
            nvps.add(new BasicNameValuePair("acctid", "0"));
            nvps.add(new BasicNameValuePair("actiontype", "send"));
            nvps.add(new BasicNameValuePair("cginame", "compose_send"));
            nvps.add(new BasicNameValuePair("cgitm",String.valueOf(System.currentTimeMillis())));
            nvps.add(new BasicNameValuePair("clitm", String.valueOf(System.currentTimeMillis())));
            nvps.add(new BasicNameValuePair("comtm", String.valueOf(System.currentTimeMillis())));
            nvps.add(new BasicNameValuePair("content__html", template.getContent()));
            nvps.add(new BasicNameValuePair("domaincheck", "0"));
            nvps.add(new BasicNameValuePair("ef", "js"));
            nvps.add(new BasicNameValuePair("from_s", "cnew"));
            nvps.add(new BasicNameValuePair("hitaddrbook", "0"));
            nvps.add(new BasicNameValuePair("logattcnt", "0"));
            nvps.add(new BasicNameValuePair("logattsize", "0"));
            nvps.add(new BasicNameValuePair("resp_charset", "UTF8"));
            nvps.add(new BasicNameValuePair("s", "comm"));
            nvps.add(new BasicNameValuePair("savesendbox", "1"));
            nvps.add(new BasicNameValuePair("selfdefinestation", "-1"));
            nvps.add(new BasicNameValuePair("sendmailname", this.username));
            nvps.add(new BasicNameValuePair("sendname", this.nick));
            nvps.add(new BasicNameValuePair("separatedcopy", "false"));
            nvps.add(new BasicNameValuePair("sid", this.login_sid));
            nvps.add(new BasicNameValuePair("subject", title.getTitle()));
            nvps.add(new BasicNameValuePair("t", "compose_send.json"));
            nvps.add(new BasicNameValuePair("to", "<" + receiver.getAddr() + ">"));
            nvps.add(new BasicNameValuePair("virtual", "virtual"));
            
       	 	post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
       	 	post.setHeader("Referer",refer);
       	 	response = client.execute(post);
			str = EntityUtils.toString(response.getEntity(),"UTF-8");
			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   time   =   formatter.format(curDate);  
			if (str.contains("errcode : \"0\""))
			{
				System.out.println("发送人： " + this.username + " 收信人： " + receiver.getAddr() + " " + title.ToString() + " 发送成功." + " " + time);
				result = AccountUtil.sent_success;
			}
			else
			{
				String err = "";
				if (str.contains("为了保障您的邮箱安全，请输入验证码以完成本次发送"))
				{
					err = "需要输入验证码";
					result = AccountUtil.need_captcha;
				}
				else
				{
					if (str.contains("timeout"))
					{
						err = "cookie失效,重新登录.";
						result = AccountUtil.time_out;
						this.is_login = 0;
					}
					else
					{
						if (str.contains("errcode"))
						{
							if (str.contains("-10"))
							{
								String regex = "/cgi-bin/login";
								int locl = str.lastIndexOf(regex);
								int locr = str.indexOf(';', locl);
								String new_url = "";
								new_url = str.substring(locl,locr);
								if (new_url != "")
								{
									new_url = new_url.replace("\\x22", "");
									new_url = new_url.replace("\\x26", "&");
									new_url = "https://mail.qq.com" + new_url;
									
						        	HttpGet get = new HttpGet(new_url);
									response = client.execute(get);
									str = EntityUtils.toString(response.getEntity());
									//System.out.println(str);
									if (str.contains("container"))
									{
										this.is_login = 1;
									}
								}
								err = "-10";
							}
						}
						else
						{
							err = str;
						}
						result = AccountUtil.sent_error;
					}
				}
		        System.out.println("发送人： " +  this.username + " 收信人： " + receiver.getAddr() + " " + title.ToString() + " 发送失败. " 
    					+ err + " " + time);

			}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		finally
		{
			client.getConnectionManager().closeExpiredConnections();
		}
		return result;
	}

	public int sendWebMail(List<MailReceiver> receiver,MailTitle title,MailTemplate template)
	{
		int result = 0;
		String str = "";
        String urlv = "";
        String refer = "";
        try
        {
        	/*获得hidden参数*/
        	refer = "http://mail.qq.com/cgi-bin/frame_html?sid="
    			  + this.login_sid + "&r=" + this.r;
        	
        	urlv = "http://mail.qq.com/cgi-bin/readtemplate?sid=" + this.login_sid 
        			+ "&t=compose&s=cnew&loc=frame_html,,,21";
        	HttpGet mail_template = new HttpGet(urlv);
        	mail_template.setHeader("Referer", refer);
			HttpResponse response = client.execute(mail_template);
			str = EntityUtils.toString(response.getEntity());
			getSendKey(str);
        	 	
        	
        	urlv = "http://mail.qq.com/cgi-bin/compose_send?sid="
  			  + this.login_sid + "&sid=" + this.login_sid;

        	HttpPost post = new HttpPost(urlv);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair(this.mail_send_para1, this.mail_send_para2));
            nvps.add(new BasicNameValuePair("acctid", "0"));
            nvps.add(new BasicNameValuePair("actiontype", "send"));
            nvps.add(new BasicNameValuePair("cginame", "compose_send"));
            nvps.add(new BasicNameValuePair("cgitm",String.valueOf(System.currentTimeMillis())));
            nvps.add(new BasicNameValuePair("clitm", String.valueOf(System.currentTimeMillis())));
            nvps.add(new BasicNameValuePair("comtm", String.valueOf(System.currentTimeMillis())));
            nvps.add(new BasicNameValuePair("content__html", template.getContent()));
            nvps.add(new BasicNameValuePair("domaincheck", "0"));
            nvps.add(new BasicNameValuePair("ef", "js"));
            nvps.add(new BasicNameValuePair("from_s", "cnew"));
            nvps.add(new BasicNameValuePair("hitaddrbook", "0"));
            nvps.add(new BasicNameValuePair("logattcnt", "0"));
            nvps.add(new BasicNameValuePair("logattsize", "0"));
            nvps.add(new BasicNameValuePair("resp_charset", "UTF8"));
            nvps.add(new BasicNameValuePair("s", "comm"));
            nvps.add(new BasicNameValuePair("savesendbox", "1"));
            nvps.add(new BasicNameValuePair("selfdefinestation", "-1"));
            nvps.add(new BasicNameValuePair("sendmailname", this.username));
            nvps.add(new BasicNameValuePair("sendname", this.nick));
            nvps.add(new BasicNameValuePair("separatedcopy", "false"));
            nvps.add(new BasicNameValuePair("sid", this.login_sid));
            nvps.add(new BasicNameValuePair("subject", title.getTitle()));
            nvps.add(new BasicNameValuePair("t", "compose_send.json"));
            nvps.add(new BasicNameValuePair("to", receiver.get(0).getAddr()));
            String bcc = "";
            for (int i = 1;i < receiver.size();i++)
            {
            	bcc += receiver.get(i).getAddr();
            	if (i != receiver.size() - 1)
            	{
            		bcc += ";";
            	}
            }
            nvps.add(new BasicNameValuePair("bcc", bcc));
            nvps.add(new BasicNameValuePair("virtual", "virtual"));
            
       	 	post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
       	 	post.setHeader("Referer",refer);
       	 	response = client.execute(post);
			str = EntityUtils.toString(response.getEntity(),"UTF-8");
			str = EntityUtils.toString(response.getEntity(),"UTF-8");
			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss     ");     
			Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
			String   time   =   formatter.format(curDate);  
			if (str.contains("errcode : \"0\""))
			{
				System.out.println("带抄送的发送，发送人： " + this.username + " 收信人： " + receiver.size() + " 个" + title.ToString() + " 发送成功." + " " + time);
				result = AccountUtil.sent_success;
			}
			else
			{
				String err = "";
				if (str.contains("为了保障您的邮箱安全，请输入验证码以完成本次发送"))
				{
					err = "需要输入验证码";
					result = AccountUtil.need_captcha;
				}
				else
				{
					if (str.contains("timeout"))
					{
						err = "cookie失效,重新登录.";
						result = AccountUtil.time_out;
						this.is_login = 0;
					}
					else
					{
						if (str.contains("地址错误"))
						{
							err = "地址错误";
							result = AccountUtil.sent_error;
							System.out.println(receiver.get(0).getAddr() + " || " + bcc);
						}
						else
						{
							if (str.contains("收件人过多，请减少收件人数量"))
							{
								err = "收件人过多";
								result = AccountUtil.too_many_receiver;
							}
							else
							{
								err = str;
								result = AccountUtil.sent_error;
							}
						}
						
					}
				}
		        System.out.println("带抄送的发送，发送人： " +  this.username + " 收信人： " + receiver.size() + " 个" + title.ToString() + " 发送失败. " 
    					+ err + " " + time);

			}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		finally
		{
			client.getConnectionManager().closeExpiredConnections();
		}
		return result;
	}
	
	public boolean openSmtp()
	{
		return openSmtp("","");
	}

	public boolean openSmtp(String verify_code,String verify_key)
	{
		String urlv = "";
		String str = "";
		boolean result = false;
		
		try
		{
			/*第一次请求获得verifykey*/
			urlv = "http://mail.qq.com/cgi-bin/setting4?sid=" + this.login_sid;
			
	       	HttpPost post = new HttpPost(urlv);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("apply", ""));
            nvps.add(new BasicNameValuePair("birthDay", "0"));
            nvps.add(new BasicNameValuePair("birthMonth", "0"));
            nvps.add(new BasicNameValuePair("ccalias", "8"));
            nvps.add(new BasicNameValuePair("clitohttps", "0"));
            nvps.add(new BasicNameValuePair("datetype", "0"));
            nvps.add(new BasicNameValuePair("del_step", "1"));
            nvps.add(new BasicNameValuePair("exchange", "1"));
            nvps.add(new BasicNameValuePair("Fun", "submit"));
            nvps.add(new BasicNameValuePair("openimap", "1"));
            nvps.add(new BasicNameValuePair("popbookMail", "0"));
            nvps.add(new BasicNameValuePair("popdelenable", "0"));
            nvps.add(new BasicNameValuePair("PopEsmtp", "1"));
            nvps.add(new BasicNameValuePair("popjunkMail", "0"));
            nvps.add(new BasicNameValuePair("popmyFolder", "0"));
            nvps.add(new BasicNameValuePair("poprecent", "0"));
            nvps.add(new BasicNameValuePair("recvaliasmail", "0"));
            nvps.add(new BasicNameValuePair("reload", "0"));
            nvps.add(new BasicNameValuePair("savesend_esmtp", "1"));
            nvps.add(new BasicNameValuePair("setBirthdayconfig", "1"));
            nvps.add(new BasicNameValuePair("setnick", "0"));
            nvps.add(new BasicNameValuePair("sid", this.login_sid));
            nvps.add(new BasicNameValuePair("signature", ""));
            nvps.add(new BasicNameValuePair("verifycode", ""));
            nvps.add(new BasicNameValuePair("verifycode_cn", verify_code));
            nvps.add(new BasicNameValuePair("verifykey", verify_key));
            
        	post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
       	 	HttpResponse response = client.execute(post);
			str = EntityUtils.toString(response.getEntity(),"UTF-8");

			if (str.contains("为了保障您的邮箱安全，请输入验证码以完成本次设置"))
			{
				/*获得verify_key*/
				String regex = "\"setting\", \"(.*?)\"";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(str);
				if (m.find())
				{
					verify_key = m.group(1);
				}
				
				/*获得验证码*/
				urlv = "http://mail.qq.com/cgi-bin/getverifyimage?aid=23000101&sid="
						+this.login_sid + "&" + System.currentTimeMillis();
				
				HttpGet captcha = new HttpGet(urlv);
				HttpEntity captchaEntity = client.execute(captcha).getEntity();
				InputStream is=captchaEntity.getContent();
		        File storeFile = new File(smtp_verify_img);
		        FileOutputStream output = new FileOutputStream(storeFile); 
		        //得到网络资源的字节数组,并写入文件
	             byte b[] = new byte[1024];  
	             int j = 0;  
	             while( (j = is.read(b))!=-1){  
	                 output.write(b,0,j);  
	             }  
	            output.flush();
	            output.close(); 

	            System.out.println(uid + " 开通smtp请输入 " + smtp_verify_img + " 中的验证码");
	            verify_code = new BufferedReader(new InputStreamReader(System.in)).readLine();
	            return openSmtp(verify_code,verify_key);
			}
			else
			{
				if (str.contains("session_timeout"))
				{
					
				}
				else
				{
					if(str.contains("success"))
					{
						this.is_smtp = 1;
						result = true;
					}
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

		return result;
	}
}
