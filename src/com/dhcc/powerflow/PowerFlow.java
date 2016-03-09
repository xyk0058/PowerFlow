package com.dhcc.powerflow;

import java.text.NumberFormat;

import com.dhcc.util.MatrixUtil;

public class PowerFlow {
	
	public static void PQiterating(int n_PQ, int n_Bus, double[][] G, double[][] B, 
			double[] Ps, double[] PL,double[] Qs, double[] QL, double[] Us, double eps) {
		int n = n_Bus;
		double[][] Bp = new double[n-1][n-1];
		double[][] Bpp = new double[n_PQ][n_PQ];
		double[] deltaP = new double[n];
		double[] deltaQ = new double[n_PQ];
		double[] U = new double[n];
		double[] F = new double[n];		//fai
		double[] deltaF = new double[n];
		double[] deltaU = new double[n];
		double[][] invBp, invBpp;
		
		//初始化迭代参数
		for (int i=0; i<n; ++i) {
			U[i] = Us[2*i];
			F[i] = Us[2*i+1];
		}
		
		for (int i=0; i<n-1; ++i) {
	        for (int j=0; j<n-1; ++j) {
	            Bp[i][j] = B[i][j];
	            if (i<n_PQ && j<n_PQ) {
	                Bpp[i][j] = B[i][j];
	            }
	        }
		}
		
		
		
		//求B'和B''矩阵的逆矩阵
		invBp = MatrixUtil.Inverse(Bp);
		invBpp = MatrixUtil.Inverse(Bpp);
		
		
		System.out.println("B");
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(6);
		nf.setMaximumFractionDigits(6);
		for (int i=0; i<n_PQ; ++i) {
			for(int j=0; j<n_PQ; ++j)
				System.out.print(nf.format(invBpp[i][j]) + " ");
			System.out.println();
		}

		
		double deltaPQU; 
		int k = 0;
		while (k < 100) {
			k++;
			for(int i=0; i<n-1; ++i)
			{
				double tmp1 = 0, tmp2 = 0;
				for(int j=0; j<n; ++j)
				{
					tmp1 += U[j]*(G[i][j]*Math.cos(F[i]-F[j])+B[i][j]*Math.sin(F[i]-F[j]));        //计算Pi
					tmp2 += U[j]*(G[i][j]*Math.sin(F[i]-F[j])-B[i][j]*Math.cos(F[i]-F[j]));         //计算Qi
				}
				deltaP[i] = Ps[i] - PL[i] - U[i] * tmp1;      //计算△Pi
				if(i < n_PQ) {
					deltaQ[i] = Qs[i] - QL[i] - U[i] * tmp2;
				}
			}
			deltaPQU = 0;
			for (int i=0; i<n-1; ++i) {
				deltaPQU = Math.max(Math.abs(deltaP[i]), deltaPQU);
				if (i < n_PQ) deltaPQU = Math.max(Math.abs(deltaQ[i]), deltaPQU);
			}
			if (deltaPQU < eps) {
				break;
			} else {
				//求P,δ修正值
	            for (int i=0; i<n-1; ++i) deltaP[i] = deltaP[i] / U[i];        //△Pi/Vi
	            for (int i=0; i<n-1; ++i) deltaF[i] = 0;        //△δ初值置0
	            for (int i=0; i<n-1; ++i) {
	                for(int j=0; j<n-1; ++j) deltaF[i] += (-invBp[i][j]) * deltaP[j];      //求Vi*△δi
	                deltaF[i] = deltaF[i] / U[i];          //求△δ
	            }
	            for (int i=0;i<n-1;i++) F[i]+=deltaF[i];           //修正δi
	            
	            //Q,U修正值
	            for (int i=0; i<n_PQ; ++i) deltaQ[i] = deltaQ[i]/U[i];      //△Qi/Vi
	            for (int i=0; i<n_PQ; ++i) deltaU[i] = 0;               //dU初值置0
	            for (int i=0; i<n_PQ; i++) {
	                for(int j=0; j<n_PQ; ++j) deltaU[i] += (-invBpp[i][j]) * deltaQ[j];
	            }
	            for (int i=0;i<n_PQ;i++) U[i]+=deltaU[i];      //修正Ui
//	            System.out.println("\r\n" + k + "U:");
//	            for (int i=0; i<n-1; ++i) System.out.print(deltaU[i] + " ");
	            //System.out.println("U:");
//	    		for (int i=0; i<n; ++i) {
//	    			System.out.print(U[i] + " ");
//	    		}
//	    		System.out.println();
			}
		}

		nf.setMinimumFractionDigits(4);
		nf.setMaximumFractionDigits(4);
		System.out.println("U:");
		for (int i=0; i<n; ++i) {
			System.out.print(nf.format(U[i]) + "    ");
		}
		System.out.println("\r\nF:");
		for (int i=0; i<n; ++i) {
			System.out.print(nf.format(F[i]/3.1415926*180) + "    ");
		}
	}
	
	public static void main(String[] args) {
//		double[][] Bp = {{-32.5,5,5,7.5},
//						 {5,-38.75,30,0},
//						 {5,30,-38.75,3.75},
//						 {7.5,0,3.75,-11.25}};
//		double[][] Bpp = {{-32.5,5,5,7.5},
//						 {5,-38.75,30,0},
//						 {5,30,-38.75,3.75},
//						 {7.5,0,3.75,-11.25}};
//		double[][] G = {{6.25,-5,-1.25,0,0},
//						{-5,10.834,-1.667,-1.667,-2.5},
//						{-1.25,-1.667,12.917,-10,0},
//						{0,-1.667,-10,12.917,-1.25},
//						{0,-2.5,0,-1.25,3.75}};
//		double[][] B = {{-18.75,15,3.75,0,0},
//						{15,-32.5,5,5,7.5},
//						{3.75,5,-38.75,30,0},
//						{0,5,30,-38.75,3.75},
//						{0,7.5,0,3.75,-11.25}};
		ProcData2 pd2 = new ProcData2();
		pd2.TestData();
		int length = pd2.get_mpc().getBranch().length;
		PQiterating(24, 30, pd2.getYG(length), pd2.getYB(length), pd2.getPs(), pd2.getPl(), pd2.getQs(), pd2.getQl(), pd2.getUs(), 0.00001);
	}
	
}
