package com.lee.tomatoplayer;

import java.util.Timer;
import java.util.TimerTask;
import android.app.ActionBar;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
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
	private SeekBar seekBar;
	private TextView duration;
	private TextView currentTime;
	private ImageView playBtn;
	private LinearLayout progressBar;
	private VideoView vv_video;
	private boolean isPlaying;
	private boolean firstPlay;
	private ActionBar mActionBar;
	private long waitTime = 2000;  
	private long touchTime = 0;  
	
	private Timer mTimer; 
	private TimerTask mTimerTask;
	
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

		// prepare提前载入流媒体url地址
		Log.i(TAG, " 获取视频文件地址");
		prepare("http://202.104.110.178:8080/video/genius.mp4");
		
		// 第一次播放标志位
		firstPlay = true;
		
	}
	
	// 预设置播放路径
	private void prepare(String path){
		
		Log.i(TAG, "指定视频源路径");
		vv_video.setVideoPath(path);//file.getAbsolutePath()
		
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
				seekBar.post(new Runnable() {	
					public void run() {
						int current = vv_video.getCurrentPosition();
						int dur = vv_video.getDuration();
						duration.setText(timeToStr(dur));
						currentTime.setText(timeToStr(current));
						seekBar.setProgress(current);
					}
				});
			}
		};
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
		
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_CENTER ){
			if(vv_video.isPlaying()){
				vv_video.pause();
				playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_pause)));
				progressBar.setVisibility(View.VISIBLE);
			} else{
				if(firstPlay){
					play(0);
					firstPlay = false;
				}else{
					vv_video.start();
					playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
					progressBar.setVisibility(View.INVISIBLE);
				}
				
			}
			Log.i(TAG, " 按下中键");
			return true;
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
			int current = vv_video.getCurrentPosition();
			play(current - 5000);
			progressBar.setVisibility(View.VISIBLE);
			playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
			Log.i(TAG, " 按下左键");
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
			int current = vv_video.getCurrentPosition();
			play(current + 5000);
			progressBar.setVisibility(View.VISIBLE);
			playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
			Log.i(TAG, " 按下右键");
		}
		if(keyCode == 765 ){			
			int current = vv_video.getCurrentPosition();
			play(current + 1000);
			progressBar.setVisibility(View.VISIBLE);
			playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
			Log.i(TAG, " 飞梭右");
		}
		if(keyCode == 764 ){
			int current = vv_video.getCurrentPosition();
			play(current - 1000);
			progressBar.setVisibility(View.VISIBLE);
			playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
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
				//System.exit(0);  
			}
			return true;  
		}
		return super.onKeyDown(keyCode, event);
	}
}