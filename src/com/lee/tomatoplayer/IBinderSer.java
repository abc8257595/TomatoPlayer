package com.lee.tomatoplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class IBinderSer extends Service {
    private final String TAG="testmax";
    public  final IBinder mBinder=new LocalBinder();
    
    public class LocalBinder extends Binder{
        // ��Binder�ж���һ���Զ���Ľӿ��������ݽ���
        // ����ֱ�Ӱѵ�ǰ�ķ��񴫻ظ�����
        public IBinderSer getService(){
            return IBinderSer.this;
        }                
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service is Created");
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "The service is binding!");
        // �󶨷��񣬰ѵ�ǰ�����IBinder��������ô��ݸ�����
        return mBinder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service is Unbinded");
        return true;
    }
    
    public int getMultipleNum(int num){
        // ����һ������ �������ݽ���
        return num*10;
    }
}