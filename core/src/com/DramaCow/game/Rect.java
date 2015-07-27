package com.DramaCow.game;

public class Rect {

	private float x, y;
	private float w, h;
	
	public Rect() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.w = 0.0f;
		this.h = 0.0f;
	}

	public Rect(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setW(float w) {
		this.w = w;
	}

	public void setH(float H) {
		this.h = h;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(float w, float h) {
		this.w = w;
		this.h = h;
	}

	public boolean contains(float x, float y, float w, float h) {
		return ((x > this.x && x < this.x + this.w) && (x + w > this.x && x + w < this.x + this.w))
			&& ((y > this.y && y < this.y + this.h) && (y + h > this.y && y + h < this.y + this.h));
	}

	public boolean overlaps(float x, float y, float w, float h) {
		return this.x < x + w && this.x + this.w > x && this.y < y + h && this.y + this.h > y;
	}

	public boolean overlaps(Rect that) {
		return this.x < that.x + that.w && this.x + this.w > that.x && this.y < that.y + that.h && this.y + this.h > that.y;
	}
}
