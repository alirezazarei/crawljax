/**
 * 
 */
package com.crawljax.core.state;

import org.apache.http.client.methods.AbortableHttpRequest;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @author arz
 *
 */
public class GraphDatabaseHolder implements Runnable {
	
	
	
	
	
	// The directory path for saving the graph database created by neo4j for
	// storing the state flow graph

	private static  String DB_PATH = "/Users/arz/Documents/state-flow-graph-db/Fresh_";

	
	
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
		 DB_PATH = DB_PATH + fresh;
		 
		 StateFlowGraph.DB_PATH = DB_PATH;
		
		StateFlowGraph.setSfgDb(
				new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH));
		
		
		StateFlowGraph.setIndexManager(
				StateFlowGraph.getSfgDb().index());
		
		StateFlowGraph.setNodeIndex(
				StateFlowGraph.getIndexManager().forNodes(NODES_INDEX_NAME));

		// again similar to nodeIndex this is a cross indexing of the edges for
		// fast retrieval


		StateFlowGraph.setEdgesIndex(
				StateFlowGraph.getIndexManager().forRelationships(EDGES_INDEX_NAME));

		Node mainNode = null;
		Transaction tx = StateFlowGraph.getSfgDb().beginTx();
		try{
		 mainNode = StateFlowGraph.getSfgDb().createNode();
			mainNode.setProperty("type", "indexing");
			
		tx.success();
		}finally{
			tx.finish();
		}
		
		
		if(mainNode == null)
			System.exit(1);
		StateFlowGraph.structuralIndexer = mainNode;
//		StateFlowGraph.getSfgDb().getReferenceNode().createRelationshipTo(mainNode, RelTypes.REFRENCES);


		


		com.crawljax.core.state.StateFlowGraph.setStatus(1);		
		
//		while(com.crawljax.core.state.StateFlowGraph.getStatus() < 2)
//		{
//		}
//		

	}

}
