/**
 * 
 */
package com.peekaboo.filebrowset.task;

import java.io.File;
import java.util.Collection;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.dialog.DialogCopyFile;
import com.peekaboo.filebrowset.utils.FileUtil;

/**
 * @author peekaboo
 * 
 */
public class CopyFileTask extends AsyncTask<Void, File, String>
{

    public static interface OnNewFileCreatedListener
    {
        void onFileCopied(File file);
    }

    public static interface OnCopyFinishedListener
    {
        void onFinished(boolean succ);
    }

    private OnNewFileCreatedListener mOnNewFileCreatedListener = null;

    public void setOnNewFileCreatedListener(OnNewFileCreatedListener l)
    {
        mOnNewFileCreatedListener = l;
    }

    private void notifyNewFileCreated(File file)
    {
        if (mOnNewFileCreatedListener != null) {
            mOnNewFileCreatedListener.onFileCopied(file);
        }
    }

    private OnCopyFinishedListener mOnCopyFinishedListener = null;

    public void setOnCopyFinishedListener(OnCopyFinishedListener l)
    {
        mOnCopyFinishedListener = l;
    }

    private void notifyOnCopyFinished(boolean succ)
    {
        if ((mOnCopyFinishedListener != null)) {
            mOnCopyFinishedListener.onFinished(succ);
        }
    }

    private Context mContext = null;
    private Collection<FileItemData> mSrcFiles = null;
    private String mDstDir = null;
    private boolean mIsCut = false;
    // private FileUtil.FileOperation mCopyOperation = null;
    private DialogCopyFile mDialog = null;

    public CopyFileTask(Context context, Collection<FileItemData> srcFiles, String dstDir, boolean isCut)
    {
        mContext = context;
        mSrcFiles = srcFiles;
        mDstDir = dstDir;
        mIsCut = isCut;
        // mCopyOperation = new FileUtil.FileOperation(context);
    }

    @Override
    protected void onPreExecute()
    {
        mDialog = new DialogCopyFile(mContext);// , mCopyOperation);
        mDialog.show();
    }

    @Override
    protected String doInBackground(Void... params)
    {
        boolean result = false;
        for (FileItemData fileItemData : mSrcFiles) {
            if (mIsCut) {
                File currentFile = new File(fileItemData.getPath());
                result = currentFile.renameTo(new File(mDstDir));
            } else {
                if (new File(fileItemData.getPath()).isFile()) {
                    result = FileUtil.copyFile(fileItemData.getPath(), mDstDir);
                } else {
                    result = FileUtil.copyFolder(fileItemData.getPath(), mDstDir);
                }
            }
        }
        // String absolute_dst_dir_path = mDstDir.getAbsolutePath();
        // for (FileItemData item : mSrcFiles) {
        // File src = GridItemManager.getFileFromURI(item.getURI());
        // if
        // (src.getParentFile().getAbsolutePath().compareToIgnoreCase(absolute_dst_dir_path)
        // == 0 ||
        // absolute_dst_dir_path.toLowerCase(Locale.getDefault()).startsWith(
        // src.getAbsolutePath().toLowerCase(Locale.getDefault()))) {
        // return mContext.getString(R.string.cannot_copy_here);
        // }
        // }
        //
        // boolean succ = true;
        // // use same policy for each copy
        // RefValue<ErrorPolicy> error_policy = new
        // RefValue<ErrorPolicy>(ErrorPolicy.Retry);
        // RefValue<ReplacePolicy> replace_policy = new
        // RefValue<ReplacePolicy>(ReplacePolicy.Ask);
        //
        // for (FileItemData item : mSrcFiles) {
        // if (this.isCancelled() || mCopyOperation.isCancelled()) {
        // return mContext.getString(R.string.cancel_copy);
        // }
        //
        // File src = GridItemManager.getFileFromURI(item.getURI());
        // File dst = new File(mDstDir, src.getName());
        // boolean dst_already_exists = dst.exists();
        //
        // succ = mCopyOperation.copyFile(src, dst, mIsCut, error_policy,
        // replace_policy);
        // if (!dst_already_exists && dst.exists()) {
        // this.publishProgress(dst);
        // }
        // }
        //
        this.notifyOnCopyFinished(result);
        if (result) {
            return mIsCut ? mContext.getString(R.string.cut_complete) : mContext.getString(R.string.copy_complete);
        } else {
            // if (this.isCancelled() || mCopyOperation.isCancelled()) {
            // return mContext.getString(R.string.cancel_copy);
            // }
            //
        	
            return mIsCut ? mContext.getString(R.string.cut_failed) : mContext.getString(R.string.copy_failed);
        }
    }

    @Override
    protected void onProgressUpdate(File... values)
    {
        this.notifyNewFileCreated(values[0]);
    }

    @Override
    protected void onPostExecute(String result)
    {
        mDialog.cancel();
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCancelled()
    {
        // mCopyOperation.cancel();
        if (mDialog.isShowing()) {
            mDialog.cancel();
        }
    }

}
