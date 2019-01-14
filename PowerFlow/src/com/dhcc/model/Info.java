package com.dhcc.model;

public class Info {
	//系统节点的总数
	private int N;
	//变压器支路总数
	private int Nt=0;//TODO
	//系统中支路数，线路和并联电容器总数
	private int Nb;
	//发电机节点总数
	private int Ng;
	//负荷节点总数
	private int Nl;
	//系统平均电压
	private double V0;
	//收敛精确度
	private double eps;
	//pv节点个数
	private int Npv;
	//pq节点个数
	private int Npq;
	
//	public Info() {};
	
	
	public Info(int n, int nb, int nt, int ng, int nl, double v0, int npv, double eps) {
		this.N = n;
		this.Nb = nb;
		this.Nt = nt;
		this.Ng = ng;
		this.Nl = nl;
		this.V0 = v0;
		this.setNpv(npv);
		this.eps = eps;
		this.Npq = n-npv-1;
	}
	
	public int getNt() {
		return Nt;
	}

	public void setNt(int nt) {
		Nt = nt;
	}

	public int getNpq() {
		return Npq;
	}

	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	public int getNb() {
		return Nb;
	}
	public void setNb(int nb) {
		Nb = nb;
	}
	public int getNg() {
		return Ng;
	}
	public void setNg(int ng) {
		Ng = ng;
	}
	public int getNl() {
		return Nl;
	}
	public void setNl(int nl) {
		Nl = nl;
	}
	public double getV0() {
		return V0;
	}
	public void setV0(double v0) {
		V0 = v0;
	}
	public double getEps() {
		return eps;
	}
	public void setEps(double eps) {
		this.eps = eps;
	}

	public int getNpv() {
		return Npv;
	}

	public void setNpv(int npv) {
		Npv = npv;
	}
}
