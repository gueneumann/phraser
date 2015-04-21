package de.dfki.lt.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import de.dfki.lt.data.NemexEntry;

/**
 * This class will transform a given Wikipedia list as defined in Ratonov and Roth, CoNLL 09.
 * A list is just a list of MWL without any further information.
 * However, the filename gives a type.
 * When scanning a list, one needs to apply some filters, e.g., spam entries that contain
 * Wikipedia meta information
 * @author gune00
 *
 */
public class WikiPediaList2Nemex {
	private String inFile ;
	private String outFile;
	private String nerType;
	private int index = 0;
	private int mwlEntries = 0;
	private int singleWordEntries = 0;
	private long time1 ;
	private long time2 ;
	private NemexEntry currentNemexEntry;

	public WikiPediaList2Nemex(String inFile, String outFile, String nerType){
		this.inFile = inFile;
		this.outFile = outFile;
		this.nerType = nerType;
	}

	// Setters and Getters
	public String getInFile() {
		return inFile;
	}
	public void setInFile(String inFile) {
		this.inFile = inFile;
	}
	public String getOutFile() {
		return outFile;
	}
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	public String getNerType() {
		return nerType;
	}
	public void setNerType(String nerType) {
		this.nerType = nerType;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getMwlEntries() {
		return mwlEntries;
	}
	public void setMwlEntries(int mwlEntries) {
		this.mwlEntries = mwlEntries;
	}
	public int getSingleWordEntries() {
		return singleWordEntries;
	}
	public void setSingleWordEntries(int singleWordEntries) {
		this.singleWordEntries = singleWordEntries;
	}
	public long getTime1() {
		return time1;
	}
	public void setTime1(long time1) {
		this.time1 = time1;
	}
	public long getTime2() {
		return time2;
	}
	public void setTime2(long time2) {
		this.time2 = time2;
	}

	/**
	 * Scans WikiPedia file and reads it linewise
	 * Files are in "J:/data/NE-Lists/wikiPedia/"
	 * @param phrasalClusterFileName
	 */
	private void scanWikiPediaListFile (){
		BufferedReader in = null;
		BufferedWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(this.getInFile())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out = new BufferedWriter(new FileWriter(this.getOutFile()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String content;

		try {
			while ((content = in.readLine()) != null){
				//TODO How to add initial lexicon line ?
				String[] parsedLine = content.split(" ");
				this.singleWordEntries=this.mwlEntries; // Would need to keep a unique hash in order to word types
				if (isValidEntry(content)){
					this.mwlEntries++;
					this.currentNemexEntry = makeNewNemexEntry(parsedLine, index++, nerType, 0.0);
					out.write(this.currentNemexEntry.toString());
					out.write("\n");
				}
			}
			//0 utf-8 EN 9875 9871
			String initialLine = "0 " + "UTF8 " + this.getMwlEntries() + " " + this.getSingleWordEntries();
			out.write(initialLine+"\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isValidEntry(String content) {
		if (content.contains("Category:"))
			return false;
		return true;
	}

	private NemexEntry makeNewNemexEntry(String[] parsedLine, int i, String nerType, double weight) {
		String[] cleanedLine = stripOfElements(parsedLine);
		String word = makeNemexWord(cleanedLine);
		NemexEntry nemexEntry = new NemexEntry(word, i, nerType, weight);
		return nemexEntry;
	}

	private String[] stripOfElements(String[] parsedLine) {
		// TODO Auto-generated method stub
		return parsedLine;
	}

	private String makeNemexWord(String[] parsedLine) {
		String word = "";
		if (parsedLine.length==1)
			word = parsedLine[0];
		else
		{
			for (int i=0; i < parsedLine.length-1; i++)
				word = word + parsedLine[i]+"#";
			word = word + parsedLine[parsedLine.length-1];
		}
		return word;
	}

	/**
	 * args[0], args[1], args[2]:
	 * J:/data/NE-Lists/wikiPedia/WikiPeople.lst
	 * J:/data/NE-Lists/wikiPedia/nemex/WikiPeople.txt
	 * PERSON
	 * in
	 * @param args
	 */
	public static void main(String[] args) {
		WikiPediaList2Nemex nemexDictionary = new WikiPediaList2Nemex(args[0], args[1], args[2]);
		nemexDictionary.scanWikiPediaListFile();
	}
}
