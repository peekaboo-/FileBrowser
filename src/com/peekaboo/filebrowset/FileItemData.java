package com.peekaboo.filebrowset;

import java.util.Date;

import android.graphics.Bitmap;

public class FileItemData extends GridItemData
{

    private String path = null;
    private Date updateTime = null;
    private FileType fileType = null;
    private String parentPath = null;
    private long size = 0;
    
    public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public FileItemData(int nameId, int imageSourceId) {
    	super(nameId, imageSourceId);
	}

	public FileItemData(String name, Bitmap imageSource) {
		super(name, imageSource);
	}
	
	public FileItemData(int nameId, Bitmap imageSource) {
		super(nameId, imageSource);
	}
	
	public FileItemData(String name, int imageSourceId) {
		super(name, imageSourceId);
	}
    
    public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public FileType getFileType()
    {
        return fileType;
    }

    public void setFileType(FileType fileType)
    {
        this.fileType = fileType;
    }

    public String getPath()
    {
        return path;
    }
    public void setPath(String path)
    {
        this.path = path;
    }
    public Date getUpdateTime()
    {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
    
    @Override
	public String toString() {
		return "FileItemData [path=" + path + ", updateTime=" + updateTime + ", fileType=" + fileType + ", parentPath="
				+ parentPath + ", size=" + size + "]";
	}
    
    
}
