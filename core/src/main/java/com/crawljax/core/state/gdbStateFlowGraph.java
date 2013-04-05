package com.crawljax.core.state;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.jcip.annotations.GuardedBy;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DirectedMultigraph;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.base.Preconditions;
import com.sun.jna.StringArray;

/**
 * The State-Flow Graph is a multi-edge directed graph with states (StateVetex) on the vertices and
 * clickables (Eventable) on the edges. It stores the data in a graph database.
 */

public class gdbStateFlowGraph implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(gdbStateFlowGraph.class.getName());
	
	// The directory path for saving the graph-db created by neo4j for persisting the state flow graph 
	
	private static final String DB_PATH = "target/state-flow-graph-db";
	
	private final GraphDatabaseService sfgDb ;
	
    private static final String STATE_KEY = "state";
    
    private static final String NODES_INDEX = "nodes";
    private static final String EDGES_INDEX = "edges";
	private static final String DOM_KEY = "DOM";
	private static final String SOURCE_KEY="source";
	private static final String	TARGET_KEY= "target";
	private static final String CLICKABLE_KEY = "clickable";
	private static final String EDGE_COMNINED_KEY ="edgeCombinedKey";
    
    private static Index<Node> nodeIndex;
   
    private static Index<Relationship> edgesIndex;



    
	/**
	 * The constructor.
	 * 
	 * @param initialState
	 *            the state to start from.
	 */
	public gdbStateFlowGraph(StateVertex initialState) {

		Preconditions.checkNotNull(initialState);
		
		// creating the graph db
		
		sfgDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		
	    
		nodeIndex = sfgDb.index().forNodes( NODES_INDEX);
		
		edgesIndex = sfgDb.index().forRelationships(EDGES_INDEX);
		
	      
		
		

		
		// add the first node to the graph
		this.addState2(initialState);
		
		this.initialState = initialState;
		
		
		
		
		// adding a shutdown hook to ensure the db will be shut down even if 
		// the program breaks
		
		registerShutdownHook(sfgDb);

	}
		
	// the relationship between a source vertex and the destination vertex
	
	private static enum RelTypes implements RelationshipType{
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
	
	public static byte [] serializeStateVertex(StateVertex stateVertex){
		
		
		byte [] serializedStateVertex = null;
		
		// this an output stream that does not require writing to the file and instead
		// the output stream is stored in a buffer
		// we use this class to utilize  the Java serialization api which writes and reads
		// object to and from streams
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			
			// seriliazing the stateVertex object to the stream
			
			oos.writeObject(stateVertex);
			
			// converting the byte array to UTF-8 string for portability reasons
			
			serializedStateVertex = baos.toByteArray();
			
			// closing streams
			
			oos.close();
			baos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return serializedStateVertex;
	}
	
	public static StateVertex deserializeStateVertex (byte [] serializedStateVertex)
	{
		// the returned value
		
		StateVertex deserializedSV = null;
				
		try {
						
			ByteArrayInputStream bais = new ByteArrayInputStream(serializedStateVertex);
			
			ObjectInputStream ois = new ObjectInputStream(bais);
		
			deserializedSV = (StateVertex) ois.readObject();
			
			// Closing streams
			
			ois.close();
			bais.close();
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return deserializedSV;
		
	}
	

public static byte [] serializeEventable(Eventable eventable){
		
		
		byte [] serializedEventable = null;
		
		// this an output stream that does not require writing to the file and instead
		// the output stream is stored in a buffer
		// we use this class to utilize  the Java serialization api which writes and reads
		// object to and from streams
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			
			// serializing the Eventable object to the stream
			
			oos.writeObject(eventable);
			
			// converting the byte array to UTF-8 string for portability reasons
			
			serializedEventable = baos.toByteArray();
			
			// closing streams
			
			oos.close();
			baos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return serializedEventable;
	}


public static Eventable deserializeEventable (byte [] serializedEventable)
{
	// the returned value
	
	Eventable deserializedEventable = null;
			
	try {
					
		ByteArrayInputStream bais = new ByteArrayInputStream(serializedEventable);
		
		ObjectInputStream ois = new ObjectInputStream(bais);
	
		deserializedEventable = (Eventable) ois.readObject();
		
		// Closing streams
		
		ois.close();
		bais.close();
	
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
	
	return deserializedEventable;
	
}

public boolean addEdge2(StateVertex sourceVert, StateVertex targetVert, Eventable clickable) {
	
	boolean exists = false;
	byte [] serializedEventable = serializeEventable(clickable);

	for (Relationship relationship: edgesIndex.get(SOURCE_KEY, sourceVert.getStrippedDom().getBytes())){
		if (relationship.getProperty(TARGET_KEY).equals(targetVert.getStrippedDom().getBytes())){
			if(relationship.getProperty(CLICKABLE_KEY).equals(serializedEventable)){
				exists= true;
				return false;
			}
		}
	}
//	
//	for (Relationship relationship: edgesIndex.get("clickable",serializedEventable )){
//		if (relationship.getStartNode().getProperty(DOM_KEY).equals(serializeStateVertex(sourceVert))){
//			if (relationship.getEndNode().getProperty(DOM_KEY).equals(serializeStateVertex(targetVert))){
//
//				alreadyExits = true;
//				return false;
//			}
//		}
//	}
	

	//
	Relationship toBeAddedEdge = null;
	Relationship alreadyExist = null;
	
	String [] sourceClickabletarget = new String[3];
	sourceClickabletarget[0]=sourceVert.getName();
	sourceClickabletarget[2]= targetVert.getName();
	sourceClickabletarget[1]=serializedEventable.toString();
	
	Transaction tx = sfgDb.beginTx();
	try{
		
		Node sourceNode = nodeIndex.get(DOM_KEY, sourceVert.getStrippedDom()).getSingle();
		Node targetNode = nodeIndex.get(DOM_KEY, targetVert.getStrippedDom()).getSingle(); 
		toBeAddedEdge = sourceNode.createRelationshipTo(targetNode, RelTypes.TRANSITIONS_TO);
		
		// adding the new edge to the index. it returns null if the edge is successfully added
		// and returns the found edge if and identical edge already exists in the index.
		alreadyExist = edgesIndex.putIfAbsent(toBeAddedEdge, EDGE_COMNINED_KEY, sourceClickabletarget);
		if (alreadyExist!=null){
			exists = true;
			tx.failure();
		}else{
			exists = false;
			
			toBeAddedEdge.setProperty(CLICKABLE_KEY, serializedEventable);
		}		
		tx.success();
	}
	finally{
		tx.finish();
		
	}
	
	if (exists){

		return false;
	}
	else{
		return true;
	}
}



	
	private final DirectedGraph<StateVertex, Eventable> sfg = null;// null added

	/**
	 * Intermediate counter for the number of states, not relaying on getAllStates.size() because of
	 * Thread-safety.
	 */
	private final AtomicInteger stateCounter = new AtomicInteger();

	private final StateVertex initialState;
	
	public StateVertex addState2(StateVertex stateVertix) {
		return addState2(stateVertix, true);
	}

	public StateVertex addState2(StateVertex state, boolean correctName)
	{
		
		Node toBeAddedNode ; // the node to be added and update for storing the state in the database
		Node alreadyEsixts ; // this is used for saving the returned result of the method putIfAbsent
		
		// starting the transaction 
		Transaction tx = sfgDb.beginTx();
		try
		{
			// adding the place holder for the state to the graph database
			

			toBeAddedNode = sfgDb.createNode();

			// indexing the state in Index manager. the key that we are using
			// for indexing is the stripped_dom field
			// the new node is added to the index and h

			alreadyEsixts = nodeIndex.putIfAbsent(toBeAddedNode, DOM_KEY, state
					.getStrippedDom().getBytes());

			if (alreadyEsixts != null) {
				// the state is already indexed
				LOG.debug("Graph already contained vertex {}", state);

				// because the state already exists in the graph the transaction
				// is marked for
				// being rolled back
				tx.failure();
			}

			// correcting the name
			int count = stateCounter.incrementAndGet();
			LOG.debug("Number of states is now {}", count);
			if (correctName) {
				correctStateName2(state);
			}

			
			// serializing the state
			byte[] serializedSV = StateFlowGraph.serializeStateVertex(state);

			// adding the state property which is the main data we store for 
			// each node (i.e. each StateVertex)
			toBeAddedNode.setProperty(STATE_KEY, serializedSV);			
			
			
			// flagging successful transaction
			tx.success();
		}
		finally
		{
		    tx.finish();
		}

		if (alreadyEsixts == null )
		{
			// the state was absent so it was put in the database
			return null;
		}else{
			
			// Return the state retrieved from db in case the state is already present in the graph
			return (StateVertex) deserializeStateVertex((byte[])alreadyEsixts.getProperty(STATE_KEY));
		}
		
	}
	
	private void correctStateName2(StateVertex stateVertix) {
		// we might need to luck the database here
		// the -1 is for the "index" state.
		int totalNumberOfStates = nodeIndex.get(STATE_KEY, "*").size() - 1;
		String correctedName = makeStateName(totalNumberOfStates, stateVertix.isGuidedCrawling());
		if (!"index".equals(stateVertix.getName())
		        && !stateVertix.getName().equals(correctedName)) {
			LOG.info("Correcting state name from {}  to {}", stateVertix.getName(), correctedName);
			stateVertix.setName(correctedName);
		}
	}

	
	




	/**
	 * @return the string representation of the graph.
	 * @see org.jgrapht.DirectedGraph#toString()
	 */
	@Override
	public String toString() {
		synchronized (sfg) {
			return sfg.toString();
		}
	}

	/**
	 * Returns a set of all clickables outgoing from the specified vertex.
	 * 
	 * @param stateVertix
	 *            the state vertix.
	 * @return a set of the outgoing edges (clickables) of the stateVertix.
	 * @see org.jgrapht.DirectedGraph#outgoingEdgesOf(Object)
	 */
	public Set<Eventable> getOutgoingClickables(StateVertex stateVertix) {
		Set<Eventable> outgoing = new HashSet<Eventable>();
		Node state = nodeIndex.get(DOM_KEY, stateVertix.getStrippedDom()).getSingle();
		for (Relationship edge: state.getRelationships(RelTypes.TRANSITIONS_TO, Direction.OUTGOING)){
			byte [] serializedEvantable = (byte[]) edge.getProperty(CLICKABLE_KEY);
			Eventable eventable = (Eventable) deserializeEventable( serializedEvantable);
			outgoing.add(eventable);
		}
	
		return outgoing;
	}

	/**
	 * Returns a set of all edges incoming into the specified vertex.
	 * 
	 * @param stateVertix
	 *            the state vertix.
	 * @return a set of the incoming edges (clickables) of the stateVertix.
	 * @see org.jgrapht.DirectedGraph#incomingEdgesOf(Object)
	 */
	public Set<Eventable> getIncomingClickable(StateVertex stateVertix) {
		
		Set<Eventable> incoming = new HashSet<Eventable>();
		Node state = nodeIndex.get(DOM_KEY, stateVertix.getStrippedDom()).getSingle();
		
		for (Relationship edge: state.getRelationships(RelTypes.TRANSITIONS_TO, Direction.INCOMING)){
			byte [] serializedEvantable = (byte[]) edge.getProperty(CLICKABLE_KEY);
			Eventable eventable = (Eventable) deserializeEventable( serializedEvantable);
			incoming.add(eventable);
		}
	
		return incoming;

	}

	/**
	 * Returns the set of outgoing states.
	 * 
	 * @param stateVertix
	 *            the state.
	 * @return the set of outgoing states from the stateVertix.
	 */
	public Set<StateVertex> getOutgoingStates(StateVertex stateVertix) {
		
	
		final	Set<StateVertex> outgoing = new HashSet<StateVertex>();
		
		Node sourceNode = nodeIndex.get(DOM_KEY, stateVertix.getStrippedDom()).getSingle();
		
		for (Relationship edge: sourceNode.getRelationships(RelTypes.TRANSITIONS_TO, Direction.OUTGOING)){
			Node endNode =edge.getEndNode();
			byte [] serializedState = (byte[]) endNode.getProperty(STATE_KEY);
			StateVertex targetState = (StateVertex) deserializeStateVertex(serializedState);
			outgoing.add(targetState);
		}
	
		return outgoing;

	}

	/**
	 * @param clickable
	 *            the edge.
	 * @return the target state of this edge.
	 */
	public StateVertex getTargetState(Eventable clickable) {
		
		byte[] serializedEventable = serializeEventable(clickable);
		
		Relationship edge = (Relationship) edgesIndex.get(CLICKABLE_KEY, serializedEventable);
		
		Node targetNode  = edge.getEndNode();
		
		byte[] srializedState = (byte[]) targetNode.getProperty(STATE_KEY);
		StateVertex target = deserializeStateVertex(srializedState);
		
		return target;
	}

	/**
	 * Is it possible to go from s1 -> s2?
	 * 
	 * @param source
	 *            the source state.
	 * @param target
	 *            the target state.
	 * @return true if it is possible (edge exists in graph) to go from source to target.
	 */
	public boolean canGoTo(StateVertex source, StateVertex target) {
		
		Node sourceNode = nodeIndex.get(DOM_KEY, source.getStrippedDom()).getSingle();
		for (Relationship edge: sourceNode.getRelationships(RelTypes.TRANSITIONS_TO, Direction.OUTGOING)){
			
			Node targetNode = edge.getEndNode();
			byte[] serializedNode = (byte[])targetNode.getProperty(STATE_KEY);
			
			StateVertex ts = deserializeStateVertex(serializedNode);
			if (ts.equals(target))
			{
				return true;
			}
		}
		
		return false;
		
	}

	/**
	 * Convenience method to find the Dijkstra shortest path between two states on the graph.
	 * 
	 * @param start
	 *            the start state.
	 * @param end
	 *            the end state.
	 * @return a list of shortest path of clickables from the state to the end
	 */
	public List<Eventable> getShortestPath(StateVertex start, StateVertex end) {
		return DijkstraShortestPath.findPathBetween(sfg, start, end);
	}

	/**
	 * Return all the states in the StateFlowGraph.
	 * 
	 * @return all the states on the graph.
	 */
	public Set<StateVertex> getAllStates() {
		
		final	Set<StateVertex> allStates = new HashSet<StateVertex>();
		
		for ( Node node:  nodeIndex.get(DOM_KEY, "*")){
			
			byte[] serializedNode = (byte[]) node.getProperty(STATE_KEY);
			
			StateVertex state = deserializeStateVertex(serializedNode);
			
			allStates.add(state);
			
		}


		return allStates;
	}

	/**
	 * Return all the edges in the StateFlowGraph.
	 * 
	 * @return a Set of all edges in the StateFlowGraph
	 */
	public Set<Eventable> getAllEdges() {
		
		final	Set<Eventable> all = new HashSet<Eventable>();
		
		for ( Relationship edge:  edgesIndex.get(EDGE_COMNINED_KEY, "*")){
			
			byte[] serializededge = (byte[]) edge.getProperty(CLICKABLE_KEY);
			
			Eventable eventable = deserializeEventable(serializededge);
			
		all.add(eventable);
			
		}


		return all;
	}


		


	/**
	 * Retrieve the copy of a state from the StateFlowGraph for a given StateVertix. Basically it
	 * performs v.equals(u).
	 * 
	 * @param state
	 *            the StateVertix to search
	 * @return the copy of the StateVertix in the StateFlowGraph where v.equals(u)
	 */
	private StateVertex getStateInGraph(StateVertex state) {
		Set<StateVertex> states = getAllStates();

		for (StateVertex st : states) {
			if (state.equals(st)) {
				return st;
			}
		}

		return null;
	}

	/**
	 * @return Dom string average size (byte).
	 */
	public int getMeanStateStringSize() {
		final Mean mean = new Mean();

		for (StateVertex state : getAllStates()) {
			mean.increment(state.getDomSize());
		}

		return (int) mean.getResult();
	}

	/**
	 * @param state
	 *            The starting state.
	 * @return A list of the deepest states (states with no outgoing edges).
	 */
	public List<StateVertex> getDeepStates(StateVertex state) {
		final Set<String> visitedStates = new HashSet<String>();
		final List<StateVertex> deepStates = new ArrayList<StateVertex>();

		traverse(visitedStates, deepStates, state);

		return deepStates;
	}

	private void traverse(Set<String> visitedStates, List<StateVertex> deepStates,
	        StateVertex state) {
		visitedStates.add(state.getName());

		Set<StateVertex> outgoingSet = getOutgoingStates(state);

		if ((outgoingSet == null) || outgoingSet.isEmpty()) {
			deepStates.add(state);
		} else {
			if (cyclic(visitedStates, outgoingSet)) {
				deepStates.add(state);
			} else {
				for (StateVertex st : outgoingSet) {
					if (!visitedStates.contains(st.getName())) {
						traverse(visitedStates, deepStates, st);
					}
				}
			}
		}
	}

	private boolean cyclic(Set<String> visitedStates, Set<StateVertex> outgoingSet) {
		int i = 0;

		for (StateVertex state : outgoingSet) {
			if (visitedStates.contains(state.getName())) {
				i++;
			}
		}

		return i == outgoingSet.size();
	}

	/**
	 * This method returns all possible paths from the index state using the Kshortest paths.
	 * 
	 * @param index
	 *            the initial state.
	 * @return a list of GraphPath lists.
	 */
	public List<List<GraphPath<StateVertex, Eventable>>> getAllPossiblePaths(StateVertex index) {
		final List<List<GraphPath<StateVertex, Eventable>>> results =
		        new ArrayList<List<GraphPath<StateVertex, Eventable>>>();

		final KShortestPaths<StateVertex, Eventable> kPaths =
		        new KShortestPaths<StateVertex, Eventable>(this.sfg, index, Integer.MAX_VALUE);

		for (StateVertex state : getDeepStates(index)) {

			try {
				List<GraphPath<StateVertex, Eventable>> paths = kPaths.getPaths(state);
				results.add(paths);
			} catch (Exception e) {
				// TODO Stefan; which Exception is catched here???Can this be removed?
				LOG.error("Error with " + state.toString(), e);
			}

		}

		return results;
	}

	/**
	 * Return the name of the (new)State. By using the AtomicInteger the stateCounter is thread-safe
	 * 
	 * @return State name the name of the state
	 */
	public String getNewStateName() {
		stateCounter.getAndIncrement();
		String state = makeStateName(stateCounter.get(), false);
		return state;
	}

	/**
	 * Make a new state name given its id. Separated to get a central point when changing the names
	 * of states. The automatic state names start with "state" and guided ones with "guide".
	 * 
	 * @param id
	 *            the id where this name needs to be for.
	 * @return the String containing the new name.
	 */
	private String makeStateName(int id, boolean guided) {

		if (guided) {
			return "guided" + id;
		}

		return "state" + id;
	}

	public boolean isInitialState(StateVertex state) {
		return initialState.equals(state);
	}
}
