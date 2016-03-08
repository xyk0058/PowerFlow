package com.dhcc.powerflow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

import com.dhcc.model.MPC;
import com.dhcc.model.PROCD;

public class ProcData {
	private MPC _mpc;
	private PROCD _procd;
	private double[][] _bp,_bpp;
	private int REF = 3;
	private int PQ = 1;
	private int PV =2;
	
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
	
	public void BusTypes() {
		//ref pv pq
		double[][] m_gen = _mpc.getGen();
		double[][] m_bus = _mpc.getBus();
		int[] m_pv = new int[m_bus.length];
		int[] m_pq = new int[m_bus.length];
		int m_pv_len = 0,m_pq_len = 0;
		//TODO gen status<=0
		int ref = -1;
		for(int i=0; i<m_gen.length; ++i) {
			if (m_gen[i][7]>0) {
				if ((int) m_bus[(int) m_gen[i][0]][1] == REF)
					ref = (int) m_gen[i][0];
				else if ((int) m_bus[(int) m_gen[i][0]][1] == PV)
					m_pv[m_pv_len++]=(int) m_gen[i][0];
				else 
					m_pq[m_pq_len++]=(int) m_gen[i][0];
			}else
				m_pq[m_pq_len++]=(int) m_gen[i][0];
		}
		if(ref != -1) {
			_procd.setRef(ref);
			_procd.setPq(Arrays.copyOf(m_pq, m_pq_len));
			_procd.setPv(Arrays.copyOf(m_pv, m_pv_len));
		} else {
			_procd.setRef(m_pv[0]);
			_procd.setPq(Arrays.copyOf(m_pq, m_pq_len));
			_procd.setPv(Arrays.copyOfRange(m_pv, 1, m_pv_len));
		}
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
