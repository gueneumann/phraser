package de.dfki.lt.data;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * *  - I will define a dictionary file object:
 *  	- cluster id
 *  	- cluster directory
 *  	- cluster filename
 *  	- number of entries
 *  	- entryToString
 *  
 * @author gune00
 *
 */
public class ClusterDictionary {
	private String clusterId ;
	private String directory ;
	private String filename ;
	private BufferedWriter writer ; 
	private int numberOfentries = 0;

	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = this.getDirectory()+filename;
	}
	public int getNumberOfentries() {
		return numberOfentries;
	}
	public void setNumberOfentries(int numberOfentries) {
		this.numberOfentries = numberOfentries;
	}

	/**
	 * Create a clusterDictionary with clusterId as filename
	 * @param clusterId
	 * @param directory
	 */
	public ClusterDictionary(String clusterId, String directory){
		this.clusterId = clusterId;
		this.setDirectory(directory);
		this.setFilename(clusterId+".txt");
		this.makeDictionaryStream(getFilename(), "UTF8");
	}
	
	/**
	 * Open a write stream to the dictionary.
	 * @param filename
	 * @param encoding
	 */
	public void makeDictionaryStream(String filename, String encoding){
		try {
			this.writer = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(filename),
							encoding));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void closeDictionaryStream() {
		try {
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeEntryString(String entryString){
		try {
			this.numberOfentries++;
			this.writer.write(entryString+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
