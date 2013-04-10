package com.crawljax.core.state;

import static com.crawljax.browser.matchers.StateFlowGraphMatchers.hasEdges;
import static com.crawljax.browser.matchers.StateFlowGraphMatchers.hasStates;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.jgrapht.GraphPath;
import org.junit.Test;

import scala.testing.SUnit.AssertFailed;

import com.crawljax.core.state.Eventable.EventType;
import com.crawljax.core.state.Identification.How;

public class gdbStateFlowGraphTest {

	@Test
	public void testSFG() throws Exception {

		StateVertex index = new StateVertex("index", "<table><div>index</div></table>");
		StateVertex state2 = new StateVertex("STATE_TWO", "<table><div>state2</div></table>");
		StateVertex state3 = new StateVertex("STATE_THREE", "<table><div>state3</div></table>");
		StateVertex state4 = new StateVertex("STATE_FOUR", "<table><div>state4</div></table>");
		StateVertex state5 = new StateVertex("STATE_FIVE", "<table><div>state5</div></table>");

		gdbStateFlowGraph graph = new gdbStateFlowGraph(index);

		StateVertex DOUBLE = new StateVertex("index", "<table><div>index</div></table>");
		assertTrue(graph.addState(state2) == null);
		assertTrue(graph.addState(state3) == null);
		assertTrue(graph.addState(state4) == null);
		assertTrue(graph.addState(state5) == null);

		assertFalse(graph.addState(DOUBLE) == null);

		assertTrue(graph.addEdge(index, state2, new Eventable(new Identification(How.xpath,
		        "/body/div[4]"), EventType.click)));

		assertTrue(graph.addEdge(state2, index, new Eventable(new Identification(How.xpath,
		        "/body/div[89]"), EventType.click)));

		assertTrue(graph.addEdge(state2, state3, new Eventable(new Identification(How.xpath,
		        "/home/a"), EventType.click)));
		assertTrue(graph.addEdge(index, state4, new Eventable(new Identification(How.xpath,
		        "/body/div[2]/div"), EventType.click)));
		assertTrue(graph.addEdge(state2, state5, new Eventable(new Identification(How.xpath,
		        "/body/div[5]"), EventType.click)));

		assertFalse(graph.addEdge(state2, state5, new Eventable(new Identification(How.xpath,
		        "/body/div[5]"), EventType.click)));

		Set<Eventable> clickables = graph.getOutgoingClickables(state2);
		assertEquals(3, clickables.size());

		clickables = graph.getIncomingClickable(state2);
		assertTrue(clickables.size() == 1);

		assertNotNull(graph.toString());

		assertEquals(state2.hashCode(), state2.hashCode());

		assertTrue(state2.equals(new StateVertex("STATE_2", "<table><div>state2</div></table>")));

		assertTrue(graph.canGoTo(state2, state3));
		assertTrue(graph.canGoTo(state2, state5));
		assertFalse(graph.canGoTo(state2, state4));

		assertTrue(graph.canGoTo(state2, index));


		Set<StateVertex> states = graph.getOutgoingStates(index);
		assertTrue(states.size() == 2);
		assertTrue(states.contains(state2));
		assertTrue(states.contains(state4));
		assertFalse(states.contains(state3));

		Set<StateVertex> allStates = graph.getAllStates();

		assertTrue(allStates.size() == 5);

	}

	@Test
	public void testCloneStates() throws Exception {
		StateVertex index = new StateVertex("index", "<table><div>index</div></table>");
		StateVertex state2 = new StateVertex("STATE_TWO", "<table><div>state2</div></table>");
		StateVertex state3 = new StateVertex("STATE_THREE", "<table><div>state2</div></table>");

		gdbStateFlowGraph graph = new gdbStateFlowGraph(index);

		StateVertex state4 = new StateVertex("STATE_FOUR", "<table><div>state4</div></table>");
		assertTrue(graph.addState(state2) == null);
		assertTrue(graph.addState(state4) == null);
		// assertFalse(graph.addState(state3));
		assertTrue(graph.addEdge(index, state2, new Eventable(new Identification(How.xpath,
		        "/body/div[4]"), EventType.click)));

		// if (graph.containsVertex(state3)) {
		// StateVertix state_clone = graph.getStateInGraph(state3);
		// assertEquals(state3, state_clone);
		// }

		assertTrue(graph.addEdge(state4, state3, new Eventable(new Identification(How.xpath,
		        "/home/a"), EventType.click)));
		// System.out.println(graph.toString());
		// assertNull(graph.getStateInGraph(new StateVertix("STATE_TEST",
		// "<table><div>TEST</div></table>")));
	}

	@Test
	public void testGetMeanStateStringSize() {
		String HTML1 =
		        "<SCRIPT src='js/jquery-1.2.1.js' type='text/javascript'></SCRIPT> "
		                + "<SCRIPT src='js/jquery-1.2.3.js' type='text/javascript'></SCRIPT>"
		                + "<body><div id='firstdiv' class='orange'></div><div><span id='thespan'>"
		                + "<a id='thea'>test</a></span></div></body>";

		String HTML2 =
		        "<SCRIPT src='js/jquery-1.2.1.js' type='text/javascript'></SCRIPT> "
		                + "<SCRIPT src='js/jquery-1.2.3.js' type='text/javascript'></SCRIPT>"
		                + "<body><div id='firstdiv' class='orange'>";

		gdbStateFlowGraph g = new gdbStateFlowGraph(new StateVertex("", HTML1));
		g.addState(new StateVertex("", HTML2));

		assertEquals(206, g.getMeanStateStringSize());
	}

	@Test
	public void testDoubleEvents() {

		StateVertex state1 = new StateVertex("STATE_ONE", "<table><div>state1</div></table>");
		StateVertex state2 = new StateVertex("STATE_TWO", "<table><div>state2</div></table>");
		gdbStateFlowGraph sfg = new gdbStateFlowGraph(state1);

		Eventable c1 =
		        new Eventable(new Identification(How.xpath, "/body/div[4]"), EventType.click);
		Eventable c2 =
		        new Eventable(new Identification(How.xpath, "/body/div[4]/div[2]"),
		                EventType.click);
		sfg.addState(state1);
		sfg.addState(state2);

		sfg.addEdge(state1, state2, c1);
		sfg.addEdge(state1, state2, c2);
		assertEquals(2, sfg.getAllEdges().size());
	}

	
	@Test
	public void largetTest() {
		StateVertex index = new StateVertex("index", "<table><div>index</div></table>");
		StateVertex state2 = new StateVertex("STATE_TWO", "<table><div>state2</div></table>");
		StateVertex state3 = new StateVertex("STATE_THREE", "<table><div>state3</div></table>");
		StateVertex state4 = new StateVertex("STATE_FOUR", "<table><div>state4</div></table>");
		StateVertex state5 = new StateVertex("STATE_FIVE", "<table><div>state5</div></table>");
		gdbStateFlowGraph g = new gdbStateFlowGraph(index);
		g.addState(state2);
		g.addState(state3);
		g.addState(state4);
		g.addState(state5);

		g.addEdge(index, state2, new Eventable(new Identification(How.xpath, "/index/2"),
		        EventType.click));
		g.addEdge(state2, index, new Eventable(new Identification(How.xpath, "/2/index"),
		        EventType.click));
		g.addEdge(state2, state3, new Eventable(new Identification(How.xpath, "/2/3"),
		        EventType.click));
		g.addEdge(index, state4, new Eventable(new Identification(How.xpath, "/index/4"),
		        EventType.click));
		g.addEdge(state2, state5, new Eventable(new Identification(How.xpath, "/2/5"),
		        EventType.click));
		g.addEdge(state4, index, new Eventable(new Identification(How.xpath, "/4/index"),
		        EventType.click));
		g.addEdge(index, state5, new Eventable(new Identification(How.xpath, "/index/5"),
		        EventType.click));
		g.addEdge(state4, state2, new Eventable(new Identification(How.xpath, "/4/2"),
		        EventType.click));
		g.addEdge(state3, state5, new Eventable(new Identification(How.xpath, "/3/5"),
		        EventType.click));


	}

	@Test
	public void guidedCrawlingFlag() {
		StateVertex index = new StateVertex("index", "<table><div>index</div></table>");
		StateVertex state2 = new StateVertex("STATE_TWO", "<table><div>state2</div></table>");
		StateVertex state3 = new StateVertex("STATE_THREE", "<table><div>state3</div></table>");
		StateVertex state4 = new StateVertex("STATE_FOUR", "<table><div>state4</div></table>");
		StateVertex state5 = new StateVertex("STATE_FIVE", "<table><div>state5</div></table>");
		gdbStateFlowGraph g = new gdbStateFlowGraph(index);
		g.addState(state2);
		g.addState(state3);
		g.addState(state4);
		g.addState(state5);

		assertTrue(g.getAllStates().size()==5);

		StateVertex state6 = new StateVertex("STATE_FIVE", "<table><div>state5</div></table>");
		state6.setGuidedCrawling(false);
		g.addState(state6);

		assertTrue(g.getAllStates().size()==5);

		state6.setGuidedCrawling(true);

		g.addState(state6);

	//	assertTrue(g.getAllStates().size()==6);

	}

	@Test
	public void testEdges() throws Exception {

		StateVertex index = new StateVertex("index", "<table><div>index</div></table>");
		StateVertex state2 = new StateVertex("STATE_TWO", "<table><div>state2</div></table>");
		StateVertex state3 = new StateVertex("STATE_THREE", "<table><div>state3</div></table>");
		StateVertex state4 = new StateVertex("STATE_FOUR", "<table><div>state4</div></table>");
		gdbStateFlowGraph graph = new gdbStateFlowGraph(index);
		assertTrue(graph.addState(state2) == null);
		assertTrue(graph.addState(state3) == null);
		assertTrue(graph.addState(state4) == null);

		Eventable e1 = new Eventable(new Identification(How.xpath, "/4/index"), EventType.click);

		Eventable e2 = new Eventable(new Identification(How.xpath, "/4/index"), EventType.click);

		Eventable e3 = new Eventable(new Identification(How.xpath, "/4/index"), EventType.click);

		Eventable e4 = new Eventable(new Identification(How.xpath, "/5/index"), EventType.click);

		Eventable e5 = new Eventable(new Identification(How.xpath, "/4/index"), EventType.click);
		Eventable e6 = new Eventable(new Identification(How.xpath, "/5/index"), EventType.click);

		assertTrue(graph.addEdge(index, state2, e1));
		assertFalse(graph.addEdge(index, state2, e1));
		assertFalse(graph.addEdge(index, state2, e3));

		assertTrue(graph.addEdge(state2, state3, e2));
		assertFalse(graph.addEdge(state2, state3, e1));


		assertTrue(graph.addEdge(index, state4, e3));
		assertTrue(graph.addEdge(index, state4, e4));
		assertFalse(graph.addEdge(index, state4, e5));
		assertFalse(graph.addEdge(index, state4, e6));
	}

		
}
