package com.dhcc.model;

public class Branch {
	//输电线路端点
	private int from;
	//输电线路端点
	private int to;
	//输电线路电阻
	private double R;
	//输电线路电抗
	private double X;
	//输电线路充电电容的容纳 是电纳B
	//Z=R+jX
	//y=G+jB
	//默认G=0
	private double Y0;
	
	public Branch(){};
	public Branch(int from, int to, double R, double X, double Y0) {
		this.from = from;
		this.to = to;
		this.R = R;
		this.X = X;
		this.Y0 = Y0;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public double getR() {
		return R;
	}

	public void setR(double r) {
		R = r;
	}

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY0() {
		return Y0;
	}

	public void setY0(double y0) {
		Y0 = y0;
	}
	
	
}
