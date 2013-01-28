package com.javaclienttest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ClientAuthTest
{
  public static final boolean LOCAL_ANDROID_RUN = false;
  public static final String LOCAL_APP_ENGINE_SERVER_URL = "http://127.0.0.1:8888";

  public static final String DEFAULT_ROOT_URL = "http://30.mydesk-cloud.appspot.com";
  public static final String testUrl = "/mydesk_cloud/test/user";
  
  public static final int buffSize = 10240;
  
  public static void main(String[] args)
  {
    HttpURLConnection conn = null;
    // Test for authentication
    try
    {
      URL serverUrl = new URL(getFullUrl());
      conn = (HttpURLConnection)serverUrl.openConnection();
      conn.setDoInput(true);
      conn.setRequestMethod("GET");
            
      InputStreamReader in = new InputStreamReader(conn.getInputStream(), "UTF-8");
      System.out.println("Encoding: " + in.getEncoding());
      BufferedReader reader = new BufferedReader(in);
      
      char [] buffer = new char[buffSize];      
      StringBuilder str = new StringBuilder();
      
      int result = 0;
      do
      { 
        result = reader.read(buffer, 0, buffSize);
        str.append(buffer);  
      }  
      while(result > 0);
            
      int http_status = conn.getResponseCode();      
      System.out.println("Status Code: " + http_status);
      if(http_status/100 != 2) // Status codes not 2XX are bad
      {
        System.out.println("Error Status Code: " + http_status);
        throw new IOException("Bad status"); 
      }
      
      System.out.println("Connection Successful:");
      System.out.println(str.toString());      
    }
    catch( MalformedURLException e )
    {  e.printStackTrace();  }
    catch( IOException e )
    {  e.printStackTrace();  }
    finally
    {
      if(conn != null)
      {  conn.disconnect();  }
    }    
  }
  
  static public String getFullUrl()
  {
    String fileUrl;
    if(LOCAL_ANDROID_RUN)
    {  fileUrl = LOCAL_APP_ENGINE_SERVER_URL + testUrl;  }
    else
    {  fileUrl = DEFAULT_ROOT_URL + testUrl;  }
    return fileUrl;
  }
}
