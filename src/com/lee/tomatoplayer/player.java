package com.lee.tomatoplayer;

import android.app.ActionBar;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.VideoView;


public class player extends Activity {
	private final String TAG = "main";
	private SeekBar seekBar;
	private VideoView vv_video;
	private boolean isPlaying;
	private boolean firstPlay;
	private ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 全屏显示窗口
		mActionBar=getActionBar();
		mActionBar.hide();

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		vv_video = (VideoView) findViewById(R.id.vv_videoview);

		// 为进度条添加进度更改事件
		seekBar.setOnSeekBarChangeListener(change);
		// prepare提前载入流媒体url地址
		prepare("http://202.104.110.178:8080/video/genius.mp4");
		
	}
	
	private OnSeekBarChangeListener change = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// 当进度条停止修改的时候触发
			// 取得当前进度条的刻度
			int progress = seekBar.getProgress();
			if (vv_video != null && vv_video.isPlaying()) {
				// 设置当前播放的位置
				vv_video.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};
	
	protected void prepare(String path){
		Log.i(TAG, " 获取视频文件地址");
		//path = "http://202.104.110.178:8080/video/genius.mp4";
		
		Log.i(TAG, "指定视频源路径");
		vv_video.setVideoPath(path);//file.getAbsolutePath()
		
		firstPlay = true;
	}
	
	protected void play(int msec) {

		Log.i(TAG, "开始播放");
		vv_video.start();
		
		// 按照初始位置播放
		vv_video.seekTo(msec);
		// 设置进度条的最大进度为视频流的最大播放时长
		seekBar.setMax(vv_video.getDuration());

		// 开始线程，更新进度条的刻度
		new Thread() {
			@Override
			public void run() {
				try {
					isPlaying = true;
					while (isPlaying) {
						// 如果正在播放，没0.5.毫秒更新一次进度条
						int current = vv_video.getCurrentPosition();
						seekBar.setProgress(current);

						sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		vv_video.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {

			}
		});

		vv_video.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// 发生错误重新播放
				play(0);
				isPlaying = false;
				firstPlay = false;
				return false;
			}
		});
	}
	
	protected void replay() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.seekTo(0);
			Toast.makeText(this, "重新播放", 0).show();
			return;
		}
		isPlaying = false;
		firstPlay = false;
		play(0);
	}
	
	protected void stop() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.stopPlayback();
			isPlaying = false;
		}
	}
	
	public boolean onKeyDown(int keyCode ,KeyEvent event){
		
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_CENTER ){
			if(vv_video.isPlaying()){
				vv_video.pause();
				Toast.makeText(this, "暂停播放", 0).show();
			} else{
				if(firstPlay){
					play(0);
					firstPlay = false;
				}		
				else{
					vv_video.start();
					Toast.makeText(this, "继续播放", 0).show();
				}
			}
			Log.i(TAG, " 按下enter");
			return true;
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
			int current = vv_video.getCurrentPosition();
			play(current - 5000);
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
			int current = vv_video.getCurrentPosition();
			play(current + 5000);
		}
		if(keyCode == 765 ){
			int current = vv_video.getCurrentPosition();
			play(current + 1000);
		}
		if(keyCode == 764 ){
			int current = vv_video.getCurrentPosition();
			play(current - 1000);
		}
		return super.onKeyDown(keyCode, event);
	}
}