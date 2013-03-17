/**
 * 
 */
package com.crawljax.core.state;

import static org.junit.Assert.*;

import java.util.Set;


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
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @author arz
 *
 */
public class StateStorageRetrieval {
	
	
	
	private static final String URL = "http://www.google.com";
	private static final int MAX_DEPTH = 2;
	private static final int MAX_NUMBER_STATES = 2;
	
	private GraphDatabaseService sfgDb ;

	
	private static enum RelTypes implements RelationshipType
	{
		TRANSITIONS_TO	    
	}
	
	private static void registerShutdownHook( final GraphDatabaseService graphDatabaseService )
	{
	    // Registering a shutdown hook for the db instance so as to
	    // shut it down nicely when the VM exits 
		
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDatabaseService.shutdown();
	        }
	    } );
	}



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
		

		// The directory path for saving the graph-db created by neo4j for persisting the state flow graph 
		
		 String DB_PATH = "target/state-flow-graph-db";
	
		// the relationship between a source vertex and the destination vertex
		
		
		
		  
			sfgDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
			
			// adding a shutdown hook to ensure the db will be shut down even if 
			// the program breaks
			
			registerShutdownHook(sfgDb);

			

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

	
	
	private class StateExaminner implements OnNewStatePlugin {

		/* (non-Javadoc)
		 * @see com.crawljax.core.plugin.OnNewStatePlugin#onNewState(com.crawljax.core.CrawlSession)
		 */
		public void onNewState(CrawlSession session) {

			StateFlowGraph sfg = session.getStateFlowGraph();
		
			
			Set<StateVertex> allStates =sfg.getAllStates();
			for (StateVertex s : allStates){
				
				byte [] serializedSV = StateFlowGraph.serializeStateVertex(s);
				
				Node firstNode ;
				
				Transaction tx = sfgDb.beginTx();
				try
				{
				    // Updating operations go here
					
					firstNode = sfgDb.createNode();
					firstNode.setProperty( "state", serializedSV );
					 
//					relationship = firstNode.createRelationshipTo( secondNode, RelTypes.TRANSITIONS_TO );
//					relationship.setProperty( "message", "brave Neo4j " );

				    tx.success();
				}
				finally
				{
				    tx.finish();
				}
				
				byte [] afterDb;
				
				Transaction tx2 = sfgDb.beginTx();
				try
				{
				   
					afterDb = (byte[]) firstNode.getProperty("state");
					
				    tx2.success();
				}
				finally
				{
				    tx2.finish();
				}

				
				
				
				StateVertex after =  (StateVertex) StateFlowGraph.deserializeStateVertex(afterDb);
				
				assertTrue(after.toString(), s.equals(after));
				
				break;
			}

		}

	}


}
