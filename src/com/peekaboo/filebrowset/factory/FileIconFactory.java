package com.peekaboo.filebrowset.factory;

import java.util.HashMap;
import java.util.Map;

import com.peekaboo.filebrowset.R;

public class FileIconFactory {

	public static int getIconResourceBySuffixName(String suffixName) {
		Map<String, Integer> icons = setupIcons();
		if (icons.containsKey(suffixName)) {
			return icons.get(suffixName);
		} else {
			return icons.get("UNKNOWN");
		}
		
	}
	
	private static Map<String, Integer> setupIcons() {
		Map<String, Integer> icons = new HashMap<String, Integer>();
		icons.put("MP3", R.drawable.mp3);
		icons.put("BMP", R.drawable.bmp);
		icons.put("CHM", R.drawable.chm);
		icons.put("GIF", R.drawable.gif);
		icons.put("JPG", R.drawable.jpg);
		icons.put("PDF", R.drawable.pdf);
		icons.put("TXT", R.drawable.txt);
		icons.put("PNG", R.drawable.png);
		icons.put("XLS", R.drawable.xls);
		icons.put("HTML", R.drawable.html);
		icons.put("ZIP", R.drawable.zip);
		icons.put("UNKNOWN", R.drawable.unknown);
		return icons;
	}
}
