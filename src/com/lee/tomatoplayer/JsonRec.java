package com.lee.tomatoplayer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Socket�ͻ���
 * ����Ϊ�����͵�Ӱ�����ַ������������ˣ����ܷ������˵ķ����������JsonArray
 */
public class JsonRec {
	
	private int adNum;
	private JSONArray jsonArray = new JSONArray();
	private final String SIGN="OK"; //������������ȷ����Ϣ
	
	private String serverIP;
	private int port;
	
	// JsonRec���캯��
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
           //��������
           socket = new Socket(serverIP,port);
           //��������   �Ƚ�filmNameתΪbyte����д��outputStream���͸�������
           os = socket.getOutputStream();
           byte[] filmName_byte = filmName.getBytes("UTF-8");
           os.write(filmName_byte);
           
           // Ŀ�꣺ ��ȡ�������
           //��������  ��inputStream ��ȡ1024���ֽ�,���ض�ȡ���ֽ���
           is = socket.getInputStream();
           byte[] buffer_tmp = new byte[1024];
           int byte_num  = is.read(buffer_tmp);
           //�����������
           String recString=new String(buffer_tmp, 0 , byte_num);
           System.out.println("����������:��Ӱ�в���������Ϊ"+recString);
           //��String��תΪint��,������������ҷ�adNum�ν���
           adNum = Integer.valueOf(recString).intValue();
           
           for (int i=0;i<adNum ;i++){
        	   byte[] buf = new byte[1024];
        	   int byte_n = is.read(buf);
        	   
        	   // ��ӡÿһ�����
	     	   String str_tmp = new String(buf,0,byte_n);
	     	   System.out.println(str_tmp + "\n");
	     	   
	     	   // stringתΪjsonObj,�����jsonArray
	     	   JSONObject jsonObj=new JSONObject(str_tmp);
	     	   jsonArray.put(jsonObj);
	 		   //������������ȷ����Ϣ
	 		   os.write(SIGN.getBytes("UTF-8"));
           }
           
       }catch (Exception error) {
           error.printStackTrace(); //��ӡ�쳣��Ϣ
       }finally{
           try {
                    //�ر���������
                    is.close();
                    os.close();
                    socket.close();
           } catch (Exception error) {
        	   error.printStackTrace(); //��ӡ�쳣��Ϣ
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