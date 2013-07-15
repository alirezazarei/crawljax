package com.crawljax.examples.experiments.graphdb.correctness;

import java.io.BufferedWriter;

import org.junit.Test;

public class CorrectnessExperimentTest {

	@Test
	public void test() {

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
}
