/**
 * 
 */
package com.example.helloworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

/**
 * @author wanghy
 *
 */
public class MobileUtil {

	public static String getSDPath() { 
        File sdDir = null; 
        boolean sdCardExist = Environment.getExternalStorageState()   
            .equals(Environment.MEDIA_MOUNTED);   
        //�ж�sd���Ƿ���� 
        if (sdCardExist) {                               
        	sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼ 
        }   
        return sdDir.toString(); 
    }
	
	public static String readText(String file) {
	    
//    	file = getSDPath() + "/bak.txt";//�ļ�·��
        // Ҳ������String fileName = "mnt/sdcard/Y.txt";
        String res = "";
        FileInputStream in = null;
        try {
        	in = new FileInputStream(file);
            // FileInputStream fin = openFileInput(fileName);
            // ������Ͳ����ˣ�������FileInputStream
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            res = EncodingUtils.getString(buffer, "GBK");////��Y.txt�ı�������ѡ����ʵı��룬���������������
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if (in != null) {
        		try {
					in.close();//�ر���Դ
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	return res;
    }
}
