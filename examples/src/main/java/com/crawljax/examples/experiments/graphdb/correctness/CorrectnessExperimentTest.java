package com.crawljax.examples.experiments.graphdb.correctness;

import java.io.BufferedWriter;

import org.junit.Test;

import com.crawljax.core.state.StateFlowGraph;

public class CorrectnessExperimentTest {

	@Test
	public void testWriter() {

		StringBuffer buffer = new StringBuffer();

		buffer.append("This is a test for checking the writer");
		buffer.append("\n");
		buffer.append(true);
		buffer.append("\n");
		buffer.append(123);
		buffer.append("\n");

		BufferedWriter writer =
		        CorrecnessExperiment.getTheWriter("http://www.ece.ubc.ca/~azarei");

		CorrecnessExperiment.writeReportToDisk(writer, buffer.toString());

	}

	@Test
	public void testExperiment() {

		CorrecnessExperiment.setMaxState(5);

		String uRL = "http://www.facebook.com";// "http://www.surveymonkey.com/";
		// "http://www.ece.ubc.ca/"; // //
		// https://en.wikipedia.org/wiki/English_Wikipedia";// "http://demo.crawljax.com";//
		// "http://localhost/correctness/c4.htm";
		// "http://localhost/correcNess/c3.htm";//
		// "http://en.wikipedia.org/wiki/Main_Page";//
		// "http://designmodo.github.io/Flat-UI/";//
		// "http://localhost/applications/ajaxfilemanagerv_tinymce1.1/tinymce_test.php";
		StateFlowGraph inMemorySfg = CorrecnessExperiment.crawlInMemory(uRL);

		StateFlowGraph inMemorySfg2 = CorrecnessExperiment.crawlInMemory(uRL);

		CorrecnessExperiment.createExperimentReport(inMemorySfg, inMemorySfg2, uRL + "test");

		// assertTrue("differnt number of states", inMemorySfg.getAllStates().size() == inMemorySfg2
		// .getAllStates().size());
		// assertTrue(CorrecnessExperiment.compareStates(inMemorySfg, inMemorySfg2).equals(
		// new String("Yes, of course!")));
		// assertTrue(CorrecnessExperiment.compareEdges(inMemorySfg, inMemorySfg2).equals(
		// new String("Yes, of course!")));

	}
}
