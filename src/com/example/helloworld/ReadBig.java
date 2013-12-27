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
	 * ������ȡ���ļ�
	 * 
	 * @param numerator ����
	 * @param denominator ��ĸ
	 * @throws Exception
	 */
	public static String batRead(String path, long numerator, int denominator) {

		StringBuffer sb = new StringBuffer();
		final int BUFFER_SIZE = 0x300000;// ��������СΪ3M
		try {
			File file = new File(path);
	
			/**
			 * 
			 * map(FileChannel.MapMode mode,long position, long size)
			 * 
			 * mode - �����ǰ�ֻ������ȡ/д���ר�ã�д��ʱ��������ӳ���ļ����ֱ�Ϊ FileChannel.MapMode �����������
			 * READ_ONLY��READ_WRITE �� PRIVATE ֮һ
			 * 
			 * position - �ļ��е�λ�ã�ӳ������Ӵ�λ�ÿ�ʼ������Ϊ�Ǹ���
			 * 
			 * size - Ҫӳ��������С������Ϊ�Ǹ����Ҳ����� Integer.MAX_VALUE
			 * 
			 * ���������ȡ�ļ���벿�����ݣ���������д�������ȡ�ı���1/8���ݣ���Ҫ����дmap(FileChannel.MapMode.
			 * READ_ONLY, f.length()*7/8,f.length()/8)
			 * 
			 * ���ȡ�ļ��������ݣ���Ҫ����дmap(FileChannel.MapMode.READ_ONLY, 0,f.length())
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
					if(beginByte < 0 || beginByte > 255) {//���һ������
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
			if((endByte < 0 || endByte > 255)) {//���һ������
				if (isCut(bytes, false)) {
					inputBuffer = (MappedByteBuffer) inputBuffer.limit(inputBuffer.capacity() - 1);
				}
			}
				
			byte[] dst = new byte[BUFFER_SIZE];// ÿ�ζ���3M������
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
	//			 new String(dst, 0, length, "GBK")��������ȡ�����汣����ַ��������Զ�����в���
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.print(Arrays.toString(dst));
//		long end = System.currentTimeMillis();

//		System.out.println("��ȡ�ļ��ļ����ݻ��ѣ�" + (end - start) + "����");
		return sb.toString();
	}
	
	/**
	 * �ж��Ƿ���Ҫ��ȡ
	 * 
	 * @param bytes ����
	 * @param isAsc ˳��������
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
			// ��ȡ�ļ���7/8��ʼ,������Ҳ���Ƕ�ȡ�ļ����1/8
			ReadBig.batRead(f, 0, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}