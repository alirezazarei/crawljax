package com.crawljax.examples.experiments.graphdb.correctness;

import java.util.ArrayList;
import java.util.List;

import com.crawljax.core.state.StateFlowGraph;

public class DeterministicFinder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CorrecnessExperiment.setMaxState(15);
		CorrecnessExperiment.setMAX_DEPTH(5);

		List<String> urls = new ArrayList<String>();

//		urls.add("http://metrics.codahale.com/");
//		urls.add("http://www.facebook.com");
//		urls.add("http://www.ece.ubc.ca/");
//		urls.add("http://www.ubc.ca/");
//		urls.add("https://www.google.ca/");
//		urls.add("http://www.bing.com/");
//		urls.add("https://mail.google.com/");
//		urls.add("https://github.com/");
//		urls.add("https://code.google.com/p/guava-libraries/");
		urls.add("http://www.vogella.com/");

		for (String uRL : urls) {

			try {
				StateFlowGraph inMemorySfg = CorrecnessExperiment.crawlInMemory(uRL);
				StateFlowGraph inMemorySfg2 = CorrecnessExperiment.crawlInMemory(uRL);
				CorrecnessExperiment.createExperimentReport(inMemorySfg, inMemorySfg2, uRL);
			} catch (Exception e) {

			}
		}

	}

}
