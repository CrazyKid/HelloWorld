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
        //判断sd卡是否存在 
        if (sdCardExist) {                               
        	sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
        }   
        return sdDir.toString(); 
    }
	
	public static String readText(String file) {
	    
//    	file = getSDPath() + "/bak.txt";//文件路径
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
            res = EncodingUtils.getString(buffer, "GBK");////依Y.txt的编码类型选择合适的编码，如果不调整会乱码
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if (in != null) {
        		try {
					in.close();//关闭资源
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    	return res;
    }
}
