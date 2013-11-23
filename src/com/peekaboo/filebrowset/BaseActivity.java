package com.peekaboo.filebrowset;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	private static final String TAG = "BaseActivity";
	protected static Boolean isExit = false;
	protected SharedPreferences mSharedPreferences = null;
	protected static final String CONFIG_USER_INFO = "user_info";
	protected final String LAST_OPEN_FOLDER = "last_open_folder";
	protected String mCurrentPath = null;
	protected final String CONFIG_DEFAULT_HOME_DIR = "default_home_dir";
	protected String mDefaultHomeDir = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = getSharedPreferences(CONFIG_USER_INFO, 0);
		
//		boolean sdCardExist = Environment.getExternalStorageState().equals(
//				android.os.Environment.MEDIA_MOUNTED);
//		if (sdCardExist) {
//			Log.d(TAG, "SD Exist");
//			mCurrentPath = Environment.getExternalStorageDirectory().getParent();
//			mDefaultHomeDir = mSharedPreferences.getString(
//					CONFIG_DEFAULT_HOME_DIR, mCurrentPath);
//			System.out.println(" mDefaultHomeDir = " + mDefaultHomeDir);
//			if (MyApplication.isSaveLastOpenFolder()) {
//				mCurrentPath = mSharedPreferences.getString(
//						LAST_OPEN_FOLDER, mCurrentPath);
//			} else {
//				mCurrentPath = mDefaultHomeDir;
//			}
//		} else {
//			mCurrentPath = "/";
//			Log.d(TAG, "SD No Exist");
//		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		if (MyApplication.isSaveLastOpenFolder()) {
//			Editor config_editor = mSharedPreferences.edit();
//			config_editor.putString(LAST_OPEN_FOLDER, mCurrentPath);
//			config_editor.commit();
//		}
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			twoClickExit();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	protected void twoClickExit() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}
}
