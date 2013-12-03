package com.peekaboo.filebrowset;

import android.graphics.Bitmap;

/**
 * 
 * @author peekaboo
 *
 */
public class GridItemData {

	private String name = null;
	private Bitmap imageSource = null;
	private int nameId = 0;
	private int imageSourceId = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getImageSource() {
		return imageSource;
	}

	public void setImageSource(Bitmap imageSource) {
		this.imageSource = imageSource;
	}

	public int getNameId() {
		return nameId;
	}

	public void setNameId(int nameId) {
		this.nameId = nameId;
	}

	public int getImageSourceId() {
		return imageSourceId;
	}

	public void setImageSourceId(int imageSourceId) {
		this.imageSourceId = imageSourceId;
	}

	public GridItemData(int nameId, int imageSourceId) {
		this.nameId = nameId;
		this.imageSourceId = imageSourceId;
	}

	public GridItemData(String name, Bitmap imageSource) {
		this.name = name;
		this.imageSource = imageSource;
	}
	
	public GridItemData(int nameId, Bitmap imageSource) {
		this.nameId = nameId;
		this.imageSource = imageSource;
	}
	
	public GridItemData(String name, int imageSourceId) {
		this.name = name;
		this.imageSourceId = imageSourceId;
	}
}
