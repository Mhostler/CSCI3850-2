package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileProcessor implements Runnable {
	ConcurrentLinkedQueue<String> files;
	ConcurrentLinkedQueue<Node> keys;
	long wordCount;
	
	public FileProcessor(ConcurrentLinkedQueue<String> fileNames ) {
		files = fileNames;
		keys = new ConcurrentLinkedQueue<Node>();
		wordCount = 0;
	}
	
	public void run() {
		BufferedReader fileReader;
		String fileName = "";
		ElimStopWords esw = new ElimStopWords();
		
		try {
			do{
				fileName = files.poll();

				if( fileName == null )
					break;
				
				String directoryName = CSCI3850.getFileName();
				fileReader = new BufferedReader(new FileReader("./" + directoryName + "/" + fileName));	
				
				String str = "";
				
				while( (str = fileReader.readLine()) != null ) {
					process(str, fileName, esw);
				}
				
				for( Node n : keys ) {
					ConcurrentLinkedQueue<FileNode> temp = n.getQueue();
					FileNode fn = temp.poll();
					
					//should only be one object in temp
					if( !temp.isEmpty() ) {
						System.out.println("Error: More than one file at a time");
					}
					
					fn.setWordCount(wordCount);
					temp.add(fn);
					n.setQueue(temp);
				}

				CSCI3850.map( keys );
				keys.clear();
				wordCount = 0;
				
				
				fileReader.close();
				
			}while( !files.isEmpty() );
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open file: " + fileName);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File I/O failed.");
			e.printStackTrace();
		}

	}
	
	public void process(String str, String fileName, ElimStopWords esw) {
		str = str.replaceAll("<DOCID>.*?</DOCID>", "");
		str = str.replaceAll("<TDTID>.*?</TDTID>", "");
		str = str.replaceAll("<DATE>.*?</DATE>", "");
		str = str.replaceAll("<HEADLINE>.*?</HEADLINE>", "");
		str = str.replaceAll("<SUBJECT>.*?</SUBJECT>", "");
		str = str.replaceAll("<DATELINE>.*?</DATELINE>", "");
		str = str.replaceAll("<.*?>", "");
		str = str.replaceAll("[^a-zA-Z0-9 ]", "");
		str = str.replaceAll("\\s+", " ");
		str = str.toLowerCase();
		
		String tokens[] = str.split("\\s");

		Stemmer s = new Stemmer();
		char[] ack;
		
		int lineCount = 0;
		
		for( String token : tokens ) {
			
			if(!token.isEmpty()) {
				Node n = new Node();
				n.setOccurrence(1);
				
				FileNode fn = new FileNode();
				fn.setFileID(fileName);
				fn.setOccurrence(1);
				n.specific.add(fn);
				

				if(esw.isStop(token)) {
					continue;
				}
				else {					
					//stemming stuff
					ack = token.toCharArray();
					s.add(ack, token.length());
					s.stem();
					token = s.toString();
					
					lineCount++;
					
					//set token in queue
					n.setKeyword(token);
					keys.add(n);
				}
			}
		}
		
		wordCount += lineCount;
	}	
	
}