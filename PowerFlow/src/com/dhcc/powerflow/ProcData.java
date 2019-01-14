package com.dhcc.powerflow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import com.dhcc.Global.Variable;
import com.dhcc.model.Branch;
import com.dhcc.model.Gene;
import com.dhcc.model.Info;
import com.dhcc.model.Load;
import com.dhcc.model.MPC;
import com.dhcc.model.Tran;
import com.dhcc.util.MatrixUtil;

public class ProcData {
	
	private MPC _mpc;
	
	public void ReadData(String filename) {
		
		try {
			InputStreamReader instrr = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(instrr);
			String row = null;
			String[] rowdata = null;
			
			row = br.readLine();int nbus = Integer.parseInt(row);
			row = br.readLine();int ngen = Integer.parseInt(row);
			row = br.readLine();int nbranch = Integer.parseInt(row);
			
			int N = nbus;
			int Nb = nbranch;
			int Ng = ngen;
			int Nl = nbus - ngen;
			int Npv = 0;
			int V0 = 0;
			
			_mpc = new MPC(nbus, ngen, nbranch);

			double[][] bus = _mpc.getBus();
			//System.out.println("lanlan");
			for (int i=0; i<nbus; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				for (int j=0; j<rowdata.length; ++j) {
					bus[i][j] = Double.parseDouble(rowdata[j]);
				}
				V0 += Math.abs(bus[i][7]);
				bus[i][0] = bus[i][0] - 1;
				bus[i][3] = bus[i][3] / 100;
				bus[i][2] = bus[i][2] / 100;
				bus[i][8] = bus[i][8] * Math.PI / 180;
				if(bus[i][1] == Variable.PV) ++Npv;
			}
			V0 = V0 / nbus;
			
			double[][] gen = _mpc.getGen();
			for (int i=0; i<ngen; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				for (int j=0; j<rowdata.length; ++j) {
					gen[i][j] = Double.parseDouble(rowdata[j]);
				}
				gen[i][0] = gen[i][0] - 1;
				gen[i][1] = gen[i][1] / 100;
				gen[i][2] = gen[i][2] / 100;
			}
			int Nt=0;
			double[][] branch = _mpc.getBranch();
			for (int i=0; i<nbranch; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				//System.out.println(row);
				for (int j=0; j<rowdata.length; ++j) {
					branch[i][j] = Double.parseDouble(rowdata[j]);
				}
				branch[i][0] = branch[i][0] - 1;
				branch[i][1] = branch[i][1] - 1;
				if (branch[i][8]!=0) 
					++Nt;
			}
			
			//System.out.print(Nt);
			
			//index
			int index[] = new int[N];
			double busc[][] = new double[bus.length][bus[0].length];
			double branchc[][] = new double[branch.length][branch[0].length];
			int _ref=N-1,  _pv=N-Npv-1, _pq = 0;
			
			for (int i=0; i<Nb; ++i) 
				for (int j=0; j<branch[i].length; ++j)
					branchc[i][j]=branch[i][j];
			
			for (int i=0; i<N; ++i) {
				int id = (int)bus[i][0];
				if ((int)bus[i][1] == Variable.REF) {
					index[(int)bus[i][0]] = _ref;
					busc[_ref] = bus[i];
					busc[_ref][0] = _ref;
					for (int j=0; j<Nb; ++j) {
						if ((int)branch[j][0] == id) 
							branchc[j][0] = _ref;
						if ((int)branch[j][1] == id)
							branchc[j][1] = _ref;
					}
				}else if ((int)bus[i][1] == Variable.PQ) {
					index[(int)bus[i][0]] = _pq;
					busc[_pq] = bus[i];
					busc[_pq][0] = _pq;
					for (int j=0; j<Nb; ++j) {
						if ((int)branch[j][0] == id) 
							branchc[j][0] = _pq;
						if ((int)branch[j][1] == id)
							branchc[j][1] = _pq;
					}
					++_pq;
				}else if ((int)bus[i][1] == Variable.PV) {
					index[(int)bus[i][0]] = _pv;
					busc[_pv] = bus[i];
					busc[_pv][0] = _pv;
					for (int j=0; j<Nb; ++j) {
						if ((int)branch[j][0] == id) 
							branchc[j][0] = _pv;
						if ((int)branch[j][1] == id)
							branchc[j][1] = _pv;
					}
					++_pv;
				}
			}
			for (int i=0; i<gen.length; ++i)
				gen[i][0] = index[(int)gen[i][0]];
			
			_mpc.setBus(busc);
			_mpc.setBranch(branchc);
			_mpc.setGen(gen);
			Nb = nbranch - Nt;
			Info pf_info = new Info(N, Nb,Nt, Ng, Nl, V0, Npv, 0.00000001);
			Variable.setPf_info(pf_info);
			
			
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
	
	public void InitData() {
		Info info = Variable.getPf_info();
		Branch[] branch = new Branch[info.getNb()];
		Tran[] tran = new Tran[info.getNt()];
		Gene[] generator = new Gene[info.getNg()];
		Load[] load = new Load[info.getNl()];
		
		ArrayList<Integer> genIdx = new ArrayList<Integer>();
		
		double[][] brch = _mpc.getBranch();
		double[][] gen = _mpc.getGen();
		double[][] bus = _mpc.getBus();
		
		//支路
		int _nb = 0, _nt = 0;
		for (int i = 0; i < info.getNb() + info.getNt(); ++i) {
			if (brch[i][8] == 0) {
				branch[_nb] = new Branch();
				branch[_nb].setFrom((int) brch[i][0]);
				branch[_nb].setTo((int) brch[i][1]);
				branch[_nb].setR(brch[i][2]);
				branch[_nb].setX(brch[i][3]);
				branch[_nb++].setY0(brch[i][4]);
			} else {
				tran[_nt] = new Tran();
				tran[_nt].setFrom((int) brch[i][0]);
				tran[_nt].setTo((int) brch[i][1]);
				tran[_nt].setR(brch[i][2]);
				tran[_nt].setX(brch[i][3]);
				tran[_nt++].setK(brch[i][8]);
			}
		}
		
		for (int i = 0; i < info.getNg(); ++i) {
			generator[i] = new Gene();
			generator[i].setI((int) gen[i][0]);
			generator[i].setP(gen[i][1]);
			generator[i].setQ(gen[i][2]);
			generator[i].setV(gen[i][5]);
			//TODO
			if ((int)bus[(int) gen[i][0]][1] == Variable.REF) {
				generator[i].setJ(Variable.REF);
			}else if ((int)bus[(int) gen[i][0]][1] == Variable.PV) {
				generator[i].setJ(Variable.PV);
			}else if ((int)bus[(int) gen[i][0]][1] == Variable.PQ) {
				generator[i].setJ(Variable.PQ);
			}
			genIdx.add((int) gen[i][0]);
		}

		int j = 0;
		for (int i = 0; i < info.getN(); ++i) {
			if (genIdx.contains((int) bus[i][0])) continue;
			//if ((int)bus[i][1] == Variable.REF) continue;
			//System.out.println(load.length + " " + j + " " + bus[i][0]);
			load[j] = new Load();
			load[j].setI((int) bus[i][0]);
			load[j].setP(bus[i][2]);
			load[j].setQ(bus[i][3]);
			j++;
		}
		Variable.setTrans(tran);
		Variable.setBranch(branch);
		Variable.setGenerator(generator);
		Variable.setLoad(load);
	}
	
	public void AdmtMatrix() {
		Info info = Variable.getPf_info();
		Branch branch[] = Variable.getBranch();
		Tran tran[] = Variable.getTrans();
		double G[][] = new double[info.getN()][info.getN()];
		double B[][] = new double[info.getN()][info.getN()];
		double r,x,b,kt;
		int i,j;
		for (int k=0; k<info.getNb(); ++k) {
			i = branch[k].getFrom();
			j = branch[k].getTo();
			r = branch[k].getR();
			x = branch[k].getX();
			b = r*r + x*x;
			r = r/b;
			x = -x/b;
			if (i==j) {
				G[i][j] += r;
				B[i][j] += x;
				continue;
			}
			b = branch[k].getY0();
			
			G[i][j] = G[i][j] - r;
			B[i][j] = B[i][j] - x;
			G[j][i] = G[i][j];
			B[j][i] = B[i][j];
			
			G[i][i] = G[i][i] + r;
			B[i][i] = B[i][i] + x + b/2;
			G[j][j] = G[j][j] + r;
			B[j][j] = B[j][j] + x + b/2;
			
//			System.out.println("G " + G.length + " " + G[0].length);
//			for (int i1=0; i1<info.getN(); ++i1) {
//				for (int j1=0; j1<G[i1].length; ++j1)
//					System.out.print(G[i1][j1] + " ");
//				System.out.print("\n");
//			}
//			System.out.println("B " + B.length + " " + B[0].length);
//			for (int i1=0; i1<info.getN(); ++i1) {
//				for (int j1=0; j1<B[i1].length; ++j1)
//					System.out.print(B[i1][j1] + " ");
//				System.out.print("\n");
//			}
		}
		
		for (int k=0; k<info.getNt(); ++k) {
			i = tran[k].getFrom();
			j = tran[k].getTo();
			r = tran[k].getR();
			x = tran[k].getX();
			b = r*r + x*x;
			r = r/b;
			x = -x/b;
			kt = tran[k].getK();
			G[i][i] = G[i][i] + r;
			B[i][i] = B[i][i] + x;
			G[i][j] = G[i][j] - r/kt;
			B[i][j] = B[i][j] - x/kt;
			G[j][i] = G[j][i];
			B[j][i] = B[j][i];
			r = r/kt/kt;x = x/kt/kt;
			G[j][j] += r;
			B[j][j] += x;
		}
		Variable.setB(B);
		Variable.setG(G);
	}

	public void CalcFactor() {
		Info info = Variable.getPf_info();
		double Bc[][] = Variable.getB();
		
		double Bp[][] = new double[info.getN()-1][info.getN()-1];
		double Bpp[][] = new double[info.getNpq()][info.getNpq()];
		
		for (int i=0; i<info.getN()-1; ++i) 
			for (int j=0; j<info.getN()-1; ++j)
				Bp[i][j] = Bc[i][j];
		for (int i=0; i<info.getNpq(); ++i) 
			for (int j=0; j<info.getNpq(); ++j)
				Bpp[i][j] = Bc[i][j];
		double invBp[][] = MatrixUtil.Inverse(Bp);
		double invBpp[][] = MatrixUtil.Inverse(Bpp);
		Variable.setInvBp(invBp);
		Variable.setInvBpp(invBpp);
		Variable.setBp(Bp);
		Variable.setBpp(Bpp);
	}
	
	public void InitOri(){
		Info info = Variable.getPf_info();
		Gene gene[] = Variable.getGenerator();
		Load load[] = Variable.getLoad();
		//double bus[][] = _mpc.getBus();
		double oriU[] = new double[info.getN()];
		double oriTheta[] = new double[info.getN()];
		for (int i=0; i<info.getN(); ++i){
				oriU[i] = 1.0; 
				oriTheta[i] = 0.0; 
		}
		for (int i=0; i<info.getNg(); ++i) 
			if (gene[i].getJ() == Variable.PV || gene[i].getJ() == Variable.REF)
				oriU[(int) gene[i].getI()] = gene[i].getV();
		for (int i=0; i<info.getNt(); ++i) 
			if (load[i].getJ() == Variable.PV || load[i].getJ() == Variable.REF)
				oriU[(int) load[i].getI()] = load[i].getV();
		//		for (int i=0; i<info.getN(); ++i) 
		//			if (bus[i][1] == Variable.PV || bus[i][1] == Variable.REF) 
		//				oriU[(int) bus[i][0]] = bus[i][7];
		
		Variable.setOriTheta(oriTheta);
		Variable.setOriU(oriU);
		System.out.println("Um " + oriU.length );
		for (int i=0; i<oriU.length; ++i) 
			System.out.print(oriU[i] + " ");
		System.out.println();
	}
	
	public void calcPQ() {
		Info info = Variable.getPf_info();
		Gene gene[] = Variable.getGenerator();
		Load load[] = Variable.getLoad();
		double Pi[] = new double[info.getN()];
		double Qi[] = new double[info.getN()];
		for (int i=0; i<info.getNg(); ++i) {
			Pi[gene[i].getI()] = gene[i].getP();
			if (gene[i].getJ() == Variable.PQ) {
				Qi[gene[i].getI()] = gene[i].getP();
			}
		}
		for (int i=0; i<info.getNt(); ++i) {
			Pi[load[i].getI()] = load[i].getP();
			if (load[i].getJ() == Variable.PQ) {
				Qi[load[i].getI()] = load[i].getP();
			}
		}
		Variable.setP(Pi);
		Variable.setQ(Qi);
	}
	
	public void CalcPQ() {
		Info info = Variable.getPf_info();
		double B[][] = Variable.getB();
		double G[][] = Variable.getG();
		double Um[] = Variable.getOriU();
		double Ua[] = Variable.getOriTheta();
		double Pi[] = new double[info.getN()];
		double Qi[] = new double[info.getN()];
		for (int i=0; i<info.getN(); ++i) {
			double vi = Um[i], di = Ua[i], dp = 0.0, dq = 0.0;
			for (int j=0; j<info.getN(); ++j) {
				if (i==j) continue;
				double g = G[i][j], b = B[i][j], dj = Ua[j];
				double dij = di-dj;
				double p = Um[j]*(g*Math.cos(dij)+b*Math.sin(dij));
				double q = Um[j]*(g*Math.sin(dij)-b*Math.cos(dij));
				dp += p;
				dq += q;
			}
			double g = G[i][i], b = B[i][i];
			Pi[i] = vi*(dp+vi*g);
			Qi[i] = vi*(dq-vi*b);
		}
		Variable.setP(Pi);
		Variable.setQ(Qi);
		
//		System.out.println("Um " + Um.length );
//		for (int i=0; i<Um.length; ++i) 
//			System.out.print(Um[i] + " ");
//		System.out.println();
//		System.out.println("Ua " + Ua.length );
//		for (int i=0; i<Ua.length; ++i) 
//			System.out.print(Ua[i] + " ");
//		System.out.println();
//		System.out.println("Pi " + Pi.length );
//		for (int i=0; i<Pi.length; ++i) 
//			System.out.print(Pi[i] + " ");
//		System.out.println();
//		System.out.println("Qi " + Qi.length );
//		for (int i=0; i<Qi.length; ++i) 
//			System.out.print(Qi[i] + " ");
//		System.out.println("\n");
	}
	
	public void TestInfo() {
		Info info = new Info(4, 4,1, 2, 2, 0, 1, 0.00000001);
		Variable.setPf_info(info);
		Branch[] branch = new Branch[info.getNb()];
		Tran[] tran = new Tran[info.getNt()];
		Gene[] generator = new Gene[info.getNg()];
		Load[] load = new Load[info.getNl()];
		
		branch[0] = new Branch(0, 3, 0.173554, 0.330579, 0.017243);
		branch[1] = new Branch(1,2,0.0,-20.0,0.0);
		branch[2] = new Branch(2,0,.130165,0.247934,0.012932);
		branch[3] = new Branch(3, 2, 0.260331, 0.495868, 0.025864);

		tran[0] = new Tran(0,1,0.0,0.166667,1.128205);
		
		generator[0] = new Gene(3,Variable.REF,0,0,1.05);
		generator[1] = new Gene(2,Variable.PV,0.2,0,1.05);
		
		load[0] = new Load(1,Variable.PQ,0.5,0.3,0.0);
		load[1] = new Load(3,Variable.PQ,0.15,0.1,0.0);
		
		Variable.setTrans(tran);
		Variable.setBranch(branch);
		Variable.setGenerator(generator);
		Variable.setLoad(load);
	}
	
	public void readCDFData(String filename) {
		int n_bus = 0;
		int n_branch = 0;
		int nt = 0;
		int ng = 0;
		int nl = 0;
		int npv = 0;
		int npq = 0;
		int nb = 0;
		Branch[] branch;
		Tran[] tran;
		Gene[] generator;
		Load[] load;
		
		try {
			InputStreamReader instrr = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(instrr);
			String row = null;
			String[] rowdata = null;
			row = br.readLine();n_bus = Integer.parseInt(row);
			generator = new Gene[n_bus];
			load = new Load[n_bus];
			
			int phIdx = 0;
			double php = 0, phq = 0, phv = 0;
			
			for (int i = 0; i < n_bus; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				int type = Integer.parseInt(rowdata[6]);
				//System.out.println("type:" + type);
				if (type == 2) {		//generator
					int idx = Integer.parseInt(rowdata[0]);
					npv++;
					double p,q,v;
					v = Double.parseDouble(rowdata[7]);
					p = Double.parseDouble(rowdata[11]);
					q = Double.parseDouble(rowdata[12]);
					generator[ng++] = new Gene(idx,Variable.PV,p/100.0,q/100.0,v);
//					System.out.println("Gene:" + idx);
				} else if (type == 0 || type == 1) {
					int idx = Integer.parseInt(rowdata[0]);
					npq++;
					double p,q,v;
					v = Double.parseDouble(rowdata[7]);
					p = Double.parseDouble(rowdata[9]);
					q = Double.parseDouble(rowdata[10]);
					load[nl++] = new Load(idx,Variable.PQ,p/100.0,q/100.0,v);
				} else if (type == 3) {
					phIdx = Integer.parseInt(rowdata[0]);
					npv++;
					phv = Double.parseDouble(rowdata[7]);
					php = Double.parseDouble(rowdata[11]);
					phq = Double.parseDouble(rowdata[12]);
				}
			}
			
			generator[ng++] = new Gene(phIdx,Variable.PV,php/100.0,phq/100.0,phv);
			
			int newIdx = 0;
			int[] newIndex = new int[n_bus + 1];
			for (int i = 0; i < nl; ++i) {
				newIndex[load[i].getI()] = newIdx;
				load[i].setI(newIdx);
				newIdx++;
			}
			for (int i = 0; i < ng - 1; ++i) {
				newIndex[generator[i].getI()] = newIdx;
				generator[i].setI(newIdx);
				newIdx++;
			}
			
			newIndex[generator[ng-1].getI()] = newIdx;
			generator[ng-1].setI(newIdx);

//			System.out.println("newidx" + phIdx);
//			for (int i = 1; i <= n_bus; ++i)
//				System.out.println(i + " " + newIndex[i]);
			
			row = br.readLine();n_branch = Integer.parseInt(row);
			tran = new Tran[n_branch];
			branch = new Branch[n_branch];

			for (int i = 0; i < n_branch; ++i) {
				row = br.readLine();
				rowdata = row.split(",");
				double k = Double.parseDouble(rowdata[14]);
				int from, to;
				double r, x, b;
				from = Integer.parseInt(rowdata[0]);
				from = newIndex[from];
				to = Integer.parseInt(rowdata[1]);
				to = newIndex[to];
				r = Double.parseDouble(rowdata[6]);
				x = Double.parseDouble(rowdata[7]);
				b = Double.parseDouble(rowdata[8]);
				if (k == 0) {
					branch[nb++] = new Branch(from,to,r,x,b);
				} else {
					tran[nt++] = new Tran(from,to,r,x,k);
				}
			}
			
			Info info = new Info(n_bus,nb,nt,ng,nl,1,npv,0.0001);
			Variable.setPf_info(info);
			Variable.setTrans(Arrays.copyOf(tran, nt));
			Variable.setBranch(Arrays.copyOf(branch, nb));
			Variable.setGenerator(Arrays.copyOf(generator, ng));
			Variable.setLoad(Arrays.copyOf(load, nl));
			
			instrr.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void PrintInfo_b() {
		Info info = Variable.getPf_info();
//		double bus[][] = _mpc.getBus();
//		double bra[][] = _mpc.getBranch();
		Branch[] branch = Variable.getBranch();
		Tran[] tran = Variable.getTrans();
		Gene[] gen = Variable.getGenerator();
		Load[] load = Variable.getLoad();	
		
//		System.out.println("Bus " + bus.length + " " + bus[0].length);
//		for (int i=0; i<info.getN(); ++i) {
//			for (int j=0; j<bus[i].length; ++j)
//				System.out.print(bus[i][j] + " ");
//			System.out.print("\n");
//		}
//		System.out.println("Bra " + bra.length + " " + bra[0].length);
//		for (int i=0; i<bra.length; ++i) {
//			for (int j=0; j<bra[i].length; ++j)
//				System.out.print(bra[i][j] + " ");
//			System.out.print("\n");
//		}
		
		System.out.println("Branch " + branch.length);
		for (int i=0; i<info.getNb(); ++i) {
			System.out.println(branch[i].getFrom() + " " + branch[i].getTo() + " "
					+ branch[i].getR() + " "+ branch[i].getX() + " "+ branch[i].getY0() + " ");
		}
		System.out.println("Tran " + tran.length );
		for (int i=0; i<info.getNt(); ++i) {
			System.out.println(tran[i].getFrom() + " " + tran[i].getTo() + " "
					+ tran[i].getR() + " "+ tran[i].getX() + " "+ tran[i].getK() + " ");
		}
		System.out.println("Gen " + gen.length);
		for (int i=0; i<gen.length; ++i) {
			System.out.println(gen[i].getI() + " " + gen[i].getJ() 
						+ " " + gen[i].getP() + " " + gen[i].getQ() + " "
						+ gen[i].getV());
		}
		System.out.println("Load " + load.length);
		for (int i=0; i<load.length; ++i) {
			System.out.println(load[i].getI() + " " + load[i].getP() 
					+ " " + load[i].getQ());
		}
	}
	public void PrintInfo(){
		Info info = Variable.getPf_info();
		double B[][] = Variable.getB();
		double Bp[][] = Variable.getBp();
		double Bpp[][] = Variable.getBpp();
		double G[][] = Variable.getG();
		
		System.out.println("G " + G.length + " " + G[0].length);
		for (int i=0; i<info.getN(); ++i) {
			for (int j=0; j<G[i].length; ++j)
				System.out.print(G[i][j] + " ");
			System.out.print("\n");
		}
		System.out.println("B " + B.length + " " + B[0].length);
		for (int i=0; i<info.getN(); ++i) {
			for (int j=0; j<B[i].length; ++j)
				System.out.print(B[i][j] + " ");
			System.out.print("\n");
		}
		System.out.println("BP " + Bp.length + " " + Bp[0].length);
		for (int i=0; i<info.getN()-1; ++i) {
			for (int j=0; j<Bp[i].length; ++j)
				System.out.print(Bp[i][j] + " ");
			System.out.print("\n");
		}
		System.out.println("BPP " + Bpp.length + " " + Bpp[0].length);
		for (int i=0; i<info.getNpq(); ++i) {
			for (int j=0; j<Bpp[i].length; ++j)
				System.out.print(Bpp[i][j] + " ");
			System.out.print("\n");
		}
	}
	
	public static void main(String[] args) {
		ProcData pd = new ProcData();
//		pd.ReadData("D:/Java/PowerFlow/src/com/dhcc/casedata/case14.txt");
		pd.ReadData("/Users/xyk0058/Git/PowerFlow/src/com/dhcc/casedata/case14.txt");
		pd.InitData();
		pd.PrintInfo_b();
//		pd.readCDFData("/Users/xyk0058/Git/PowerFlow/src/com/dhcc/casedata/ieee14cdf.txt");
//		pd.PrintInfo_b();
		pd.AdmtMatrix();
		pd.CalcFactor();
		pd.InitOri();
		pd.CalcPQ();
		pd.PrintInfo();		
		PowerFlow pf = new PowerFlow();
		pf.Run();
		pf.PrintInfo();
	}
}
