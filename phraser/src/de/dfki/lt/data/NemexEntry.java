package de.dfki.lt.data;

import java.util.Collections;
import java.util.Iterator;

/**
 * <it>NEMEX entry: index weight list-of(CAT:Weight)</it>
 * @author GN
 *
 */
public final class NemexEntry extends AbstractLexiconEntry<Pair<String,Double>>{

	public NemexEntry(String word, int index, String type, double weight){
		this.setIndex(index);
		this.setWeight(weight);
		this.setWord(word);
		this.setNewReading(new Pair<String,Double>(type, weight));
	}

	// Methods

	/**
	 * Finds the maximum element within readings using Comparator class CocwReadingComparator
	 * which basically compares the weight of two readings
	 * @return
	 */
	public Pair<String,Double> selectMaxReading (){
		return Collections.max(this.readings, new ReadingComparator());
	}

	/**
	 * Iterates through all readings and concatenates them to one large printable string
	 * @param ccwLexiconEntry
	 * @return
	 */
	public String readingsToString () {
		// 
		String stringOfreadings = "";
		Iterator<Pair<String,Double>> it = this.getReadings().iterator();
		while (it.hasNext()) {
			Pair<String,Double> nextReading = it.next();
			stringOfreadings = stringOfreadings+" " + nextReading.getL()+":"+nextReading.getR();
		}
		return stringOfreadings;
	}

	/**
	 * Iterates through all readings and concatenates them to one large printable string
	 * @param ccwLexiconEntry
	 * @return
	 */
	public String toString() {
		// A single reading only of form: 1 -9.197762 abacterial NG:1:-9.197762
		String nemexString = "";
		nemexString = this.getIndex()+" "
				+this.getWeight()+" "
				+this.getWord()+" "
				+this.readingsToString()
				;
		return nemexString;
	}
}
