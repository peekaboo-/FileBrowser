package com.peekaboo.filebrowset;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;

public class SettingsActivity extends PreferenceActivity {
	private PreferenceManager mPreferenceManager = null;
	private ListPreference mListPreferenceThumbnailsSetting = null;
	private CheckBoxPreference mCheckBoxPreferenceFirstFolder = null;
	private CheckBoxPreference mCheckBoxPreferenceIsShowHiddenFile = null;
	private SharedPreferences mSharedPreferences = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_preference);
		mPreferenceManager = getPreferenceManager();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mListPreferenceThumbnailsSetting = (ListPreference) findPreference(getResources().getString(R.string.setting_thumbnails_list_preference_key));
		mListPreferenceThumbnailsSetting.setDefaultValue(MyApplication.getViewType());
		
		mCheckBoxPreferenceFirstFolder = (CheckBoxPreference) findPreference(getResources().getString(R.string.setting_first_folder_checkbox_preference_key));
		mCheckBoxPreferenceFirstFolder.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if (((CheckBoxPreference)preference).isChecked()) {
					mCheckBoxPreferenceFirstFolder.setSummary("始终先列出文件夹，再列出剩余的文件");
				} else {
					mCheckBoxPreferenceFirstFolder.setSummary("文件和文件夹以指定的排序方式组合排列");
				}
				return false;
			}
		});
		
		mCheckBoxPreferenceIsShowHiddenFile = (CheckBoxPreference) findPreference(getResources().getString(R.string.setting_first_folder_checkbox_preference_key));
		mCheckBoxPreferenceIsShowHiddenFile.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if (((CheckBoxPreference)preference).isChecked()) {
					mCheckBoxPreferenceIsShowHiddenFile.setSummary("显示隐藏文件和文件夹（以“.”号开头）");
				} else {
					mCheckBoxPreferenceIsShowHiddenFile.setSummary("不显示隐藏文件和文件夹（以“.”号开头）");
				}
				return false;
			}
		});
		
//		if (isFirstFolder) {
//			mCheckBoxPreferenceFirstFolder.setSummary("始终先列出文件夹，再列出剩余的文件");
//		} else {
//			mCheckBoxPreferenceFirstFolder.setSummary("文件和文件夹以指定的排序方式组合排列");
//		}
		
//		mListPreferenceThumbnailsSetting.set
//		mSharedPreferences.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
//			
//			@Override
//			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
//					String key) {
//				
//			}
//		});
//		mListPreferenceThumbnailsSetting.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});
//		mListPreferenceThumbnailsSetting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
////				preference.getKey()
//				System.out.println(preference.getKey() + " === " + preference.getTitle());
//				return false;
//			}
//		});
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	    	super.onCreateOptionsMenu(menu);
	        MenuInflater menuInflater = getMenuInflater();
	        menuInflater.inflate(R.menu.main_menu, menu);
	        System.out.println("执行了哦");
	        return true;
	    }
	
}
