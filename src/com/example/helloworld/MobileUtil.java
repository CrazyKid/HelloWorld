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
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

/**
 * @author wanghy
 * 
 */
public class MobileUtil {
	
	/**
	 * GBK����
	 */
	public static final String GBK = "GBK";
	
	/**
	 * ������
	 */
	public static final String END = "END";

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		// �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
		}
		return sdDir.toString();
	}
	
	/**
	 * ���ж�ȡtxt
	 * 
	 * @param filePath �ļ�
	 * @param encoding ����
	 * @param pages ҳ��
	 * @param rows ÿҳ����
	 * @return
	 */
	public static String readFile(String filePath, String encoding, int pages, int rows) {
        InputStreamReader isr = null;
        BufferedReader reader = null;
        String line = null;
        StringBuffer sb = new StringBuffer();
        ArrayList<String> list = new ArrayList<String>();
		try {
			isr = new InputStreamReader(new FileInputStream(filePath), encoding);
	        reader = new BufferedReader(isr);
	        while ((line = reader.readLine()) != null) {
	            list.add(line);
	        }
	        int count = pages * rows;
	        int size = list.size();
	        for (int i = 0; i < rows; i++) {
	        	if (count < size) {
		        	sb.append("\n");
					sb.append(list.get(count));
					count++;
	        	} else if (count == size) {
	        		break;
	        	} else {
	        		sb.append(END);
	        		break;
	        	}
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

		// file = getSDPath() + "/bak.txt";//�ļ�·��
		// Ҳ������String fileName = "mnt/sdcard/Y.txt";
		String res = "";
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			// FileInputStream fin = openFileInput(fileName);
			// ������Ͳ����ˣ�������FileInputStream
//			int length = in.available();
			byte[] buffer = new byte[1024];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "GBK");// ��Y.txt�ı�������ѡ����ʵı��룬���������������
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();// �ر���Դ
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
}
