package com.yoloapps.autonitrotyper;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Ocr 
{
	private static final String apikey="";
	private String image="images/screencapture.jpg";
	private String text="no text";
	private long endTime;
	private long startTime;
	private String mode="document_scan";
	private static String URL = "https://api.idolondemand.com/1/api/sync/ocrdocument/v1";
	private static Scanner s;
	
	public Ocr()
	{
		
	}
	
	public String getTextFromImage(String path)
	{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
		    HttpPost httppost = new HttpPost(URL);
		
		    File f = new File(path);
		    FileBody fileBody = new FileBody(f);
		    StringBody apikeyStringBody = new StringBody(apikey, ContentType.TEXT_PLAIN);
		    StringBody modeStringBody = new StringBody(mode, ContentType.TEXT_PLAIN);
		
		    HttpEntity reqEntity = MultipartEntityBuilder.create()
			    .addPart("file", fileBody)
			    .addPart("apikey", apikeyStringBody)
			    .addPart("mode", modeStringBody)
			    .build();
		
		    httppost.setEntity(reqEntity);
		
		    CloseableHttpResponse response = null;
		    
		    try {
		    startTime=System.currentTimeMillis();
			response = httpclient.execute(httppost);
			endTime=System.currentTimeMillis();
			System.out.println("OCR response in: "+(endTime-startTime));
			
			HttpEntity entity = response.getEntity();
			String s=EntityUtils.toString(entity);
//			System.out.println(""+s);
			
			text=getText(s);
//			endTime=System.currentTimeMillis();
//			System.out.println("OCR response in: "+(endTime-startTime));
			
		    }catch(ClientProtocolException cpe){
			cpe.printStackTrace();
		    }catch(IOException ioe){
			ioe.printStackTrace();
		    } finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    }
		} finally {
		    try {
			httpclient.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
		return text;
	}
	
	private String getText(String s) 
	{
		int start=0;
		int stop=0;
		String text="no text";
		boolean done=false;
		boolean done2=false;
		int n=0;
		//s=s.replaceAll("\n", " ");
		while(!done&&n+8<s.length())
		{
			if(s.substring(n, n+7).equals("\"text\":"))
			{
				start=n+9;
				done=true;
				n=start;
				
				while(!done2&&n+1<s.length())
				{
					if(s.substring(n, n+2).equals("\","))
					{
						stop=n;
						done2=true;
					}
					n++;
				}
			}
			n++;
		}
		
		if(start!=0&&stop!=0);
		{
			text=s.substring(start-1, stop);
		}
		return text;
	}
}
