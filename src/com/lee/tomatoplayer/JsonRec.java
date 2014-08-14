package com.lee.tomatoplayer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Socket客户端
 * 功能为：发送电影名称字符串到服务器端，接受服务器端的反馈，并输出JsonArray
 */
public class JsonRec {
	
	private int adNum;
	private JSONArray jsonArray = new JSONArray();
	private final String SIGN="OK"; //发给服务器的确认消息
	
	private String serverIP;
	private int port;
	
	// JsonRec构造函数
	public JsonRec(String serverIP, int port ){
		this.serverIP = serverIP;
		this.port = port;
	}
	
	public void connect(String filmName) {
		// TODO Auto-generated constructor stub
       Socket socket = null;
       InputStream is = null;
       OutputStream os = null;
       
       try {
           //建立连接
           socket = new Socket(serverIP,port);
           //发送数据   先将filmName转为byte，再写入outputStream发送给服务器
           os = socket.getOutputStream();
           byte[] filmName_byte = filmName.getBytes("UTF-8");
           os.write(filmName_byte);
           
           // 目标： 获取广告条数
           //接收数据  从inputStream 读取1024个字节,返回读取的字节数
           is = socket.getInputStream();
           byte[] buffer_tmp = new byte[1024];
           int byte_num  = is.read(buffer_tmp);
           //输出反馈数据
           String recString=new String(buffer_tmp, 0 , byte_num);
           System.out.println("服务器反馈:电影中插入广告数量为"+recString);
           //将String类转为int类,即广告条数，且分adNum次接收
           adNum = Integer.valueOf(recString).intValue();
           
           for (int i=0;i<adNum ;i++){
        	   byte[] buf = new byte[1024];
        	   int byte_n = is.read(buf);
        	   
        	   // 打印每一条广告
	     	   String str_tmp = new String(buf,0,byte_n);
	     	   System.out.println(str_tmp + "\n");
	     	   
	     	   // string转为jsonObj,再组成jsonArray
	     	   JSONObject jsonObj=new JSONObject(str_tmp);
	     	   jsonArray.put(jsonObj);
	 		   //发给服务器的确认信息
	 		   os.write(SIGN.getBytes("UTF-8"));
           }
           
       }catch (Exception error) {
           error.printStackTrace(); //打印异常信息
       }finally{
           try {
                    //关闭流和连接
                    is.close();
                    os.close();
                    socket.close();
           } catch (Exception error) {
        	   error.printStackTrace(); //打印异常信息
           }
       }
	}
	
	public int getAdNum(){
		return adNum;
	}
	
	public JSONArray getJsonArray(){
		return jsonArray;
	}
	
}