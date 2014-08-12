package com.lee.tomatoplayer;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class player extends Activity {
	private final String TAG = "main";
	private final int IS_PAUSE = 1;
	private final int PLAY_TO = 2;
	private final int IS_CONTINUE = 3;
	private final int FIRST_PLAY = 4;
	private final int SEEKBAR_CHANGE = 5;
	
	private SeekBar seekBar;
	private TextView duration;
	private TextView currentTime;
	private static ImageView playBtn;
	private ProgressDialog dialog;
	
	private static LinearLayout progressBar;
	private VideoView vv_video;
	private boolean isPlaying;
	private boolean firstPlay;
	private ActionBar mActionBar;
	private long waitTime = 5000;  
	private long touchTime = 0;  
	
	private Timer mTimer; 
	private TimerTask mTimerTask;
	
	private ImageView littleAD;
	private ImageView bigAD;
	private Bitmap smallAdPic;
	private Bitmap bigAdPic;
	private int litAdTime; 
	private int AdLastTime;
	private int AdAlpha = 100;
	
    private static Handler handler ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 全屏显示窗口
		mActionBar=getActionBar();
		mActionBar.hide();

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		vv_video = (VideoView) findViewById(R.id.vv_videoview);
		progressBar = (LinearLayout) findViewById(R.id.progressBar);   // 整个LinearLayout 含暂停、时间、进度条
		playBtn = (ImageView) findViewById(R.id.playBtn);				// 进度条上的播放图标
		duration = (TextView) findViewById(R.id.duration);			   // 总时长 
		currentTime = (TextView) findViewById(R.id.currentTime);		// 当前播放点
		littleAD = (ImageView) findViewById(R.id.littleAD); 			// 小广告弹窗
		bigAD = (ImageView) findViewById(R.id.bigAD); 			// 大广告弹窗
		
		// 专门用于控制主线程的UI组件
		handler = new Handler() {
	        // TODO
	        public void handleMessage(android.os.Message msg) {
	            if (msg.what == PLAY_TO) {
	        		progressBar.setVisibility(View.VISIBLE);
	        		bigAD.setVisibility(View.INVISIBLE);
	        		playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
	            } else if(msg.what == IS_PAUSE){
	            	progressBar.setVisibility(View.VISIBLE);
					playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_pause)));
					bigAD.setImageBitmap(bigAdPic);
					bigAD.setVisibility(View.VISIBLE);
					littleAD.setVisibility(View.GONE);
	            }else if(msg.what == IS_CONTINUE){
	            	progressBar.setVisibility(View.INVISIBLE);
					playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
					bigAD.setVisibility(View.INVISIBLE);
	            }else if(msg.what == FIRST_PLAY){
	            	playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
	        	}else if(msg.what == SEEKBAR_CHANGE){
	        		seekBar.setProgress(vv_video.getCurrentPosition());
	        	}
	        };
	    };
		
	    // 开始的提示框
        dialog = new ProgressDialog(this);
        dialog.setTitle("稍候");
        dialog.setMessage("正在不要命地装载中...");
        dialog.setCancelable(false);
        
        dialog.show();
        
		// prepare提前载入流媒体url地址
		Log.i(TAG, " 获取视频文件地址");
		Intent intent =getIntent();
		prepare(intent.getStringExtra("url"));
		
		// 第一次播放标志位
		firstPlay = true;
		
		// 广告设置位
		litAdTime = 10000;
		AdLastTime = 5000;
		//AdAlpha = 50;
	}
	
	// 预设置播放路径
	private void prepare(String path){
		
		final String path_tmp = path;
		littleAD.setVisibility(View.INVISIBLE);
		bigAD.setVisibility(View.INVISIBLE);
		
		new Thread(new Runnable() {
			public void run() {
				Log.i(TAG, "指定视频源路径");
				vv_video.setVideoPath(path_tmp);
				smallAdPic = loadImageFromNetwork("http://202.104.110.178:8080/picture/small.png");
				bigAdPic = loadImageFromNetwork("http://202.104.110.178:8080/picture/big.jpg");
				dialog.dismiss();
			}
		}).start();
		
	}
	
	// 时间从数字转字符串
	private String timeToStr(int mileSec){
		String s_time = String.valueOf(mileSec / 1000 / 60 / 10) + 
				   		String.valueOf(mileSec / 1000 / 60 % 10) +
				   		":" +
				   		String.valueOf(mileSec / 1000 % 60 / 10) +
				   		String.valueOf(mileSec / 1000 % 60 % 10);
		return s_time;
	}
	
	// 从网络上加载图片
	private Bitmap loadImageFromNetwork(String uri)
	{
		Bitmap bitmap=null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				byte[] data = EntityUtils.toByteArray(httpResponse
						.getEntity());
				bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	// 跳转到某一msec时刻
	protected void playTo(int msec){
		// TODO
		vv_video.seekTo(msec);
		handler.sendEmptyMessage(PLAY_TO);
	}
	
	// 播放影片进程，包含进度条更新、时间更新
	protected void play(int msec) {
		vv_video.start();
		
		// 按照初始位置播放
		vv_video.seekTo(msec);
		// 设置进度条的最大进度为视频流的最大播放时长
		seekBar.setMax(vv_video.getDuration());  
		
		// 计时器，500ms更新一次进度条的刻度
		mTimer = new Timer(); 
		mTimerTask = new TimerTask() {
			public void run() {
				
				handler.sendEmptyMessage(SEEKBAR_CHANGE);
//				seekBar.post(new Runnable() {	
//					public void run() {
//						int current = vv_video.getCurrentPosition();
//						seekBar.setProgress(current);
//					}
//				});
				
				currentTime.post(new Runnable(){
					public void run(){
						int dur = vv_video.getDuration();
						int current = vv_video.getCurrentPosition();
						duration.setText(timeToStr(dur));
						currentTime.setText(timeToStr(current));
					}
				});
				
				littleAD.post(new Runnable(){
					public void run() {
						if(vv_video.getCurrentPosition() > litAdTime && 
						   vv_video.getCurrentPosition() < (litAdTime + AdLastTime)	){
							littleAD.setVisibility(View.VISIBLE);
							littleAD.setImageBitmap(smallAdPic);
							AdAlpha += 40;
							if(AdAlpha >=250 ) AdAlpha = 250 ;
							littleAD.setAlpha(AdAlpha);
						} else{
							AdAlpha -= 40;
							if(AdAlpha <= 0) {
								AdAlpha = 0;
								littleAD.setVisibility(View.GONE);
							}
							littleAD.setAlpha(AdAlpha);
						}
					}
				}); 
				
			}
		}; 
		// 每500ms执行一次定时器任务
		mTimer.schedule(mTimerTask, 0, 500);

		// 播放完了 结束
		vv_video.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				finish();
			}
		});
		
		// 发生错误重新播放
		vv_video.setOnErrorListener(new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				play(0);
				isPlaying = false;
				return false;
			}
		});
	}
	
	// 重新播放
	protected void replay() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.seekTo(0);
			Toast.makeText(this, "重新播放", Toast.LENGTH_SHORT).show();
			return;
		}
		isPlaying = false;
		play(0);
	}
	
	// 停止播放
	protected void stop() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.stopPlayback();
			isPlaying = false;
		}
	}
	
	// 响应遥控器  定义了中键 上下左右 返回
	public boolean onKeyDown(int keyCode ,KeyEvent event){
		// 按下中键
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_CENTER ){
			if(vv_video.isPlaying()){
				vv_video.pause();
				handler.sendEmptyMessage(IS_PAUSE);
			} else{
				if(firstPlay){
					play(0);
					firstPlay = false;
					handler.sendEmptyMessage(FIRST_PLAY);
				}else{
					vv_video.start();
					handler.sendEmptyMessage(IS_CONTINUE);
				}
			}
			Log.i(TAG, " 按下中键");
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
			int current = vv_video.getCurrentPosition();
			playTo(current - 5000);
			Log.i(TAG, " 按下左键");
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
			int current = vv_video.getCurrentPosition();
			playTo(current + 5000);
			Log.i(TAG, " 按下右键");
		}
		if(keyCode == 765 ){			
			int current = vv_video.getCurrentPosition();
			playTo(current + 1000);
			Log.i(TAG, " 飞梭右");
		}
		if(keyCode == 764 ){
			int current = vv_video.getCurrentPosition();
			playTo(current - 1000);
			Log.i(TAG, " 飞梭左");
		}
		if(keyCode == KeyEvent.KEYCODE_BACK ){
			long currentTime = System.currentTimeMillis();  
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				touchTime = currentTime;  
			} else{
				MainActivity.outPlayer = true;
				finish(); 
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}