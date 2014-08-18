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
			//������Ϣ
			
			bundle.clear();
			try {
				//���ӷ����� ���������ӳ�ʱΪ5��
				socket = new Socket();
				socket.connect(new InetSocketAddress(ipAdr, 30000), 5000);
				//��ȡ���������
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				//��ȡ������������Ϣ
				String line = null;
				buffer="";
				while ((line = bff.readLine()) != null) {
					buffer = line + buffer;
				}
				
				//�������������Ϣ
				ou.write((message).getBytes("UTF-8"));
				ou.flush();
				Log.i("json","���ͳɹ�");
				//��ȡ�����ݷ���bundle��
				bundle.putString("msg", buffer.toString());
				//�رո������������
				bff.close();
				ou.close();
				socket.close();
			} catch (SocketTimeoutException aa) {
				//���ӳ�ʱ ��UI������ʾ��Ϣ
				bundle.putString("msg", "����������ʧ�ܣ����������Ƿ��");
				//������Ϣ �޸�UI�߳��е����
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}