package com.crawljax.examples.experiments.graphdb.correctness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.crawljax.core.state.StateFlowGraph;

public class DeterministicFinder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.setProperty("webdriver.firefox.bin",
		        "/ubc/ece/home/am/grads/azarei/firefox 7/firefox/firefox-bin");

		CorrecnessExperiment.setMaxState(15);
		CorrecnessExperiment.setMAX_DEPTH(5);

		List<String> urls = new ArrayList<String>();

		Set<String> alreadyTestedUrls = new HashSet<String>();

		alreadyTestedUrls.addAll(urls);
		urls.clear();

		urls.add("http://metrics.codahale.com/");
		urls.add("http://www.facebook.com");
		urls.add("http://www.ece.ubc.ca/");
		urls.add("http://www.ubc.ca/");
		urls.add("https://www.google.ca/");
		urls.add("http://www.bing.com/");
		urls.add("https://mail.google.com/");
		urls.add("https://github.com/");
		urls.add("https://code.google.com/p/guava-libraries/");
		urls.add("http://www.vogella.com/");
		urls.add("http://www.cacno.org");
		urls.add("http://www.cultofmac.com/");
		urls.add("http://www.heatcityreview.com/somervillenews.htm");
		urls.add("http://sedo.co.uk/search/details.php4?domain=thestart.net");
		urls.add("http://www.iltasanomat.fi/");
		urls.add("http://www.fairvote.org/");
		urls.add("http://www.beehive.nu/");
		urls.add("http://www.uss.de/");
		urls.add("http://www.littlewhitedog.com/");

		urls.add("http://www.python.org/");
		urls.add("http://www.martinkrenn.net/");
		urls.add("http://www.provincetown.com/");
		urls.add("http://www.expedia.ca/?semcid=ni.ask.12908&kword=DEFAULT.NNNN.kid&rfrr=Redirect.From.www.expedia.com/Home.htm");
		urls.add("http://www.airtoons.com/");
		urls.add("http://www.loco.pl/pl/");
		urls.add("http://www.grudge-match.com/current.html");
		urls.add("http://www.kastanova.nl/");
		urls.add("http://www.acces-local.com/wordpress/");
		urls.add("http://www.sfchronicle.com/");
		urls.add("http://www.eldritch.com/");
		urls.add("http://www.eldritch.com/");
		urls.add("http://www.al-awda.org/");
		urls.add("http://thething.it/");
		urls.add("http://ruyguy15.150m.com/");
		urls.add("http://www.bghs.org/");
		urls.add("http://www.axis-of-aevil.net/");
		urls.add("http://www.infoshop.org/");
		urls.add("http://www.introducingmonday.co.uk/");
		urls.add("http://www.engruppo.com/");

		urls.add("http://www.linuxdevcenter.com/pub/a/linux/2000/06/29/hdparm.html");
		urls.add("http://www.twentysevenrecords.com/");
		urls.add("http://www.hallwalls.org/");
		urls.add("http://www.justfood.org/");
		urls.add("http://bshigley.tumblr.com/");
		urls.add("http://www.ottawacitizen.com/index.html");
		urls.add("http://emeraldforestseattle.com/forums/ubbthreads/");
		urls.add("http://www.cancernews.com/default2.asp");
		urls.add("http://www.turbopatents.com/");
		urls.add("http://www.usablenet.com/");
		urls.add("http://www.viz.com/naruto");
		urls.add("http://www.viz.com/naruto");
		urls.add("http://www.needcoffee.com/");
		urls.add("http://www.libraryplanet.com/");
		urls.add("http://www.coversproject.com/");
		urls.add("http://windows.microsoft.com/en-us/windows/support#top-solutions=windows-8");
		urls.add("http://buffalo.bisons.milb.com/index.jsp?sid=t422");
		urls.add("http://www.b3ta.com/");
		urls.add("http://antiadvertisingagency.com/");
		urls.add("http://www.sfbike.org/");

		for (String uRL : urls) {

			if (alreadyTestedUrls.contains(uRL) == false) {
				try {
					StateFlowGraph inMemorySfg = CorrecnessExperiment.crawlInMemory(uRL);
					StateFlowGraph inMemorySfg2 = CorrecnessExperiment.crawlInMemory(uRL);
					CorrecnessExperiment.createExperimentReport(inMemorySfg, inMemorySfg2, uRL);
				} catch (Exception e) {

				}
			}
		}

	}

}
