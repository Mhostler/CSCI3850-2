package project;

import java.util.concurrent.ConcurrentLinkedQueue;

//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Arrays;

public class ElimStopWords {

	public static ConcurrentLinkedQueue<String> stahp;
	static String add;
	static int x;
	//HowTo: call this method as part of startup process to set up the arrays or add this method to another file
	public ElimStopWords() {
		
		//int stopSize = 175;
		stahp = CSCI3850.getStopWords();
		x = 0;
		
		//Use file IO to import words from stopWords.txt into array stahp
//		BufferedReader stopReader;
//		try {
//			stopReader = new BufferedReader(new FileReader("stopWords.txt"));
//			for(int i = 0; i < stopSize; i++) {
//				if( (add = stopReader.readLine()) != null ) {
//					stahp[i] = add;
//				}
//			}
//			
////			while( (add = stopReader.readLine()) != null ){
////				stahp[x] = add;
////				x++;
////			}
//			
//			stopReader.close();
//		} catch (FileNotFoundException e) {
//			System.out.println("Failed to open file: stopWords.txt");
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.out.println("File I/O failed.");
//			e.printStackTrace();
//		}
		
	}
	//uses binary search to find value in sorted list of stopwords, returns true if found else false

	public boolean isStop(String x) {
		return stahp.contains(x);
	}
}
