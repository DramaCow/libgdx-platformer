// Sam Coward - 2015

package com.DramaCow.maths;

public class Rect {

	public float x, y;
	public float w, h;
	
	public Rect() {
		this.x = this.y = 0.0f;
		this.w = this.h = 0.0f;
	}

	public Rect(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Rect(Rect that) {
		this.x = that.x;
		this.y = that.y;
		this.w = that.w;
		this.h = that.h;
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
		
	public boolean contains(Rect r) {
		return contains(r.x, r.y, r.w, r.h);
	}

	public boolean contains(float x, float y) {
		return contains(x, y, 0.0f, 0.0f);
	}

	public boolean overlaps(float x, float y, float w, float h) {
		return this.x < x + w && this.x + this.w > x && this.y < y + h && this.y + this.h > y;
	}

	public boolean overlaps(Rect that) {
		return this.x < that.x + that.w && this.x + this.w > that.x && this.y < that.y + that.h && this.y + this.h > that.y;
	}	
}
