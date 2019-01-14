package com.dhcc.Global;

import com.dhcc.model.Branch;
import com.dhcc.model.Gene;
import com.dhcc.model.Info;
import com.dhcc.model.Load;
import com.dhcc.model.Tran;

public class Variable {
	
	public static int PQ = 1;
	public static int PV = 2;
	public static int REF = 3;
	private static Info pf_info;
	private static Branch[] branch;		//Nb
	private static Gene[] generator;		//Ng
	private static Load[] load;			//Nl
	private static Tran[] trans;
	private static double[][] G;
	private static double[][] B;
	private static double[][] Bp;
	private static double[][] Bpp;
	private static double[] oriU;
	private static double[] oriTheta;
	private static double[] P;
	private static double[] Q;
	private static double[] Ptemp;
	private static double[] Qtemp;
	private static double[][] invBp;
	private static double[][] invBpp;
	
	public static double[][] getInvBp() {
		return invBp;
	}
	public static void setInvBp(double[][] invBp) {
		Variable.invBp = invBp;
	}
	public static double[][] getInvBpp() {
		return invBpp;
	}
	public static void setInvBpp(double[][] invBpp) {
		Variable.invBpp = invBpp;
	}
	public static Tran[] getTrans() {
		return trans;
	}
	public static void setTrans(Tran[] trans) {
		Variable.trans = trans;
	}
	public static double[] getPtemp() {
		return Ptemp;
	}
	public static void setPtemp(double[] ptemp) {
		Ptemp = ptemp;
	}
	public static double[] getQtemp() {
		return Qtemp;
	}
	public static void setQtemp(double[] qtemp) {
		Qtemp = qtemp;
	}
	public static double[] getP() {
		return P;
	}
	public static void setP(double[] p) {
		P = p;
	}
	public static double[] getQ() {
		return Q;
	}
	public static void setQ(double[] q) {
		Q = q;
	}
	public static double[] getOriTheta() {
		return oriTheta;
	}
	public static void setOriTheta(double[] oriTheta2) {
		Variable.oriTheta = oriTheta2;
	}
	public static double[] getOriU() {
		return oriU;
	}
	public static void setOriU(double[] oriU) {
		Variable.oriU = oriU;
	}
	public static double[][] getBp() {
		return Bp;
	}
	public static void setBp(double[][] bp) {
		Bp = bp;
	}
	public static double[][] getBpp() {
		return Bpp;
	}
	public static void setBpp(double[][] bpp) {
		Bpp = bpp;
	}
	public static double[][] getG() {
		return G;
	}
	public static void setG(double[][] g) {
		G = g;
	}
	public static double[][] getB() {
		return B;
	}
	public static void setB(double[][] b) {
		B = b;
	}
	public static Info getPf_info() {
		return pf_info;
	}
	public static void setPf_info(Info pf_info) {
		Variable.pf_info = pf_info;
	}
	public static Branch[] getBranch() {
		return branch;
	}
	public static void setBranch(Branch[] branch) {
		Variable.branch = branch;
	}
	public static Gene[] getGenerator() {
		return generator;
	}
	public static void setGenerator(Gene[] generator) {
		Variable.generator = generator;
	}
	public static Load[] getLoad() {
		return load;
	}
	public static void setLoad(Load[] load) {
		Variable.load = load;
	}
	
}
