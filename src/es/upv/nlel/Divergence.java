package es.upv.nlel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import es.upv.nlel.KLKde.KDE;
import es.upv.nlel.KLKde.KL;
import es.upv.nlel.io.FileIO;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TDoubleList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class Divergence {
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		Divergence klkde = new Divergence();
		
		// specify the training and validation file in Letor format.
		String trainFile = "train.txt";
		String valiFile = "vali.txt";
		int numRelLevels = 3; // Number of relevance levels. eg. in mq2007, mq2008 it is 3.
		
		TIntDoubleHashMap div = klkde.computeExpectedDiv(trainFile, valiFile, numRelLevels);
		
		// Print Top 5 discriminative features
		TIntList list = new TIntArrayList();
		list = FileIO.getTopNFeatures(div, 5);
		
		TIntIterator iter = list.iterator();
		
		while(iter.hasNext()) {
			int f = iter.next();
			System.out.println("ED of Feature "+f+"\t= " + div.get(f));
		}
		
		// save it for future use
		FileOutputStream fos = new FileOutputStream("objects/expected-div.obj");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(div);
		
		oos.close();
		fos.close();
	} // main closed
	
	/** This method comuptes the binary divergence.
	 * 
	 * @param trainFile
	 * @param valiFile
	 * @param relLevels
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public TIntDoubleHashMap computeDiv(String trainFile, String valiFile, int relLevels) throws NumberFormatException, IOException {
		TIntObjectHashMap<TDoubleList> trainPos = new TIntObjectHashMap<TDoubleList>();
		TIntObjectHashMap<TDoubleList> trainNeg = new TIntObjectHashMap<TDoubleList>();
		
		TIntObjectHashMap<TDoubleList> vali = new TIntObjectHashMap<TDoubleList>();

		trainNeg = FileIO.loadFeatures(trainFile, 0, relLevels);
		trainPos = FileIO.loadFeatures(trainFile, 1, relLevels);
		
		vali = FileIO.loadAllFeatures(valiFile);
		
		
		TIntDoubleHashMap divergence = new TIntDoubleHashMap();
		KDE kde = new KDE();
		KL kl = new KL();
		
		for(int i=1; i<=trainNeg.size(); i++) {
			TDoubleList PX = new TDoubleArrayList();
			TDoubleList NX = new TDoubleArrayList();
			
			PX = trainPos.get(i);
			NX = trainNeg.get(i);
			
			TDoubleList valiX = new TDoubleArrayList();
			
			valiX = vali.get(i);
			
			TDoubleIterator iter = valiX.iterator();
			
			double tempDiv = 0.0;
			
			while(iter.hasNext()) {
				double x = iter.next();
				double p = kde.kde(PX, x);
				double q = kde.kde(NX, x);
				
				tempDiv+=kl.JSdiv(p, q);
			}
			divergence.put(i, tempDiv);
			System.out.println("Divergence of Feature "+i+": "+ tempDiv);
		} // for closed
		
		return divergence;
	}
	
	/** This method computes the expected-divergence of the features over all relavance classes.
	 * As exlained in 
	 * Parth Gupta, Paolo Rosso: Expected Divergence Based Feature Selection for Learning to Rank. COLING (Posters) 2012: 431-440
	 * 
	 * @param trainFile
	 * @param valiFile
	 * @param relLevels
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public TIntDoubleHashMap computeExpectedDiv(String trainFile, String valiFile, int relLevels) throws NumberFormatException, IOException {
		Map<Integer, TIntObjectHashMap<TDoubleList>> trainData = new HashMap<Integer, TIntObjectHashMap<TDoubleList>>();
		for(int i=0; i<relLevels; i++) {
			TIntObjectHashMap<TDoubleList> tempData = new TIntObjectHashMap<TDoubleList>();
			tempData = FileIO.loadFeatures(trainFile, i);
			trainData.put(i, tempData);
		}
		
		TIntObjectHashMap<TDoubleList> vali = new TIntObjectHashMap<TDoubleList>();
		
		vali = FileIO.loadAllFeatures(valiFile);
		
		
		TIntDoubleHashMap divergence = new TIntDoubleHashMap();
		KDE kde = new KDE();
		KL kl = new KL();
		
		for(int i=1; i<=vali.size(); i++) {
			TDoubleList valiX = new TDoubleArrayList();	
			valiX = vali.get(i);
			TDoubleIterator iter = valiX.iterator();
			
			double tempDiv = 0.0;
			
			while(iter.hasNext()) {
				double x = iter.next();
				
				double[] rDist = new double[relLevels];
				for(int r=0;r<relLevels;r++) {
					rDist[r] = kde.kde(trainData.get(r).get(i), x);
				}
				
				for(int outerR=0; outerR<relLevels; outerR++) {
					for(int innerR=outerR+1;innerR<relLevels; innerR++) {
						tempDiv += (innerR-outerR)* kl.JSdiv(rDist[outerR], rDist[innerR]); // eq. 2 in the FS-ED paper
					}
				}
			}
			divergence.put(i, tempDiv);
			System.out.println("Divergence of Feature "+i+": "+ tempDiv);
		} // for closed
		
		return divergence;
	}
} // class closed
