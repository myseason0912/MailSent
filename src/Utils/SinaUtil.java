package Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

public class SinaUtil {
	public static final String login_nonce = "\"nonce\":\"(.*?)\",";
	public static final String login_rsakv = "\"rsakv\":\"(.*?)\",";
	public static final String login_pubkey = "\"pubkey\":\"(.*?)\",";
	public static final String login_server_time = "\"servertime\":(.*?),";
	public static final String login_err_reason = "\"reason\":\"(.*?)\"}";
	public static final String sina_uid = "\"uid\":\"(.*?)\"}";
	public static final String send_msg = "\"msg\":\"(.*?)\",";
	public static final String send_token = "\"token\":\"(.*?)\",\"backend\"";
	
	public static String encodeAccount(String account) {
		String userName = "";
		try {
			userName = Base64.encodeBase64String(URLEncoder.encode(account,
					"UTF-8").getBytes());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return userName;
	}
}
