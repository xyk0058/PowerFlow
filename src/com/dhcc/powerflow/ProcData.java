package com.dhcc.powerflow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.dhcc.model.MPC;
import com.dhcc.model.PROCD;

public class ProcData {
	private MPC _mpc;
	private PROCD _procd;
	private double[][] _bp,_bpp;
	
	public void ReadData(String filename) {
		
		try {
			InputStreamReader instrr = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(instrr);
			String row = null;
			String[] rowdata = null;
			
			row = br.readLine();int nbus = Integer.parseInt(row);
			row = br.readLine();int ngen = Integer.parseInt(row);
			row = br.readLine();int nbranch = Integer.parseInt(row);
			
			_mpc = new MPC(nbus, ngen, nbranch);
			
			double[][] bus = _mpc.getBus();
			for (int i=0; i<nbus; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				for (int j=0; j<rowdata.length; ++j) {
					bus[i][j] = Double.parseDouble(rowdata[j]);
				}
			}
			double[][] gen = _mpc.getGen();
			for (int i=0; i<ngen; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				for (int j=0; j<rowdata.length; ++j) {
					gen[i][j] = Double.parseDouble(rowdata[j]);
				}
			}
			double[][] branch = _mpc.getBus();
			for (int i=0; i<nbranch; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				for (int j=0; j<rowdata.length; ++j) {
					branch[i][j] = Double.parseDouble(rowdata[j]);
				}
			}
			
			_mpc.setBus(bus);
			_mpc.setBranch(branch);
			_mpc.setGen(gen);
			
			br.close();
			instrr.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public void InitData() {
		return;
	}
	
	public void makeB() {
		return ;
	}
	
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
	
	
}
