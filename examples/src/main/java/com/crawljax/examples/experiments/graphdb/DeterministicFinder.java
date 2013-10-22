package com.crawljax.examples.experiments.graphdb;

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

//		System.setProperty("webdriver.firefox.bin", "/ubc/ece/home/am/grads/azarei/firefox 7/firefox/firefox-bin");

		Experiment.setMaxState(50);
		Experiment.setMAX_DEPTH(5);

		List<String> urls = new ArrayList<String>();

		Set<String> alreadyTestedUrls = new HashSet<String>();

		// /////////////////////////////////////////////////////
		// ///////////////////first set//////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////

		// ////////////////50 det//////////////

		urls.add("http://www.heatcityreview.com/somervillenews.htm");
		urls.add("http://www.martinkrenn.net/");
		urls.add("http://www.kastanova.nl/");
		urls.add("http://www.al-awda.org/");
		urls.add("http://thething.it/");
		urls.add("http://www.engruppo.com/");
		urls.add("http://www.turbopatents.com/");

		// ////////15 det ///////////////////////////////

		urls.add("http://www.ece.ubc.ca/");
		urls.add("http://www.ubc.ca/");

		// /////////////// non det////////////////////////

		urls.add("http://www.cultofmac.com/");
		urls.add("http://www.python.org/");
		urls.add("http://metrics.codahale.com/");
		urls.add("http://www.uss.de/");

		urls.add("http://www.facebook.com");

		urls.add("https://www.google.ca/");
		urls.add("http://www.bing.com/");
		urls.add("https://mail.google.com/");
		urls.add("https://github.com/");
		urls.add("https://code.google.com/p/guava-libraries/");
		urls.add("http://www.vogella.com/");
		urls.add("http://www.cacno.org");

		urls.add("http://sedo.co.uk/search/details.php4?domain=thestart.net");
		urls.add("http://www.iltasanomat.fi/");
		urls.add("http://www.fairvote.org/");
		urls.add("http://www.beehive.nu/");
		urls.add("http://www.littlewhitedog.com/");

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

		urls.add("http://ruyguy15.150m.com/");
		urls.add("http://www.bghs.org/");
		urls.add("http://www.axis-of-aevil.net/");
		urls.add("http://www.infoshop.org/");
		urls.add("http://www.introducingmonday.co.uk/");

		urls.add("http://www.linuxdevcenter.com/pub/a/linux/2000/06/29/hdparm.html");
		urls.add("http://www.twentysevenrecords.com/");
		urls.add("http://www.hallwalls.org/");
		urls.add("http://www.justfood.org/");
		urls.add("http://bshigley.tumblr.com/");
		urls.add("http://www.ottawacitizen.com/index.html");
		urls.add("http://emeraldforestseattle.com/forums/ubbthreads/");
		urls.add("http://www.cancernews.com/default2.asp");

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

		// /////////////////////////////////////////////////////
		// ////////////////end of first set beginning of next
		// set/////////////////////////////////////
		// /////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////

		urls.add("http://www.grimemonster.com/");
		urls.add("http://www.threadless.com/");
		urls.add("https://wilwheaton.net/");
		urls.add("http://www.hasbrouck.org/");
		urls.add("http://www.uberbin.net/");
		urls.add("http://www.gaijinagogo.com/");
		urls.add("http://lalibertad.com.co/dia/p0.html");
		urls.add("http://www.wrightfield.com/");

		urls.add("http://www.modernhumorist.com/");
		urls.add("http://www.bcdb.com/");
		urls.add("http://desktopgaming.com/");
		urls.add("http://www.metalbite.com/");
		urls.add("http://nowyoulistentomelittlemissy.blogspot.ca/");
		urls.add("http://www.cimgf.com/");
		urls.add("http://www.paulmadonna.com/");
		urls.add("http://dawnm.com/");

		urls.add("http://typicalculture.com/wordpress/");

		urls.add("http://www.vegweb.com/");
		urls.add("http://www.newdream.org/");

		alreadyTestedUrls.addAll(urls);
		urls.clear();

		// /////////////////// det 50
		urls.add("http://www.isc.org/downloads/BIND/");// states: 1
		urls.add("http://hunyyoung.com/"); // det 1
		urls.add("http://home.planet.nl/~mooij321/"); // states: 2
		urls.add("http://www.antique-hangups.com/"); // states : 1
		urls.add("http://www.project451.com/");// states : 3
		urls.add("http://lmuwnmd.wpengine.com/wp-signup.php?new=www.techblog.com");// states : 1

		urls.add("http://c-level.org/"); // det 50
		urls.add("http://rprogress.org/index.htm");// det 50
		urls.add("http://www.math.mcgill.ca/");// det 50

		for (String uRL : urls) {

			if (alreadyTestedUrls.contains(uRL) == false) {

			try {
				StateFlowGraph inMemorySfg = Experiment.crawlInMemory(uRL);
				StateFlowGraph inMemorySfg2 = Experiment.crawlInMemory(uRL);
				Experiment.createExperimentReport(inMemorySfg, inMemorySfg2, uRL);
			} catch (Exception e) {


				
				}
			}
		}

	}
}
