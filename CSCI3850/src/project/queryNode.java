package project;

public class queryNode {
	String docID;
	double weight = 0;
	
	public String getDocID() {return docID;}
	public double getWeight() {return weight;}
	
	public void setDocID(String a) {docID = a;}
	public void setWeight(double b) {weight = b;}
	public void addWeight(double c) {weight += c;}
	
	public boolean equals(Object e) {
		if (e == this)
    		return true;
    	
    	if( !(e instanceof queryNode)) {
    		return false;
    	}
    	
    	queryNode n = (queryNode)e;
    	
    	return this.getDocID().equals(n.getDocID());
	}
}
