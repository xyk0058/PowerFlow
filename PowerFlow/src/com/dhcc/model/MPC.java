package com.dhcc.model;

public class MPC {
	private double[][] bus = null;
	private double[][] branch = null;
	private double[][] gen = null;
	
	public MPC (int nbus, int ngen, int nbranch) {
		setBus(new double[nbus][14]);
		setBranch(new double[nbranch][14]);
		setGen(new double[ngen][22]);
	}

	public double[][] getBus() {
		return bus;
	}

	public void setBus(double[][] bus) {
		this.bus = bus;
	}

	public double[][] getBranch() {
		return branch;
	}

	public void setBranch(double[][] branch) {
		this.branch = branch;
	}

	public double[][] getGen() {
		return gen;
	}

	public void setGen(double[][] gen) {
		this.gen = gen;
	}
	
}
