package com.peekaboo.filebrowset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.peekaboo.filebrowset.adapter.FileBrowsetAdapter;
import com.peekaboo.filebrowset.data.GoUpItemData;
import com.peekaboo.filebrowset.interfaces.ILoadingWork;
import com.peekaboo.filebrowset.smb.RemoteAccessUtil;
import com.peekaboo.filebrowset.task.LoadingDialogTask;
import com.peekaboo.filebrowset.task.LoadingDialogTask.OnLoadingListener;

public class SMBActivity extends BaseActivity implements OnItemClickListener {

	public static final String REMOTE_FILE_PATH = "REMOTE_FILE_PATH";
	public static final String SMB_PATH = "SMB_PATH";
	private GridView mFileListView = null;
	private FileBrowsetAdapter mFileBrowsetAdapter = null;
	private String mCurrentPath = "smb://192.168.1.4/values";
	private List<GridItemData> list = new ArrayList<GridItemData>();
	private LoadingDialogTask mLoadingDialogTask = null;
	private Context mContext = null;
	private ILoadingWork mLoadingWork = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smb);
		this.mContext = this;
		mFileListView = (GridView) findViewById(R.id.file_listview);
		if (getIntent().getStringExtra(SMB_PATH) != null) {
			mCurrentPath = "smb://" + getIntent().getStringExtra(SMB_PATH);
		}
//		mCurrentPath = "smb://";
		loadData(mCurrentPath);
		mFileListView.setOnItemClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData(mCurrentPath);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (view.getTag() instanceof GoUpItemData) {
			mCurrentPath = getParentPath(mCurrentPath);
			loadData(mCurrentPath);

		} else {
			mCurrentPath = ((FileItemData) view.getTag()).getPath();
			if (FileType.FOLDER.equals(((FileItemData) view.getTag())
					.getFileType())) {
				loadData(mCurrentPath);
			} else {
				Intent intent = new Intent(SMBActivity.this,
						SMBFileContentActivity.class);
				intent.putExtra(REMOTE_FILE_PATH, mCurrentPath);
				startActivity(intent);
			}
		}
	}

	private void loadData(String mCurrentPath) {
		this.setTitle(mCurrentPath);
		mLoadingWork = new ILoadingWork() {

			@Override
			public Object work(Object... params) {
				try {
					list = RemoteAccessUtil.smbGet(String.valueOf(params[0]));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return list;
			}
		};

		mLoadingDialogTask = new LoadingDialogTask(this, mLoadingWork);
		mLoadingDialogTask.setOnLoadingListener(new OnLoadingListener() {
			
			@Override
			public void OnLoadingFinish(Object result) {
				mFileBrowsetAdapter = new FileBrowsetAdapter(mContext, (List<GridItemData>)result);
				mFileListView.setAdapter(mFileBrowsetAdapter);				
			}

			@Override
			public void OnLoadingFailure() {
				// TODO Auto-generated method stub
				
			}
		});
		mLoadingDialogTask.execute(mCurrentPath);
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
////			mCurrentPath = new File(mCurrentPath).getParent();
////			this.setTitle(mCurrentPath);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_settings:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private String getParentPath(String mCurrentPath) {
		if (mCurrentPath.endsWith("/")) {
			mCurrentPath = String.valueOf(mCurrentPath.subSequence(0,
					mCurrentPath.length() - 1));
		}
		int endIndex = mCurrentPath.lastIndexOf("/");
		if (endIndex < 0) {
			return mCurrentPath;
		}
		mCurrentPath = String.valueOf(mCurrentPath.subSequence(0, endIndex));
		return mCurrentPath + "/";
	}

}
