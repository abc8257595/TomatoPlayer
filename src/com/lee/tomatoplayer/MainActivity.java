package com.lee.tomatoplayer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static boolean outPlayer = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		// sleep  显示欢迎界面再跳入播放器  待做
		if(outPlayer == false){
			Intent intent = new Intent(MainActivity.this, player.class);
			startActivity(intent);
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
