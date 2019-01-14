package com.dhcc.model;

public class Tran {
	private int from;
	private int to;
	private double R;
	private double X;
	private double K;
	public Tran() {}
	public Tran(int f,int t,double r,double x,double k) {
		from = f;
		to = t;
		R = r;
		X = x;
		K = k;
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
	public double getK() {
		return K;
	}
	public void setK(double k) {
		K = k;
	}
}
