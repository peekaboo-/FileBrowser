///**
// * 
// */
//package com.peekaboo.filebrowset.task;
//
//import java.io.File;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.peekaboo.filebrowset.R;
//import com.peekaboo.filebrowset.handler.FileOperationHandler;
//
///**
// * @author joy
// *
// */
//public class DeleteFileTask extends AsyncTask<File, FileItemData, String>
//{
//	private static final String TAG = "DeleteFileTask";
//    public static interface OnMessageListener { void onMessage(String msg); }
//    
//    private Context mContext = null;
//    private FileOperationHandler mFileHandler = null;
//    FileUtil.FileOperation mDeleteOperation = null;
//    private DialogDeleteFile mDialog = null;
//    
//    public DeleteFileTask(Context context, FileOperationHandler fileHandler) 
//    {
//        mContext = context;
//        mFileHandler = fileHandler;
//        mDeleteOperation = new FileUtil.FileOperation(mContext);
//        mDialog = new DialogDeleteFile(context, mDeleteOperation);
//    }
//    
//    @Override
//    protected void onPreExecute()
//    {
//        mDialog.show();
//    }
//
//    @Override
//    protected String doInBackground(File... params)
//    {
//        boolean succ = true;
//        // use same policy for each deletion
//        RefValue<ErrorPolicy> error_policy = new RefValue<ErrorPolicy>(ErrorPolicy.Retry);
//        
//        for (FileItemData i : mFileHandler.getSourceItems()) {
//            if (this.isCancelled() || mDeleteOperation.isCancelled()) {
//                return mContext.getString(R.string.cancel_delete);
//            }
//
//            File f = GridItemManager.getFileFromURI(i.getURI());
//            String md5 = null;
//            try {
//                md5 = FileUtil.computeMD5(f);
//            } catch (Exception e) {
//                Log.e(TAG, "exception", e);
//            }
//            if (mDeleteOperation.removeFile(f, error_policy)) {
//                OnyxCmsCenter.deleteLibraryItem(mContext , f.getAbsolutePath());
//                OnyxHistoryEntryHelper.deleteHistoryByMD5(mContext, md5);
//
//				this.publishProgress(i);
//			}
//            else {
//                succ = false;
//            }
//        }
//        
//        if (succ) {
//            return mContext.getString(R.string.delete_success);
//        }
//        else {
//            if (this.isCancelled() || mDeleteOperation.isCancelled()) {
//                return mContext.getString(R.string.cancel_delete);
//            }
//            
//            return mContext.getString(R.string.delete_failed);
//        }
//    }
//    
//    @Override
//    protected void onProgressUpdate(FileItemData... values)
//    {
//        for (FileItemData item : values) {
//            mFileHandler.getAdapter().removeItem(item);
//        }
//    }
//    
//    @Override
//    protected void onPostExecute(String result)
//    {
//        mDialog.cancel();
//        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
//    }
//    
//    @Override
//    protected void onCancelled()
//    {
//        mDeleteOperation.cancel();
//        if (mDialog.isShowing()) {
//            mDialog.cancel();
//        }
//    }
//
//}
