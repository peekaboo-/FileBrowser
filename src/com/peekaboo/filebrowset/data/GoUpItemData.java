package com.peekaboo.filebrowset.data;

import android.graphics.Bitmap;

import com.peekaboo.filebrowset.GridItemData;

public class GoUpItemData extends GridItemData{

	public GoUpItemData(int nameId, Bitmap imageSource) {
		super(nameId, imageSource);
	}

	public GoUpItemData(int nameId, int imageSourceId) {
		super(nameId, imageSourceId);
	}

	public GoUpItemData(String name, Bitmap imageSource) {
		super(name, imageSource);
	}

	public GoUpItemData(String name, int imageSourceId) {
		super(name, imageSourceId);
	}
	
}
