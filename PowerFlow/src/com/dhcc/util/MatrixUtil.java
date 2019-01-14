package com.dhcc.util;

public class MatrixUtil {
	
	//ÇóÄæ¾ØÕó
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
					if (B[i][i] == 0) continue;
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
				if (t == 0) continue;
				for(int j=0; j<n; ++j) {
					invB[i][j] = invB[i][j] / t;
				}
			}
		}
		
		return invB;
	}
	
	public static double[] Multi(double[] A, double[][] B) {
		int N = A.length;
		double[] C = new double[N];
		for (int i = 0; i < N; ++i) {
			C[i] = 0;
			for (int j = 0; j < N; ++j) {
				C[i] += A[j] * B[i][j];
			}
		}
		return C;
	}
	
	public static void main(String[] args) {
		double[] A = {1,2,3};
		double[][] B = {{1,2,3},
						{1,2,3},
						{1,2,3}};
		double[] C = Multi(A,B);
		for (int i = 0; i < C.length; ++i) {
			System.out.println(C[i]);
		}
	}
	
}