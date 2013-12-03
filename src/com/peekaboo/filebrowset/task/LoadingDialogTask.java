package com.peekaboo.filebrowset.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.peekaboo.filebrowset.interfaces.ILoadingWork;

public class LoadingDialogTask extends AsyncTask<Object, Integer, Object>{

	private OnLoadingListener mOnLoadingListener = null;
	private int mProgressStyle = ProgressDialog.STYLE_SPINNER;
	
	public static interface OnLoadingListener {
		void OnLoadingFinish(Object result);
		void OnLoadingFailure();
	}
	
	private void notifyLoadingFinish(Object result) {
		if (mOnLoadingListener != null) {
			mOnLoadingListener.OnLoadingFinish(result);
		}
	}
	
	private void notifyLoadingFailure() {
		if (mOnLoadingListener != null) {
			mOnLoadingListener.OnLoadingFailure();
		}
	}
	
	public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
		this.mOnLoadingListener = onLoadingListener;
	}
	
	private ProgressDialog progress_dialog = null;
	private Context mContext = null;
	private ILoadingWork mLoadingWork = null;
	
	public LoadingDialogTask(Context context, ILoadingWork loadingWork) {
		super();
		this.mContext = context;
		this.mLoadingWork = loadingWork;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		initLoadingDialog();
	}
	
	public void setLoadingProgressStyle(int progressStyle) {
		this.mProgressStyle = progressStyle;
	}

	private void initLoadingDialog() {
		progress_dialog = new ProgressDialog(mContext);
		progress_dialog.setTitle("请稍等...");
//		progress_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress_dialog.setProgressStyle(mProgressStyle);
		progress_dialog.show();
	}

	@Override
	protected Object doInBackground(Object... params) {
		return mLoadingWork.work(params);
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		System.out.println("aaa 加载的结果为空");
		if (result != null) {
			this.notifyLoadingFinish(result);
		} else {
			this.notifyLoadingFailure();
		}
		progress_dialog.dismiss();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
}
