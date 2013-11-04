package Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.HttpClient;

import Account.Mail163Account;

public class Mail163Util {
	
	public static final String[] gz = {"gzt", "gzc", "gze"};
	public static final String[] hz = {"hz"};
	public static final String[] bj = {"t", "c", "e"};
	public static final String[] net = {"t : 电信,","c :联通","e : 教育网"};
	public String[] aSpdQueue = {"t","c","e"};
	public String[] aSpdResult = {"-2","-2","-2","db"};
	public String sLocationInfo = "failed";
	public long[] aSpdStartTime = new long[4];
	public long[] aSpdEndTime = new long[4];
	public long[] aSpdStartTimeUser = new long[4];
	public boolean bSpdAuto = true;

	public  void fSpeedTestPre(HttpClient client,String area)
	{
		int nSpdRndPosition = (int)(Math.random() * 100);
		aSpdQueue = gz;
		aSpdResult[3] = "gz";
		if (nSpdRndPosition <= 33)
		{
			aSpdQueue = hz;
			aSpdResult[3] = "hz";
		}
		if(nSpdRndPosition >33 && nSpdRndPosition <= 66)
		{
			aSpdQueue = bj;
			aSpdResult[3] = "bj";
		}
		if (area.equalsIgnoreCase("gz"))
		{
			aSpdQueue = gz;
			aSpdResult[3] = "gz";
		}
		if (area.equalsIgnoreCase("hz"))
		{
			aSpdQueue = hz;
			aSpdResult[3] = "hz";
		}
		if (area.equalsIgnoreCase("bj"))
		{
			aSpdQueue = bj;
			aSpdResult[3] = "bj";
		}
		String urlv = "";
		String str = "";
		try
		{
			for( int i=0;i<aSpdQueue.length;i++){
					String sLoca = aSpdQueue[i];
					urlv = "http://"+ sLoca +"p.127.net/cte/" + sLoca + "test?" + System.currentTimeMillis();
					HttpGet check = new HttpGet(urlv);
					HttpResponse response = client.execute(check);
					str = EntityUtils.toString(response.getEntity());
			}
		}
		catch(Exception e)
		{
			fNetErrDebug("ErrfSpeedTestPre");	
		}
	}
	
	public void fSpdUserInit()
	{
		aSpdResult[0] = "-3";
		aSpdResult[1] = "-3";
		aSpdResult[2] = "-3";
		aSpdResult[3] = "db";
		bSpdAuto = false;
		
	}
	
	public void fNetErrDebug(String sStep)
	{
		if(sLocationInfo.contains("err")){
			String sFlow = '-' + sStep;
			aSpdResult[3] += sFlow;
		}
	}
	
	public void fSpeedTest(HttpClient client,int nCount)
	{
		long nRnd = 0;
		if (bSpdAuto)
		{
			fNetErrDebug("fSpeedTest" + nCount);
			aSpdStartTime[nCount] = System.currentTimeMillis();
			nRnd = aSpdStartTime[nCount];
		}
		else
		{
			aSpdStartTimeUser[nCount] =System.currentTimeMillis();
			nRnd = aSpdStartTimeUser[nCount];
		}
		String sLoca = aSpdQueue[nCount];
		try
		{
			String urlv = "http://"+ sLoca +"p.127.net/cte/" + sLoca +"p?" + nRnd;
			HttpGet check = new HttpGet(urlv);
			HttpResponse response = client.execute(check);
			String str = EntityUtils.toString(response.getEntity());
			if (bSpdAuto)
			{
				aSpdResult[nCount] = "-1";
			}
		}
		catch(Exception e)
		{
			fNetErrDebug("ErrfSpeedTest" + nCount);	
		}
	}
	
	public void fSpd(int nCount)
	{
		if(bSpdAuto){
			fNetErrDebug("Spd" + nCount);
			aSpdEndTime[nCount] = System.currentTimeMillis();
			long gap = aSpdEndTime[nCount] - aSpdStartTime[nCount];
			aSpdResult[nCount] = String.valueOf(gap);
		}
	}
	
	public String getRace(HttpClient client)
	{
		//fSpdUserInit();
		String urlv = "http://iplocator.mail.163.com/iplocator?callback=fGetLocator";
		try
		{
			HttpGet check = new HttpGet(urlv);
			HttpResponse response = client.execute(check);
			String str = EntityUtils.toString(response.getEntity(),"UTF-8");
			System.out.println(str);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		fSpeedTestPre(client,"bj");
		for (int i = 0;i < 3;i++)
		{
			fSpeedTest(client,i);		
		}

		for (int i = 0;i < 3;i++)
		{
			fSpd(i);		
		}
		return aSpdResult[1]+"_"+aSpdResult[0]+"_"+aSpdResult[2]+"_"+aSpdResult[3];
	}
}
