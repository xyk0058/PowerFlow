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
	
	private double[] Us={1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,
			1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,
			1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0000,0,1.0500,0
			};
	private double[] Ps={0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,
			  0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,0.0000,
			  0.0000,0.0000,0.0000,0.0000,0.5756,0.2456,0.3500,0.1793,0.1691
			}; 
	private double[] Pl={0.024,0.076,0.000,0.228,0.000,0.058,0.112,0.062,0.082,0.035,
			  0.090,0.032,0.095,0.022,0.175,0.000,0.032,0.087,0.000,0.035,
			  0.000,0.000,0.024,0.106,0.217,0.942,0.300,0.000,0.000
			}; 
	private double[] Qs={0}; 
	private double[] Ql={0.012,0.016,0.000,0.109,0.000,0.020,0.075,0.016,0.025,0.018,
			  0.058,0.009,0.034,0.007,0.112,0.000,0.016,0.067,0.000,0.023,
			  0.000,0.000,0.009,0.019,0.127,0.190,0.300,0.000,0.000
			}; 
	
	public void TestData() {
		double K1=1,K2=1,K3=1,K4=1;          //变压器当前变比
		double Yc1=0,Yc2=0;
		
		
	}
	
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
	
	public double[][] getYG(int n_Bus) {
		double[][] G = new double[n_Bus][n_Bus];
		for (int i=0; i<n_Bus; ++i) {
			for (int j=0; j<n_Bus; ++j) {
				G[i][j] = 0;
			}
		}
		double[][] branch = _mpc.getBranch();
		for (int i=0; i<n_Bus; ++i) {
			if(branch[i][0] != branch[i][1])      //左节点号与右节点号不同
	        {
	            double Z2 = (branch[i][2])*(branch[i][2])+(branch[i][3])*(branch[i][3]);   //阻抗的平方
	            //串联阻抗等效导纳值
	            //非对角元素
	            G[(int) branch[i][0]][(int) branch[i][1]] = (-branch[i][2])/Z2;
	            G[(int) branch[i][1]][(int) branch[i][0]] = (-branch[i][2])/Z2;
	            //对角元素
	            G[(int) branch[i][0]][(int) branch[i][0]] += branch[i][2]/Z2;
	            G[(int) branch[i][1]][(int) branch[i][1]] += branch[i][2]/Z2;
	        }
		}
		return G;
	}
	
	public double[][] getYB(int n_Bus) {
		double[][] B = new double[n_Bus][n_Bus];
		for (int i=0; i<n_Bus; ++i) {
			for (int j=0; j<n_Bus; ++j) {
				B[i][j] = 0;
			}
		}
		double[][] branch = _mpc.getBranch();
		for (int i=0; i<n_Bus; ++i) {
			if(branch[i][0] != branch[i][1])      //左节点号与右节点号不同
	        {
	            double Z2 = (branch[i][2])*(branch[i][2])+(branch[i][3])*(branch[i][3]);   //阻抗的平方
	          //串联阻抗等效导纳值
	            //非对角元素
	            B[(int) branch[i][0]][(int) branch[i][1]] = branch[i][3]/Z2;
	            B[(int) branch[i][1]][(int) branch[i][0]] = branch[i][3]/Z2;
	            //对角元素
	            B[(int) branch[i][0]][(int) branch[i][0]] += (-branch[i][3]/Z2);
	            B[(int) branch[i][1]][(int) branch[i][1]] += (-branch[i][3]/Z2);
	            //节点自导纳需加上充电导纳值
	            B[(int) branch[i][0]][(int) branch[i][0]] += branch[i][4]/2.0;
	            B[(int) branch[i][1]][(int) branch[i][1]] += branch[i][4]/2.0;
	        }
	        else           //左节点号=右节点号，即节点有并联阻抗的情况
	        {
	            B[(int) branch[i][0]][(int) branch[i][0]] += branch[i][3];
	        }
		}
		return B;
	}
	
	public void ProcData(){
		double[][] m_bus = _mpc.getBus();
		double[][] m_gen = _mpc.getGen();
		int[] index = new int[N];
		//节点电压赋初值，PV节点电压幅值已知，相角置0；
		//平衡节点电压幅值和相角均已知；
		//PQ节点电压幅值设1，相角设0.
		Us = new double[2*N];
		//各节点有功负荷赋值
		Pl = new double[N-1];
		//各节点无功负荷赋值
		Ql = new double[N-1];
		//PV节点发电机有功赋初值（除平衡节点外）
		Ps = new double[N-1];
		//发电机无功初值置0
		Qs = new double[N-1];

		int _ref=N-1, _pq=0, _pv=N-2;
		for (int i=0; i<N; ++i) {
			if ((int)m_bus[i][1] == REF) {
				if (_ref>N-1) {
					System.out.println("结点输入错误 ProcData REF");
					break;
				}
				index[_ref] = (int)m_bus[i][0];
				Us[2*_ref] = m_bus[i][7];
				Us[2*_ref+1] = 0;
				++_ref;
			}else if ((int)m_bus[i][1] == PQ) {
				index[_pq] = (int)m_bus[i][0];
				Us[2*_pq] = 1;
				Us[2*_pq+1] = 0;
				Pl[_pq] = m_bus[i][2];
				Ql[_pv] = m_bus[i][3];
				Ps[_pq] = 0;
				Qs[_pq] = 0;
				++_pq;
			}else if ((int)m_bus[i][1] == PV) {
				index[_pv] = (int)m_bus[i][0];
				Us[2*_pv] = 1;
				Us[2*_pv+1] = 0;
				Pl[_pv] = m_bus[i][2];
				Ql[_pv] = m_bus[i][3];
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
		//double[][] G = new double[N][N];
		//double[][] B = new double[N][N];
		//double[][] B1 = new double[N-1][N-1];
		//double[][] B2 = new double[_pq][_pq];
		//double[][] invB1 = new double[N-1][N-1];
		//double[][] invB2 = new double[_pq][_pq];   	
	}
	
	
	
}
