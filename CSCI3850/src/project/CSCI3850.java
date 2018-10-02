package project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CSCI3850 {

	private static File directory;
	
	private static ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();
	private static ConcurrentLinkedQueue<String> stopWords = new ConcurrentLinkedQueue<String>();
	public static ConcurrentLinkedQueue<sPair> processedQueries = new ConcurrentLinkedQueue<sPair>();
	public static ConcurrentHashMap<String, Node> mapping = new ConcurrentHashMap<String, Node>();
	
	public static Node[] bottom = new Node[10];
	public static Node[] top = new Node[10];
	public static String[] queryList;
	public static String queries;
	
	private static long timeStop;

	public static String getFileName() { return directory.getName(); }
	
	public static void main(String[] args) {
		
		int threadNo = 20;		
		Thread t[] = new Thread[threadNo];
		
		BufferedReader stopReader;
		String word;
		
		long timeStart = System.currentTimeMillis();
		
		if(args == null) {
			System.out.println("ERROR: to execute type 'java CSCI3850p0 DATA query.txt'");
			System.exit(0);
		}

		directory = new File(args[0]);
		String fileList[] = directory.list();

		for( String str : fileList ) {
			fileQueue.add(str);
		}
		
		try {
			stopReader = new BufferedReader(new FileReader("stopWords.txt"));
			while( (word = stopReader.readLine()) != null) {
					stopWords.add(word);
				}
			stopReader.close();
		} catch (FileNotFoundException e1) {
			System.out.println("Failed to find stopWords.txt");
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("stopwords io exception");
			e.printStackTrace();
		}		
		
		//System.out.println( "Beginning File Parsing." );
		for( int i = 0; i < threadNo; i++ ) {
			t[i] = new Thread( new FileProcessor( fileQueue ) );
			t[i].start();
		}
		

		for( Thread th : t ) {
			try {
				th.join();
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted");
				e.printStackTrace();
			}
		}
		//System.out.println( "Finished Parsing" );
		
		timeStop = System.currentTimeMillis() - timeStart;

		displayMap();
		
		//System.out.println( "Printing to output file." );
		//HomeworkPrinter.setTime( timeStop );
		//HomeworkPrinter.printHomework();
		
		setupQuery(args[1], queryList);
		
		int numq = processedQueries.size();
//		System.out.println("Number of threads: " + numq);
//		
//		while( !processedQueries.isEmpty() ) {
//			Thread singleton = new Thread(new Query(processedQueries.poll()));
//			singleton.start();
//			try {
//				singleton.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		Thread [] qs = new Thread[numq];
		for( int i = 0; i < numq; i++ ) {
			sPair sp = processedQueries.poll();
			qs[i] = new Thread(new Query(sp.query, sp.processed));
			qs[i].start();
		}
		
		for( Thread q : qs ) {
			try {
				q.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println( "Program Finished. Time: " + Long.toString(timeStop) + " miliseconds" );
	}

	public static long getTime() {
		return timeStop;
	}
	
	public static ConcurrentLinkedQueue<String> getStopWords() { return stopWords; }
	
	public static void setupQuery(String fname, String[] querylist) {
		String query = "";
		String[] tokens;
		char[] stems;
		ElimStopWords y = new ElimStopWords();
		Stemmer stemmer = new Stemmer();
		
		try {
			Scanner in = new Scanner(new File(fname));
			while(in.hasNextLine()) {
				
				String line = in.nextLine();
				sPair qholder = new sPair();
				qholder.query = line;
				
				line = line.replaceAll("<.*?>", "");
				line = line.replaceAll("[^a-zA-Z0-9 ]", "");
				line = line.replaceAll("\\s+", " ");
				line = line.toLowerCase();
				tokens = line.split(" ");
				//add in stemming and etc
				for(int i = 0; i < tokens.length; i++ ) {
					if(y.isStop(tokens[i])) {
						tokens[i] = "";
					}
					else {
						stems = tokens[i].toCharArray();
						stemmer.add(stems, stems.length);
						stemmer.stem();
						tokens[i] = stemmer.toString();
					}
				}
				query = String.join(" ", tokens);
				
				qholder.processed = query;
				processedQueries.add(qholder);
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error 404: File not Found");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void displayMap() {
		try {
			BufferedWriter bw = new BufferedWriter( new FileWriter( "MapOut.txt." ));
			for( Map.Entry<String, Node> e : mapping.entrySet() ) {
				bw.write(e.getValue().getKeyword() + ":" + e.getValue().getOccurrence() + "\n     ");
				
				int count = 0;
				for( FileNode fn : e.getValue().getQueue() ) {
					bw.write(fn.getFileID() + ": " + fn.getWordCount() + "  " );
					
					if( count++ >= 5 ) {
						bw.write("\n     ");
						count = 0;
					}
				}
				
				bw.write("\n\n");
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized static void map( ConcurrentLinkedQueue<Node> cn ) {
		for( Node n : cn ) {
			Node tmp = mapping.get(n.getKeyword());
			if( tmp != null ) {
				n.addSimilar( tmp );
				mapping.replace(n.getKeyword(), n);
			}
			else {
				mapping.put(n.getKeyword(), n);
			}

		}
	}
}