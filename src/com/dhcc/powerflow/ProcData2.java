package com.dhcc.powerflow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.dhcc.model.MPC;

public class ProcData2 {
	private int N;
	private int Ngen;
	private MPC _mpc;
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
			
			N = nbus;
			Ngen=ngen;
			
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
	
	public void ProcData(){
		double[][] m_bus = _mpc.getBus();
		double[][] m_gen = _mpc.getGen();
		int[] index = new int[N];
		//节点电压赋初值，PV节点电压幅值已知，相角置0；
		//平衡节点电压幅值和相角均已知；
		//PQ节点电压幅值设1，相角设0.
		double[] Us = new double[3*N];
		//各节点有功负荷赋值
		double[] Pl = new double[N-1];
		//PV节点发电机有功赋初值（除平衡节点外）
		double[] Ps = new double[N-1];
		//发电机无功初值置0
		double[] Qs = new double[N-1];

		int _ref=N-1, _pq=0, _pv=N-2;
		for (int i=0; i<N; ++i) {
			if ((int)m_bus[i][1] == REF) {
				if (_ref>N-1) {
					System.out.println("结点输入错误 ProcData REF");
					break;
				}
				index[_ref] = (int)m_bus[i][0];
				Us[3*_ref] = m_bus[i][7];
				Us[3*_ref+1] = 0;
				++_ref;
			}else if ((int)m_bus[i][1] == PQ) {
				index[_pq] = (int)m_bus[i][0];
				Us[3*_pq] = 1;
				Us[3*_pq+1] = 0;
				Pl[_pq] = 0;
				Ps[_pq] = 0;
				Qs[_pq] = 0;
				++_pq;
			}else if ((int)m_bus[i][1] == PV) {
				index[_pv] = (int)m_bus[i][0];
				Us[3*_pv] = 1;
				Us[3*_pv+1] = 0;
				Pl[_pv] = m_bus[i][2];
				for (int j=0; j<Ngen; ++j) {
					if ((int)m_bus[i][0] == m_gen[j][0]) {
						Ps[_pv] = m_gen[j][1];
						//Qs[_pv] = m_gen[j][2];
						Qs[_pv] = 0;
						break;
					}
				}
				--_pv;		
			}else {
				System.out.println("结点输入错误 ProcData");
				break;
			}
			if (_pq > _pv){
				System.out.println("结点输入错误 ProcData PQ PV");
				break;
			}
		}
		//阻抗参数
		double[][] G = new double[N][N];
		double[][] B = new double[N][N];
		double[][] B1 = new double[N-1][N-1];
		double[][] B2 = new double[_pq][_pq];
		double[][] invB1 = new double[N-1][N-1];
		double[][] invB2 = new double[_pq][_pq];   
		
		
	}

}
