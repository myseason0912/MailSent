package Account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.protocol.HTTP;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import Mail.MailReceiver;
import Mail.MailTemplate;
import Mail.MailTitle;
import Utils.SinaUtil;
import Utils.SinaSSOEncoder;
import Utils.UnicodeDecoder;

public class SinaMailAccount extends MailAccount{
	
	protected String token = "";

	public SinaMailAccount()
	{
		client.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, this.connect_time_out);
	}

	public SinaMailAccount(String username,String pass,int type,String smtp_host,String pop3_host)
	{
		this.username = username;
		this.password = pass;
		this.type = type;
		this.smtp_host = smtp_host;
		this.pop3_host = pop3_host;
		this.status = AccountUtil.status_normal;
		client.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, this.connect_time_out);
	}
	
	public boolean login()
	{
		boolean result = false;
		
		String str = "";
		String urlv = "";
		String nonce = "";
		String rsakv = "";
		String pubkey = "";
		String server_time = "";
		String encode_name = SinaUtil.encodeAccount(username);
	    Pattern p;
	    Matcher m;
		try
		{
			urlv = "http://login.sina.com.cn/sso/prelogin.php?entry=cnmail"
					+ "&callback=sinaSSOController.preloginCallBack"
					+ "&su=" + encode_name + 
					"&rsakt=mod&client=ssologin.js(v1.4.7)&_=" + System.currentTimeMillis();
			
			HttpGet check = new HttpGet(urlv);
			HttpResponse response = client.execute(check);
			str = EntityUtils.toString(response.getEntity());
			
			/*获得nonce*/
			p = Pattern.compile(SinaUtil.login_nonce);  
	        m = p. matcher(str);  
	        if (m.find())
	        {
	        	nonce = m.group(1);
	        }
	        
			/*获得rsakv*/
			p = Pattern.compile(SinaUtil.login_rsakv);  
	        m = p. matcher(str);  
	        if (m.find())
	        {
	        	rsakv = m.group(1);
	        }
	        
			/*获得pubkey*/
			p = Pattern.compile(SinaUtil.login_pubkey);  
	        m = p. matcher(str);  
	        if (m.find())
	        {
	        	pubkey = m.group(1);
	        }
	        
			/*获得server time*/
			p = Pattern.compile(SinaUtil.login_server_time);  
	        m = p. matcher(str);  
	        if (m.find())
	        {
	        	server_time = m.group(1);
	        }
	        
	        /*登录*/
	        urlv = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.7)";
	        
	        HttpPost post = new HttpPost(urlv);
	        /*post data*/
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	        nvps.add(new BasicNameValuePair("callback", "parent.sinaSSOController.loginCallBack"));
	        nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
			nvps.add(new BasicNameValuePair("entry", "cnmail"));
			nvps.add(new BasicNameValuePair("from", ""));
			nvps.add(new BasicNameValuePair("gateway", "1"));
			nvps.add(new BasicNameValuePair("nonce", nonce));
			nvps.add(new BasicNameValuePair("pagerefer", ""));
			nvps.add(new BasicNameValuePair("prelt", "36"));
			nvps.add(new BasicNameValuePair("pwencode", "rsa2"));
			nvps.add(new BasicNameValuePair("returntype", "IFRAME"));
			nvps.add(new BasicNameValuePair("rsakv", rsakv));
			nvps.add(new BasicNameValuePair("savestate", "30"));
			nvps.add(new BasicNameValuePair("servertime", server_time));
			nvps.add(new BasicNameValuePair("service", "sso"));
			nvps.add(new BasicNameValuePair("setdomain", "1"));
			nvps.add(new BasicNameValuePair("sp", SinaSSOEncoder.rsaCrypt(pubkey, "10001", 
												server_time + "\t" + nonce + "\n"+ password)));
			nvps.add(new BasicNameValuePair("su",encode_name));
			nvps.add(new BasicNameValuePair("useticket", "0"));
			
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			response = client.execute(post);
			str = EntityUtils.toString(response.getEntity());
			
			if (str.contains("\"retcode\":0"))
			{
				/*获得uid*/
				p = Pattern.compile(SinaUtil.sina_uid);  
		        m = p. matcher(str);  
		        if (m.find())
		        {
		        	this.uid = m.group(1);
		        }
		       
		        /*二次登录*/
		        urlv ="http://mail.sina.com.cn/cgi-bin/login.php?a="
		        		+ System.currentTimeMillis() + "&b=" + System.currentTimeMillis();
		        
		        HttpGet login = new HttpGet(urlv);
				response = client.execute(login);
				str = EntityUtils.toString(response.getEntity());
				
				 /*获得token*/
		        urlv ="http://m0.mail.sina.com.cn/classic/index.php";
	        
		        HttpGet token = new HttpGet(urlv);
		        response = client.execute(token);
		        str = EntityUtils.toString(response.getEntity());

				p = Pattern.compile(SinaUtil.send_token);  
		        m = p. matcher(str);  
		        if (m.find())
		        {
		        	this.token = m.group(1);
		        	result =true;
		        	this.is_login = 1;
		        }
		        else
		        {
		        	System.out.println("用户名： " + this.username  + " 密码： "+ this.password + " 获取token失败.");
		        }
		    
			}
			else
			{
				p = Pattern.compile(SinaUtil.login_err_reason);  
		        m = p. matcher(str);  
		        String reason = "";
		        if (m.find())
		        {
		        	reason = m.group(1);
		        }		        
		        reason = UnicodeDecoder.decode(reason);
		       
				System.out.println("用户名： " + this.username  + " 密码： "+ this.password + " " + reason);

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

	
	public int senWebMail(MailReceiver receiver,MailTitle title,MailTemplate template)
	{
		//TODO token和编码
		int result = 0;
		String str = "";
		String urlv = "http://m0.mail.sina.com.cn/classic/send.php?ts=" 
						+ System.currentTimeMillis();
		try
		{
			HttpPost post = new HttpPost(urlv);
			MultipartEntity reqEntity = new MultipartEntity();  
			reqEntity.addPart("nickname", new StringBody(""));  
			reqEntity.addPart("sender", new StringBody(this.username));  
			reqEntity.addPart("issend", new StringBody("1"));  
			reqEntity.addPart("savedraft", new StringBody("0"));  
			reqEntity.addPart("youxj", new StringBody("3"));  
			reqEntity.addPart("type", new StringBody("html"));  
			reqEntity.addPart("authcode", new StringBody(""));  
			reqEntity.addPart("fid", new StringBody(""));  
			reqEntity.addPart("mid", new StringBody(""));  
			reqEntity.addPart("sinafriends", new StringBody("0"));  
			reqEntity.addPart("token", new StringBody(this.token));  
			reqEntity.addPart("paperid", new StringBody("0"));  
			reqEntity.addPart("bigfile", new StringBody(""));  
			reqEntity.addPart("bigfile_old", new StringBody(""));  
			reqEntity.addPart("sc", new StringBody("0"));  
			reqEntity.addPart("sendtime", new StringBody(""));  
			reqEntity.addPart("cf_mid", new StringBody(""));  
			reqEntity.addPart("encode", new StringBody("0"));  
			reqEntity.addPart("att_swf", new StringBody(""));  
			reqEntity.addPart("from", new StringBody(this.username));  
			reqEntity.addPart("to", new StringBody(receiver.getAddr()));  
			reqEntity.addPart("cc", new StringBody(""));  
			reqEntity.addPart("bcc", new StringBody("")); 
			reqEntity.addPart("subj", new StringBody(title.getTitle()));  
			reqEntity.addPart("atth0", new StringBody("")); 
			reqEntity.addPart("atth1", new StringBody(""));  
			reqEntity.addPart("msgtxt", new StringBody(template.getContent())); 
			reqEntity.addPart("snt", new StringBody("2")); 
      
			post.setEntity(reqEntity);  

			post.addHeader("Referer", "http://m0.mail.sina.com.cn/classic/index.php");
			
			HttpResponse response = client.execute(post);
			str = EntityUtils.toString(response.getEntity(), "UTF-8");

		    Pattern p;
		    Matcher m;
			/*获得msg*/
			p = Pattern.compile(SinaUtil.send_msg);  
	        m = p. matcher(str);  
	        if (m.find())
	        {
	        	String msg = m.group(1);
	        	msg = UnicodeDecoder.decode(msg);
	        	if(msg.contains("发送成功"))
	        	{
	        		result = 1;
	        	}	
				System.out.println("发送人： " + this.username + " 收信人： " + receiver.getAddr() + " " + title + " " + msg);
	        }
	        else
	        {
	        	result = 0;
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

}
