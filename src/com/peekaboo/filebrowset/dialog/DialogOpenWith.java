/**
 * 
 */
package com.peekaboo.filebrowset.dialog;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.peekaboo.filebrowset.R;

/**
 * @author peekaboo
 *
 */
public class DialogOpenWith extends Dialog
{
    private static final String MIME_TEXT = "text/*";
    private static final String MIME_AUDIO = "audio/*";
    private static final String MIME_IMAGE = "image/*";
    
    private Button mButtonText = null;
    private Button mButtonAudio = null;
    private Button mButtonImage = null;
    private Activity mActivity = null;
    private File mFile = null;

    public DialogOpenWith(Activity activity, File file)
    {
        super(activity, R.style.dialog_no_title);

        DialogOpenWith.this.setContentView(R.layout.dialog_open_with);

        mActivity = activity;
        mFile = file;

        mButtonText = (Button) findViewById(R.id.button_text);
        mButtonAudio = (Button) findViewById(R.id.button_audio);
        mButtonImage = (Button) findViewById(R.id.button_image);

        mButtonText.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                openWith(MIME_TEXT);
            }
        });

//        if(!DeviceInfo.singleton().getDeviceController().hasAudio(activity)){
//        	mButtonAudio.setVisibility(View.GONE);
//        } else {
//        	mButtonAudio.setOnClickListener(new View.OnClickListener()
//        	{
//
//        		@Override
//        		public void onClick(View v)
//        		{
//        			openWith(MIME_AUDIO);
//        		}
//        	});
//        }

        mButtonImage.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                openWith(MIME_IMAGE);
            }
        });

        Button button_cancel = (Button) findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                DialogOpenWith.this.dismiss();
            }
        });
    }

    private void openWith(String type)
    {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mFile), type);

        List<ResolveInfo> info_list = mActivity.getPackageManager()
                .queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        intent.setData(Uri.fromFile(mFile));

        if (info_list.size() <= 0) {
            Toast.makeText(mActivity,
                    R.string.unable_to_open_this_type_of_file,
                    Toast.LENGTH_SHORT).show();
        }
        else {
            DialogApplicationOpenList dlg = new DialogApplicationOpenList(
                    mActivity, info_list, mFile, true);
//            dlg.setOnApplicationSelectedListener(new OnApplicationSelectedListener()
//            {
//
//                @Override
//                public void onApplicationSelected(ResolveInfo info,
//                        boolean makeDefault)
//                {
//                    ActivityUtil.startActivitySafely(mActivity,
//                            intent, info.activityInfo);
//                }
//
//            });
            dlg.show();
        }

        DialogOpenWith.this.dismiss();
    }
}
