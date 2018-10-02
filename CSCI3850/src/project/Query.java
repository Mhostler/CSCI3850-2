package project;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Query implements Runnable {
	private ConcurrentLinkedQueue<queryNode> found = new ConcurrentLinkedQueue<queryNode>();
	private ConcurrentLinkedQueue<FileNode> hit;
	private double weight;
	private Iterator<FileNode> hitIt;
	String query = "";
	String original;

	public Query(String q, String p) {
		query = p;
		original = q;
	}
	
	public void commence(String list){
		ConcurrentHashMap<String, Node> archive = CSCI3850.mapping;
		ConcurrentLinkedQueue<queryNode> mergeFound = new ConcurrentLinkedQueue<queryNode>();
		String[] xyz = list.split(" ");
		for(String term : xyz) {
			if(archive.containsKey(term)) {
				Node alpha = archive.get(term);
				hit = alpha.getQueue();
				hitIt = hit.iterator();
				while(hitIt.hasNext()) {
					queryNode a = new queryNode();
					FileNode b = hitIt.next();
					a.setDocID(b.getFileID());
					weight = (double)b.getOccurrence() / (double)b.getWordCount();
					a.addWeight(weight);
					mergeFound.add(a);
				}
				
				merger(mergeFound);
				mergeFound.clear();
			}
		}
	}
	
	//may need to be modified later
	public void printOut(ConcurrentLinkedQueue<queryNode> toPrint, long time, String query) {
		Iterator<queryNode> printIt = toPrint.iterator();
		//int counter = 0;
		String toOut = "Query '%s', time to process: %d\n";
		//System.out.printf("Query '%s', time to process: %d\n", query, time);
//		while (printIt.hasNext() && counter < 10) {
//			queryNode p = printIt.next();
//			System.out.printf("DocID: %s  Weight: %f\n", p.getDocID(), p.getWeight());
//			counter++;
//		}
		
		String [] qn = new String[10];
		for(int i = 0; i < 10; i++ ) {
			toOut += "DocID: %s\n";
			qn[i] = printIt.next().getDocID();
		}
		toOut += "\n";
		System.out.printf(toOut, original, time, qn[0], qn[1], qn[2], qn[3], qn[4], qn[5], qn[6], qn[7], qn[8], qn[9]);
	}
	
	public void merger(ConcurrentLinkedQueue<queryNode> toMerge) {
		//for every item in tomerge if found in found add the weights together if not add the node to found
		ConcurrentLinkedQueue<queryNode> ttt = new ConcurrentLinkedQueue<queryNode>();
		
		while(!toMerge.isEmpty() || !found.isEmpty()) {
			if(toMerge.isEmpty()) {
				ttt.addAll(found);
				break;
			} else if(found.isEmpty()) {
				ttt.addAll(toMerge);
				break;
			}
			
			if(toMerge.peek().getDocID().compareTo(found.peek().getDocID()) < 0) {
				ttt.add(toMerge.poll());
			} else if(toMerge.peek().getDocID().compareTo(found.peek().getDocID()) > 0) {
				ttt.add(found.poll());
			} else {
				queryNode j = found.poll();
				j.addWeight(toMerge.poll().getWeight());
				ttt.add(j);
			}
		}
		found = ttt;
	}
	
	public ConcurrentLinkedQueue<queryNode> processQueue(ConcurrentLinkedQueue<queryNode> a) {
		queryNode[] arrs = new queryNode[11];
		ConcurrentLinkedQueue<queryNode> sorted = new ConcurrentLinkedQueue<queryNode>(); //change this so it returns something
		
		for(int i = 0; i < arrs.length; i++) {
			arrs[i] = new queryNode();
			arrs[i].setWeight(0f);
		}
		for(queryNode v : a) {
			if(v == null) {
				System.out.println("Nulls in Final list");
				continue;
			}
			arrs[10] = v;
			arrs = sortHelp(arrs);
			
		}
		for(queryNode k : arrs) {
			sorted.add(k);
		}
		
		return sorted;
	}
	
	//helper method
	public queryNode[] sortHelp(queryNode[] toSort) {
		//insertion sort implementation
		for(int l = (toSort.length - 1); l > 0 ; l--) {
			if(toSort[l].getWeight() > toSort[l - 1].getWeight()) {
				queryNode temp = toSort[l];
				toSort[l] = toSort[l - 1];
				toSort[l - 1] = temp;
			}
			else
				break;
		}
		return toSort;
	}
	
	public void run() {
		long timeStart = System.currentTimeMillis();
		commence(query);
		found = processQueue(found);
		long timestop = System.currentTimeMillis() - timeStart;
		printOut(found, timestop, query);
	}
	
}

