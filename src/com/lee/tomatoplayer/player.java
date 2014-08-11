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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	
	private ImageView littleAD;
	private ImageView bigAD;
	private Bitmap smallAdPic;
	private Bitmap bigAdPic;
	private int litAdTime; 
	private int AdLastTime;
	private int AdAlpha = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// ȫ����ʾ����
		mActionBar=getActionBar();
		mActionBar.hide();

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		vv_video = (VideoView) findViewById(R.id.vv_videoview);
		progressBar = (LinearLayout) findViewById(R.id.progressBar);   // ����LinearLayout ����ͣ��ʱ�䡢������
		playBtn = (ImageView) findViewById(R.id.playBtn);				// �������ϵĲ���ͼ��
		duration = (TextView) findViewById(R.id.duration);			   // ��ʱ�� 
		currentTime = (TextView) findViewById(R.id.currentTime);		// ��ǰ���ŵ�
		littleAD = (ImageView) findViewById(R.id.littleAD); 			// С��浯��
		bigAD = (ImageView) findViewById(R.id.bigAD); 			// ���浯��
		
		// prepare��ǰ������ý��url��ַ
		Log.i(TAG, " ��ȡ��Ƶ�ļ���ַ");
		prepare("http://202.104.110.178:8080/video/����.mp4");
		
		// ��һ�β��ű�־λ
		firstPlay = true;
		
		// �������λ
		litAdTime = 5000;
		AdLastTime = 5000;
		//AdAlpha = 50;
	}
	
	// Ԥ���ò���·��
	private void prepare(String path){
		
		Log.i(TAG, "ָ����ƵԴ·��");
		vv_video.setVideoPath(path);//file.getAbsolutePath()
		
		littleAD.setVisibility(View.INVISIBLE);
		bigAD.setVisibility(View.INVISIBLE);
		
		new Thread(new Runnable() {
			public void run() {
				smallAdPic = loadImageFromNetwork("http://202.104.110.178:8080/picture/small.png");
				bigAdPic = loadImageFromNetwork("http://202.104.110.178:8080/picture/big.png");
			}
		}).start();
	}
	
	// ʱ�������ת�ַ���
	private String timeToStr(int mileSec){
		String s_time = String.valueOf(mileSec / 1000 / 60 / 10) + 
				   		String.valueOf(mileSec / 1000 / 60 % 10) +
				   		":" +
				   		String.valueOf(mileSec / 1000 % 60 / 10) +
				   		String.valueOf(mileSec / 1000 % 60 % 10);
		return s_time;
	}
	
	// �������ϼ���ͼƬ
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
	
	protected void playTo(int msec){
		vv_video.seekTo(msec);
		progressBar.setVisibility(View.VISIBLE);
		playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
	}
	
	protected void play(int msec) {

		vv_video.start();
		
		// ���ճ�ʼλ�ò���
		vv_video.seekTo(msec);
		// ���ý�������������Ϊ��Ƶ������󲥷�ʱ��
		seekBar.setMax(vv_video.getDuration());  
		
		// ��ʱ����500ms����һ�ν������Ŀ̶�
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
							if(AdAlpha <= 0) AdAlpha = 0;
							littleAD.setAlpha(AdAlpha);
						}
					}
				}); 
				
//				bigAD.post(new Runnable(){
//					public void run(){
//						bigAD.setImageBitmap(bigAdPic);
//					}
//				});
			}
		}; 
		// ÿ500msִ��һ�ζ�ʱ������
		mTimer.schedule(mTimerTask, 0, 500);
		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				final Bitmap btm=loadImageFromNetwork("http://202.104.110.178:8080/picture/small.png");
//				littleAD.post(new Runnable() {
//					public void run() {
//						if(vv_video.getCurrentPosition() > litAdTime && 
//								   vv_video.getCurrentPosition() < (litAdTime + AdLastTime)	){
//									littleAD.setVisibility(View.VISIBLE);
//									littleAD.setImageBitmap(btm);
//									//AdAlpha += 20;
//									//if(AdAlpha >=250 ) AdAlpha = 250 ;
//									//littleAD.setAlpha(AdAlpha);
//								} else{
//									//AdAlpha -= 20;
//									//if(AdAlpha <= 0) AdAlpha = 0;
//									//littleAD.setAlpha(AdAlpha);
//									littleAD.setVisibility(View.INVISIBLE);
//								}
//					}
//				});
//			}
//		}).start();

		// �������� ����
		vv_video.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				finish();
			}
		});
		
		// �����������²���
		vv_video.setOnErrorListener(new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				play(0);
				isPlaying = false;
				return false;
			}
		});
	}
	
	// ���²���
	protected void replay() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.seekTo(0);
			Toast.makeText(this, "���²���", Toast.LENGTH_SHORT).show();
			return;
		}
		isPlaying = false;
		play(0);
	}
	
	// ֹͣ����
	protected void stop() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.stopPlayback();
			isPlaying = false;
		}
	}
	
	// ��Ӧң����  �������м� �������� ����
	public boolean onKeyDown(int keyCode ,KeyEvent event){
		
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_CENTER ){
			if(vv_video.isPlaying()){
				vv_video.pause();
				playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_pause)));
				progressBar.setVisibility(View.VISIBLE);
				//bigAD.setVisibility(View.VISIBLE);
			} else{
				if(firstPlay){
					play(0);
					firstPlay = false;
				}else{
					vv_video.start();
					playBtn.setImageDrawable((getResources().getDrawable(R.drawable.ic_media_play)));
					progressBar.setVisibility(View.INVISIBLE);
					//bigAD.setVisibility(View.INVISIBLE);
				}
				
			}
			Log.i(TAG, " �����м�");
			return true;
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
			int current = vv_video.getCurrentPosition();
			playTo(current - 5000);
			Log.i(TAG, " �������");
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
			int current = vv_video.getCurrentPosition();
			playTo(current + 5000);
			Log.i(TAG, " �����Ҽ�");
		}
		if(keyCode == 765 ){			
			int current = vv_video.getCurrentPosition();
			playTo(current + 1000);
			Log.i(TAG, " ������");
		}
		if(keyCode == 764 ){
			int current = vv_video.getCurrentPosition();
			playTo(current - 1000);
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
			}
			return true;  
		}
		return super.onKeyDown(keyCode, event);
	}
}