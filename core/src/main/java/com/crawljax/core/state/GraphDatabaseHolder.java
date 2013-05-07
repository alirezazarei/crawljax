/**
 * 
 */
package com.crawljax.core.state;

import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @author arz
 *
 */
public class GraphDatabaseHolder implements Runnable {
	
	
	
	
	
	// The directory path for saving the graph database created by neo4j for
	// storing the state flow graph

	private static final String DB_PATH = "target/state-flow-graph-db/Fresh_";

	
	
	// the id used for the for node indexer object
	private static final String NODES_INDEX_NAME = "nodes";

	// the id used for the for edge indexer object
	private static final String EDGES_INDEX_NAME = "edges";


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		// creating the graph database

		// get the database to rewrite on previous database file
		// each time it is initiated

		// fresh is used to ensure that every time we run the program a
		// fresh empty database is used for storing the data

		long fresh = System.nanoTime();
		String path = DB_PATH + fresh;
		
		com.crawljax.core.state.StateFlowGraph.setSfgDb(
				new GraphDatabaseFactory().newEmbeddedDatabase(path));
		
		
		
		com.crawljax.core.state.StateFlowGraph.setNodeIndex(
				com.crawljax.core.state.StateFlowGraph.getSfgDb().index().forNodes(NODES_INDEX_NAME));

		// again similar to nodeIndex this is a cross indexing of the edges for
		// fast retrieval

		com.crawljax.core.state.StateFlowGraph.setEdgesIndex(
				com.crawljax.core.state.StateFlowGraph.getSfgDb().index().forRelationships(EDGES_INDEX_NAME));


		com.crawljax.core.state.StateFlowGraph.setStatus(1);		
		
		while(com.crawljax.core.state.StateFlowGraph.getStatus() < 2)
		{
			
			System.out.println("db running");
		}
		

	}

}
