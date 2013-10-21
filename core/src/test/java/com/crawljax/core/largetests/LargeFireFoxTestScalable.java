/**
 * 
 */
package com.crawljax.core.largetests;

import com.crawljax.core.state.StateFlowGraph.StateFlowGraphType;

/**
 * @author arz
 */
public class LargeFireFoxTestScalable extends LargeFirefoxTest {

	@Override
	StateFlowGraphType getGraphType() {
		System.setProperty("webdriver.firefox.bin",
		        "/ubc/ece/home/am/grads/azarei/firefox 7/firefox/firefox-bin");

		return StateFlowGraphType.SCALABLE;
	}

}
