package com.dhcc.powerflow;

import com.dhcc.util.MatrixUtil;

public class PowerFlow {
	
	public static void PQiterating(int n_PQ, int n_Bus, double[][] G, double[][] B, double[] Ps, double[] PL,double[] Qs, double[] QL, double eps) {
		int n = n_Bus;
		double[][] Bp = new double[n_PQ][n_PQ];
		double[][] Bpp = new double[n][n];
		double[] deltaP = new double[n];
		double[] deltaQ = new double[n_PQ];
		double[] U = new double[n];
		double[] F = new double[n];		//fai
		double[] deltaF = new double[n];
		double[] deltaU = new double[n];
		double[][] invBp, invBpp;
		
		//初始化迭代参数
		for (int i=0; i<n; ++i) {
			U[i] = 1.0;
			F[i] = 0.0;
		}
		
		for (int i=0; i<n-1; ++i) {
	        for (int j=0; j<n-1; ++j) {
	            Bp[i][j] = B[i][j];
	            if (i<n_PQ && j<n_PQ) {
	                Bpp[i][j]=B[i][j];
	            }
	        }
		}
		
		//求B'和B''矩阵的逆矩阵
		invBp = MatrixUtil.Inverse(Bp);
		invBpp = MatrixUtil.Inverse(Bpp);
		//
		double deltaPQU; 
		int k = 0;
		while (k < 1) {
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
					deltaQ[i]=Qs[i]-QL[i]-U[i] * tmp2;
				}
			}
			deltaPQU = 0;
			for (int i=0; i<n; ++i) {
				deltaPQU = Math.max(Math.abs(deltaP[i]), deltaPQU);
				if(i < n_PQ) deltaPQU = Math.max(Math.abs(deltaQ[i]), deltaPQU);
			}
			if (deltaPQU < eps) {
				break;
			} else {
				//求P,δ修正值
	            for(int i=0; i<n-1; ++i) deltaP[i] = deltaP[i] / U[i];        //△Pi/Vi
	            for(int i=0; i<n-1; ++i) deltaF[i] = 0;        //△δ初值置0
	            for(int i=0; i<n-1; ++i) {
	                for(int j=0; j<n-1; ++j) deltaF[i] += (-invBp[i][j]) * deltaP[j];      //求Vi*△δi
	                deltaF[i] = deltaF[i] / U[i];          //求△δ
	            }
	            for(int i=0;i<n-1;i++) F[i]+=deltaF[i];           //修正δi
	            
	            //Q,U修正值
	            for(int i=0; i<n_PQ; ++i) deltaQ[i] = deltaQ[i]/U[i];      //△Qi/Vi
	            for(int i=0; i<n_PQ; ++i) deltaU[i]=0;               //dU初值置0
	            for(int i=0; i<n_PQ; i++) {
	                for(int j=0; j<n_PQ; ++i) deltaU[i]+=-invBpp[i][j]*deltaQ[j];
	            }
	            for(int i=0;i<n_PQ;i++) U[i]+=deltaU[i];      //修正Ui
			}
			
		}
	}
	
	public static void main(String[] args) {
		double[][] Bp = {{-32.5,5,5,7.5},
						 {5,-38.75,30,0},
						 {5,30,-38.75,3.75},
						 {7.5,0,3.75,-11.25}};
		double[][] Bpp = {{-32.5,5,5,7.5},
						 {5,-38.75,30,0},
						 {5,30,-38.75,3.75},
						 {7.5,0,3.75,-11.25}};
		double[][] G = {{6.25,-5,-1.25,0,0},
						{-5,10.834,-1.667,-1.667,-2.5},
						{-1.25,-1.667,12.917,-10,0},
						{0,-1.667,-10,12.917,-1.25},
						{0,-2.5,0,-1.25,3.75}};
		double[][] B = {{-18.75,15,3.75,0,0},
						{15,-32.5,5,5,7.5},
						{3.75,5,-38.75,30,0},
						{0,5,30,-38.75,3.75},
						{0,7.5,0,3.75,-11.25}};
	}
	
}
