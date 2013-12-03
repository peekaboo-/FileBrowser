package com.peekaboo.filebrowset.dialog;

import java.io.File;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.FileType;
import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.handler.FileOperationHandler;

/**
 * 
 * @author peekaboo
 *
 */
public class DialogCreateFileOrFolder extends Dialog {
	public static boolean mIsCreateFile = true;
	private EditText mEditTextFileName = null;
	private TextView mTextViewDialogTitle = null;
	private Button mButtonSet = null;
	private Button mButtonCancel = null;
	private FileOperationHandler mFileOperationHandler = null;
	private FileItemData mFileItemData = null;
	private Context mContext = null;

	public DialogCreateFileOrFolder(Context context, final FileOperationHandler fileHandler, boolean isCreateFile) {
		super(context, R.style.dialog_no_title);

		this.setContentView(R.layout.dialog_create_file_or_folder);
		mFileOperationHandler = fileHandler;
		mContext = context;
		mFileItemData = mFileOperationHandler.getSourceItems().get(0);
		mTextViewDialogTitle = (TextView) findViewById(R.id.textview_create_file_title);
		mIsCreateFile = isCreateFile;
		mEditTextFileName = (EditText) this.findViewById(R.id.edittext_filename);
		mButtonSet = (Button) this.findViewById(R.id.button_set);
		mButtonCancel = (Button) this.findViewById(R.id.button_cancel);

		if (!isCreateFile) {
			mTextViewDialogTitle.setText(R.string.create_folder_title);
			mEditTextFileName.setHint(R.string.create_folder_title);
		}

		mButtonSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String fileName = mEditTextFileName.getText().toString();
				if (fileName.length() <= 0) {
					Toast.makeText(mContext, R.string.name_can_not_empty, Toast.LENGTH_LONG).show();
					return;
				}
				String filePath = mFileItemData.getParentPath() + File.separator + fileName;
				File file = new File(filePath);
				boolean result = false;
				FileItemData fileItemData = null;
				try {
					if (mIsCreateFile) {
						fileItemData = new FileItemData(fileName, R.drawable.icon_file);
						fileItemData.setFileType(FileType.FILE);
						result = file.createNewFile();
					} else {
						fileItemData = new FileItemData(fileName, R.drawable.icon_folder);
						fileItemData.setFileType(FileType.FOLDER);
						result = file.mkdir();
					}
					fileItemData.setName(fileName);
					fileItemData.setParentPath(mFileItemData.getParentPath());
					fileItemData.setPath(filePath);
					fileItemData.setUpdateTime(new Date());
					mFileOperationHandler.getAdapter().appendItem(fileItemData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Toast.makeText(mContext, result ? "创建成功" : "创建失败", Toast.LENGTH_LONG).show();
				DialogCreateFileOrFolder.this.dismiss();
			}
		});

		mButtonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogCreateFileOrFolder.this.cancel();
			}
		});
	}

	@Override
	public void show() {
		super.show();

		mEditTextFileName.requestFocus();
		InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.showSoftInput(mEditTextFileName, InputMethodManager.RESULT_SHOWN);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	@Override
	public void dismiss() {
		InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(mEditTextFileName.getWindowToken(), 0);
		}
		super.dismiss();
	}
}
