package com.dhcc.util;

public class MatrixUtil {
	
	//求逆矩阵
	public static double[][] Inverse(double[][] B) {
		int n = B.length;
		double[][] invB = new double[n][n];
		for (int i=0; i<n; ++i) {
			for (int j=0; j<n; ++j) {
				if (i == j) {
					invB[i][j] = 1;
				} else {
					invB[i][j] = 0;
				}
			}
		}
		for (int i=0; i<n; ++i) {
			for (int j=0; j<n; ++j) {
				if (i != j) {
					double t = B[j][i] / B[i][i];
					for (int k=0; k<n; ++k) {
						B[j][k] -= B[i][k] * t;
						invB[j][k] -= invB[i][k]*t;
					}
				}
			}
		}
		for (int i=0; i<n; ++i) {
			if (B[i][i] != 1) {
				double t = B[i][i];
				for(int j=0; j<n; ++j) {
					invB[i][j] = invB[i][j] / t;
				}
			}
		}
		
		return invB;
	}
	
	/*
	public static void main(String[] args) {
		double[][] B = {{4,0,0},{0,5,0},{0,0,1}};
		double[][] invB = Inverse(B);
		int n = invB.length;
		for (int i=0; i<n; ++i) {
			for (int j=0; j<n; ++j) {
				System.out.print(invB[i][j] + " ");
			}
			System.out.println();
		}
	}
	*/
}
