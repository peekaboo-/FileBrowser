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
    
    private Object mLocker = new Object();

    public DialogCopyFile(Context context)
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
    }
    
    private void cancelTask()
    {
    	//TODO
    }

}
