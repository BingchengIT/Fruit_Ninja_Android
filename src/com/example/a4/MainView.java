/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
package com.example.a4;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/*
 * View of the main game area.
 * Displays pieces of fruit, and allows players to slice them.
 */
public class MainView extends View implements Observer {
    private final Model model;
    private final MouseDrag drag = new MouseDrag();

    private int game_limit = 0;
    
    // Constructor
    MainView(Context context, Model m) {
        super(context);

        // register this view with the model
        model = m;
        model.addObserver(this);
        //ScoreModel.addObserver(this);

        // TODO BEGIN CS349
        // test fruit, take this out before handing in!
        Fruit f1 = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
        f1.translate(830, 100);
        model.add(f1);
        
        Fruit f2 = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
        f2.translate(810, 200);
        model.add(f2);
        
        Fruit f3 = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
        f3.translate(810, 60);
        model.add(f3);

        
        // TODO END CS349

        // add controller
        // capture touch movement, and determine if we intersect a shape
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    	//System.out.println("action down");
                        //Log.d(getResources().getString(R.string.app_name), "Touch down");
                        drag.start(event.getX(), event.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                    	//System.out.println("action up");
                        //Log.d(getResources().getString(R.string.app_name), "Touch release");
                        drag.stop(event.getX(), event.getY());

                        // find intersected shapes
                        Iterator<Fruit> i = model.getShapes().iterator();
                        while(i.hasNext()) {
                            Fruit s = i.next();
                            if (s.intersects(drag.getStart(), drag.getEnd()) && !s.IsSliced_IsDirectory_down()) {
                            	//System.out.println("intersect happen!!!");
                            	
                        		Fruit NewFruit = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
                        		Random r = new Random();
                        		int Low = 50;
                            	int High = 430;
                            	int ver_low = 800;
                            	int ver_high = 900;
                            	int R1 = r.nextInt(High-Low) + Low;
                            	int R2 = r.nextInt(ver_high-ver_low) + ver_low;
                            	NewFruit.translate(R1, R2);
                            	
                            	Random rnd = new Random(); 
                            	int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                            	NewFruit.setFillColor(color);
                            	model.add(NewFruit);
                            	
//                            	Score = Score + 10;
                            	Score.getSharedScore().SetScore(10); 
                            	//s.setFillColor(Color.RED);
                                try {
                                    Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());
                                    
                                    // TODO BEGIN CS349
                                    // you may want to place the fruit more carefully than this
                                    
                                    // TODO END CS349
                                    model.add(newFruits[0]);
                                    model.add(newFruits[1]);
                                    for (Fruit f : newFruits) {
                                    	f.setFillColor(s.getFillColor());
                                    }
                                    // TODO BEGIN CS349
                                    // delete original fruit from model
                                    model.remove(s);
                                    // TODO END CS349

                                } catch (Exception ex) {
                                    Log.e("fruit_ninja", "Error: " + ex.getMessage());
                                }
                            }
                            else {
                                //System.out.println("intersect failed!!!");
                                //s.setFillColor(Color.BLUE);
                            }
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
    }


    // inner class to track mouse drag
    // a better solution *might* be to dynamically track touch movement
    // in the controller above
    class MouseDrag {
        private float startx, starty;
        private float endx, endy;

        protected PointF getStart() { return new PointF(startx, starty); }
        protected PointF getEnd() { return new PointF(endx, endy); }

        protected void start(float x, float y) {
            this.startx = x;
            this.starty = y;
        }

        protected void stop(float x, float y) {
            this.endx = x;
            this.endy = y;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background
        setBackgroundColor(Color.WHITE);
        
        // draw all pieces of fruit
        
        for (Fruit s : model.getShapes()) {
//        	canvas.concat(s.getTransform());
        	s.translate(s.H_Velocity(), s.Velocity());
        	if (s.Fruit_shouldbeRemoved()) {
        		model.remove(s);
        		game_limit ++;
        		//System.out.println(game_limit);
//        		if (game_limit == 5) {
//        		}
        		Fruit NewFruit = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
        		Random r = new Random();
        		int Low = 50;
            	int High = 430;
            	int ver_low = 800;
            	int ver_high = 900;
            	int R1 = r.nextInt(High-Low) + Low;
            	int R2 = r.nextInt(ver_high-ver_low) + ver_low;
            	NewFruit.translate(R1, R2);
            	
            	Random rnd = new Random(); 
            	int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            	NewFruit.setFillColor(color);
            	model.add(NewFruit);
        	}
        	if (s.IsSliced_IsDirectory_down() && s.Getdistance_between_topbound() > 800) {
        		model.remove(s);
        	}
            s.draw(canvas);

        }
    }

    public int getGame_limit() {
		return game_limit;
	}

	@Override
    public void update(Observable observable, Object data) {
        invalidate();
    }
}
