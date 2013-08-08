/**
 * 
 */
package com.crawljax.examples;

import com.crawljax.examples.experiments.graphdb.correctness.CorrecnessExperiment;

/**
 * @author arz
 */
public class MemoryExperiment {

	/**
	 * @param args
	 */

	public static String URL = "https:www.google.com";
	// "http://en.wikipedia.org/wiki/Main_Page";
	// "http://www.yahoo.com";
	// "http://localhost/correctness/c5__MemoryExpObject.htm";
	public static int MAX_STATES = 500;
	public static int MAX_DEPTH = 3;

	public static void main(String[] args) {

		CorrecnessExperiment.setMaxState(MAX_STATES);
		CorrecnessExperiment.setMAX_DEPTH(MAX_DEPTH);

		// CorrecnessExperiment.crawlInMemory(URL);

		CorrecnessExperiment.crawlInDb(URL);

	}

}
