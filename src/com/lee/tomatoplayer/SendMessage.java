package com.lee.tomatoplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Bundle;
import android.util.Log;

class SendMessage extends Thread {
		private Socket socket = null;
		private String buffer = "";
		public String message;
		public String ipAdr;
		public Bundle bundle = new Bundle();

		public SendMessage(String message,String ipAdr) {
			this.message = message;
			this.ipAdr = ipAdr;
		}
		
		public String getBundleMessage(){
			return bundle.getString("msg");
		}

		@Override
		public void run() {
			//定义消息
			
			bundle.clear();
			try {
				//连接服务器 并设置连接超时为5秒
				socket = new Socket();
				socket.connect(new InetSocketAddress(ipAdr, 30000), 5000);
				//获取输入输出流
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				//读取发来服务器信息
				String line = null;
				buffer="";
				while ((line = bff.readLine()) != null) {
					buffer = line + buffer;
				}
				
				//向服务器发送信息
				ou.write((message).getBytes("UTF-8"));
				ou.flush();
				Log.i("json","发送成功");
				//读取的数据放在bundle中
				bundle.putString("msg", buffer.toString());
				//关闭各种输入输出流
				bff.close();
				ou.close();
				socket.close();
			} catch (SocketTimeoutException aa) {
				//连接超时 在UI界面显示消息
				bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
				//发送消息 修改UI线程中的组件
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}