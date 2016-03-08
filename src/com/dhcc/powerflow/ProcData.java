package com.dhcc.powerflow;

import com.dhcc.model.MPC;
import com.dhcc.model.PROCD;

public class ProcData {
	private MPC _mpc;
	private PROCD _procd;
	private double[][] _bp,_bpp;
	
	public PROCD get_procd() {
		return _procd;
	}

	public void set_procd(PROCD _procd) {
		this._procd = _procd;
	}
	
	public MPC get_mpc() {
		return _mpc;
	}

	public void set_mpc(MPC _mpc) {
		this._mpc = _mpc;
	}

	public double[][] get_bp() {
		return _bp;
	}

	public void set_bp(double[][] _bp) {
		this._bp = _bp;
	}

	public double[][] get_bpp() {
		return _bpp;
	}

	public void set_bpp(double[][] _bpp) {
		this._bpp = _bpp;
	}

	public void ReadData(String filename){
		return;
	}
	
	public void InitData(){
		return;
	}
	
	public void makeB(){
		return ;
	}
	
	
}
