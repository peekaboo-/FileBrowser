package com.peekaboo.filebrowset.smb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.peekaboo.filebrowset.FileItemData;
import com.peekaboo.filebrowset.FileType;
import com.peekaboo.filebrowset.GridItemData;
import com.peekaboo.filebrowset.MyApplication;
import com.peekaboo.filebrowset.R;
import com.peekaboo.filebrowset.data.GoUpItemData;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class RemoteAccessUtil {

	// /**
	// * @param args
	// * @throws IOException
	// */
	// public static void main(String[] args) throws IOException
	// {
	// // smbGet1("smb://qingyue:jialin@192.168.0.52/test");
	// // smbGet1("smb://192.168.0.11/share/peekaboo/a.txt");
	// // smbGet1("smb://192.168.1.2/test/a.txt");
	// // smbGet1("smb://192.168.1.101/compass/AndroidManifest.xml");
	// smbGet("smb://192.168.1.4/values/", "/home/peekaboo/桌面/a/");
	// // smbPut("smb://192.168.0.11/share/peekaboo/",
	// "/home/qingyue/Desktop/aaa/");
	// }

	/**
	 * 方法一：
	 * 
	 * @param remoteUrl
	 *            远程路径 smb://192.168.0.11/share/peekaboo/a.txt
	 * @throws IOException
	 */
	public static String smbGetFileContent(String remoteUrl) throws IOException {
		SmbFile smbFile = new SmbFile(remoteUrl);
		int length = smbFile.getContentLength();
		StringBuilder stringBuilder = new StringBuilder();
		
		
		byte buffer[] = new byte[length];
		SmbFileInputStream in = new SmbFileInputStream(smbFile);
		while ((in.read(buffer)) != -1) {
			System.out.println(new String(buffer));
			stringBuilder.append(new String(buffer));
		}
		in.close();
		return stringBuilder.toString();
	}

	// 从共享目录下载文件
	/**
	 * 方法二： 路径格式：smb://192.168.0.11/share/peekaboo/a.txt
	 * smb://username:password@192.168.0.77/test
	 * 
	 * @param remoteUrl
	 *            远程路径
	 * @param localDir
	 *            要写入的本地路径
	 */
	public static void smbGet(String remoteUrl, String localDir) {
		try {
			SmbFile remoteFile = new SmbFile(remoteUrl);
			if (remoteFile.isFile()) {
				pullAndPushFile(localDir, remoteUrl, true);
			} else {
				pullAndPushFolder(localDir, remoteUrl, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<GridItemData> smbGet(String remoteUrl)
			throws IOException {
		if (!remoteUrl.endsWith("/")) {
			remoteUrl = remoteUrl + "/";
		}

		List<GridItemData> files = new ArrayList<GridItemData>();
		files.add(new GoUpItemData(R.string.go_up, R.drawable.icon_go_up));
		SmbFile remoteFile = new SmbFile(remoteUrl);
		SmbFile[] smbFiles = remoteFile.listFiles();
		for (SmbFile file : smbFiles) {
			FileItemData fileItemData = null;

			if (file.isFile()) {
				fileItemData = new FileItemData(file.getName(),
						R.drawable.icon_file);
				fileItemData.setFileType(FileType.FILE);
				fileItemData.setName(file.getName());
			} else {
				fileItemData = new FileItemData(file.getName().substring(0,
						file.getName().length() - 1), R.drawable.icon_folder);
				fileItemData.setFileType(FileType.FOLDER);
				fileItemData.setName(file.getName().substring(0,
						file.getName().length() - 1));
			}
			fileItemData.setPath(file.getPath());
			fileItemData.setSize(file.length());
			fileItemData.setParentPath(file.getParent());
			Long time = file.lastModified();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);

			fileItemData.setUpdateTime(calendar.getTime());
			files.add(fileItemData);
		}
		return files;
	}

	private static void pullAndPushFolder(String localParentPath,
			String remotePath, boolean isDownLoad) {
		try {
			SmbFile remoteFile = new SmbFile(remotePath);
			if (isDownLoad) {
				SmbFile[] smbFiles = remoteFile.listFiles();
				for (SmbFile file : smbFiles) {
					if (file.isFile()) {
						pullAndPushFile(localParentPath, file.getPath(), true);
					} else {
						localParentPath = localParentPath + file.getName();
						File localFile = new File(localParentPath);
						if (!localFile.exists()) {
							localFile.mkdir();
						}
						pullAndPushFolder(localParentPath,
								remoteFile + file.getName(), isDownLoad);
					}
				}
			} else {
				remoteFile = new SmbFile(remotePath);
				if (!remoteFile.exists()) {
					remoteFile.mkdir();
				}

				File localFile = new File(localParentPath);
				File[] files = localFile.listFiles();
				for (File file : files) {
					if (file.isFile()) {
						pullAndPushFile(localParentPath, remotePath
								+ File.separator + file.getName(), isDownLoad);
					} else {
						pullAndPushFolder(localParentPath + File.separator
								+ file.getName(), remotePath + File.separator
								+ file.getName(), isDownLoad);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void pullAndPushFile(String localParentPath,
			String remotePath, boolean isDownLoad) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		File localFile = null;
		try {
			SmbFile remoteFile = new SmbFile(remotePath);
			String fileName = remoteFile.getName();
			if (isDownLoad) {
				localFile = new File(localParentPath + fileName);
				inputStream = new BufferedInputStream(new SmbFileInputStream(
						remoteFile));
				outputStream = new BufferedOutputStream(new FileOutputStream(
						localFile));
			} else {
				localFile = new File(localParentPath + File.separator
						+ fileName);
				inputStream = new BufferedInputStream(new FileInputStream(
						localFile));
				outputStream = new BufferedOutputStream(
						new SmbFileOutputStream(new SmbFile(
								remoteFile.getPath())));
			}

			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream));
			String content = null;
			while ((content = bufferedReader.readLine()) != null) {
				bufferedWriter.write(content);
			}
			bufferedWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 向共享目录上传文件
	public static void smbPut(String remoteUrl, String localFilePath) {
		try {

			File localFile = new File(localFilePath);

			String fileName = localFile.getName();
			if (localFile.isFile()) {
				pullAndPushFile(localFilePath, remoteUrl + fileName, false);
			} else {
				pullAndPushFolder(localFilePath, remoteUrl + fileName, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	// 远程url smb://192.168.0.77/test
	// 如果需要用户名密码就这样：
	// smb://username:password@192.168.0.77/test

}
