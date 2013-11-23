package com.peekaboo.filebrowset.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.peekaboo.filebrowset.AscDescOrder;
import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.FileType;
import com.peekaboo.filebrowset.GridItemData;
import com.peekaboo.filebrowset.MyApplication;
import com.peekaboo.filebrowset.SortOrder;
import com.peekaboo.filebrowset.data.GoUpItemData;

public class ItemSorterUtil {
	private static final Map<SortOrder, Comparator<GridItemData>> ASC_COMPARATORS = new HashMap<SortOrder, Comparator<GridItemData>>();
	private static final Map<SortOrder, Comparator<GridItemData>> DESC_COMPARATORS = new HashMap<SortOrder, Comparator<GridItemData>>();

	static {
		Comparator<GridItemData> asc_name = new Comparator<GridItemData>() {

			@Override
			public int compare(GridItemData file1, GridItemData file2) {
				if (MyApplication.isFirstFolder()) {
					if (file1 instanceof GoUpItemData && file2 instanceof FileItemData) {
						return -1;
					} else if (file2 instanceof GoUpItemData && file1 instanceof FileItemData) {
						return 1;
					} else if (file1 instanceof FileItemData && file2 instanceof FileItemData){
						if (FileType.FOLDER.equals(((FileItemData)file1).getFileType()) && FileType.FILE.equals(((FileItemData)file2).getFileType())) {
							return -1;
						} else if (FileType.FILE.equals(((FileItemData)file1).getFileType()) && FileType.FOLDER.equals(((FileItemData)file2).getFileType())) {
							return 1;
						} else if (FileType.FOLDER.equals(((FileItemData)file1).getFileType())
								&& FileType.FOLDER.equals(((FileItemData)file2).getFileType())) {
							return file1.getName().compareToIgnoreCase(file2.getName());
						}
					}
				} else {
					if (file1 instanceof GoUpItemData && file2 instanceof FileItemData) {
						return 1;
					} else {
						return -1;
					}
				}
				return file1.getName().compareToIgnoreCase(file2.getName());
			}

		};

		Comparator<GridItemData> desc_name = new Comparator<GridItemData>() {

			@Override
			public int compare(GridItemData file1, GridItemData file2) {
				if (FileType.FOLDER.equals(((FileItemData)file1).getFileType()) && FileType.FILE.equals(((FileItemData)file2).getFileType())) {
					return 1;
				} else if (FileType.FILE.equals(((FileItemData)file1).getFileType()) && FileType.FOLDER.equals(((FileItemData)file2).getFileType())) {
					return -1;
				} else if (FileType.FOLDER.equals(((FileItemData)file1).getFileType()) && FileType.FOLDER.equals(((FileItemData)file2).getFileType())) {
				}

				return 0;
			}

		};

		ASC_COMPARATORS.put(SortOrder.NAME, asc_name);
		DESC_COMPARATORS.put(SortOrder.NAME, desc_name);
	}

	public static Comparator<GridItemData> getComparator(SortOrder order, AscDescOrder ascOrder) {
		return ItemSorterUtil.ASC_COMPARATORS.get(order);
	}
}
