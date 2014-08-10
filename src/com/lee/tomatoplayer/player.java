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
	private ActionBar mActionBar;
	private long waitTime = 2000;  
	private long touchTime = 0;  
	
	private Timer mTimer; 
	private TimerTask mTimerTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// ȫ����ʾ����
		mActionBar=getActionBar();
		mActionBar.hide();

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		vv_video = (VideoView) findViewById(R.id.vv_videoview);
		progressBar = (LinearLayout) findViewById(R.id.progressBar);
		duration = (TextView) findViewById(R.id.duration);
		currentTime = (TextView) findViewById(R.id.currentTime);
		playBtn = (ImageView) findViewById(R.id.playBtn);

		// prepare��ǰ������ý��url��ַ
		prepare("http://202.104.110.178:8080/video/genius.mp4");
		play(0);
		
	}
	
	protected void prepare(String path){
		Log.i(TAG, " ��ȡ��Ƶ�ļ���ַ");
		//path = "http://202.104.110.178:8080/video/genius.mp4";
		
		Log.i(TAG, "ָ����ƵԴ·��");
		vv_video.setVideoPath(path);//file.getAbsolutePath()
		
	}
	
	protected void play(int msec) {

		Log.i(TAG, "��ʼ����");
		vv_video.start();
		
		// ���ճ�ʼλ�ò���
		vv_video.seekTo(msec);
		// ���ý�������������Ϊ��Ƶ������󲥷�ʱ��
		seekBar.setMax(vv_video.getDuration());
		String s_duration = String.valueOf(vv_video.getDuration() / 1000 / 60) + ":" +
							String.valueOf(vv_video.getDuration() / 1000 % 60);
		duration.setText(s_duration);
		
		int current = vv_video.getCurrentPosition();
		String s_current = String.valueOf(current / 1000 / 60) + ":" +
				String.valueOf(current / 1000 % 60);
		currentTime.setText(s_current);
		
		// ��ʼ�̣߳����½������Ŀ̶�
		mTimer = new Timer(); 
		mTimerTask = new TimerTask() {
			public void run() {
				int current = vv_video.getCurrentPosition();
				seekBar.setProgress(current);
			}
		};
		mTimer.schedule(mTimerTask, 0, 500);
		
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					isPlaying = true;			
//					while (isPlaying) {
//						// ������ڲ��ţ�û0.5.�������һ�ν�����
//						int current = vv_video.getCurrentPosition();
//						seekBar.setProgress(current);
////						String s_current = String.valueOf(current / 1000 / 60) + ":" +
////								String.valueOf(current / 1000 % 60);
////						currentTime.setText("test");
//						sleep(500);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();

		vv_video.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {

			}
		});

		vv_video.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// �����������²���
				play(0);
				isPlaying = false;
				return false;
			}
		});
	}
	
	protected void replay() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.seekTo(0);
			Toast.makeText(this, "���²���", Toast.LENGTH_SHORT).show();
			return;
		}
		isPlaying = false;
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
				playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_pause)));
				progressBar.setVisibility(View.VISIBLE);
			} else{
				vv_video.start();
				playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
				progressBar.setVisibility(View.INVISIBLE);
			}
			Log.i(TAG, " �����м�");
			return true;
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
			int current = vv_video.getCurrentPosition();
			play(current - 5000);
			progressBar.setVisibility(View.VISIBLE);
			Log.i(TAG, " �������");
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
			int current = vv_video.getCurrentPosition();
			play(current + 5000);
			progressBar.setVisibility(View.VISIBLE);
			Log.i(TAG, " �����Ҽ�");
		}
		if(keyCode == 765 ){			
			int current = vv_video.getCurrentPosition();
			play(current + 1000);
			progressBar.setVisibility(View.VISIBLE);
			Log.i(TAG, " ������");
		}
		if(keyCode == 764 ){
			int current = vv_video.getCurrentPosition();
			play(current - 1000);
			progressBar.setVisibility(View.VISIBLE);
			Log.i(TAG, " ������");
		}
		if(keyCode == KeyEvent.KEYCODE_BACK ){
			long currentTime = System.currentTimeMillis();  
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
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