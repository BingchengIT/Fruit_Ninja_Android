/**
 * CS349 Winter 2014
 * Bingcheng Zhu
 * University of Waterloo
 */

package com.example.a4;

import java.util.Observable;

public class Score extends Observable {
	private static int Score = 0;
	
	private static Score sharedScore;
	
	public static Score getSharedScore() {
		if (sharedScore == null) {
			sharedScore = new Score();
		}
		return sharedScore;
	}
	
	public int getScore() {
		return Score;
	}
	
	public void SetScore(int delta) {
		Score = Score + delta;
		setChanged();
		notifyObservers();
	}
}
