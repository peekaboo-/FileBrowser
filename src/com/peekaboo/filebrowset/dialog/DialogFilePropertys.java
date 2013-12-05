/**
 * 
 */
package com.peekaboo.filebrowset.dialog;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.FileType;
import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.handler.FileOperationHandler;
import com.peekaboo.filebrowset.utils.DateTimeUtil;

/**
 * @author peekaboo
 * 
 */
public class DialogFilePropertys extends Dialog {
	private FileItemData mFileItemData = null;
	private TextView mFileUpdateTime = null;
	private TextView mFilePath = null;
	private TextView mFileType = null;
	private FileOperationHandler mFileHandler = null;

	TextView mTextViewName = null;
	private Context mContext = null;

	private static final String AUTHOR_SEPERATOR = " ";

	public DialogFilePropertys(Context context, FileOperationHandler fileHandler) {
		super(context);

		init(context, fileHandler);
	}

	private void init(Context context, FileOperationHandler fileHandler) {
		this.mFileHandler = fileHandler;
		mFileItemData = fileHandler.getSourceItems().get(0);
		this.mContext = context;
		this.setTitle(mContext.getResources().getString(R.string.menu_preferences));
		this.setContentView(R.layout.dialog_file_propertys);
		this.setCanceledOnTouchOutside(true);
		mTextViewName = (TextView) findViewById(R.id.textview_name);
		mFileUpdateTime = (TextView) findViewById(R.id.textview_date);
		mFilePath = (TextView) findViewById(R.id.textview_path);
		mFileType = (TextView) findViewById(R.id.textview_file_type);

		mTextViewName.setText(mFileItemData.getName());
		mFileUpdateTime.setText(DateTimeUtil.formatDate(mFileItemData.getUpdateTime()));
		mFilePath.setText(mFileItemData.getPath());
		mFileType.setText(new File(fileHandler.getSourceItems().get(0).getPath()).isFile() ? FileType.FILE.toString()
				: FileType.FOLDER.toString());

		ImageView imageview_cover = (ImageView) this.findViewById(R.id.imageview_cover);
		TextView textview_title = (TextView) this.findViewById(R.id.textview_title);
	}
}