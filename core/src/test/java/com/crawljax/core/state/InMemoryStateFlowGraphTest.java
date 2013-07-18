package com.crawljax.core.state;

import com.crawljax.core.ExitNotifier;

public class InMemoryStateFlowGraphTest extends StateFlowGraphTest {

	public StateFlowGraph createStateFlowGraph() {
		return new InMemoryStateFlowGraph(new ExitNotifier(0));
	}

}
