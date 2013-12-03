/**
 * 
 */
package com.peekaboo.filebrowset.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekaboo.filebrowset.R;

/**
 * @author peekaboo
 *
 */
public class GridViewApplicationOpenListAdapter extends BaseAdapter
{
    private static final String TAG = "GridViewApplicationOpenListAdapter";
    
    private static final int sItemMinWidth = 145;
    private static final int sItemMinHeight = 60;
    private static final int sHorizontalSpacing = 0;
    private static final int sVerticalSpacing = 0;
    
    private Context mContext = null;
    ArrayList<ResolveInfo> mResolveInfoList = new ArrayList<ResolveInfo>();
    
    
    public GridViewApplicationOpenListAdapter(Context context, GridView gridView, List<ResolveInfo> resolveInfoList)
    {
        super();

        mContext = context;
        mResolveInfoList.addAll(resolveInfoList);
        
//        this.getPageLayout().setItemMinWidth(sItemMinWidth);
//        this.getPageLayout().setItemMinHeight(sItemMinHeight);
//        this.getPageLayout().setItemThumbnailMinHeight(sItemMinHeight);
//        this.getPageLayout().setItemDetailMinHeight((int)context.getResources().getDimension(R.dimen.application_open_list_item_detail_min_height));
//        this.getPageLayout().setHorizontalSpacing(sHorizontalSpacing);
//        this.getPageLayout().setVerticalSpacing(sVerticalSpacing);
//        this.getPageLayout().setViewMode(GridViewMode.Detail);
//        
//        this.getPaginator().initializePageData(mResolveInfoList.size(), 
//                this.getPaginator().getPageSize());
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View ret_view = null;
        System.out.println("aaa mResolveInfoList.size() = " + mResolveInfoList.size());
//	    int idx = this.getPaginator().getAbsoluteIndex(position);
        ResolveInfo info = mResolveInfoList.get(position);
        
        if (convertView != null) {
            ret_view = convertView;
        }
        else {
            ret_view = View.inflate(mContext, R.layout.dialog_application_openlist_item, null);
        }
        
        ImageView image_view = (ImageView)ret_view.findViewById(R.id.imageview_app_cover);
        image_view.setImageDrawable(info.activityInfo.applicationInfo.loadIcon(mContext.getPackageManager()));
//        LayoutParams params = new LayoutParams(this.getPageLayout().getItemCurrentHeight() - 2,
//                this.getPageLayout().getItemCurrentHeight() - 2);
//        image_view.setLayoutParams(params);
        
        TextView text_view = (TextView)ret_view.findViewById(R.id.textview_app_name);
        text_view.setText(info.activityInfo.applicationInfo.loadLabel(mContext.getPackageManager()));
        
        // TODO: getItemCurrentHeight() maybe 0, need further watch
//        final int height = Math.max(this.getPageLayout().getItemCurrentHeight(),
//                this.getPageLayout().getItemMinHeight());
//        text_view.getLayoutParams().height = height;
//        Log.d(TAG, "set text_view height: " + height);
        
        ret_view.setTag(info);
        
        return ret_view;
    }
    
    public static ResolveInfo getViewTag(View view)
    {
        return (ResolveInfo)view.getTag();
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return mResolveInfoList.size();
    }

}
