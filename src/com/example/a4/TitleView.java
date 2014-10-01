/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
package com.example.a4;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.TextView;
import com.example.a4complete.R;

import java.util.Observable;
import java.util.Observer;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
public class TitleView extends TextView implements Observer {
	
   // private MainView mainview;
    
    // Constructor requires model reference
    public TitleView(Context context, Model model) {
        super(context);

        // set width, height of this view
        this.setHeight(235);
        this.setWidth(MainActivity.displaySize.x);
        //this.Score = MainActivity.
        // register with model so that we get updates
        model.addObserver(this);
        Score.getSharedScore().addObserver(this);
        
    }
    
    

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO BEGIN CS349
        
        // add high score, anything else you want to display in the title
        // TODO END CS349
        setBackgroundColor(Color.BLUE);
        setTextSize(28);
        int score = Score.getSharedScore().getScore();
        setText(getResources().getString(R.string.app_title) + "      "+
        		getResources().getString(R.string.High_score)+ " " + score);
        //setText(getResources().getString(R.string.High_score));
    }

    // Update from model
    // ONLY useful for testing that the view notifications work
    @Override
    public void update(Observable observable, Object data) {
        // TODO BEGIN CS349
        // do something more meaningful here
        // TODO END CS349
        //update score here
    	
        invalidate();
    }
}
