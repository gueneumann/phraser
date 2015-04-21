/*
 * Created on 15.11.2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.dfki.lt.data;

import java.util.HashSet;

/**
 * A abstract class for lexicon entries which should keep the main slots for
 * different systems.
 * <P>I assume that a lexical entry has a either a single reading or a collection of alternative
 * readings; In order to handle this systematically I will use a boolean variable to indicate whether
 * the reading collection has more than one element or not.<p>
 * 
 * <P>A reading itself can be different for the different systems I have in mind:
 * <UL>
 * <it>Nereid: CAT:Weight</it>
 * <it>Morphix: INFL -> encoded as either a integer or a list containing partial assoc-list and integer</it>
 * <it>LTIG: Tree-Index</it>
 * </UL>
 * </P>
 * 
 * @author Günter Neumann
 */

abstract class AbstractLexiconEntry <R>{
	//~ Parameters -----------------------------------------------------------
	
	String word ="";
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @param readings is a set of alternative readings; no ordering is assumed
	 * the concrete type of its elements depends on the specific realization.
	 */
	HashSet<R> readings = new HashSet<R>();
	/**
	 * @param disjunctive is a binary variable that indicate whether the entry
	 * contains more than one element (reading)
	 */
	boolean disjunctive = false;
	
	/**
	 * The global weight value for the lexical entry; default value is (log 1.0 base=2)
	 */
	double weight = 0.0;
	
	int index = 0;
	
	//~ Constructors -----------------------------------------------------------
	
	/**
	 * This constructor makes sure the proper default setting of slots
	 */
	public AbstractLexiconEntry () {
		disjunctive = false;
		weight = 0.0;
	}
	
	//~ Set and get methods -----------------------------------------------------
	
	public void setDisjunctiveFlag (boolean flag) {
		disjunctive = flag;
	}
	public boolean getDisjunctiveFlag () {
		return disjunctive;
	}
	
	public void setWeight (double weight) {
		this.weight = weight;
	}
	public double getWeight () {
		return weight;
	}
	
	public void setIndex (int index) {
		this.index = index;
	}
	public int getIndex () {
		return index;
	}
	
	public HashSet<R> getReadings(){
		return this.readings;
	}
	
	//~ Methods ----------------------------------------------------------------
	
	/**
	 * Returns true if disjunctive == true else false
	 * @return disjunctive
	 */
	
	public boolean hasMultipleReadings () {
		return disjunctive == true;
	}
	
	/**
	 * Returns true if lexical entry has some readings, false otherwise
	 * @return
	 */
	public boolean hasReadings () {
		return ! readings.isEmpty();
	}
	
	/**
	 * Inserts a new reading (if it does not exist) into the readings hashSet
	 * and sets disjunctive to true if the length of the readings hash is larger 
	 * than 1.
	 * @param reading
	 */
	
	public void insertNewReading (R reading) {
		getReadings().add(reading);
		if (getReadings().size() > 1) 
			disjunctive = true;
	}
	
	// TODO I need to define deleteReading() functions!
	
	public void clearReadings () {
		getReadings().clear();
	}
	
	public void setNewReading (R reading) {
		clearReadings();
		insertNewReading(reading);
	}

}