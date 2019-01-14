package com.dhcc.powerflow;

import com.dhcc.Global.Variable;
import com.dhcc.model.Info;
import com.dhcc.util.MatrixUtil;

public class PowerFlow {
	private int k = 0;
	private int kp = 1, kq = 1;
	
	public boolean CalcDp() {
		Info info = Variable.getPf_info();
		double B[][] = Variable.getB();
		double G[][] = Variable.getG();
		double Um[] = Variable.getOriU();
		double Ua[] = Variable.getOriTheta();
		double Pi[] = Variable.getP();
		double dp[] = new double[info.getN()-1];
		
		double max = 0;
		for (int i=0; i<info.getN()-1; ++i) {
			double sump = 0.0, di = Ua[i];
			for (int j=0; j<info.getN(); ++j) {
				double g = G[i][j], b = B[i][j], dj = Ua[j];
				double dij = di-dj;
				sump += Um[i]*Um[j]*(g*Math.cos(dij)+b*Math.sin(dij));
			}
			dp[i] = Pi[i] - sump; 
			if (Math.abs(dp[i]) > max)
				max = Math.abs(dp[i]);
		}
		Variable.setPtemp(dp);
		if (max < info.getEps())
			return true;
		return false;
	}
	
	public void CalcTheta() {
		double pi[] = Variable.getPtemp();
		double Um[] = Variable.getOriU();
		double Ua[] = Variable.getOriTheta();
		double dtheta[] = new double[pi.length];
		double invBp[][] = Variable.getInvBp();
		
		for (int i=0; i<pi.length; ++i) 
			pi[i] = -pi[i]/Um[i];
		
		//System.out.println("pi " + pi.length );
		//for (int i=0; i<pi.length; ++i) 
		//	System.out.print(pi[i] + " ");
		//System.out.println();
		
		pi = MatrixUtil.Multi(pi, invBp);
		for (int i=0; i<pi.length; ++i){
			dtheta[i] = pi[i]/Um[i];
			Ua[i] += dtheta[i];
		}
	}

	public boolean CalcDq() {
		Info info = Variable.getPf_info();
		double B[][] = Variable.getB();
		double G[][] = Variable.getG();
		double Um[] = Variable.getOriU();
		double Ua[] = Variable.getOriTheta();
		double Qi[] = Variable.getQ();
		double dq[] = new double[info.getNpq()];
		
		double max = 0;
		for (int i=0; i<info.getNpq(); ++i) {
			double sump = 0.0, di = Ua[i];
			for (int j=0; j<info.getN(); ++j) {
				double g = G[i][j], b = B[i][j], dj = Ua[j];
				double dij = di-dj;
				sump += Um[i]*Um[j]*(g*Math.sin(dij)-b*Math.cos(dij));
			}
			dq[i] = Qi[i] - sump; 
			if (Math.abs(dq[i]) > max)
				max = Math.abs(dq[i]);
		}
		Variable.setQtemp(dq);
		if (max < info.getEps())
			return true;
		return false;
	}

	public void CalcV() {
		double qi[] = Variable.getQtemp();
		double Um[] = Variable.getOriU();
		double invBpp[][] = Variable.getInvBpp();
		
		for (int i=0; i<qi.length; ++i) 
			qi[i] = -qi[i]/Um[i];
		
		//System.out.println("qi " + qi.length );
		//for (int i=0; i<qi.length; ++i) 
		//	System.out.print(qi[i] + " ");
		//System.out.println();
		
		qi = MatrixUtil.Multi(qi, invBpp);
		for (int i=0; i<qi.length; ++i)
			Um[i] += qi[i];
	}
	
	public void run() {
		k = 0;
		while (k < 10) {
			CalcDp();
			CalcTheta();
			CalcDq();
			CalcV();
			PrintInfo();
			k++;
		}
	}
	
	public void Run() {
		while (true) {
			//if (k>5) break;
			if (!CalcDp()) {
				CalcTheta();
				kq = 1;
				if (!CalcDq()) {
					CalcV();
					kp = 1;
//					System.out.print(136);
//					PrintInfo();
					++k;
				}else {
					kq = 0;
					if(kp == 0)
						break;
					else {
//						System.out.print(144);
//						PrintInfo();
						++k;
					}
				}
			}else{
				kp = 0;
				if (kq == 0)
					break;
				else {
					if (!CalcDq()) {
						CalcV();
						kp = 1;
//						System.out.print(157);
//						PrintInfo();
						++k;
					}else {
						kq = 0;
						if(kp == 0)
							break;
						else {
//							System.out.print(165);
//							PrintInfo();
							++k;
						}
					}
				}
			}
		}
		PrintInfo();
		System.out.println("The End! " + k);
	}
	
	public void PrintInfo() {
		
		double Um[] = Variable.getOriU();
		double Ua[] = Variable.getOriTheta();
		double Ptemp[] = Variable.getPtemp();
		double Qtemp[] = Variable.getQtemp();
		
		System.out.println(k + "'s iterator.");
		
		System.out.println("Um " + Um.length );
		for (int i=0; i<Um.length; ++i) 
			System.out.print(Um[i] + " ");
		System.out.println();
		System.out.println("Ua " + Ua.length );
		for (int i=0; i<Ua.length; ++i) 
			System.out.print(Ua[i] + " ");
		System.out.println();
		
		System.out.println("Ptemp " + Ptemp.length );
		for (int i=0; i<Ptemp.length; ++i) 
			System.out.print(Ptemp[i] + " ");
		System.out.println();
		System.out.println("Qtemp " + Qtemp.length );
		for (int i=0; i<Qtemp.length; ++i) 
			System.out.print(Qtemp[i] + " ");
		System.out.println();
	}
	
	public static void main(String[] args) {
		ProcData pd = new ProcData();
		pd.ReadData("/Users/xyk0058/Documents/PowerFlow/src/com/dhcc/casedata/case14.txt");
		//pd.ReadData("D:/Java/PowerFlow/src/com/dhcc/casedata/case14.txt");
		pd.InitData();
		pd.TestInfo();
		pd.AdmtMatrix();
		pd.CalcFactor();
		pd.InitOri();
//		pd.CalcPQ();
		pd.calcPQ();
		//pd.PrintInfo();
		PowerFlow pf = new PowerFlow();
		pf.Run();
		pf.PrintInfo();
	}
}