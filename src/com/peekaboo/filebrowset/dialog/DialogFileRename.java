package com.peekaboo.filebrowset.dialog;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.handler.FileOperationHandler;
import com.peekaboo.filebrowset.utils.FileUtil;

/**
 * 
 * @author peekaboo
 *
 */
public class DialogFileRename extends Dialog
{
    private TextView mTextViewCurrentName = null;
    private EditText mEditTextRename = null;
    private Button mButtonSet = null;
    private Button mButtonCancel = null;
    private File mCurrentFile = null;
    private Context mContext = null;

    public DialogFileRename(Context context, final FileOperationHandler fileHandler)
    {
        super(context, R.style.dialog_no_title);

        this.setContentView(R.layout.dialog_rename);

        mContext = context;
        mTextViewCurrentName = (TextView) this.findViewById(R.id.textview_currentname);
        mEditTextRename = (EditText) this.findViewById(R.id.edittext_rename);
        mButtonSet = (Button) this.findViewById(R.id.button_set_rename);
        mButtonCancel = (Button) this.findViewById(R.id.button_cancel_rename);

        mTextViewCurrentName.setText(fileHandler.getSourceItems().get(0).getName());
        mEditTextRename.setText(fileHandler.getSourceItems().get(0).getName());

        mButtonSet.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mCurrentFile = new File(fileHandler.getSourceItems().get(0).getPath());
                String newPath = mCurrentFile.getParentFile() + "/"
                        + mEditTextRename.getText().toString();
                FileUtil.reName(mContext, mCurrentFile, newPath);

                fileHandler.getSourceItems().get(0).setName(mEditTextRename.getText().toString());
                fileHandler.getSourceItems().get(0).setPath(newPath);

                fileHandler.getAdapter().notifyDataSetChanged();
                DialogFileRename.this.dismiss();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogFileRename.this.cancel();
            }
        });
    }
}
