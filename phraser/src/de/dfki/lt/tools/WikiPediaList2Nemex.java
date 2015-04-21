package de.dfki.lt.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
			out = new BufferedWriter(new FileWriter(this.getOutFile()+".tmp"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String content;

		try {
			while ((content = in.readLine()) != null){
				String[] parsedLine = content.split(" ");
				if (isValidEntry(content)){
					index++;
					this.mwlEntries++;
					this.singleWordEntries=this.mwlEntries; // Would need to keep a unique hash in order to word types
					this.currentNemexEntry = makeNewNemexEntry(parsedLine, index, nerType, 0.0);
					out.write(this.currentNemexEntry.toString());
					out.write("\n");
				}
			}
			
			in.close();
			out.close();

			//0 utf-8 EN 9875 9871
			// Copy file to new file which has initial line and delete old file.
			// This is probably inefficient but might work.

			String initialLine = "0 " + "UTF8 " + this.getMwlEntries() + " " + this.getSingleWordEntries();
			WikiPediaList2Nemex.transcode(this.getOutFile()+".tmp", this.getOutFile(), initialLine);
			WikiPediaList2Nemex.deleteTmpFile(this.getOutFile()+".tmp");
			

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

	private static void transcode(String sourceFileName, String targetFileName, String initialLine)
			throws IOException {

		// init reader
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(sourceFileName),
						"utf8"));

		// init writer
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(targetFileName),
						"utf8"));
		writer.write(initialLine+"\n");

		// write each character from the source file to the target file; this is
		// done character by character instead of line by line to exactly copy the
		// source's line termination character(s) which wouldn't be possible in some
		// cases otherwise (java.io.BufferedWriter.newLine() uses a system dependent
		// line separator instead of the one from the source file).
		int currChar = 0;
		while ((currChar = reader.read()) != -1) {
			writer.write(currChar);
		}

		reader.close();
		writer.close();


	}

	private static void deleteTmpFile(String sourceFileName){
		String fileName = sourceFileName;
		// A File object to represent the filename
		File f = new File(fileName);

		// Make sure the file or directory exists and isn't write protected
		if (!f.exists())
			throw new IllegalArgumentException(
					"Delete: no such file or directory: " + fileName);

		if (!f.canWrite())
			throw new IllegalArgumentException("Delete: write protected: "
					+ fileName);

		// If it is a directory, make sure it is empty
		if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0)
				throw new IllegalArgumentException(
						"Delete: directory not empty: " + fileName);
		}

		// Attempt to delete it
		boolean success = f.delete();

		if (!success)
			throw new IllegalArgumentException("Delete: deletion failed");
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
