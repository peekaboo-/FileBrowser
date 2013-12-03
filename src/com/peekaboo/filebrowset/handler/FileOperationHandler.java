package com.peekaboo.filebrowset.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.peekaboo.filebrowset.CopyService;
import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.adapter.PeekabooAdapter;
import com.peekaboo.filebrowset.dialog.DialogApplicationOpenList;
import com.peekaboo.filebrowset.dialog.DialogApplicationOpenList.OnApplicationSelectedListener;
import com.peekaboo.filebrowset.dialog.DialogCreateFileOrFolder;
import com.peekaboo.filebrowset.dialog.DialogFileProperty;
import com.peekaboo.filebrowset.dialog.DialogFileRemove;
import com.peekaboo.filebrowset.dialog.DialogFileRename;
import com.peekaboo.filebrowset.dialog.DialogOpenWith;
import com.peekaboo.filebrowset.interfaces.IFileOperation;
import com.peekaboo.filebrowset.utils.FileUtil;

/**
 * 
 * @author peekaboo
 *
 */
public class FileOperationHandler implements IFileOperation
{
    private List<FileItemData> mFileList = new ArrayList<FileItemData>();
    private Context mContext = null;
    private PeekabooAdapter mAdapter = null;

    public void setSourceItems(Context context, List<FileItemData> fileList)
    {
        this.mContext = context;
        this.mFileList = fileList;
    }

    public List<FileItemData> getSourceItems()
    {
        return mFileList;
    }

    public FileOperationHandler(PeekabooAdapter adapter)
    {
        super();
        this.mAdapter = adapter;
    }

    public PeekabooAdapter getAdapter()
    {
        return mAdapter;
    }

    @Override
    public void onRename()
    {
        DialogFileRename renameDialog = new DialogFileRename(mContext, this);
        renameDialog.show();
    }

    @Override
    public void onCopy()
    {
        CopyService.copy(getSourceItems());
    }

    @Override
    public void onDelete()
    {
        DialogFileRemove deleteDialog = new DialogFileRemove(mContext, this);
        deleteDialog.show();
    }

    @Override
    public void onMove()
    {
        CopyService.cut(getSourceItems());
    }

    @Override
    public void onProperty()
    {
        if (mFileList.size() > 1) {
            Toast.makeText(mContext, R.string.more_than_1_item, Toast.LENGTH_LONG).show();
            return;
        }
        DialogFileProperty dlg = new DialogFileProperty(mContext, this);
        dlg.show();
    }

    @Override
    public void onOpenWith()
    {
        if (mFileList.size() > 0) {
            final FileItemData item = mFileList.get(0);
            
//            File file = GridItemManager.getFileFromURI(item.getURI());
            File file = new File(getSourceItems().get(0).getPath());
            Uri uri = Uri.fromFile(file);
            final Intent intent = new Intent();
            System.out.println("aaa uri = " + uri);
            intent.setData(uri);
            intent.setAction(Intent.ACTION_VIEW);
            System.out.println("aaa FileUtil.getFileExtension(file.getName()) = " + FileUtil.getFileExtension(file.getName()));
            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtil.getFileExtension(file.getName()));
            System.out.println("aaa type = " + type);
            if (type != null) {
                intent.setDataAndType(uri, type);
            }
            else {
                intent.setData(Uri.fromFile(new File(file.getParent() + File.separator +
                        "file_name." + FileUtil.getFileExtension(file.getName()))));
            }

            List<ResolveInfo> info_list = mContext.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            
            
            HashSet<String> app_dict = new HashSet<String>();
            List<ResolveInfo> unique_app_list = new ArrayList<ResolveInfo>();
            for (ResolveInfo info : info_list) {
                if (!app_dict.contains(info.activityInfo.packageName)) {
                    unique_app_list.add(info);
                    app_dict.add(info.activityInfo.packageName);
                }
            }

            intent.setData(uri);

            if (unique_app_list.size() <= 0) {
                DialogOpenWith dialogOpenWith = new DialogOpenWith((Activity)mContext, file);
                dialogOpenWith.show();
            } else {
                assert (unique_app_list.size() > 1);

                DialogApplicationOpenList dlg = new DialogApplicationOpenList((Activity)mContext, unique_app_list, file, false);
                dlg.setOnApplicationSelectedListener(new OnApplicationSelectedListener()
                {

                    @Override
                    public void onApplicationSelected(ResolveInfo info,
                            boolean makeDefault)
                    {
                    	//TODO
                    }

                });

                dlg.show();
            }
        }
    }

	@Override
	public void onCreateFile() {
		DialogCreateFileOrFolder mDialogCreateFileOrFolder = new DialogCreateFileOrFolder(mContext, this, true);
		mDialogCreateFileOrFolder.show();
	}

	@Override
	public void onCreateFolder() {
		DialogCreateFileOrFolder mDialogCreateFileOrFolder = new DialogCreateFileOrFolder(mContext, this, false);
		mDialogCreateFileOrFolder.show();		
	}

}
