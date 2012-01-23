package com.seavenois.tetris;

import android.graphics.drawable.Drawable;

public class Box {
	private int top, left, side, color;
	Drawable img;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getTop() {
		return top;
	}

	public int getLeft() {
		return left;
	}

	public int getSide() {
		return side;
	}
	
	public Box(int _top, int _left, int _side){
		top = _top;
		left = _left;
		side = _side;
		color = Values.COLOR_NONE;
	}
}
