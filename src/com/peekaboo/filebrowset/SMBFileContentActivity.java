package com.peekaboo.filebrowset;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.peekaboo.filebrowset.interfaces.ILoadingWork;
import com.peekaboo.filebrowset.smb.RemoteAccessUtil;
import com.peekaboo.filebrowset.task.LoadingDialogTask;
import com.peekaboo.filebrowset.task.LoadingDialogTask.OnLoadingListener;

/**
 * 
 * @author peekaboo
 *
 */
public class SMBFileContentActivity extends Activity {

	private TextView mFileContentTextView = null;
	private Intent mIntent = null;
	private String mRemoteFilePath = null;
	private String mFileContent = null;
	private ILoadingWork mLoadingWork = null;
	
	private LoadingDialogTask mLoadingTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_content);
		mFileContentTextView = (TextView) findViewById(R.id.textview_content);
		mIntent = getIntent();
		mRemoteFilePath = mIntent.getStringExtra(SMBActivity.REMOTE_FILE_PATH);
		
		mLoadingWork = new ILoadingWork() {
			
			@Override
			public Object work(Object... params) {
				String mFileContent = null;
				try {
					mFileContent = RemoteAccessUtil.smbGetFileContent(String.valueOf(params[0]));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return mFileContent;
			}
		};
		
		mLoadingTask = new LoadingDialogTask(this, mLoadingWork);
		mLoadingTask.setOnLoadingListener(new OnLoadingListener() {
			
			@Override
			public void OnLoadingFinish(Object result) {
				mFileContentTextView.setText(String.valueOf(result));
			}

			@Override
			public void OnLoadingFailure() {
				// TODO Auto-generated method stub
				
			}
		});
		
		mLoadingTask.execute(mRemoteFilePath);
	}

}
