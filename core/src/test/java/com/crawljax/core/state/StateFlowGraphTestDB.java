/**
 *	testing serialize and deserialize methods of the class stateflow graph 
 */
package com.crawljax.core.state;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import scala.annotation.target.setter;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawljaxController;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.plugin.Plugin;
import com.crawljax.core.state.StateFlowGraph;
import com.crawljax.core.state.StateVertex;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author arz
 *
 */
public class StateFlowGraphTestDB {
	
	
	
	private static final String URL = "http://www.google.com";
	private static final int MAX_DEPTH = 2;
	private static final int MAX_NUMBER_STATES = 2;


	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.crawljax.core.state.StateFlowGraph#serializeStateVertex(com.crawljax.core.state.StateVertex)}.
	 */
	@Test
	public void testSerializeStateVertex() {

		
		
		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(URL);
		builder.crawlRules().insertRandomDataInInputForms(false);

		builder.crawlRules().click("a");
		builder.crawlRules().click("button");

		// except these
		builder.crawlRules().dontClick("a").underXPath("//DIV[@id='guser']");
		builder.crawlRules().dontClick("a").withText("Language Tools");

		// limit the crawling scope
		builder.setMaximumStates(MAX_NUMBER_STATES);
		builder.setMaximumDepth(MAX_DEPTH);
		
		Plugin p =new StateExaminner();
		
		builder.addPlugin(p);
		

		builder.crawlRules().setInputSpec(getInputSpecification());

		CrawljaxController crawljax = new CrawljaxController(builder.build());
		crawljax.run();
	}

	private static InputSpecification getInputSpecification() {
		InputSpecification input = new InputSpecification();
		input.field("gbqfq").setValue("Crawljax");
		return input;
	}

	/**
	 * Test method for {@link com.crawljax.core.state.StateFlowGraph#deserializeStateVertex(java.lang.String)}.
	 */
	@Test
	public void testDeserializeStateVertex() {
//		fail("Not yet implemented"); // TODO
	}
	
	
	private class StateExaminner implements OnNewStatePlugin {

		/* (non-Javadoc)
		 * @see com.crawljax.core.plugin.OnNewStatePlugin#onNewState(com.crawljax.core.CrawlSession)
		 */
		public void onNewState(CrawlSession session) {

			StateFlowGraph sfg = session.getStateFlowGraph();
		
			
			Set<StateVertex> allStates =sfg.getAllStates();
			for (StateVertex s : allStates){
				
				byte [] serializedSV = StateFlowGraph.serializeStateVertex(s);
				
				StateVertex after =  (StateVertex) StateFlowGraph.deserializeStateVertex(serializedSV);
				
				assertTrue(after.toString(), s.equals(after));
				
				break;
			}

		}

	}


}
