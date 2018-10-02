package project;

import java.util.Map;

public class FindHighest {

	public static Node [] highest = new Node[10];
	public static Node [] lowest = new Node[10];
	
	public static Node [] findHighest() {
		Node [] highestf = new Node[10];
		int counter = 0;
		
		for( int i = 0; i < 10; i++ ) { highestf[i] = new Node(); }
		
		for( Map.Entry<String, Node> e : CSCI3850.mapping.entrySet() ) {
			Node n = e.getValue();
			
			if( n == null ) {
				continue;
			}
			
			//run initial pass and then sort that array				
			if (counter < 10) {
				highestf[counter].setKeyword( n.getKeyword() );
				highestf[counter].setOccurrence( n.getOccurrence() );
				
				if( counter == 9 ) { 
					sortHigh(highestf); 
					}
				
				counter++;
			} else if (highestf[0].getOccurrence() < n.getOccurrence()) {
				highestf[0].setKeyword( n.getKeyword() );
				highestf[0].setOccurrence( n.getOccurrence() );
				sortHigh(highestf);
				

			} else {
				continue;
			}
		}
		
		return highestf;
	}
	
	public static Node [] findLowest() {
		Node [] lowestf = new Node[10];
		int counter = 0;
		
		for( int i = 0; i < lowestf.length; i++ ) { lowestf[i] = new Node(); }
		
		for( Map.Entry<String, Node> e : CSCI3850.mapping.entrySet() ) {
			Node n = e.getValue();
			if( n == null ) {
				continue;
			}
				
			//run initial pass and then sort that array
			if (counter < 10) {
				lowestf[counter] = n;
				
				if( counter == 9 ) { 
					sortBot(lowestf); 
					}
				
				counter++;
			} else if (lowestf[0].getOccurrence() > n.getOccurrence()) {
				lowestf[0] = n;
				sortBot(lowestf);
			} else {
				continue;
			}
		}
		return lowestf;
	}

	// greatest value first
	public static void sortBot(Node[] arr) {
		for (int i = 1; i < 10; ++i)
        {
			Node key = arr[i];

			int j = i-1;

			while (j>=0 && arr[j].getOccurrence() < key.getOccurrence())
			{
				arr[j+1] = arr[j];
				j = j-1;
			}
			arr[j+1] = key;
        }
	}
	//lowest value first
	public static void sortHigh(Node[] arr) {
		for (int i = 1; i < 10; ++i)
        {
			Node key = arr[i];

			int j = i-1;

			while (j>=0 && arr[j].getOccurrence() > key.getOccurrence())
			{
				arr[j+1] = arr[j];
				j = j-1;
			}
			arr[j+1] = key;
        }
	}
}
