package com.lee.tomatoplayer;


import org.json.JSONArray;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class MainActivity extends Activity {
	public static JSONArray jsonArray;
	public static int adNum;
	
	// gallery�õ���7�Ź̶�Сͼ
	private int[] images_small_IDs = new int[] { 
			R.drawable.walle_small,
			R.drawable.transformer_small,	
			R.drawable.bigbang_small,
			R.drawable.fromstar_small,
			R.drawable.frozen_small,
			R.drawable.laputa_small,
			R.drawable.gravity_small
	};
	// gallery�õ���7�Ź̶���ͼ
	private int[] images_big_IDs = new int[]{  
			 R.drawable.walle_big,
			 R.drawable.transformer_big,
			 R.drawable.bigbang_big,
			 R.drawable.fromstar_big, 
			 R.drawable.frozen_big,
			 R.drawable.laputa_big,
			 R.drawable.gravity_big
	};
	private Gallery gallery;
	private ImageView image;		// ������ʾ��Ӱ��ͼ
	private ActionBar mActionBar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		// ȫ����ʾ���� TODO ����һ��ʼ����ȫ��
		mActionBar=getActionBar();
		mActionBar.hide();
		gallery = (Gallery) findViewById(R.id.gallery);
		image = (ImageView) findViewById(R.id.filmImage);
		gallery.setAdapter(new ImageAdapter(MainActivity.this));
		
		// ѡ��Сͼʱ����ͼ�Զ��л�
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				image.setImageResource(images_big_IDs[position]);
			}
			public void onNothingSelected(AdapterView<?> parent){
			}
		});
		
		// ���Сͼʱ ��ת����Ӧ��activity
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){
					Intent intent = new Intent(MainActivity.this, player.class);
					intent.putExtra("url", "http://202.104.110.178:8080/video/���.mp4");
					startActivity(intent);
				}
				if(position == 1 ){
					new Thread(new Runnable(){
						public void run(){
							try{
								JsonRec jsonRec = new JsonRec("202.104.110.178",10000);
								jsonRec.connect("���ν��3"); 
								jsonArray = jsonRec.getJsonArray();
								adNum = jsonRec.getAdNum();
								Log.i("json", " �������Ϊ��"+ adNum); 
							}catch(Exception e){
								Log.e("myerror", "Exception: "+Log.getStackTraceString(e));
							}
						}
					}).start();

					Intent intent = new Intent(MainActivity.this, player.class);
					// ���ν�յ�url
					intent.putExtra("url", "http://202.104.110.178:8080/video/video.webm");
					startActivity(intent);
				}
			}
		});
	}

	
	// ����һ��BaseAdapter
	public class ImageAdapter extends BaseAdapter {
		// ʹ��Adapter�������ı���
		Context context;
		// ������ʽ��Id
		int itemBackground;

		public ImageAdapter(Context c) {
			// �ڹ��캯���д�����Ҫʹ�����Adapter�������ı���
			context = c;
			// ͨ��XML��Դ�ж������ʽ���趨����
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			itemBackground = a.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}

		@Override
		public int getCount() {
			// ���ص�ǰ���ݼ������ݵĸ���
			return images_small_IDs.length;
		}

		@Override
		public Object getItem(int position) {
			// �������ݼ��У���ǰpositionλ�õ�����
			return images_small_IDs[position];
		}

		@Override
		public long getItemId(int position) {
			// �������ݼ��У���ǰpositionλ�õ����ݵ�Id
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// ���ص�ǰpositionλ�õ���ͼ
			ImageView imageview;
			if (convertView == null) {
				// ͨ�����������Ķ�������һ��ImageView���������������
				imageview = new ImageView(context);
				imageview.setImageResource(images_small_IDs[position]);
				imageview.setScaleType(ImageView.ScaleType.FIT_XY);
				imageview.setLayoutParams(new Gallery.LayoutParams(200, 200));

			} else {
				imageview = (ImageView) convertView;
			}
			// ʹ��XML �ж������ʽΪ����ʾ��View�趨������ʽ
			imageview.setBackgroundResource(itemBackground);

			return imageview;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// �����ؼ�ʱɱ����������
	public boolean onKeyDown(int keyCode ,KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK ){
			System.exit(0);  
			return true;  
		}
		return super.onKeyDown(keyCode, event);
	}
}
