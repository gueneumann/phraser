/**
 * Created on 13.12.2012
 * by gune00 
 * LT Lab.
 * German Research Center for Artificial Intelligence
 * (Deutsches Forschungszentrum fuer Kuenstliche Intelligenz GmbH = DFKI)
 * http://www.dfki.de
 * Saarbruecken, Saarland, Germany
 */
package de.dfki.lt.data;

import java.util.Comparator;

public class ReadingComparator implements
		Comparator<Pair<String,Double>> {

	@Override
	//Compares two readings based on their weights
	public int compare(Pair<String,Double> o1, Pair<String,Double> o2) {
		if (o1.getR() == o2.getR()) return 0;
		else
			if (o1.getR() < o2.getR()) return -1;
			else
				return 1;
	}
}
