package com.peekaboo.filebrowset;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.peekaboo.filebrowset.adapter.FileBrowsetAdapter;
import com.peekaboo.filebrowset.data.GoUpItemData;
import com.peekaboo.filebrowset.dialog.SMBConnectionDialog;
import com.peekaboo.filebrowset.handler.FileOperationHandler;
import com.peekaboo.filebrowset.interfaces.ILoadingWork;
import com.peekaboo.filebrowset.task.CopyFileTask;
import com.peekaboo.filebrowset.task.LoadingDialogTask;
import com.peekaboo.filebrowset.task.LoadingDialogTask.OnLoadingListener;
import com.peekaboo.filebrowset.utils.OpenFileUtil;

public class MainActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {

	private static final String TAG = "MainActivity";
	private static final String CONFIG_USER_INFO = "user_info";
	private final String CONFIG_DEFAULT_HOME_DIR = "default_home_dir";
	private final String LAST_OPEN_FOLDER = "last_open_folder";
	private String mDefaultHomeDir = null;
	private GridView mFileListView = null;
	private String mCurrentPath = null;
	private FileBrowsetAdapter mFileBrowsetAdapter = null;
	private File[] mFiles = null;
	private File mCurrentFile = null;
	private File mCurrentSelectFile = null;
	private FileOperationHandler mFileOperationHandler = null;
	private LinearLayout mCopyPasteLinearLayout = null;
	private Button mPasteButton = null;
	private Button mCancelButton = null;
	private SharedPreferences mSharedPreferences = null;
	private ILoadingWork mLoadingWork = null;
	private LoadingDialogTask mLoadingTask = null;
	private Context mContext = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mContext = this;
		mSharedPreferences = getSharedPreferences(CONFIG_USER_INFO, 0);
		mFileListView = (GridView) findViewById(R.id.file_listview);
		// TextView backTextView = new TextView(this);
		// backTextView.setText("..");
		// System.out.println("genmulu = " +
		// Environment.getRootDirectory().getPath());
		// backImageView.setImageResource(R.drawable.go_up);
		// backImageView.setLayoutParams(new LayoutParams(40, 40));
		// mFileListView.addHeaderView(backImageView);

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			Log.d(TAG, "SD Exist");
			mCurrentFile = Environment.getExternalStorageDirectory();
			mDefaultHomeDir = mSharedPreferences.getString(
					CONFIG_DEFAULT_HOME_DIR, mCurrentFile.getPath());
			System.out.println(" mDefaultHomeDir = " + mDefaultHomeDir);
			if (MyApplication.isSaveLastOpenFolder()) {
				mCurrentPath = mSharedPreferences.getString(
						LAST_OPEN_FOLDER, mCurrentFile.getPath());
				System.out.println("aaaa = " + mCurrentPath);
			} else {
				System.out.println("bbb + " + mCurrentPath);
				mCurrentPath = mDefaultHomeDir;
			}
			mCurrentFile = new File(mCurrentPath);
		} else {
			mCurrentFile = new File("/");
			Log.d(TAG, "SD No Exist");
		}

		mFileListView.setOnItemClickListener(this);
		mFileListView.setOnItemLongClickListener(this);

		mCopyPasteLinearLayout = (LinearLayout) findViewById(R.id.copy_paste_layout);
		mPasteButton = (Button) findViewById(R.id.paste);
		mCancelButton = (Button) findViewById(R.id.cancel);
		mPasteButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);
		if (CopyService.getSourceItems().size() > 0) {
			mCopyPasteLinearLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if (MyApplication.isSaveLastOpenFolder()) {
			Editor config_editor = mSharedPreferences.edit();
			config_editor.putString(LAST_OPEN_FOLDER, mCurrentPath);
			config_editor.commit();
		}
	}
	
	private List<GridItemData> getData(List<File> fileList) {
		List<GridItemData> files = new ArrayList<GridItemData>();
		files.add(new GoUpItemData(R.string.go_up, R.drawable.icon_go_up));
		for (File file : fileList) {
			FileItemData fileItemData = null;

			if (file.isFile()) {
				fileItemData = new FileItemData(file.getName(),
						R.drawable.icon_file);
				fileItemData.setFileType(FileType.FILE);
			} else {
				fileItemData = new FileItemData(file.getName(),
						R.drawable.icon_folder);
				fileItemData.setFileType(FileType.FOLDER);
			}

			fileItemData.setName(file.getName());
			fileItemData.setPath(file.getPath());
			fileItemData.setSize(file.length());
			fileItemData.setParentPath(file.getParent());
			Long time = file.lastModified();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);

			fileItemData.setUpdateTime(calendar.getTime());

			if (file.isHidden() && !MyApplication.isShowHiddenFile()) {
				continue;
			}

			files.add(fileItemData);
		}
		return files;
	}

	private void loadData(File file) {
		mLoadingWork = new ILoadingWork() {

			@Override
			public Object work(Object... params) {
				File file = (File) params[0];
				if (file == null) {
					return null;
				}
				mCurrentFile = file;
				if (file.isFile()) {
					file = file.getParentFile();
				}
				if (file.canRead()) {
					mFiles = file.listFiles();
					mCurrentPath = file.getPath();
					System.out.println("aaaaaaa = " + mCurrentPath);
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(mContext, "没有权限读取该文件夹！",
									Toast.LENGTH_LONG).show();
						}
					});
					return null;
				}

				// TODO
				List<GridItemData> files = getData(Arrays.asList(mFiles));
				return files;
			}
		};

		mLoadingTask = new LoadingDialogTask(this, mLoadingWork);
		mLoadingTask.setOnLoadingListener(new OnLoadingListener() {

			@Override
			public void OnLoadingFinish(Object result) {
				mFileBrowsetAdapter = new FileBrowsetAdapter(mContext,
						(List<GridItemData>) result);
				mFileOperationHandler = new FileOperationHandler(
						mFileBrowsetAdapter);
				mFileListView.setAdapter(mFileBrowsetAdapter);
				Log.d(TAG, "mCurrentPath = " + mCurrentPath);
				MainActivity.this.setTitle(mCurrentPath);
			}

			@Override
			public void OnLoadingFailure() {
//				Toast.makeText(mContext, "没有权限读取该文件夹！",
//						Toast.LENGTH_LONG).show();
			}
		});

		mLoadingTask.execute(file);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (mFileOperationHandler.getSourceItems().size() == 0) {
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.main_menu, menu);
		} else {
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.select_menu, menu);
			if (!mCurrentFile.isFile()) {
				for (int i = 0; i < menu.size(); i++) {
					if (menu.getItem(i).getItemId() == R.id.menu_open_with) {
						menu.getItem(i).setEnabled(false);
					} else {
						menu.getItem(i).setEnabled(true);
					}
				}
			} else {
				for (int i = 0; i < menu.size(); i++) {
					menu.getItem(i).setEnabled(true);
				}
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		if (CopyService.getSourceItems().size() > 0) {
			findViewById(R.id.copy_paste_layout).setVisibility(View.VISIBLE);
		} else {
			mFileOperationHandler.getSourceItems().clear();
			findViewById(R.id.copy_paste_layout).setVisibility(View.GONE);
		}
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_open_with:
			mFileOperationHandler.onOpenWith();
			break;
		case R.id.menu_rename:
			mFileOperationHandler.onRename();
			break;
		case R.id.menu_copy:
			mFileOperationHandler.onCopy();
			break;
		case R.id.menu_move:
			mFileOperationHandler.onMove();
			break;
		case R.id.menu_delete:
			mFileOperationHandler.onDelete();
			break;
		case R.id.menu_preferences:
			mFileOperationHandler.onProperty();
			break;
		case R.id.menu_set_home:
			boolean result = setHomeDir();
			Toast.makeText(this, result ? "设置成功" : "设置失败", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.menu_create_file:
			if (mFileOperationHandler.getSourceItems().size() == 0) {
				setCurrentSource();
			}
			mFileOperationHandler.onCreateFile();
			break;
		case R.id.menu_create_folder:
			if (mFileOperationHandler.getSourceItems().size() == 0) {
				setCurrentSource();
			}
			mFileOperationHandler.onCreateFolder();
			break;
		case R.id.menu_settings:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_smb:
			showSMBDialog();
//			intent = new Intent(this, SMBActivity.class);
//			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showSMBDialog() {
		SMBConnectionDialog smbDialog = new SMBConnectionDialog(this);
		smbDialog.show();
	}

	private void setCurrentSource() {
		List<FileItemData> fileList = new ArrayList<FileItemData>();
		FileItemData fileItemData = new FileItemData(mCurrentFile.getName(),
				R.drawable.icon_folder);
		fileItemData.setName(mCurrentFile.getName());
		fileItemData.setParentPath(mCurrentFile.getPath());
		fileItemData.setUpdateTime(new Date());
		fileList.add(fileItemData);
		mFileOperationHandler.setSourceItems(this, fileList);
	}

	private boolean setHomeDir() {
		Editor config_editor = mSharedPreferences.edit();
		config_editor.putString(CONFIG_DEFAULT_HOME_DIR, mCurrentSelectFile
				.isDirectory() ? mCurrentSelectFile.getPath()
				: mCurrentSelectFile.getParent());
		boolean result = config_editor.commit();
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (view.getTag() instanceof GoUpItemData) {
			if ("/".equals(mCurrentFile.getPath())) {
				return;
			}
			mCurrentFile = mCurrentFile.getParentFile();
			if (mCurrentFile != null) {
				mCurrentPath = mCurrentFile.getPath();
				loadData(mCurrentFile);
			}
		} else {
			mCurrentFile = new File(((FileItemData) view.getTag()).getPath());
			if (FileType.FOLDER.equals(((FileItemData) view.getTag())
					.getFileType())) {
				loadData(mCurrentFile);
			} else {
				Intent intent = OpenFileUtil.openFile(mCurrentFile.getPath());
				startActivity(intent);
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (view.getTag() instanceof GoUpItemData) {
			return false;
		}
		List<FileItemData> file_data = new ArrayList<FileItemData>();
		file_data.add((FileItemData) view.getTag());
		mFileOperationHandler.setSourceItems(this, file_data);
		mCurrentSelectFile = new File(((FileItemData) view.getTag()).getPath());
		this.openOptionsMenu();
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData(mCurrentFile);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (CopyService.getSourceItems().size() > 0) {
//				mCopyPasteLinearLayout.setVisibility(View.GONE);
//				mFileOperationHandler.getSourceItems().clear();
//			} else {
//				loadData(mCurrentFile.getParentFile());
//			}
			twoClickExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.paste:
			CopyFileTask task = new CopyFileTask(MainActivity.this,
					CopyService.getSourceItems(),
					mCurrentFile.getAbsolutePath() + "/"
							+ CopyService.getSourceItems().get(0).getName(),
					CopyService.isCut());

			task.setOnCopyFinishedListener(new CopyFileTask.OnCopyFinishedListener() {

				@Override
				public void onFinished(boolean succ) {
					if (succ) {
						CopyService.clean();
						MyApplication.singleton().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								mCopyPasteLinearLayout.setVisibility(View.GONE);
								loadData(mCurrentFile);
							}
						});
					}
				}
			});

			task.execute();
			break;
		case R.id.cancel:
			CopyService.clean();
			mCopyPasteLinearLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

}
