package com.peekaboo.filebrowset.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.FileType;
import com.peekaboo.filebrowset.GridItemData;
import com.peekaboo.filebrowset.MyApplication;
import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.data.ViewType;
import com.peekaboo.filebrowset.factory.FileIconFactory;
import com.peekaboo.filebrowset.utils.FileUtil;

/**
 * 
 * @author peekaboo
 *
 */
public class FileBrowsetAdapter extends PeekabooAdapter {

	private Context mContext = null;

	public FileBrowsetAdapter(Context context, List<GridItemData> fileList) {
		super(fileList);
		this.mContext = context;
	}

	public boolean remove(FileItemData fileItemData) {
		if (mFileList.contains(fileItemData)) {
			return mFileList.remove(fileItemData);
		}
		this.notifyDataSetChanged();
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView file_thumbnails = null;
		TextView textview_file_name = null;

		LayoutInflater layout_inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = layout_inflater.inflate(
				R.layout.filebrowset_simple_list_item, null);
		if (MyApplication.getViewType().equals(ViewType.SimpleList)) {
			// file_thumbnails = (ImageView)
			// convertView.findViewById(R.id.file_thumbnails);
			// file_name = (TextView) convertView.findViewById(R.id.file_name);
			// if
			// (FileType.FOLDER.equals(mFileList.get(position).getFileType())) {
			// file_thumbnails.setImageResource(R.drawable.icon_folder);
			// } else {
			// file_thumbnails.setImageResource(R.drawable.icon_file);
			// }
			//
			// file_name.setText(mFileList.get(position).getName());
			//
			// convertView.setTag(mFileList.get(position));
		} else if (MyApplication.getViewType().equals(ViewType.Thumbnails)) {
			convertView = layout_inflater.inflate(
					R.layout.filebrowset_thumbnails_list_item, null);
		} else if (MyApplication.getViewType().equals(ViewType.DetailedList)) {
			convertView = layout_inflater.inflate(
					R.layout.filebrowset_detailed_list_item, null);
			TextView file_date = (TextView) convertView
					.findViewById(R.id.file_date);
			TextView file_size = (TextView) convertView
					.findViewById(R.id.file_size);
			if (mFileList.get(position) instanceof FileItemData) {
				file_date.setText(((FileItemData) mFileList.get(position))
						.getUpdateTime().toLocaleString());
				if (((FileItemData) mFileList.get(position)).getFileType()
						.equals(FileType.FILE)) {
					file_size.setText(formatSize(((FileItemData) mFileList
							.get(position)).getSize()));
				}
			}
		}

		file_thumbnails = (ImageView) convertView
				.findViewById(R.id.file_thumbnails);
		textview_file_name = (TextView) convertView
				.findViewById(R.id.file_name);
		String file_name = mFileList.get(position).getName();
		if (mFileList.get(position) instanceof FileItemData) {
			if (FileType.FILE == ((FileItemData) mFileList.get(position))
					.getFileType()) {
				file_thumbnails.setImageResource(FileIconFactory
						.getIconResourceBySuffixName(FileUtil.getFileExtension(file_name).toUpperCase()));
			} else {
				file_thumbnails.setImageResource(mFileList.get(position)
						.getImageSourceId());
			}
		} else {
			file_thumbnails.setImageResource(mFileList.get(position)
					.getImageSourceId());
		}

		textview_file_name.setText(file_name);

		convertView.setTag(mFileList.get(position));

		return convertView;
	}

	private static String formatSize(long size) {
		DecimalFormat decimalFormat = new DecimalFormat("#.0");
		double value_g = size / (1024.0 * 1024.0 * 1024.0);
		double value_m = size / (1024.0 * 1024.0);
		double value_k = size / 1024.0;
		if (value_g >= 1) {
			return decimalFormat.format(value_g) + "G";
		} else if (value_m >= 1) {

			return decimalFormat.format(value_m) + "M";
		} else if (value_k >= 1) {
			return decimalFormat.format(value_k) + "K";
		} else {
			if (size == 0) {
				return 0 + "B";
			} else {
				return decimalFormat.format(size) + "B";
			}
		}
	}
}