/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */
package com.example.a4;

import android.util.Log;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/*
 * Class the contains a list of fruit to display.
 * Follows MVC pattern, with methods to add observers,
 * and notify them when the fruit list changes.
 */
public class Model extends Observable {
    // List of fruit that we want to display
    private ArrayList<Fruit> shapes = new ArrayList<Fruit>();

    
   // private int S = 0;
    //private Timer timer = new Timer(); 
    
    //private fruit_task Ftask = new fruit_task();
    // Constructor
    Model() {
        shapes.clear();
        //timer.scheduleAtFixedRate(Ftask, 0, (long) (1000.0 / 60.0));
    }

//    private class fruit_task extends TimerTask {
//    	private int T = 0;
//    	public void run() {
//  		  T++; 
//  		  setChanged();
//  		  notifyObservers();
//  	  }
//    }
    
    // Model methods
    // You may need to add more methods here, depending on required functionality.
    // For instance, this sample makes to effort to discard fruit from the list.
    public void add(Fruit s) {
        shapes.add(s);
        setChanged();
        notifyObservers();
    }

    public void remove(Fruit s) {
        shapes.remove(s);
    }

    public ArrayList<Fruit> getShapes() {
        return (ArrayList<Fruit>) shapes.clone();
    }

    // MVC methods
    // Basic MVC methods to bind view and model together.
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    // a helper to make it easier to initialize all observers
    public void initObservers() {
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObserver(Observer observer) {
        super.deleteObserver(observer);
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
        setChanged();
        notifyObservers();
    }
}
