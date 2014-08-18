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
	
	// gallery用到的7张固定小图
	private int[] images_small_IDs = new int[] { 
			R.drawable.walle_small,
			R.drawable.transformer_small,	
			R.drawable.bigbang_small,
			R.drawable.fromstar_small,
			R.drawable.frozen_small,
			R.drawable.laputa_small,
			R.drawable.gravity_small
	};
	// gallery用到的7张固定大图
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
	private ImageView image;		// 用来显示电影大图
	private ActionBar mActionBar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		// 全屏显示窗口 TODO 让他一开始就是全屏
		mActionBar=getActionBar();
		mActionBar.hide();
		gallery = (Gallery) findViewById(R.id.gallery);
		image = (ImageView) findViewById(R.id.filmImage);
		gallery.setAdapter(new ImageAdapter(MainActivity.this));
		
		// 选中小图时，大图自动切换
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				image.setImageResource(images_big_IDs[position]);
			}
			public void onNothingSelected(AdapterView<?> parent){
			}
		});
		
		// 点击小图时 跳转到对应的activity
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){
					Intent intent = new Intent(MainActivity.this, player.class);
					intent.putExtra("url", "http://202.104.110.178:8080/video/天才.mp4");
					startActivity(intent);
				}
				if(position == 1 ){
					new Thread(new Runnable(){
						public void run(){
							try{
								JsonRec jsonRec = new JsonRec("202.104.110.178",10000);
								jsonRec.connect("变形金刚3"); 
								jsonArray = jsonRec.getJsonArray();
								adNum = jsonRec.getAdNum();
								Log.i("json", " 广告数量为："+ adNum); 
							}catch(Exception e){
								Log.e("myerror", "Exception: "+Log.getStackTraceString(e));
							}
						}
					}).start();

					Intent intent = new Intent(MainActivity.this, player.class);
					// 变形金刚的url
					intent.putExtra("url", "http://202.104.110.178:8080/video/video.webm");
					startActivity(intent);
				}
			}
		});
	}

	
	// 声明一个BaseAdapter
	public class ImageAdapter extends BaseAdapter {
		// 使用Adapter的上下文变量
		Context context;
		// 背景样式的Id
		int itemBackground;

		public ImageAdapter(Context c) {
			// 在构造函数中传递需要使用这个Adapter的上下文变量
			context = c;
			// 通过XML资源中定义的样式，设定背景
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			itemBackground = a.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}

		@Override
		public int getCount() {
			// 返回当前数据集中数据的个数
			return images_small_IDs.length;
		}

		@Override
		public Object getItem(int position) {
			// 返回数据集中，当前position位置的数据
			return images_small_IDs[position];
		}

		@Override
		public long getItemId(int position) {
			// 返回数据集中，当前position位置的数据的Id
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 返回当前position位置的视图
			ImageView imageview;
			if (convertView == null) {
				// 通过数据上下文对象声明一个ImageView，并设置相关属性
				imageview = new ImageView(context);
				imageview.setImageResource(images_small_IDs[position]);
				imageview.setScaleType(ImageView.ScaleType.FIT_XY);
				imageview.setLayoutParams(new Gallery.LayoutParams(200, 200));

			} else {
				imageview = (ImageView) convertView;
			}
			// 使用XML 中定义的样式为待显示的View设定背景样式
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
	
	// 按返回键时杀死整个进程
	public boolean onKeyDown(int keyCode ,KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK ){
			System.exit(0);  
			return true;  
		}
		return super.onKeyDown(keyCode, event);
	}
}
