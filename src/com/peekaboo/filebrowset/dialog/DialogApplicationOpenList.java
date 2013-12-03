/**
 * 
 */
package com.peekaboo.filebrowset.dialog;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.adapter.GridViewApplicationOpenListAdapter;

/**
 * 
 * @author peekaboo
 *
 */
public class DialogApplicationOpenList extends Dialog
{
    @SuppressWarnings("unused")
    private static final String TAG = "DialogApplicationOpenList";
    
    public interface OnApplicationSelectedListener
    {
        public void onApplicationSelected(ResolveInfo info, boolean makeDefault);
    }
    
    // initialize to avoid null checking
    private OnApplicationSelectedListener mOnApplicationSelectedListener = new OnApplicationSelectedListener()
    {
        
        @Override
        public void onApplicationSelected(ResolveInfo info, boolean makeDefault)
        {
            // do nothing
            System.out.println("aaa info = " + info);
        }
    };
    
    public void setOnApplicationSelectedListener(OnApplicationSelectedListener l)
    {
        mOnApplicationSelectedListener = l;
    }

    private GridView mGridView = null;
    private Button mButtonNext = null;
    private Button mButtonPrevious = null;
    private TextView mTextViewPage = null;
    private CheckBox mCheckBoxDefaultOpen = null;
    private Activity mActivity = null;
    private View mView = null;
    private GridViewApplicationOpenListAdapter mAdapter = null;
    private File mFile = null;

    private boolean mIsBackOpenWith = false;

    public DialogApplicationOpenList(Activity activity, List<ResolveInfo> resolveInfoList, File file, boolean backOpenWith)
    {
        super(activity, R.style.CustomDialog);

        this.setContentView(R.layout.dialog_application_openlist);
        mView = findViewById(R.id.layout_dialog);

        mActivity = activity;
        mFile = file;
        mIsBackOpenWith = backOpenWith;

        mGridView = (GridView)this.findViewById(R.id.gridview_openlist);
        mButtonNext = (Button)this.findViewById(R.id.button_next);
        mButtonPrevious = (Button)this.findViewById(R.id.button_prev);
        mTextViewPage = (TextView)this.findViewById(R.id.textview_dialog_application_page);
        mCheckBoxDefaultOpen = (CheckBox)this.findViewById(R.id.checkbox_default_open);
        
        mGridView.setOnItemClickListener(new OnItemClickListener()
        { 
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                
                ResolveInfo resolve_info = (ResolveInfo) view.getTag();
                
                if (mCheckBoxDefaultOpen.isChecked()) {
                	Toast.makeText(mActivity, R.string.Succeed_setting, Toast.LENGTH_LONG).show();
                }
                
                ResolveInfo info = GridViewApplicationOpenListAdapter.getViewTag(view);
                mOnApplicationSelectedListener.onApplicationSelected(info, false);
                DialogApplicationOpenList.this.dismiss();
                
                
                
                String packageName = info.activityInfo.packageName;
                String className = info.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                mActivity.startActivity(intent);
            }
        });
        
        mButtonNext.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	//TODO
            }
        });
        
        mButtonPrevious.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	//TODO
            }
        });
        
        mAdapter = new GridViewApplicationOpenListAdapter(activity, mGridView, resolveInfoList);
        mAdapter.registerDataSetObserver(new DataSetObserver()
        {
            @Override
            public void onChanged()
            {
                DialogApplicationOpenList.this.updateTextViewPage();
            }
            
            @Override
            public void onInvalidated()
            {
                DialogApplicationOpenList.this.updateTextViewPage();
            }
        });
        
        mGridView.setAdapter(mAdapter);
    }
    
    private void updateTextViewPage()
    {
    	//TODO
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mIsBackOpenWith && keyCode == KeyEvent.KEYCODE_BACK) {
            DialogApplicationOpenList.this.dismiss();
            DialogOpenWith dialogOpenWith = new DialogOpenWith(mActivity, mFile);
            dialogOpenWith.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
