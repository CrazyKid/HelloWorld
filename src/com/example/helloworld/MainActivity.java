package com.example.helloworld;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public static final String BTN_ID = "BTN_ID";
	public static final String ENGLISH = "ENGLISH";
	public static final String CHINESE = "CHINESE";

	static SharedPreferences sp = null;
	static Editor editor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = getIntent();
        
        
        Context ctx = MainActivity.this;       
        sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        //¥Ê»Î ˝æ›
        editor = sp.edit();
        if (intent.getIntExtra(BTN_ID, 0) == R.id.selectEnglish) {
        	editor.putString("epath", intent.getStringExtra(FileDialog.RESULT_PATH));
        } else if (intent.getIntExtra(BTN_ID, 0) == R.id.selectChinese) {
        	editor.putString("cpath", intent.getStringExtra(FileDialog.RESULT_PATH));
        }
        editor.commit();
        
        
    	EditText e = (EditText) findViewById(R.id.edit_english);
    	e.setText(sp.getString("epath", ""));
    	EditText c = (EditText) findViewById(R.id.edit_chinese);
    	c.setText(sp.getString("cpath", ""));
    	
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
    	Intent intent = new Intent(this, DisplayActivity.class);
    	EditText e = (EditText) findViewById(R.id.edit_english);
    	editor.putString("epath", e.getText().toString());
    	EditText c = (EditText) findViewById(R.id.edit_chinese);
    	editor.putString("cpath", c.getText().toString());
    	editor.commit();
    	intent.putExtra(ENGLISH, sp.getString("epath", ""));
    	intent.putExtra(CHINESE, sp.getString("cpath", ""));
    	
        startActivity(intent);
    }
    
    public void selectFile(View view) {
    	Intent intent = new Intent(this, FileDialog.class);
    	intent.putExtra(BTN_ID, view.getId());
    	startActivity(intent);
    }
    
}
