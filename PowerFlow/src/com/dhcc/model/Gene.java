package com.dhcc.model;

public class Gene {
	private int i;
	private int j;//type
	private double p;//有功功率
	private double q;//无功功率
	private double v;
	public Gene() {}
	
	public Gene(int i,int j,double p,double q,double v) {
		this.i=i;
		this.j=j;this.p=p;this.q=q;this.v=v;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public double getQ() {
		return q;
	}
	public void setQ(double q) {
		this.q = q;
	}
	public double getV() {
		return v;
	}
	public void setV(double v) {
		this.v = v;
	}
}
