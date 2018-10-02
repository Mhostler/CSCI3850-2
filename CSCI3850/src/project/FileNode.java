package project;

public class FileNode {
    String fileID;
    String title;
    int occurrence = 0;
    long wordCount = 0;
    
    public String getFileID() { return fileID; }
    public String getTitle() { return title; }
    public int getOccurrence() { return occurrence; }
    public long getWordCount() { return wordCount; }
    
    public void setFileID(String id) { fileID = id; }
    public void setTitle(String name) { title = name; }
    public void setOccurrence(int oc) { occurrence = oc; }
    public void setWordCount(long wc) {wordCount = wc;}
    
    public synchronized void increment() { occurrence++; }
    
    public int compareTo( FileNode fn ) { return this.getFileID().compareTo( fn.getFileID() ); }
}
