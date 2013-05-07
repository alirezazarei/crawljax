/**
 * 
 */
package com.crawljax.core.state;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawljaxController;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.plugin.Plugin;
import com.crawljax.core.plugin.PostCrawlingPlugin;

/**
 * @author arz
 *
 */
public class StateFlowGraphPersistenceTest {
	
	
	private static final String URL = "http://localhost/domChangeTest/pluginTestFirst.htm";
	
//	private static final String URL = "http://www.google.com";

	private static final int MAX_CRAWL_DEPTH = 2;
	private static final int MAX_STATES = 10;


	/**
	 * Test method for {@link com.crawljax.core.state.StateFlowGraph#serializeStateVertex(com.crawljax.core.state.StateVertex)}.
	 */
	@Test
	public void testSerializeStateVertex() {
		
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(URL);
		builder.crawlRules().clickDefaultElements();
		builder.crawlRules().click("h1");
		builder.crawlRules().click("h2");
		builder.crawlRules().click("span");
		builder.crawlRules().click("div");


		// limit the crawling scope
		builder.setMaximumStates(MAX_STATES);
		builder.setMaximumDepth(MAX_CRAWL_DEPTH);
		
	
		Plugin p =new TestSerializeStateVertex();
		
		builder.addPlugin(p);
		
		Plugin p2 = new Post();
		
		builder.addPlugin(p2);
	

		builder.crawlRules().setInputSpec(getInputSpecification());

		CrawljaxController crawljax = new CrawljaxController(builder.build());
		crawljax.run();
		com.crawljax.core.state.StateFlowGraph.setStatus(2);		
		

	}
	
	private class TestSerializeStateVertex implements OnNewStatePlugin{

		@Override
		public void onNewState(CrawlSession session) {
			// TODO Auto-generated method stub
			
			StateFlowGraph sfg = session.getStateFlowGraph();
		
			
			Set<StateVertex> allStates =sfg.getAllStates();
			for (StateVertex s : allStates){
				
				byte [] serializedSV = StateFlowGraph.serializeStateVertex(s);
				
				StateVertex after =  (StateVertex) StateFlowGraph.deserializeStateVertex(serializedSV);
				
				
				assertTrue(after.toString(), s.equals(after));
				assertTrue(after.toString(), s.getCandidateActions().size() == after.getCandidateActions().size());
				assertTrue(after.toString(), s.getRegisterdCandidateActions().size() == after.getRegisterdCandidateActions().size());
				assertTrue(after.toString(),s.getRegisteredCrawlers().size() == after.getRegisteredCrawlers().size());
				
				break;
			}

			
		}
		
	}
	
	
	private static InputSpecification getInputSpecification() {
		InputSpecification input = new InputSpecification();

		// enter "Crawljax" in the search field
		input.field("q").setValue("Crawljax");
		return input;
	}


	/**
	 * Test method for {@link com.crawljax.core.state.StateFlowGraph#deserializeStateVertex(byte[])}.
	 */
	@Test
	public void testDeserializeStateVertex() {
	}

	/**
	 * Test method for {@link com.crawljax.core.state.StateFlowGraph#serializeEventable(com.crawljax.core.state.Eventable)}.
	 */
	@Test
	public void testSerializeEventableEventable() {
	}

	/**
	 * Test method for {@link com.crawljax.core.state.StateFlowGraph#deserializeEventable(byte[])}.
	 */
	@Test
	public void testDeserializeEventableByteArray() {
	
	}
	
	private class Post implements PostCrawlingPlugin {

		@Override
		public void postCrawling(CrawlSession session) {
			// TODO Auto-generated method stub
			
			com.crawljax.core.state.StateFlowGraph.setStatus(2);		
			
			
		}
		
	}
	

}
