package Utils;


public class QQUtil {
	
	public static final String login_msg = "\'(.*?)\'";
	public static final int check_field_num = 3;
	public static final int login_result_field_num = 6;
	public static final int login_err_field = 4;
	public static final int login_nick_field = 5;
	public static final int login2_url_field = 2;
	public static final String login_mail_key = "[name: qm_antisky][value: ";
	public static final String login_sid = "sid=";
	public static final String login_r = "&r=";
	public static final String mail_hidden_value = "value=\"virtual\" />";
	public static final String mail_para_value = "name=\"(.*?)\" value=\"(.*?)\" /";
	public static final String mail_sent_msg = "errmsg : \"(.*?)\\";
	
	
	public static String hexchar2bin(String s)
	{
	    int slen = s.length();
	    int i = 0;
	    String r = "";
	    while( i < slen)
	    {
	        r = r + (char)(Integer.parseInt(s.substring(i, i+2),16));
	        i = i + 2;
	    }
	    return r;
	}
	
	public static String getPass(String p,String v,String uni)
	{
	    MD5 md5 = new MD5();
	    p = hexchar2bin(md5.calcMD5(p));
	    uni = hexchar2bin(uni.replace("\\x","").toUpperCase());
	    p = md5.calcMD5(p + uni);
	    p = md5.calcMD5(p + v.toUpperCase());		
		return p;
	}
	

}
