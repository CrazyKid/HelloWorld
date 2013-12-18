/**
 * 
 */
package com.example.helloworld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

/**
 * @author wanghy
 * 
 */
public class MobileUtil {
	
	public static final String GBK = "GBK";

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		// 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}
	
	public static String readFile(String filename, String encoding) {
        InputStreamReader isr = null;
        BufferedReader reader = null;
        String line = null;
        StringBuffer sb = new StringBuffer();
		try {
			isr = new InputStreamReader(new FileInputStream(filename), encoding);
	        reader = new BufferedReader(isr);
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				isr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return sb.toString();
    }

	public static String readByChannelMap(String filepath) {
		FileChannel fc = null;
		String res = null;
		try {
			fc = new FileInputStream(new File(filepath)).getChannel();
			ByteBuffer cb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).asReadOnlyBuffer();
			byte[] buffer = cb.array();
			res = EncodingUtils.getString(buffer, "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public static String read(String path) {
		File aFile = new File(path);
		FileInputStream inFile = null;
		String res = null;
		String str;
		try {
			inFile = new FileInputStream(aFile);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		FileChannel inChannel = inFile.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(1024);
		try {
			while (inChannel.read(buf) != -1) {
				str = ((ByteBuffer) (buf.flip())).asCharBuffer().toString();
				res = EncodingUtils.getString(str.getBytes(), "GBK");
				buf.clear();
			}
			inFile.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return res;
	}

	public static String readText(String file) {

		// file = getSDPath() + "/bak.txt";//文件路径
		// 也可以用String fileName = "mnt/sdcard/Y.txt";
		String res = "";
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			// FileInputStream fin = openFileInput(fileName);
			// 用这个就不行了，必须用FileInputStream
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "GBK");// 依Y.txt的编码类型选择合适的编码，如果不调整会乱码
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();// 关闭资源
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
}
