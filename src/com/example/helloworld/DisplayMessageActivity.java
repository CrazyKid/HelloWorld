package com.example.helloworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class DisplayMessageActivity extends Activity implements OnGestureListener {

	private ViewFlipper flipper;
	private GestureDetector detector;
	private String enFile = "english";
	private String chFile = "chinese";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		detector = new GestureDetector(this, this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);

		flipper.addView(addTextView(enFile));
		flipper.addView(addTextView(chFile));
		
		//Get the message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(message);
		
		setContentView(textView);
	}
	
	public String getSDPath() { 
        File sdDir = null; 
        boolean sdCardExist = Environment.getExternalStorageState()   
            .equals(Environment.MEDIA_MOUNTED);   
        //判断sd卡是否存在 
        if (sdCardExist) {                               
        	sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
        }   
        return sdDir.toString(); 
    }
    
    private String readText(String file) {
    
    	file = getSDPath() + "/ebook/平淡生活.txt";//文件路径
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
            res = EncodingUtils.getString(buffer, "UTF-8");////依Y.txt的编码类型选择合适的编码，如果不调整会乱码
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
    
    private View addTextView(String file) {
		TextView tv = new TextView(this);
		tv.setText(getSDPath());
//		tv.setText(file);
		return tv;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			this.flipper.showNext();
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			this.flipper.showPrevious();
			return true;
		}
		
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

}
