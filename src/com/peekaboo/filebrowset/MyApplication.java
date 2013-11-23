/**
 * 
 */
package com.peekaboo.filebrowset;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.peekaboo.filebrowset.data.ViewType;

/**
 * @author peekaboo
 *
 */
public class MyApplication extends Application
{
    private final static String TAG = "MyApplication";
    
    private static MyApplication sInstance = null;
    
    public static boolean UpdatePolicyInitialized = false;
    
    private Handler mHandler = new Handler();
    
    static SharedPreferences mSharedPreferences = null;
    
    /**
     * called from android, only once when startup
     */
    public MyApplication()
    {
        Log.d(TAG, "creating MyApplication");
        sInstance = this;
    }

    public static MyApplication singleton()
    {
        return sInstance;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(sInstance);
    }
    
    public void runOnUiThread(final Runnable r)
    {
        mHandler.post(r);
    }
    
    /**
     * return false when interrupted
     * 
     * @param r
     * @return
     */
    public boolean runAndWaitOnUiThread(final Runnable r)
    {
        final Object waiter = new Object();
        
        synchronized (waiter) {
            mHandler.post(new Runnable()
            {
                
                @Override
                public void run()
                {
                    synchronized (waiter) {
                        r.run();
                        waiter.notifyAll();
                    }
                }
            });
            
            try {
                waiter.wait();
            }
            catch (InterruptedException e) {
                Log.e(TAG, "Exception", e);
                return false;
            }
        }
        
        return true;
    }

	public static ViewType getViewType() {
		String view_type = mSharedPreferences.getString(sInstance.getResources().getString(R.string.setting_thumbnails_list_preference_key), ViewType.SimpleList.toString());
		return Enum.valueOf(ViewType.class, view_type);
	}

	public static boolean isFirstFolder() {
		return mSharedPreferences.getBoolean(sInstance.getResources().getString(R.string.setting_first_folder_checkbox_preference_key), true);
	}

	public static boolean isShowHiddenFile() {
		return mSharedPreferences.getBoolean(sInstance.getResources().getString(R.string.setting_show_hidden_checkbox_preference_key), false);
	}
	
	public static boolean isSaveLastOpenFolder() {
		return mSharedPreferences.getBoolean(sInstance.getResources().getString(R.string.setting_save_last_open_folder_checkbox_preference_key), true);
	}

}
