/**
 * 
 */
package com.crawljax.examples.experiments.graphdb;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.state.InDatabaseStateFlowGraph;
import com.crawljax.core.state.StateFlowGraph;

/**
 * @author arz
 */
public class MemoryExperiment {

	private static final Logger LOG = LoggerFactory.getLogger(MemoryExperiment.class
	        .getName());
	/**
	 * @param args
	 */

	public static String URL = "http:www.amazon.com";
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

		//sfg = CorrecnessExperiment.crawlInDb(URL);

		sfg = CorrecnessExperiment.crawlInMemory(URL);
		} catch (Exception e) {
			message.append(e).append("\n").append(e.getMessage()).append('\n');

		}

		long endTime = System.currentTimeMillis() - startTime;
		long time = endTime / 1000;
		int numberOfstates = sfg.getNumberOfStates();
		int averageDomSize = sfg.getMeanStateStringSize();

		message.append(
		        "time: " + time + "\nnumber of states" + numberOfstates + "\naverage mean size: "
		                + averageDomSize);
		JOptionPane.showMessageDialog(null, message);
		LOG.info(message.toString());

	}

}
