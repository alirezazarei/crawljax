/**
 * 
 */
package com.crawljax.core.state;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import com.crawljax.core.ExitNotifier;

/**
 * @author arz
 */
public class InDatabaseStateFlowGraphTest extends StateFlowGraphTest {

	/**
	 * @throws java.lang.Exception
	 */

	public StateFlowGraph createStateFlowGraph() {
		return new InDatabaseStateFlowGraph(new ExitNotifier(0));
	}

	/**
	 * This test case tests if serialization and deserialization leaves the original states
	 * unchanged. Test method for
	 * {@link com.crawljax.core.state.InDatabaseStateFlowGraph#serializeStateVertex(com.crawljax.core.state.StateVertex)}
	 * and {@link com.crawljax.core.state.InDatabaseStateFlowGraph#deserializeStateVertex(byte[])}.
	 */
	@Test
	public void testWhenStatesAreSereializedTheyRemainUnchanged() {

		testWhenAStateIsSereializedItRemainsUnchanged(index);
		testWhenAStateIsSereializedItRemainsUnchanged(state2);
		testWhenAStateIsSereializedItRemainsUnchanged(state3);
		testWhenAStateIsSereializedItRemainsUnchanged(state4);
		testWhenAStateIsSereializedItRemainsUnchanged(state5);

	}

	/**
	 * This test case tests if serialization and deserialization leaves the original states
	 * unchanged. Test method for
	 * {@link com.crawljax.core.state.InDatabaseStateFlowGraph#serializeStateVertex(com.crawljax.core.state.StateVertex)}
	 * and {@link com.crawljax.core.state.InDatabaseStateFlowGraph#deserializeStateVertex(byte[])}.
	 */
	private void testWhenAStateIsSereializedItRemainsUnchanged(
	        StateVertex stateVertex)
	{
		byte[] serializedStateVertex = InDatabaseStateFlowGraph.serializeStateVertex(stateVertex);
		StateVertex deserializedStateVertex =
		        InDatabaseStateFlowGraph.deserializeStateVertex(serializedStateVertex);
		assertTrue(stateVertex.equals(deserializedStateVertex));

	}

	/**
	 * Test method for
	 * {@link com.crawljax.core.state.InDatabaseStateFlowGraph#serializeEventable(com.crawljax.core.state.Eventable)}
	 * and {@link com.crawljax.core.state.InDatabaseStateFlowGraph#deserializeEventable(byte[])}.
	 */
	@Test
	public void testWhenAnEventableWithNoSorceNorTargetIsSereializedItRemainsUnchanged() {

		String xPath = "/body/div[4]";
		Eventable original = newXpathEventable(xPath);
		testWhenAnEventableIsSereializedItRemainsUnchanged(original);
	}

	/**
	 * Test method for
	 * {@link com.crawljax.core.state.InDatabaseStateFlowGraph#serializeEventable(com.crawljax.core.state.Eventable)}
	 * and {@link com.crawljax.core.state.InDatabaseStateFlowGraph#deserializeEventable(byte[])}.
	 */

	@Test
	public void testWhenAnEventableWitSourceIsSereializedItRemainsUnchanged() {

		String xPath = "/body/div[4]";
		Eventable original = newXpathEventable(xPath);
		original.setSource(index);
		testWhenAnEventableIsSereializedItRemainsUnchanged(original);
	}

	/**
	 * Test method for
	 * {@link com.crawljax.core.state.InDatabaseStateFlowGraph#serializeEventable(com.crawljax.core.state.Eventable)}
	 * and {@link com.crawljax.core.state.InDatabaseStateFlowGraph#deserializeEventable(byte[])}.
	 */
	@Test
	public void testWhenAnEventableWithSourceAndTargetIsSereializedItRemainsUnchanged() {

		System.out.println("hi");
		String xPath = "/body/div[4]";
		Eventable original = newXpathEventable(xPath);
		original.setSource(index);
		original.setTarget(state4);
		testWhenAnEventableIsSereializedItRemainsUnchanged(original);
	}

	/**
	 * Test method for
	 * {@link com.crawljax.core.state.InDatabaseStateFlowGraph#serializeEventable(com.crawljax.core.state.Eventable)}
	 * and {@link com.crawljax.core.state.InDatabaseStateFlowGraph#deserializeEventable(byte[])}.
	 * 
	 * @param original
	 */
	private void testWhenAnEventableIsSereializedItRemainsUnchanged(Eventable original) {
		byte[] serializedEventable = InDatabaseStateFlowGraph.serializeEventable(original);
		Eventable deserializedEventable =
		        InDatabaseStateFlowGraph.deserializeEventable(serializedEventable);

		assertTrue(original.equals(deserializedEventable));
	}

	/**
	 * Test method for {@link com.crawljax.core.state.InDatabaseStateFlowGraph#buildJgraphT()}.
	 */
	@Test
	public void testWhenDataBaseIsCovertedToJgraphtTheNumberOfEdgesIsValid() {

		graph.putIfAbsent(state2);
		graph.putIfAbsent(state3);
		graph.putIfAbsent(state4);
		graph.putIfAbsent(state5);

		int numberOfEdges = 0;
		graph.addEdge(index, state2, newXpathEventable("/body/div[12]"));
		numberOfEdges++;
		graph.addEdge(index, state3, newXpathEventable("/body/div[13]"));
		numberOfEdges++;
		graph.addEdge(index, state5, newXpathEventable("/body/div[15]"));
		numberOfEdges++;

		graph.addEdge(state2, index, newXpathEventable("/body/div[21]"));
		numberOfEdges++;
		graph.addEdge(state2, state3, newXpathEventable("/body/div[23]"));
		numberOfEdges++;
		graph.addEdge(state2, state4, newXpathEventable("/body/div[24]"));
		numberOfEdges++;
		graph.addEdge(state2, state5, newXpathEventable("/body/div[25]"));
		numberOfEdges++;

		InDatabaseStateFlowGraph inDbGraph = (InDatabaseStateFlowGraph) graph;

		assertTrue(inDbGraph.buildJgraphT().edgeSet().size() == numberOfEdges);

	}

	@Test
	public void testWhenACrawlPathIsAddedTheNumberOfCrawlPathsIsIncreasesByOne() {

		List<Eventable> path = createAPath();

		((InDatabaseStateFlowGraph) graph).addCrawlPath(path);

		assertTrue(((InDatabaseStateFlowGraph) graph).getCrawlPathsSize() == 1);

	}

	@Test
	public void testWhenACrawlPathIsAddedAndThenRetrievedTheyAreTheSamee() {

		List<Eventable> path = createAPath();

		((InDatabaseStateFlowGraph) graph).addCrawlPath(path);

		ConcurrentLinkedQueue<List<Eventable>> paths =
		        (ConcurrentLinkedQueue<List<Eventable>>) ((InDatabaseStateFlowGraph) graph)
		                .getCrawlPaths();

		List<Eventable> retrievedPath = paths.peek();

		assertTrue(retrievedPath.equals(path));

	}

	@Test
	public void testWhenTwoCrawlPathAreAddedAndThenRetrievedTheyAreTheSame() {

		List<Eventable> path1 = createAPath();
		List<Eventable> path2 = createAnotherPath();

		((InDatabaseStateFlowGraph) graph).addCrawlPath(path1);
		((InDatabaseStateFlowGraph) graph).addCrawlPath(path2);

		ConcurrentLinkedQueue<List<Eventable>> retrievedPaths =
		        (ConcurrentLinkedQueue<List<Eventable>>) ((InDatabaseStateFlowGraph) graph)
		                .getCrawlPaths();

		assertTrue(retrievedPaths.contains(path1));
		assertTrue(retrievedPaths.contains(path2));

	}

	private List<Eventable> createAPath() {

		graph.putIfAbsent(state2);
		graph.putIfAbsent(state3);
		graph.putIfAbsent(state4);

		Eventable e1 = newXpathEventable("/body/div[1]");
		e1.setSource(index);
		e1.setTarget(state2);
		graph.addEdge(index, state2, e1);

		Eventable e2 = newXpathEventable("/body/div[2]");
		e2.setSource(state2);
		e2.setTarget(state3);
		graph.addEdge(state2, state3, e2);
		Eventable e3 = newXpathEventable("/body/div[3]");
		e3.setSource(state3);
		e3.setTarget(state4);
		graph.addEdge(state3, state4, e3);

		List<Eventable> path = new ArrayList<Eventable>();
		path.add(e1);
		path.add(e2);
		path.add(e3);

		return path;
	}

	private List<Eventable> createAnotherPath() {

		graph.putIfAbsent(state5);
		Eventable e1 = newXpathEventable("/body/div[4]");
		e1.setSource(state4);
		e1.setTarget(state5);

		graph.addEdge(state4, state5, e1);
		Eventable e2 = newXpathEventable("/body/div[5]");
		e2.setSource(state5);
		e2.setTarget(index);

		graph.addEdge(state5, index, e2);
		List<Eventable> path = new ArrayList<Eventable>();
		path.add(e1);
		path.add(e2);

		return path;
	}

}
