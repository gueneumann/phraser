package de.dfki.lt.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import de.dfki.lt.data.ClusterDictionary;
import de.dfki.lt.data.Pair;

/**
 * This is the main class for creating dictionaries of phrases which all belong to the same cluster.
 * The cluster files are in /Users/gune00/data/PhrasalClusters
 *
 * Format
The file contains an alphabetical listing of phrases and their cluster memberships. 
Each line is a tab-separated list of the phrase and each cluster it belongs to, 
along with the similarity to each cluster centroid. 
Up to twenty clusters with the highest centroid similarities are included for each phrase.

AutoCAD LT      401     0.260717        736     0.196809        783     0.183296        525     0.177165        808     0.165705        218     0.162994        815     0.141620        97      0.140854        812     0.139386        244     0.127848        111  0.124953 163     0.124443        55      0.123724        324     0.117858        832     0.115209        70      0.113849        197     0.111080        5       0.109667        357     0.107718        833     0.107164

There are 9901505 phrases in total. 
 * 
 * From this, I will create for each cluster a dictionary which contains the phrases and their centroid-similarity.
 * 
 * Assuming, phrases are unique for cluster id, the dictionary is just a list of 
 * phrase	score
 * 
 * Method:
 * - iterate through gzipped file
 * - parse each line into 
 * 	- pairs of cluster id and score
 *  - create cluster-file name if not already done
 *  - create entry string and add to file
 *  
 *  - I will define a dictionary file object:
 *  	- cluster id
 *  	- cluster directory
 *  	- cluster filename
 *  	- number of entries
 *  	- entryToString
 *  
 *  Question: can I create already a sorted file or should I do it with unix tools ?
 * 
 * @author gune00
 *
 */
public class CreateClusterDictionaries {
	private HashMap<String, ClusterDictionary> clusterMap = new HashMap<String, ClusterDictionary>();
	private String sourceDir ;
	private String targetDir;
	private long time1 ;
	private long time2 ;

	public CreateClusterDictionaries(String sourceDir, String targetDir){
		this.sourceDir = sourceDir;
		this.targetDir = targetDir;
	}


	/**
	 * Scans gzip files and reads it linewise
	 * Files are in "/Users/gune00/data/PhrasalClusters/phraseClusters.1.txt.gz"
	 * @param phrasalClusterFileName
	 */
	private void scanGZIPphrasalClustersFile (String phrasalClusterFileName){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					new GZIPInputStream(new FileInputStream(phrasalClusterFileName))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String content;

		try {
			while ((content = in.readLine()) != null){
				String[] parsedLine = content.split("\t");
				String phrase = parsedLine[0];
				List<Pair<String,String>> clusterIdScorePairs = this.makeClusterIdScorePairs(parsedLine);
				createAndUpdateClusterDictionaries(phrase, clusterIdScorePairs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Pair<String,String>> makeClusterIdScorePairs(String[] parsedLine){
		List<Pair<String,String>> pairs = new ArrayList<Pair<String,String>>();
		for (int i = 1; i < parsedLine.length; i++){
			Pair<String,String> newPair = new Pair<String,String>(parsedLine[i], parsedLine[i+1]);
			pairs.add(newPair);
			i++;
		}
		return pairs;
	}

	private void createAndUpdateClusterDictionaries(String phrase,
			List<Pair<String, String>> clusterIdScorePairs) {
		Iterator<Pair<String, String>> iterator = clusterIdScorePairs.iterator();
		while (iterator.hasNext()) {
			Pair<String, String> nextPair = iterator.next();
			ClusterDictionary clusterDictionary = addAndReturnCLusterDictionary(nextPair.getL());
			String entryString = phrase+"\t"+nextPair.getR();
			clusterDictionary.writeEntryString(entryString);
		}	
	}

	private ClusterDictionary addAndReturnCLusterDictionary(String clusterId) {
		if (this.clusterMap.containsKey(clusterId)){
			return this.clusterMap.get(clusterId);}
		else
		{
			ClusterDictionary newClusterDictionary = new ClusterDictionary(clusterId, this.targetDir);
			System.out.println("Creating ... " + newClusterDictionary.getFilename());
			this.clusterMap.put(clusterId, newClusterDictionary);
			return newClusterDictionary;
		}
	}

	/**
	 * This is for processing files stored in phrasalClusters.
	 * NOTE: This could also be processed by a clsuter-based map
	 */
	private void processPhrasalDirectory (){
		for (int i=1; i < 11; i++){
			String inFileName = this.sourceDir + "phraseClusters." + i + ".txt.gz";
			System.out.println(inFileName);
			time1 = System.currentTimeMillis();
			this.scanGZIPphrasalClustersFile(inFileName);
			time2 = System.currentTimeMillis();
			System.out.println("System time (msec): " + (time2-time1));
		}
	}

	private void closeAllDictionaryStreams() {
		Iterator<Entry<String, ClusterDictionary>> iterator = this.clusterMap.entrySet().iterator();
		while (iterator.hasNext()){
			ClusterDictionary nextDic = iterator.next().getValue();
			System.out.println("Closing ... " + nextDic.getFilename());
			nextDic.closeDictionaryStream();
		}
	}

	public void sortPhrasalFiles () throws IOException{
		Iterator<Entry<String, ClusterDictionary>> iterator = this.clusterMap.entrySet().iterator();
		while (iterator.hasNext()){
			ClusterDictionary nextDic = iterator.next().getValue();
			System.out.println("Sorting ... " + nextDic.getFilename());
			time1 = System.currentTimeMillis();

			//TODO Should define one method
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(nextDic.getFilename()));
			Map<Double, String> map=new TreeMap<Double, String>(new myDoubleComparator());
			String line="";
			while((line=reader.readLine())!=null){
				map.put(getField(line),line);
			}
			reader.close();

			BufferedWriter writer = new BufferedWriter(new FileWriter(nextDic.getFilename()));
			for(String val : map.values()){
				writer.write(val);      
				writer.newLine();
			}
			writer.close();

			time2 = System.currentTimeMillis();
			System.out.println("System time (msec): " + (time2-time1));
		}
	}
	
	private static double getField(String line) {
		String[] lineSeparated = line.split("\t");
		String lastElem = lineSeparated[lineSeparated.length-1];
		Double lastElemInt = Double.valueOf(lastElem);

		return lastElemInt;//extract value you want to sort on
	}

	/**
	 * Main method:
	 * initialize Custer dictionary set.
	 * scan all files from sourceDir and create dictionary files
	 * finally close all open stream.
	 * <p>"/Users/gune00/data/PhrasalClusters/",
	 * <p>"/Users/gune00/data/PhrasalClusters/clusterDics/"
	 * @param args
	 */
	public static void main(String[] args) {
		CreateClusterDictionaries clusterDic = new CreateClusterDictionaries(
				args[0], args[1]);
		clusterDic.processPhrasalDirectory();
		clusterDic.closeAllDictionaryStreams();
		try {
			clusterDic.sortPhrasalFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class myDoubleComparator implements Comparator<Double> {
	@Override
	//Compares two readings based on their weights
	public int compare(Double o1, Double o2) {
		if (o1 == o2) return 0;
		else
			if (o1 > o2) return -1;
			else
				return 1;
	}
}
