package project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HomeworkPrinter {

	private static long processTime = 0;
	
	private static Node [] highest = new Node[10];
	private static Node [] lowest = new Node[10];

	public static void setTime( long pT ) { processTime = pT; }
	
	public static void printHomework() {
		String file;
		BufferedWriter writer;
		file = "hw_StopStem.txt";

		highest = FindHighest.findHighest();
		lowest = FindHighest.findLowest();
		
		try {
			writer = new BufferedWriter( new FileWriter( file ) );
			
			writer.write(" Unique Terms: " + CSCI3850.mapping.size() + "\n" );
			writer.write(" Program Time: " + Long.toString(processTime) + "\n\n" );
			writer.write( "Most Frequent Terms: \n" );
			
			for( Node n : highest ) {
				writer.write("  " + n.getKeyword() + " : " + Integer.toString( n.getOccurrence() ) + "\n" );
			}
			
			writer.newLine();
			writer.write( "Least Frequent Terms: \n" );
			
			for( Node n : lowest ) {
				writer.write("  " + n.getKeyword() + " : " + Integer.toString( n.getOccurrence() ) + "\n" );
			}
			
			writer.close();
		} catch (IOException e) {
			System.out.println( "Failed to open " + file );
			e.printStackTrace();
		}
	}
	
}
