package es.upv.nlel.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TDoubleList;
import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.map.hash.TDoubleIntHashMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class FileIO {
	public static TIntObjectHashMap<TDoubleList> loadAllFeatures(String file) throws NumberFormatException, IOException {
		TIntObjectHashMap<TDoubleList> fMap = new TIntObjectHashMap<TDoubleList>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))));
		String line;
		
		int maxFeatures = 0;
		
		while((line = br.readLine())!= null) {
		if(line.contains("qid")) {
			if(line.contains("#"))
				line = line.substring(0, line.indexOf("#"));
			line = line.trim();
			String s[] = line.split(" ");
			
			Map<Integer, Double> map = new HashMap<Integer,Double>();
			for(int i=2 ; i< s.length ; i++) {
				if(s[i].contains(":")) {
					int f_index = Integer.parseInt(s[i].substring(0, s[i].indexOf(":")));
					double f_value = Double.parseDouble(s[i].substring(s[i].indexOf(":")+1));
					map.put(f_index, f_value);
					
					maxFeatures = f_index; 
				}
			}
			
			for(int i=0; i< maxFeatures; i++) {
				if(!fMap.contains((i+1))) {
					TDoubleList list = new TDoubleArrayList();
					list.add(map.get((i+1)));
					fMap.put((i+1), list);
				}
				else {
					TDoubleList list = new TDoubleArrayList();
					list = fMap.get((i+1));
					list.add(map.get((i+1)));
					fMap.put((i+1), list);
				}
			}
		} // if qid closed
		}// while close
		return fMap;
	}
	
	public static TIntObjectHashMap<TDoubleList> loadFeatures(String file, int rel, int relLevels) throws NumberFormatException, IOException {
		TIntObjectHashMap<TDoubleList> fMap = new TIntObjectHashMap<TDoubleList>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))));
		String line;
		
		int maxFeatures = 0;
		
		while((line = br.readLine())!= null) {
		if(line.contains("qid")) {
			if(line.contains("#"))
					line = line.substring(0, line.indexOf("#"));
			line = line.trim();
//			System.out.println(line);
			String s[] = line.split(" ");
			int relevance = 0;
//			if(Integer.parseInt(s[0])==1 || Integer.parseInt(s[0])==2)
			if(relLevels==3 || relLevels==2) {
				if(Integer.parseInt(s[0])==1 || Integer.parseInt(s[0])==2)
					relevance = 1;
			}
			else if(relLevels == 5) {
					if(Integer.parseInt(s[0])>1)
						relevance = 1;
			}
			if(relevance == rel) {
				Map<Integer, Double> map = new HashMap<Integer,Double>();
				for(int i=2 ; i< s.length ; i++) {
					if(s[i].contains(":")) {
						int f_index = Integer.parseInt(s[i].substring(0, s[i].indexOf(":")));
						double f_value = Double.parseDouble(s[i].substring(s[i].indexOf(":")+1));
						map.put(f_index, f_value);
						
						maxFeatures = f_index; 
					}
				}
				
				for(int i=0; i< maxFeatures; i++) {
					if(!fMap.contains((i+1))) {
						TDoubleList list = new TDoubleArrayList();
						list.add(map.get((i+1)));
						fMap.put((i+1), list);
					}
					else {
						TDoubleList list = new TDoubleArrayList();
						list = fMap.get((i+1));
						list.add(map.get((i+1)));
						fMap.put((i+1), list);
					}
				}
			}	
		} // if qid
		}// while close
		return fMap;
	}
	
	
	public static TIntObjectHashMap<TDoubleList> loadFeatures(String file, int rel) throws NumberFormatException, IOException {
		TIntObjectHashMap<TDoubleList> fMap = new TIntObjectHashMap<TDoubleList>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))));
		String line;
		
		int maxFeatures = 0;
		
		while((line = br.readLine())!= null) {
		if(line.contains("qid")) {
			if(line.contains("#"))
					line = line.substring(0, line.indexOf("#"));
			line = line.trim();
//			System.out.println(line);
			String s[] = line.split(" ");
			int relevance = 0;
			relevance = Integer.parseInt(s[0]);

			if(relevance == rel) {
				Map<Integer, Double> map = new HashMap<Integer,Double>();
				for(int i=2 ; i< s.length ; i++) {
					if(s[i].contains(":")) {
						int f_index = Integer.parseInt(s[i].substring(0, s[i].indexOf(":")));
						double f_value = Double.parseDouble(s[i].substring(s[i].indexOf(":")+1));
						map.put(f_index, f_value);
						
						maxFeatures = f_index; 
					}
				}
				
				for(int i=0; i< maxFeatures; i++) {
					if(!fMap.contains((i+1))) {
						TDoubleList list = new TDoubleArrayList();
						list.add(map.get((i+1)));
						fMap.put((i+1), list);
					}
					else {
						TDoubleList list = new TDoubleArrayList();
						list = fMap.get((i+1));
						list.add(map.get((i+1)));
						fMap.put((i+1), list);
					}
				}
			}	
		} // if qid
		}// while close
		return fMap;
	}
	
	public static void reduceFetures(String inFile, String outFile, TIntList list) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile))));
		String line;
		
		FileOutputStream fos = new FileOutputStream(outFile);
		PrintStream p = new PrintStream(fos);

		System.out.print("Top "+list.size()+" features are ");
		for(int j=0;j<list.size(); j++)
			System.out.print(list.get(j)+" ");
		System.out.println();
		
		while((line = br.readLine())!= null) {
		if(line.contains("qid")) {
			String trailer = "";
			if(line.contains("#")) {
				trailer = line.substring(line.lastIndexOf("#"));
				line = line.substring(0, line.lastIndexOf("#"));
			}
			line = line.trim();
			String s[] = line.split(" ");
			p.print(s[0]+" "+s[1]);
			int f=1;
			for(int i=2; i< s.length; i++) {
				if(s[i].contains(":")) {
					int f_index = Integer.parseInt(s[i].substring(0, s[i].indexOf(":")));
					if(list.contains(f_index)) {
						p.print(" "+f+":"+ s[i].substring(s[i].indexOf(":")+1));
						f++;
					}
				}
			}
			if(trailer.length()>1)
				p.print(" "+trailer);
			p.println();
		}
		} //while close
	}
	
	public static TIntList sortFeatures(TIntDoubleHashMap div) {
		System.out.println("In the feature sorting module:");
		TIntList list = new TIntLinkedList();
		for(int i: div.keys()) {
			double value = div.get(i);
//			System.out.println("Adding feature i = "+i+" with value "+value+" size of the list is "+list.size());
			if(list.isEmpty())
				list.add(i);
			else if(Double.isNaN(value)) {
				list.add(i);
			}
			else {
				int total = list.size();
				for(int j:list.toArray()) {
//					System.out.println("index of j =" + list.indexOf(0,j));
					total--;
					if(!Double.isNaN(div.get(j))) {
						if(value<div.get(j) && total>0)
							continue;
						else if(value>=div.get(j)) {
							list.insert(list.indexOf(j), i);
							break;
						}
						else if(total==0) {
							list.add( i);
							break;
						}
					}
					else {
						list.insert(list.indexOf(j),i);
						break;
					}
				}
			}
		}
		for(int i: list.toArray()) {
			System.out.println(i+"-->"+div.get(i));
		}
		return list;
	}

	public static TIntList getTopNFeatures(TIntDoubleHashMap div, int n) {
		TIntLinkedList list = new TIntLinkedList();
		TDoubleIntHashMap revMap = new TDoubleIntHashMap();
		
		for(int i:div.keys())
			revMap.put(div.get(i), i);
		
		double[] temp = new double[div.size()];
		
		temp = div.values();
		Arrays.sort(temp);
		
		ArrayUtils.reverse(temp);
		
		int count=0;
		for(double i:temp) {
			
			if(count<n && !Double.isNaN(i)) {
				list.add(revMap.get(i));
				count++;
			}
			else if(count>=n)
				break;
			else
				continue;
		}
		
		return list;
	}
}
