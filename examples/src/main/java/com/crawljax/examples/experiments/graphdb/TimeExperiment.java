/**
 * 
 */
package com.crawljax.examples.experiments.graphdb;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawljax.core.state.StateFlowGraph;

/**
 * @author arz
 */
public class TimeExperiment {

	/**
	 * @param args
	 */

	private static final Logger LOG = LoggerFactory.getLogger(TimeExperiment.class);

	
	public static String URL = "http:www.google.com";
	// "http://en.wikipedia.org/wiki/Main_Page";
	// "http://www.yahoo.com";
	// "http://localhost/correctness/c5__MemoryExpObject.htm";
	public static int MAX_STATES = 100;
	public static int MAX_DEPTH = 30;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		Experiment.setMaxState(MAX_STATES);
		LOG.info("Max_state set to {}",MAX_STATES);
		Experiment.setMAX_DEPTH(MAX_DEPTH);
		LOG.info("max_dpeth is set to {}",MAX_DEPTH);
		LOG.info("URL: {}",URL);
		StringBuffer message = new StringBuffer();
		StateFlowGraph sfg = null;
		try {

		// sfg = CorrecnessExperiment.crawlInDb(URL);

		sfg = Experiment.crawlInMemory(URL);
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
		JOptionPane.showMessageDialog(null, message.toString());
		LOG.info(message.toString());
		

	}

}
