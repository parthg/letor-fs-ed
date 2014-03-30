package es.upv.nlel;

import es.upv.nlel.io.FileIO;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntDoubleHashMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Test {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		testTopNFeatures();
	}
	
	public static void testTopNFeatures() throws IOException, ClassNotFoundException {
		
		TIntDoubleHashMap div = new TIntDoubleHashMap();
		FileInputStream fis = new FileInputStream("objects/expected-div.obj");
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		div = (TIntDoubleHashMap) ois.readObject();
		
		ois.close();
		fis.close();
		
		@SuppressWarnings("unused")
		TIntList list = new TIntArrayList();
		list = FileIO.getTopNFeatures(div, 5);
		
		
	}
}