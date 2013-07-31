package com.crawljax.examples;

import javax.swing.JOptionPane;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.state.StateFlowGraph.StateFlowGraphType;

/**
 * Crawls our demo site with the database backed stateFlowGraph implementation. This will save the
 * state flow graph in a database.
 */
public class ScalableExample {

	/**
	 * Run this method to start the crawl.
	 */
	public static void main(String[] args) {
		CrawljaxConfigurationBuilder builder =
		        CrawljaxConfiguration
		                .builderFor("http://localhost/applications/ajaxfilemanagerv_tinymce1.1/tinymce_test.php");
		builder.setGraphType(StateFlowGraphType.SCALABLE);

		builder.crawlRules().clickOnce(false);
		// builder.setMaximumRunTime(1, TimeUnit.MINUTES);
		builder.setMaximumStates(50);
		CrawljaxRunner crawljax =
		        new CrawljaxRunner(builder.build());
		CrawlSession session = crawljax.call();
		int size = session.getStateFlowGraph().getAllStates().size();

		JOptionPane.showMessageDialog(null, size);
	}
}
