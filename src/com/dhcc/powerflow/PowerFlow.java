package com.dhcc.powerflow;

import com.dhcc.util.MatrixUtil;

public class PowerFlow {
	public void PQiterating(double[][] Bp, double[][] Bpp) {
		double[] deltaU = new double[Bp.length]; 
		double[][] invBp, invBpp;
		invBp = MatrixUtil.Inverse(Bp);
		invBpp = MatrixUtil.Inverse(Bpp);
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
	}
}
