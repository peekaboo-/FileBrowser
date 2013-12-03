package com.peekaboo.filebrowset.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.peekaboo.filebrowset.AscDescOrder;
import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.GridItemData;
import com.peekaboo.filebrowset.SortOrder;
import com.peekaboo.filebrowset.utils.ItemSorterUtil;

/**
 * 
 * @author peekaboo
 *
 */
public class PeekabooAdapter extends BaseAdapter {

	protected List<GridItemData> mFileList = null;
	
	public PeekabooAdapter (List<GridItemData> fileList) {
		super();
		if (fileList == null) {
			fileList = new ArrayList<GridItemData>();
		}
		
		Collections.sort(fileList, ItemSorterUtil.getComparator(SortOrder.NAME, AscDescOrder.ASC));
		mFileList = fileList;
	}
	
	@Override
    public int getCount()
    {
        return mFileList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void appendItem(FileItemData fileItemData) {
		mFileList.add(fileItemData);
		Collections.sort(mFileList, ItemSorterUtil.getComparator(SortOrder.NAME, AscDescOrder.ASC));
	}

}
