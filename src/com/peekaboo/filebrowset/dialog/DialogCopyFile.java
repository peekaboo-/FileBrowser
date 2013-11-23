/**
 * 
 */
package com.peekaboo.filebrowset.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.peekaboo.filebrowset.R;

/**
 * @author peekaboo
 *
 */
public class DialogCopyFile extends Dialog
{
    private static final String TAG = "DialogCopyFile";
    
//    private FileUtil.FileOperation mCopyOperation = null;
    private Object mLocker = new Object();

    public DialogCopyFile(Context context)//, FileUtil.FileOperation copyOperation)
    {
        super(context, R.style.dialog_no_title);
        
        this.setContentView(R.layout.dialog_copy_file);
        this.setCanceledOnTouchOutside(false);
        
        Button button_cancel = (Button)findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogCopyFile.this.cancelTask();
            }
        });
        
//        mCopyOperation = copyOperation;
//        mCopyOperation.setOnPreReplaceListener(new FileUtil.FileOperation.OnPreReplaceListener()
//        {
//            
//            @Override
//            public void onPreReplace(File src, final File dst, final RefValue<ReplacePolicy> replacePolicy)
//            {
//                final Context context = DialogCopyFile.this.getContext();
//                final String[] buttons = new String [] { context.getString(R.string.replace), 
//                        context.getString(R.string.replace_all),
//                        context.getString(R.string.skip),
//                        context.getString(R.string.skip_all),
//                        context.getString(R.string.cancel) };
//                
//                final Object waiter = new Object();
//                synchronized (waiter) {
//                    OnyxApplication.singleton().runOnUiThread(new Runnable()
//                    {
//
//                        @Override
//                        public void run()
//                        {
//                                new AlertDialog.Builder(context)
//                                .setTitle(context.getString(R.string.file_already_exists) + ": " + dst.getAbsolutePath())
//                                .setItems(buttons, new OnClickListener()
//                                {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which)
//                                    {
//                                        synchronized (waiter) {
//                                            switch (which) {
//                                            case 0:
//                                                replacePolicy.setValue(ReplacePolicy.Replace);
//                                                break;
//                                            case 1:
//                                                replacePolicy.setValue(ReplacePolicy.ReplaceAll);
//                                                break;
//                                            case 2:
//                                                replacePolicy.setValue(ReplacePolicy.Skip);
//                                                break;
//                                            case 3:
//                                                replacePolicy.setValue(ReplacePolicy.SkipAll);
//                                                break;
//                                            case 4:
//                                                replacePolicy.setValue(ReplacePolicy.Cancel);
//                                                DialogCopyFile.this.cancelTask();
//                                                break;
//                                            default:
//                                                assert(false);
//                                                replacePolicy.setValue(ReplacePolicy.Skip);
//                                                break;
//                                            }
//                                            
//                                            waiter.notify();
//                                        }
//                                    }
//                                })
//                                .show();
//                        }
//                    });
//                    
//                    try {
//                        waiter.wait();
//                    }
//                    catch (InterruptedException e) {
//                        Log.e(TAG, "Exception", e);
//                    }
//                }
//            }
//        });
    }
    
    private void cancelTask()
    {
//        if (mCopyOperation != null) {
//            synchronized (mLocker) {
//                mCopyOperation.cancel();
//                this.dismiss();
//            }
//        }
    }

}
