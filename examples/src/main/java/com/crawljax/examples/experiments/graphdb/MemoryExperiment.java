/**
 * 
 */
package com.crawljax.examples.experiments.graphdb;

import javax.swing.JOptionPane;

import com.crawljax.core.state.StateFlowGraph;

/**
 * @author arz
 */
public class MemoryExperiment {

	/**
	 * @param args
	 */

	public static String URL = "http:www.yahoo.com";
	// "http://en.wikipedia.org/wiki/Main_Page";
	// "http://www.yahoo.com";
	// "http://localhost/correctness/c5__MemoryExpObject.htm";
	public static int MAX_STATES = 20000;
	public static int MAX_DEPTH = 30;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		CorrecnessExperiment.setMaxState(MAX_STATES);
		CorrecnessExperiment.setMAX_DEPTH(MAX_DEPTH);

		StringBuffer message = new StringBuffer();
		StateFlowGraph sfg = null;
		try {

		sfg = CorrecnessExperiment.crawlInDb(URL);

		//sfg = CorrecnessExperiment.crawlInMemory(URL);
		} catch (Exception e) {
			message.append(e).append("\n").append(e.getMessage()).append('\n');

		}

		long endTime = System.currentTimeMillis() - startTime;
		long time = endTime / 1000;
		int numberOfstates = sfg.getNumberOfStates();
		int averageDomSize = sfg.getMeanStateStringSize();

		message.append(
		        "time: " + time + "\nnumber of states" + numberOfstates + "average mean size: "
		                + averageDomSize);
		JOptionPane.showMessageDialog(null, message);

	}

}
