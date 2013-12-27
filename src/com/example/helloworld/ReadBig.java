package com.example.helloworld;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.http.util.EncodingUtils;

public class ReadBig {
	
	public static String f = "d:/download/love and money.txt";

	/**
	 * 
	 * 分批读取大文件
	 * 
	 * @param numerator 分子
	 * @param denominator 分母
	 * @throws Exception
	 */
	public static String batRead(String path, long numerator, int denominator) {

		StringBuffer sb = new StringBuffer();
		final int BUFFER_SIZE = 0x300000;// 缓冲区大小为3M
		try {
			File file = new File(path);
	
			/**
			 * 
			 * map(FileChannel.MapMode mode,long position, long size)
			 * 
			 * mode - 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的
			 * READ_ONLY、READ_WRITE 或 PRIVATE 之一
			 * 
			 * position - 文件中的位置，映射区域从此位置开始；必须为非负数
			 * 
			 * size - 要映射的区域大小；必须为非负数且不大于 Integer.MAX_VALUE
			 * 
			 * 所以若想读取文件后半部分内容，如例子所写；若想读取文本后1/8内容，需要这样写map(FileChannel.MapMode.
			 * READ_ONLY, f.length()*7/8,f.length()/8)
			 * 
			 * 想读取文件所有内容，需要这样写map(FileChannel.MapMode.READ_ONLY, 0,f.length())
			 * 
			 */
//			int amount = 1024;
//			denominator = (int) (file.length()%amount==0?file.length()/amount:file.length()/amount + 1);
			long pos = file.length() * numerator / denominator;
			long size = file.length() / denominator;
			MappedByteBuffer inputBuffer = new RandomAccessFile(file, "r")
				.getChannel().map(FileChannel.MapMode.READ_ONLY,
						pos, size
				// f.length()*7/8,f.length()/8
				);
	//			inputBuffer = new RandomAccessFile(f, "r")
	//			.getChannel().map(FileChannel.MapMode.READ_ONLY,
	//					f.length() * numerator / denominator, f.length() / denominator
	//			// f.length()*7/8,f.length()/8
	//			);
			
			int capacity = inputBuffer.capacity();
			byte[] bytes = new byte[capacity];
			for (int offset = 0; offset < capacity; offset += BUFFER_SIZE) {
				if (capacity - offset >= BUFFER_SIZE) {
					for (int i = 0; i < BUFFER_SIZE; i++) {
						bytes[i] = inputBuffer.get(offset + i);
					}
				} else {
					for (int i = 0; i < capacity - offset; i++) {
						bytes[i] = inputBuffer.get(offset + i);
					}
				}
			}
			
				byte beginByte = inputBuffer.get(0);
				if (pos != 0) {
					if(beginByte < 0 || beginByte > 255) {//组成一个汉字
						if (isCut(bytes, true)) {
							pos -= 1;
							size += 3;
						}
					}
				}
			inputBuffer = new RandomAccessFile(file, "r")
				.getChannel().map(FileChannel.MapMode.READ_ONLY, 
				pos, size
			// f.length()*7/8,f.length()/8
			);
			byte endByte = inputBuffer.get(inputBuffer.capacity() - 1);
			if((endByte < 0 || endByte > 255)) {//组成一个汉字
				if (isCut(bytes, false)) {
					inputBuffer = (MappedByteBuffer) inputBuffer.limit(inputBuffer.capacity() - 1);
				}
			}
				
			byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容
	//		long start = System.currentTimeMillis();
	
			int limit = inputBuffer.limit();
			
			for (int offset = 0; offset < limit; offset += BUFFER_SIZE) {
	
				if (limit - offset >= BUFFER_SIZE) {
					for (int i = 0; i < BUFFER_SIZE; i++) {
						dst[i] = inputBuffer.get(offset + i);
					}
				} else {
					for (int i = 0; i < limit - offset; i++) {
						dst[i] = inputBuffer.get(offset + i);
					}
				}
	
				int length = (limit % BUFFER_SIZE == 0) ? BUFFER_SIZE
						: limit % BUFFER_SIZE;
				
				sb.append(EncodingUtils.getString(dst, 0, length, "GBK"));
				
	//			System.out.print(sb);
	//			 new String(dst, 0, length, "GBK")这样可以取出缓存保存的字符串，可以对其进行操作
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.print(Arrays.toString(dst));
//		long end = System.currentTimeMillis();

//		System.out.println("读取文件文件内容花费：" + (end - start) + "毫秒");
		return sb.toString();
	}
	
	/**
	 * 判断是否需要截取
	 * 
	 * @param bytes 数组
	 * @param isAsc 顺序还是逆序
	 * @return
	 */
	private static boolean isCut(byte[] bytes, boolean isAsc) {
		int count = 0;
		if (isAsc) {
			for (byte b : bytes) {
				if (b < 0 || b > 255) {
					count++;
				} else {
					break;
				}
			}
		} else {
			for (int i = bytes.length - 1; i > 0; i--) {
				if (bytes[i] < 0 || bytes[i] > 255) {
					count++;
				} else {
					break;
				}
			}
		}
		return count % 2 != 0;
	}

	public static void main(String[] args) {
		try {
			// 读取文件从7/8开始,到结束也就是读取文件最后1/8
			ReadBig.batRead(f, 0, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}