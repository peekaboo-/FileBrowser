/**
 * 
 */
package com.peekaboo.filebrowset.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.peekaboo.filebrowset.R;

/**
 * @author peekaboo
 *
 */
public class SelectionAdapter extends BaseAdapter
{
    /**
     * absolute index of adapter's items
     */
    private int mSelection= -1;
    private String[] mAppNames = null;
    private ArrayList<Pair<String, Object>> mItems = null;
    private Context mContext = null;
    
    private static final int sItemMinWidth = 145;
    private static final int sItemMinHeight = 60;
    private static final int sHorizontalSpacing = 0;
    private static final int sVerticalSpacing = 0;
    
    public SelectionAdapter(Context context, GridView gridView)
    {
        this(context, gridView, null);
    }
    
    public SelectionAdapter(Context context, GridView gridView, String[] arrays)
    {
        this(context, gridView, arrays, -1);
    }
    
    public SelectionAdapter(Context context, GridView gridView, String[] arrays, int initialSelection)
    {
        super();
        
        mSelection = initialSelection;
        mContext = context;
        mAppNames = arrays;

        init(mAppNames.length);
    }
    
    /**
     * 
     * @param context
     * @param gridView
     * @param items String stands for item name, Object stores item tag
     */
    public SelectionAdapter(Context context, GridView gridView, ArrayList<Pair<String, Object>> items, int initialSelection)
    {
        super();
        
        mSelection = initialSelection;
        mContext = context;
        mItems = items;

        init(mItems.size());
    }

    private void init(int count)
    {
//        int itemDetailMinHeight = (int)mContext.getResources().getDimension(R.dimen.selection_adapter_min_height);
//        this.getPageLayout().setItemMinWidth(sItemMinWidth);
//        this.getPageLayout().setItemMinHeight(sItemMinHeight);
//        this.getPageLayout().setItemThumbnailMinHeight(sItemMinHeight);
//        this.getPageLayout().setItemDetailMinHeight(itemDetailMinHeight);
//        this.getPageLayout().setHorizontalSpacing(sHorizontalSpacing);
//        this.getPageLayout().setVerticalSpacing(sVerticalSpacing);
//        this.getPageLayout().setViewMode(GridViewMode.Detail);
//
//        this.getPaginator().initializePageData(count, this.getPaginator().getPageSize());
    }
    
    /**
     * absolute index of adapter's items
     */
    public int getSelection()
    {
        return mSelection;
    }
    /**
     * absolute index of adapter's items
     */
    public void setSelection(int index)
    {
        mSelection = index;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View ret_view = null;
        
//        int idx = -1;
//        idx = this.getPaginator().getAbsoluteIndex(position);
        
        String text = null;
        if (mItems != null) {
            text = mItems.get(position).first;
        }
        else {
            assert(mAppNames != null);
            text = mAppNames[position];
        }
        
        if (convertView != null) {
            ret_view = convertView;
        }
        else {
            ret_view = View.inflate(mContext, R.layout.dialog_settings_gridview_item, null);
        }
        
        TextView text_view = (TextView)ret_view.findViewById(R.id.textview_gridview_item);
        text_view.setText(text);
        
        CheckBox check_box = (CheckBox) ret_view.findViewById(R.id.checkbox_gridview_item);
        
        if (position == mSelection) {
            check_box.setChecked(true);
        }
        else {
            check_box.setChecked(false);
        }
        
        // TODO: getItemCurrentHeight() maybe 0, need further watch
//        final int height = Math.max(this.getPageLayout().getItemCurrentHeight(),
//                this.getPageLayout().getItemMinHeight());
//        text_view.getLayoutParams().height = height;

        ret_view.setTag(text);
        
        return ret_view;
    }

    @Override
    public int getCount()
    {
        return mAppNames.length;
    }

}
