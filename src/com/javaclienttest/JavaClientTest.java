package com.javaclienttest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


// Code 413 is request to large
public class JavaClientTest
{
  static final String rootpath = "C:\\Users\\Aname\\Desktop\\";
  //static final String bFileName = "byteFile.b";
  //static final String bFileName = "OneMoreTime.flac";
  static final String bFileName = "HeyBoyHeyGirl.flac";
  static final String LOCAL_APP_ENGINE_SERVER_URL = "http://127.0.0.1:8888";
  static final String DEFAULT_ROOT_URL = "http://8261.imlctest.appspot.com";

  static final String serverPath = "/mydesk/cloud";
  static final int bufferSize = 10240;
  static final boolean LOCAL_ANDROID_RUN = false;
  
  public static void main(String[] args)
  { 
    HttpURLConnection conn = null;
    BufferedInputStream file = null;
    try
    {
      // Open file, create buffered stream
      File fileInfo = new File(rootpath + bFileName);
      file = new BufferedInputStream(new FileInputStream(fileInfo), bufferSize);
      
      // Set connection properties and open connection to server
      URL serverUrl = new URL(getFileServeUrl());
      conn = (HttpURLConnection)serverUrl.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      //conn.setChunkedStreamingMode(bufferSize);
      //conn.setFixedLengthStreamingMode(16);      
      OutputStream out = conn.getOutputStream();

      // Send file to server
      byte [] buffer = new byte [bufferSize];
      int length, count = 0;
      while((length = file.read(buffer)) > 0)
      {
        out.write(buffer, 0, length);
        count++;
      }
      System.out.println("Count : " + count);
      int http_status = conn.getResponseCode();      
      System.out.println("Status Code: " + http_status);
      
      if(http_status/100 != 2)
      {  throw new IOException("Bad status"); }      
    }
    catch( MalformedURLException e )
    {  e.printStackTrace();  }
    catch( IOException e )
    {  e.printStackTrace();  }
    finally
    {
      if(conn != null)
      {  conn.disconnect();  }
      if(file != null)
      {  
        try
        {  file.close();  }
        catch( IOException e )
        { }  
      }
    }
  }
  
  //===========================================================================
  // Return a URL pointing to the BlobServer servlet
  static public String getFileServeUrl()
  {
    String fileUrl;
    if(LOCAL_ANDROID_RUN)
    {  
      fileUrl = LOCAL_APP_ENGINE_SERVER_URL + serverPath;
    }
    else
    {
      fileUrl = DEFAULT_ROOT_URL + serverPath;
    }
    return fileUrl;
  }
  
  //===========================================================================
  // Verify the binary file worked
  void verifyFile()
  {
    FileInputStream file;
    try
    {
      File fileInfo = new File(rootpath + bFileName);
      int length = (int)fileInfo.length();
      
      file = new FileInputStream(rootpath + bFileName);
      byte [] data;
      data = new byte [length];
      file.read(data);
      int i=0;
      for(byte b: data)
      {
        System.out.print(""+ b + " ");
        if((i % 4) == 3)
        { System.out.println();  }
        i++;
        file.close();
      }      
      System.out.println("Done");
    }
    catch( FileNotFoundException e )
    {
      System.out.println("File not found");
      e.printStackTrace();
    }
    catch( IOException e )
    {
      System.out.println("IOException");
      e.printStackTrace();
    }    
  }
}
