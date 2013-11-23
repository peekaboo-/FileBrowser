package com.peekaboo.filebrowset.dialog;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.adapter.FileBrowsetAdapter;
import com.peekaboo.filebrowset.handler.FileOperationHandler;

public class DialogFileRemove extends Dialog
{

    private Context mContext = null;

    public DialogFileRemove(Context context, final FileOperationHandler fileHandler)
    {
        super(context, R.style.dialog_no_title);

        this.setContentView(R.layout.dialog_deletefile);
        this.mContext = context;

        TextView textview_fileName = (TextView) this.findViewById(R.id.textview_filename_deletefile);
        Button button_set = (Button) this.findViewById(R.id.button_set_deletefile);
        Button button_cancel = (Button) this.findViewById(R.id.button_cancel_deletefile);

        textview_fileName.setText(fileHandler.getSourceItems().get(0).getName());

        button_set.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                // DeleteFileTask task = new
                // DeleteFileTask(DialogFileRemove.this.getContext(),
                // fileHandler);
                // task.execute();

                boolean result = new File(fileHandler.getSourceItems().get(0).getPath()).delete();
                if (result) {
                    ((FileBrowsetAdapter) fileHandler.getAdapter()).remove(fileHandler.getSourceItems().remove(0));
                    fileHandler.getAdapter().notifyDataSetChanged();
                }
                Toast.makeText(mContext, result ? "删除成功" : "删除失败", Toast.LENGTH_LONG).show();
                DialogFileRemove.this.dismiss();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogFileRemove.this.cancel();
            }
        });

    }
}
