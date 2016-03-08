package com.dhcc.powerflow;

import com.dhcc.util.MatrixUtil;

public class PowerFlow {
	public static void PQiterating(double[][] Bp, double[][] Bpp, double[][] G, double[][] B) {
		int n = B.length;
		double[] P = new double[n];
		double[] deltaP = new double[n];
		double[] Q = new double[n];
		double[] deltaQ = new double[n];
		double[] U = new double[n];
		double[] F = new double[n];		//fai
		double[] deltaF = new double[n];
		double[] deltaU = new double[n];
		double[][] invBp, invBpp;
		//初始化迭代参数
		for (int i=0; i<n; ++i) {
			P[i] = 0.2;
			Q[i] = 0.2;
			U[i] = 1.0;
			F[i] = 0;
		}
		//求B'和B''矩阵的逆矩阵
		invBp = MatrixUtil.Inverse(Bp);
		invBpp = MatrixUtil.Inverse(Bpp);
		//
		boolean Kp = true, Kq = true;
		int k = 0;
		while (k <= 5) {
			k++;
			System.out.println(k + " iterator.");
			//calc deltaP
			for (int i=0; i<n; ++i) {
				deltaP[i] = P[i];
				for (int j=0; j<n; ++j) {
					deltaP[i] -= U[i] * U[j] * (G[i][j] * Math.sin(F[i]-F[j]) + B[i][j] * Math.cos(F[i]-F[j]));
				}
			}
			for (int i=0; i<n; ++i) {
				P[i] = deltaP[i];
				System.out.print(deltaP[i] + " ");
				deltaP[i] = deltaP[i] / U[i];
			}
			System.out.println();
			//solve deltaF
			for (int i=0; i<n; ++i) {
				deltaF[i] = 0;
				for (int j=0; j<n; ++j) {
					deltaF[i] += (-invBp[i][j]) * deltaP[j] / U[i];
				}
			}
			
			//calc deltaQ
			for (int i=0; i<n; ++i) {
				deltaQ[i] = Q[i];
				for (int j=0; j<n; ++j) {
					deltaQ[i] -= U[i] * U[j] * (G[i][j] * Math.sin(F[i]-F[j]) - B[i][j] * Math.cos(F[i]-F[j]));
				}
			}
			for (int i=0; i<n; ++i) {
				Q[i] = deltaQ[i];
				deltaQ[i] = deltaQ[i] / U[i];
			}
			//solve deltaU
			for (int i=0; i<n; ++i) {
				deltaU[i] = 0;
				for (int j=0; j<n; ++j) {
					deltaU[i] += (-invBpp[i][j]) * deltaQ[j];
				}
				U[i] = deltaU[i];
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
		double[][] G = {{10.834,-1.667,-1.667,-2.5},
						{-1.667,-12.917,-10,0},
						{-1.667,-10,12.917,-1.25},
						{-2.5,0,-1.25,3.74}};
		PQiterating(Bp, Bpp, G, Bp);
	}
}
