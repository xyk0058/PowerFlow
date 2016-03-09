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
	private double[] Qs=new double[30];
	private double[] Ql={0.012,0.016,0.000,0.109,0.000,0.020,0.075,0.016,0.025,0.018,
			  0.058,0.009,0.034,0.007,0.112,0.000,0.016,0.067,0.000,0.023,
			  0.000,0.000,0.009,0.019,0.127,0.190,0.300,0.000,0.000
			}; 
	
	
	public int getPQ() {
		return PQ;
	}

	public double[] getUs() {
		return Us;
	}

	public double[] getPs() {
		return Ps;
	}

	public double[] getPl() {
		return Pl;
	}

	public double[] getQs() {
		return Qs;
	}

	public double[] getQl() {
		return Ql;
	}

	public void TestData() {
		double K1=1,K2=1,K3=1,K4=1;          //变压器当前变比
		double Yc1=0,Yc2=0;
		
		double[][] branch  = {
				/*1*/		{29,24,0.0192,0.0575,0.0264,0.0264},      //一般支路参数
				/*2*/		{29,0,0.0452,0.1852,0.0204,0.0204},
				/*3*/		{24,1,0.0570,0.1737,0.0184,0.0184},
				/*4*/		{0,1,0.0132,0.0379,0.0042,0.0042},
				/*5*/		{24,25,0.0472,0.1983,0.0209,0.0209},
				/*6*/		{24,2,0.0581,0.1763,0.0187,0.0187},
				/*7*/		{1,2,0.0119,0.0414,0.0045,0.0045},
				/*8*/		{25,3,0.0460,0.1160,0.0102,0.0102},
				/*9*/		{2,3,0.0267,0.0820,0.0085,0.0085},
				/*10*/		{2,26,0.0120,0.0420,0.0045,0.0045},
				/*11*/		{4,27,0.0000,0.2080,0.0000,0.0000},
				/*12*/		{4,5,0.0000,0.1100,0.0000,0.0000},
				/*13*/		{6,28,0.0000,0.1400,0.0000,0.0000},
				/*14*/		{6,7,0.1231,0.2559,0.0000,0.0000},
				/*15*/		{6,8,0.0662,0.1304,0.0000,0.0000},
				/*16*/		{6,9,0.0945,0.1987,0.0000,0.0000},
				/*17*/		{7,8,0.2210,0.1997,0.0000,0.0000},
				/*18*/		{9,10,0.0824,0.1932,0.0000,0.0000},
				/*19*/		{8,11,0.1070,0.2185,0.0000,0.0000},
				/*20*/		{11,12,0.0639,0.1292,0.0000,0.0000},
				/*21*/		{12,13,0.0340,0.0680,0.0000,0.0000},
				/*22*/		{5,13,0.0936,0.2090,0.0000,0.0000},
				/*23*/		{5,10,0.0324,0.0845,0.0000,0.0000},
				/*24*/		{5,14,0.0348,0.0749,0.0000,0.0000},
				/*25*/		{5,15,0.0727,0.1499,0.0000,0.0000},
				/*26*/		{14,15,0.0116,0.0236,0.0000,0.0000},
				/*27*/		{8,16,0.1000,0.2020,0.0000,0.0000},
				/*28*/		{15,17,0.1150,0.1790,0.0000,0.0000},
				/*29*/		{16,17,0.1320,0.2700,0.0000,0.0000},
				/*30*/		{17,18,0.1885,0.3292,0.0000,0.0000},
				/*31*/		{18,19,0.2554,0.3800,0.0000,0.0000},
				/*32*/		{18,20,0.1093,0.2087,0.0000,0.0000},
				/*33*/		{20,22,0.2198,0.4153,0.0000,0.0000},
				/*34*/		{20,23,0.3202,0.6027,0.0000,0.0000},
				/*35*/		{22,23,0.2399,0.4533,0.0000,0.0000},
				/*36*/		{26,21,0.0636,0.2000,0.0214,0.0214},
				/*37*/		{2,21,0.0169,0.0599,0.0065,0.0065},
				/*38*/		{2,4,0.0000,K1*0.2080,(K1-1)/(K1*0.2080),(1-K1)/(K1*K1*0.2080)},      //变压器支路参数
				/*39*/		{2,5,0.0000,K2*0.5560,(K2-1)/(K2*0.5560),(1-K2)/(K2*K2*0.5560)},
				/*40*/		{1,6,0.0000,K3*0.2560,(K3-1)/(K3*0.2560),(1-K3)/(K3*K3*0.2560)},
				/*41*/		{21,20,0.0000,K4*0.3960,(K4-1)/(K4*0.3960),(1-K4)/(K4*K4*0.3960)},
									
							{5,5,0.0000,Yc1,0.0000,0.0000},       //5号节点有并联电容，电纳标幺值为0.19
							{17,17,0.0000,Yc2,0.0000,0.0000},       //17号节点有并联电容，电纳标幺值为0.02	
		};
		for (int i=0; i<branch.length; ++i) {
			branch[i][4] *= 2;
			branch[i][5] *= 2;
		}
		_mpc = new MPC(30, 10, 24);
		_mpc.setBranch(branch);
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
