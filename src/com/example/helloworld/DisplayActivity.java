package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_display_message);
        
        detector = new GestureDetector(this, this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);

		flipper.addView(addImageView(R.drawable.one));
		ScrollView sv = new ScrollView(this);
		sv.addView(addTextView(getIntent().getStringExtra(MainActivity.ENGLISH)));
		sv.addView(addTextView(getIntent().getStringExtra(MainActivity.CHINESE)));
		flipper.addView(sv);
		flipper.addView(addTextView(getIntent().getStringExtra(MainActivity.CHINESE)));
    }
    
    private View addTextView(String path) {
    	
    	TextView tv = new TextView(this);
    	tv.setText(MobileUtil.readText(path));
    	
    	return tv;
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