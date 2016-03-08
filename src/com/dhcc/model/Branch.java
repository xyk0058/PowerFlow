package com.dhcc.model;

public class Branch {
	private int bus_i;
	private int type;
	private double Pd;
	private double Qd;
	private double Gs;
	private double Bs;
	private double area;
	private double Vm;
	public int getBus_i() {
		return bus_i;
	}
	public void setBus_i(int bus_i) {
		this.bus_i = bus_i;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getPd() {
		return Pd;
	}
	public void setPd(double pd) {
		Pd = pd;
	}
	public double getQd() {
		return Qd;
	}
	public void setQd(double qd) {
		Qd = qd;
	}
	public double getGs() {
		return Gs;
	}
	public void setGs(double gs) {
		Gs = gs;
	}
	public double getBs() {
		return Bs;
	}
	public void setBs(double bs) {
		Bs = bs;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public double getVm() {
		return Vm;
	}
	public void setVm(double vm) {
		Vm = vm;
	}
	public double getVa() {
		return Va;
	}
	public void setVa(double va) {
		Va = va;
	}
	public double getBaseKV() {
		return baseKV;
	}
	public void setBaseKV(double baseKV) {
		this.baseKV = baseKV;
	}
	public double getZone() {
		return zone;
	}
	public void setZone(double zone) {
		this.zone = zone;
	}
	public double getVmax() {
		return Vmax;
	}
	public void setVmax(double vmax) {
		Vmax = vmax;
	}
	public double getVmin() {
		return Vmin;
	}
	public void setVmin(double vmin) {
		Vmin = vmin;
	}
	private double Va;
	private double baseKV;
	private double zone;
	private double Vmax;
	private double Vmin;
}
