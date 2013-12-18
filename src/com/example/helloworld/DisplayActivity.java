package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Android实现左右滑动效果
 * @author Administrator
 *
 */
public class DisplayActivity extends Activity implements OnGestureListener {
	private ViewFlipper flipper;
	private GestureDetector detector;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        
        detector = new GestureDetector(this, this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);
		String e = getIntent().getStringExtra(MainActivity.ENGLISH);
		String c = getIntent().getStringExtra(MainActivity.CHINESE);
		if (!"".equals(e)) {
			flipper.addView(addTextView(e));
		} else {
			flipper.addView(addImageView(R.drawable.datu));
		}
		
		if (!"".equals(c)) {
			flipper.addView(addTextView(c));
		} else {
			flipper.addView(addImageView(R.drawable.datu));
		}
    }
    
    private View addTextView(String path) {
    	ScrollView sv = new ScrollView(this);
    	TextView tv = new TextView(this);
    	SpannableString msp = new SpannableString(MobileUtil.readFile(path, MobileUtil.GBK));
    	msp.setSpan(new TypefaceSpan("Arial"), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小（绝对值,单位：像素）
        msp.setSpan(new AbsoluteSizeSpan(30), 0, msp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(msp);
        tv.setLineSpacing(1f, 2f);
        tv.setPadding(20, 20, 20, 0);
    	sv.addView(tv);
    	sv.setHorizontalScrollBarEnabled(false);
    	sv.setVerticalScrollBarEnabled(false);
    	return sv;
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