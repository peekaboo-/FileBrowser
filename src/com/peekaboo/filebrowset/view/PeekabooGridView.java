package com.peekaboo.filebrowset.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.peekaboo.filebrowset.MyApplication;
import com.peekaboo.filebrowset.data.ViewType;

public class PeekabooGridView extends GridView{
	
	public interface OnViewTypeChangeListener{
		void setOnViewTypeChangeListener();
	}
	List<OnViewTypeChangeListener> mOnViewTypeChange = new ArrayList<OnViewTypeChangeListener>();
	
	ViewType mViewType = ViewType.Thumbnails;

	public PeekabooGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PeekabooGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PeekabooGridView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		mViewType = MyApplication.getViewType();
	}
	
	public void setViewType(ViewType viewType) {
		this.mViewType = viewType;
	}
	
	public ViewType getViewType() {
		return mViewType;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		
		mViewType = MyApplication.getViewType();
		
		if (mViewType.equals(ViewType.Thumbnails)) {
			this.setNumColumns(3);
		} else {
			this.setNumColumns(1);
		}
	}
}
