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
        // 在Binder中定义一个自定义的接口用于数据交互
        // 这里直接把当前的服务传回给宿主
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
        // 绑定服务，把当前服务的IBinder对象的引用传递给宿主
        return mBinder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service is Unbinded");
        return true;
    }
    
    public int getMultipleNum(int num){
        // 定义一个方法 用于数据交互
        return num*10;
    }
}