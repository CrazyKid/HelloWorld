package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * Android实现左右滑动效果
 * @author Administrator
 *
 */
public class DisplayActivity extends Activity implements OnGestureListener {
	
	public static final int ROWS = 30;
	
	private ViewFlipper flipper;
	
	private GestureDetector detector;
	
	StringBuffer sb1 = new StringBuffer();
	
	StringBuffer sb2 = new StringBuffer();
	
	int start1 = 0;
	
	int start2 = 0;
	
	boolean isEnd1 = false;
	
	boolean isEnd2 = false;
	
	boolean isNight = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_display);
        
        detector = new GestureDetector(this, this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);
		String e = getIntent().getStringExtra(MainActivity.ENGLISH);
		String c = getIntent().getStringExtra(MainActivity.CHINESE);
		if (!"".equals(e)) {
			flipper.addView(addTextView(e, sb1, true));
		} else {
			flipper.addView(addImageView(R.drawable.datu));
		}
		
		if (!"".equals(c)) {
			flipper.addView(addTextView(c, sb2, false));
		} else {
			flipper.addView(addImageView(R.drawable.datu));
		}
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
    	super.onCreateOptionsMenu(menu);
    	getMenuInflater().inflate(R.menu.display, menu);
	    return true;
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			if (isNight) {
				this.setTheme(R.style.AppTheme);
				isNight = false;
			} else {
				this.setTheme(R.style.AppNightTheme);
				isNight = true;
			}
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
    }
    
    private View addTextView(final String path, final StringBuffer sb, final boolean flag) {
    	ScrollView sv = new ScrollView(this);
    	TextView tv = new TextView(this);
    	genContent(path, tv, sb);
    	sv.addView(tv);
    	sv.setHorizontalScrollBarEnabled(false);
    	sv.setVerticalScrollBarEnabled(false);
    	
    	sv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				ScrollView s = (ScrollView)v;
				View c = s.getChildAt(0);
				if (c.getMeasuredHeight() <= (v.getScrollY() + v.getHeight())) {
					String end1 = null;
					String end2 = null;
					switch (e.getAction()) {
						case MotionEvent.ACTION_DOWN:
							if (flag) {
								if (!isEnd1) {
									end1 = genContent(path, (TextView)c, sb, ++start1);
									if (MobileUtil.END.equals(end1)) {
										isEnd1 = true;
										break;
									}
								}
							} else {
								if (!isEnd2) {
									end2 = genContent(path, (TextView)c, sb, ++start2);
									if (MobileUtil.END.equals(end2)) {
										isEnd2 = true;
										break;
									}
								}
							}
							break;
						default:
							break;
					}
				}
				return false;
			}
		});
    	return sv;
    }
    
    private void genContent(String path, TextView tv, StringBuffer sb) {
    	sb.append(MobileUtil.readFile(path, MobileUtil.GBK, 0, ROWS));
    	SpannableString msp = new SpannableString(sb.toString());
    	msp.setSpan(new TypefaceSpan("Arial"), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    	//设置字体大小（绝对值,单位：像素）
    	msp.setSpan(new AbsoluteSizeSpan(30), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    	tv.setText(msp);
    	tv.setLineSpacing(1f, 2f);
    	tv.setPadding(20, 20, 20, 0);
    }

	private String genContent(String path, TextView tv, StringBuffer sb, int start) {
		String temp = MobileUtil.readFile(path, MobileUtil.GBK, start, ROWS);
		if (!MobileUtil.END.equals(temp)) {
			sb.append(temp);
	    	SpannableString msp = new SpannableString(sb.toString());
	    	msp.setSpan(new TypefaceSpan("Arial"), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	        //设置字体大小（绝对值,单位：像素）
	        msp.setSpan(new AbsoluteSizeSpan(30), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	        tv.setText(msp);
	        tv.setLineSpacing(1f, 2f);
	        tv.setPadding(20, 20, 20, 0);
		}
        return temp;
	}
    
    private View addImageView(int id) {
		ImageView iv = new ImageView(this);
		iv.setImageResource(id);
		return iv;
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	return this.detector.onTouchEvent(event);
    }
    
    @Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	detector.onTouchEvent(ev);
    	return super.dispatchTouchEvent(ev);
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