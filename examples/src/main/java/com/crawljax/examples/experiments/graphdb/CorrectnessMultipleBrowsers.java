package com.crawljax.examples.experiments.graphdb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.crawljax.core.state.StateFlowGraph;

public class CorrectnessMultipleBrowsers {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		System.setProperty("webdriver.firefox.bin",
//		        "/ubc/ece/home/am/grads/azarei/firefox 7/firefox/firefox-bin");

		Experiment.setMaxState(50);
		Experiment.setMAX_DEPTH(5);
		

		List<String> urls = new ArrayList<String>();

		Set<String> alreadyTestedUrls = new HashSet<String>();

		// /////////////////////////////////////////////////////
		// ///////////////////first set//////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////

		// ////////////////50 det//////////////

		urls.add("http://www.heatcityreview.com/somervillenews.htm");// 4

		urls.add("http://www.kastanova.nl/");// five
		urls.add("http://www.engruppo.com/");// 1
		urls.add("http://www.turbopatents.com/");// 1

		// ////////////////////////////////////////////
		urls.add("http://www.martinkrenn.net/");// 50
		urls.add("http://www.al-awda.org/");// 50
		urls.add("http://thething.it/");// 50

		// /////////////////////////////////////////////////////
		// ////////////////end of first set beginning of next
		// set/////////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////

		// /////////////////// det 50
		urls.add("http://www.isc.org/downloads/BIND/");// states: 1
		urls.add("http://hunyyoung.com/"); // det 1
		urls.add("http://home.planet.nl/~mooij321/"); // states: 2
		urls.add("http://www.antique-hangups.com/"); // states : 1
		urls.add("http://www.project451.com/");// states : 3
		urls.add("http://lmuwnmd.wpengine.com/wp-signup.php?new=www.techblog.com");// states : 1

		urls.add("http://c-level.org/"); // det 50

		for (String uRL : urls) {

			if (alreadyTestedUrls.contains(uRL) == false) {
				try {
					Experiment.setNumberOfBrowsers(1);

					StateFlowGraph inMemorySfg2 = Experiment.crawlInMemory(uRL);
					
					Experiment.setNumberOfBrowsers(2);


					StateFlowGraph inMemorySfg = Experiment.crawlInMemory(uRL);

					Experiment.createExperimentReport(inMemorySfg, inMemorySfg2, uRL);
				} catch (Exception e) {

				}
			}
		}

	}
}
