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

	public static String URL = "http://www.yahoo.com";
	// "http://localhost/correctness/c5__MemoryExpObject.htm";
	public static int MAX_STATES = 100;
	public static int MAX_DEPTH = 10;

	public static void main(String[] args) {

		CorrecnessExperiment.setMaxState(MAX_STATES);
		CorrecnessExperiment.setMAX_DEPTH(MAX_DEPTH);

		// CorrecnessExperiment.crawlInMemory(URL);

		CorrecnessExperiment.crawlInDb(URL);

	}

}
