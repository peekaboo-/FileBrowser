/**
 * 
 */
package com.peekaboo.filebrowset.dialog;

import java.io.File;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.FileType;
import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.adapter.SelectionAdapter;
import com.peekaboo.filebrowset.handler.FileOperationHandler;
import com.peekaboo.filebrowset.utils.DateTimeUtil;
import com.peekaboo.filebrowset.utils.FileUtil;

/**
 * @author peekaboo
 * 
 */
public class DialogFileProperty extends Dialog
{
    private ViewSwitcher mSwitcher = null;
    private Button mButtonProperty = null;
    private Button mButtonOpenWith = null;
    private Button mButtonNextPage = null;
    private Button mButtonPreviousPage = null;
    private TextView mTextViewProgress = null;
    private TextView mTextViewUnableOpenFile = null;
    private LinearLayout mLayout = null;
    private File mFile = null;
    private FileItemData mFileItemData = null;
    private List<ResolveInfo> mListInfos = null;
    private String mExt = null;
    private TextView mFileUpdateTime = null;
    private TextView mFilePath = null;
    private TextView mFileType = null;
    private FileOperationHandler mFileHandler = null;

    private Handler mHandlerSet = null;
    private GridView mGridView = null;
    private SelectionAdapter mAdapter = null;

    TextView mTextViewName = null;
    TextView mTextViewTag = null;
    private Context mContext = null;

    private static final String AUTHOR_SEPERATOR = " ";

    public DialogFileProperty(Context context, FileOperationHandler fileHandler)
    {
        super(context, R.style.dialog_no_title);

        this.mFileHandler = fileHandler;
        mFileItemData = fileHandler.getSourceItems().get(0);
        this.mContext = context;

        this.setContentView(R.layout.dialog_file_property);

        mFile = new File(mFileItemData.getPath());
        mTextViewName = (TextView) findViewById(R.id.edittext_name);
        mFileUpdateTime = (TextView) findViewById(R.id.edittext_date);
        mFilePath = (TextView) findViewById(R.id.edittext_path);
        mFileType = (TextView) findViewById(R.id.edittext_file_type);
        mGridView = (GridView) this.findViewById(R.id.gridview_open_with);
        mTextViewName.setText(mFileItemData.getName());
        mFileUpdateTime.setText(DateTimeUtil.formatDate(mFileItemData.getUpdateTime()));
        mFilePath.setText(mFileItemData.getPath());
        mFileType.setText(new File(fileHandler.getSourceItems().get(0).getPath()).isFile() ? FileType.FILE.toString()
                : FileType.FOLDER.toString());

        ImageView imageview_cover = (ImageView) this.findViewById(R.id.imageview_cover);
        TextView textview_title = (TextView) this.findViewById(R.id.textview_title);
        mTextViewUnableOpenFile = (TextView) this.findViewById(R.id.textview_unable_open_file);
        mSwitcher = (ViewSwitcher) this.findViewById(R.id.view_switcher);
        mButtonProperty = (Button) this.findViewById(R.id.button_directory_details);
        mButtonOpenWith = (Button) this.findViewById(R.id.button_open_with);
        mButtonPreviousPage = (Button) this.findViewById(R.id.button_previous);
        mButtonNextPage = (Button) this.findViewById(R.id.button_next);
        mTextViewProgress = (TextView) this.findViewById(R.id.textview_paged);
        mLayout = (LinearLayout) this.findViewById(R.id.layout_paged);

        mSwitcher.setDisplayedChild(0);

        mButtonProperty.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                if (mSwitcher.getDisplayedChild() != 0) {
                    mSwitcher.setDisplayedChild(0);

                }
            }
        });
        mButtonOpenWith.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                if (mSwitcher.getDisplayedChild() != 1) {
                    mSwitcher.setDisplayedChild(1);

                }
            }
        });

        Button button_cancel = (Button) this.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogFileProperty.this.dismiss();
            }
        });

        Button button_set = (Button) this.findViewById(R.id.button_set);
        button_set.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mTextViewName.getText().toString().trim().equals("")) {
                    Toast.makeText(mContext, R.string.name_can_not_empty, Toast.LENGTH_LONG).show();
                    return;
                }
                Message msg = new Message();

                mHandlerSet.sendMessage(msg);
            }
        });

        mGridView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
//                int idx = mGridView.getPagedAdapter().getPaginator().getAbsoluteIndex(position);

                int selection = mAdapter.getSelection();
                if (selection == position) {
                    DialogFileProperty.this.setSelection(-1);
                }
                else {
                    DialogFileProperty.this.setSelection(position);
                }
            }
        });

        mButtonPreviousPage.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
            }
        });

        mButtonNextPage.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
            }
        });

        // imageview_cover.setImageBitmap(book.getBitmap());
        mTextViewName.setText(mFileItemData.getName());

        if (mFile.isFile()) {
            final Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);

            mExt = FileUtil.getFileExtension(mFile.getName());

            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExt);
            File file = new File("mnt/sdcard/dummy." + mExt);
            if (type != null) {
                intent.setDataAndType(Uri.fromFile(file), type);
            }
            else {
                intent.setData(Uri.fromFile(file));
            }

            mListInfos = mContext.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);

            if (mListInfos.size() <= 0) {
                if (mTextViewUnableOpenFile.getVisibility() == View.GONE) {
                    mTextViewUnableOpenFile.setVisibility(View.VISIBLE);
                }

                mGridView.setVisibility(View.GONE);
                mLayout.setVisibility(View.GONE);
            }
            else {
                String defaultAppName = "";
                String[] app_names = new String[mListInfos.size()];
                int selection = -1;
                for (int i = 0; i < mListInfos.size(); i++) {
                    if (defaultAppName.equals(mListInfos.get(i).activityInfo.applicationInfo.loadLabel(mContext
                            .getPackageManager()))) {
                        selection = i;
                    }
                    app_names[i] = mListInfos.get(i).activityInfo.applicationInfo.loadLabel(
                            mContext.getPackageManager()).toString();
                }

                mAdapter = new SelectionAdapter(mContext, mGridView, app_names, selection);
                mGridView.setAdapter(mAdapter);

            }
        }
        else {
            if (mTextViewUnableOpenFile.getVisibility() == View.GONE) {
                mTextViewUnableOpenFile.setVisibility(View.VISIBLE);
            }

            mGridView.setVisibility(View.GONE);
            mLayout.setVisibility(View.GONE);
        }

        mHandlerSet = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {

                String new_path = mFile.getParent() + File.separator + mTextViewName.getText().toString().trim();

                if (mTextViewName.getText().toString().trim().equals(mFile.getName().trim())) {
                    Toast.makeText(mContext, R.string.name_can_not_be_same, Toast.LENGTH_LONG).show();
                }
                else {
                    FileUtil.reName(mContext, mFile, new_path);
                    mFileHandler.getSourceItems().get(0).setName(mTextViewName.getText().toString());
                    mFileHandler.getSourceItems().get(0).setPath(new_path);

                    mFileHandler.getAdapter().notifyDataSetChanged();
                }

                DialogFileProperty.this.dismiss();
            }
        };
    }

    private void setSelection(int position)
    {
        mAdapter.setSelection(position);
        mAdapter.notifyDataSetChanged();
    }

    private void updateTextViewProgress()
    {
    	//TODO
    }

}