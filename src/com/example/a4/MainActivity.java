/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
package com.example.a4;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import com.example.a4complete.R;

public class MainActivity extends Activity {
    private Model model;
    private MainView mainView;
    private TitleView titleView;
    public static Point displaySize;
    
   // public EditText txtInput;
    private Timer timer;
    
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setTitle("CS349 A4 Demo");
        
        // save display size
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);

        // initialize model
        model = new Model();
        
        // set view
        setContentView(R.layout.main);
        
    }
    
 

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // create the views and add them to the main activity
        titleView = new TitleView(this.getApplicationContext(), model);
        ViewGroup v1 = (ViewGroup) findViewById(R.id.main_1);
        v1.addView(titleView);

        mainView = new MainView(this.getApplicationContext(), model);
        ViewGroup v2 = (ViewGroup) findViewById(R.id.main_2);
        v2.addView(mainView);
        

        
        
        timer = new Timer();
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
				        if(mainView.getGame_limit() == 5) {
				        	//System.out.println("OMG");
				        	timer.cancel();
				        }
						// TODO Auto-generated method stub
						mainView.update(null, null);
					}
				});
			}
		}, 500,(1000 / 60));
        // notify all views
        

        model.initObservers();
    }
}
