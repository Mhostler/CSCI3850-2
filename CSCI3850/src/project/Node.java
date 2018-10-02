package project;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * keyword: Dictionary item
 * concurrence: how many times it appears
 * specific: filenames for document appearances 
 */
public class Node {
    private String keyword;
    public ConcurrentLinkedQueue<FileNode> specific;
    private int occurrence;
    
    public Node() {
    	specific = new ConcurrentLinkedQueue<FileNode>();
    }
    
    public String getKeyword() { return keyword; }    	
    
    public void setKeyword(String key) { keyword = key; }
    
    public int getOccurrence() { return occurrence; }
    public void setOccurrence(int val) { occurrence = val; }
    
    public ConcurrentLinkedQueue<FileNode> getQueue() { return specific; }
    public void setQueue( ConcurrentLinkedQueue<FileNode> queue ) { specific = queue; }
    
    public FileNode deQueue() { return specific.remove(); }
    public void enQueue(FileNode node) { specific.add(node); }

    public synchronized void addSimilar(Node n) {
    	occurrence = occurrence + n.getOccurrence();
    	fmerge(n.getQueue());
    }
    
    public synchronized void fmerge( ConcurrentLinkedQueue<FileNode> q2 ) {
		if( specific.isEmpty() ) {
			specific.addAll(q2);
		}
		else if( q2.isEmpty() ) { return; }
		
		ConcurrentLinkedQueue<FileNode> merged = new ConcurrentLinkedQueue<FileNode>();
		
		while( !specific.isEmpty() || !q2.isEmpty() ) {
			
			if( specific.isEmpty() ) {
				merged.addAll( q2 );
				break;
			}
			else if( q2.isEmpty() ) {
				merged.addAll( specific );
				break;
			}
			
			FileNode tmp = q2.poll();
			if( tmp == null ) {
				if( q2.isEmpty() ) {
					continue;
				}
				
				while( tmp == null && !q2.isEmpty() ) {
					System.out.println("Nulls in Node");
					tmp = q2.poll();
				}
			}
			
			if( specific.peek().compareTo( tmp ) < 0 ) {
				merged.add( specific.poll() );
			}
			else if( specific.peek().compareTo( tmp ) > 0 ) {
				merged.add( tmp );
			}
			else {
				FileNode n = specific.poll();
				FileNode n2 = tmp;
				n.setOccurrence( n.getOccurrence() + n2.getOccurrence() );
				
				if( n.getWordCount() != n2.getWordCount() ) {
					System.out.println("Word count mismatch.");
				}
				
				merged.add( n );
			}
		}
		
		
		specific = merged;
    }
    
    public boolean equals(Object e) {
    	if (e == this)
    		return true;
    	
    	if( !(e instanceof Node)) {
    		return false;
    	}
    	
    	Node n = (Node)e;
    	
    	return this.getKeyword().equals(n.getKeyword());
    }
    
    public int compareTo( Node n ) {
    	return this.getKeyword().compareTo( n.getKeyword() );
    }
}
