/**
 * 
 */
package com.crawljax.examples.experiments.graphdb.correctness;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Date;

import com.crawljax.core.CrawljaxException;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.StateFlowGraph;
import com.crawljax.core.state.StateFlowGraph.StateFlowGraphType;
import com.crawljax.core.state.StateVertex;

/**
 * @author arz
 */
public class CorrecnessExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// correcnessExperimentOn("http://www.ece.ubc.ca/~azarei");
		// correcnessExperimentOn("http://www.google.com");
		correcnessExperimentOn("file://localhost/Users/arz/localhost/applications/chess/index.html");
		// correcnessExperimentOn("http://demo.crawljax.com");

	}

	private static void correcnessExperimentOn(String uRL) {

		StateFlowGraph inDatabaseSfg = crawlInDb(uRL);
		StateFlowGraph inMemorySfg = crawlInMemory(uRL);

		createExperimentReport(inMemorySfg, inDatabaseSfg, uRL);

	}

	private static StateFlowGraph crawlInDb(String uRl) {

		CrawljaxConfiguration inDatabaseConfiguration = buildInDatabaseConfiguration(uRl);
		CrawljaxRunner inDatabaseCrawljax = new CrawljaxRunner(inDatabaseConfiguration);
		StateFlowGraph inDatabaseSfg = inDatabaseCrawljax.call().getStateFlowGraph();
		return inDatabaseSfg;
	}

	private static StateFlowGraph crawlInMemory(String uRL) {
		CrawljaxConfiguration inMemoryConfiguration = buildInMemoryConfiguration(uRL);
		CrawljaxRunner inMemoryCrawljax = new CrawljaxRunner(inMemoryConfiguration);
		StateFlowGraph inMemorySfg = inMemoryCrawljax.call().getStateFlowGraph();
		return inMemorySfg;
	}

	public static void createExperimentReport(StateFlowGraph inMemorySfg,
	        StateFlowGraph inDatabaseSfg, String uRL) {
		String report = composeReport(inMemorySfg, inDatabaseSfg);
		BufferedWriter writer = getTheWriter(uRL);
		writeReportToDisk(writer, report);
	}

	public static BufferedWriter getTheWriter() {
		return getTheWriter("no URL was provided");

	}

	public static BufferedWriter getTheWriter(String uRL) {

		return getWriter(createFile(createFolder(new String(
		        "/Users/arz/Desktop/graph-db-experiemtns/correctness")), uRL));

	}

	public static void writeReportToDisk(BufferedWriter writer, String report) {

		try {
			writer.write(report);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String composeReport(StateFlowGraph inMemorySfg,
	        StateFlowGraph inDatabaseSfg) {

		StringBuffer report = new StringBuffer();

		report.append("Experiment on website: " + inMemorySfg.getInitialState().getUrl()
		        + "\n\n\n");

		report.append("Number os states in memory based crawling: "
		        + inMemorySfg.getAllStates().size() + "\n");
		report.append("Number os states in Database based crawling: "
		        + inDatabaseSfg.getAllStates().size() + "\n\n\n");

		report.append("Are the states identical in both graphs? "
		        + compareStates(inMemorySfg, inDatabaseSfg) + "\n\n");
		report.append("Are all the eges and their start and end states identical: "
		        + compareEdges(inMemorySfg, inDatabaseSfg) + "\n");

		return report.toString();
	}

	private static String compareEdges(StateFlowGraph inMemorySfg, StateFlowGraph inDatabaseSfg) {

		for (StateVertex inDbState : inDatabaseSfg.getAllStates()) {
			for (Eventable inDbEdge : inDatabaseSfg.getOutgoingClickables(inDbState)) {
				int edgeFound = 0;
				for (Eventable inMemEdge : inMemorySfg.getOutgoingClickables(inDbState)) {
					if (inDbEdge.equals(inMemEdge)
					        && inDbEdge.getSourceStateVertex().equals(
					                inMemEdge.getSourceStateVertex())) {
						edgeFound++;
					}
					if (edgeFound != 1) {
						return new String("edge: " + inDbEdge.toString()
						        + " was found" + edgeFound
						        + " times. This edge must have been between state: "
						        + inDbEdge.getSourceStateVertex().getId() + " and state: "
						        + inDbEdge.getTargetStateVertex().getId());
					}

				}

			}
		}
		return new String("Yes, of course!");
	}

	private static String compareStates(StateFlowGraph inMemorySfg, StateFlowGraph inDatabaseSfg) {

		if (inDatabaseSfg.getAllStates().containsAll(inMemorySfg.getAllStates())) {
			return new String("Yes, of course!");
		} else {
			return new String("No");

		}

	}

	public static BufferedWriter getWriter(File outputFile) {

		Charset charset = Charset.defaultCharset();
		BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(outputFile.toPath(), charset);
		} catch (Exception e)
		{
			throw new CrawljaxException();
		}
		return writer;
	}

	public static File createFile(File outputFolder, String uRL) {
		checkNotNull(outputFolder, "output folder cannot be null!");
		checkArgument(outputFolder.exists(), outputFolder + "folder does not exist!");
		checkArgument(outputFolder.canWrite(), outputFolder + "folder cannot be written to!");
		String fileName = "expetiment" + createTimeBasedString() + "   " + trimURL(uRL) + ".txt";

		File reportFile = new File(outputFolder, fileName);
		if (reportFile.exists()) {
			throw new CrawljaxException("file already exists");
		} else {
			try {
				reportFile.createNewFile();
			} catch (IOException e) {
				throw new CrawljaxException("cannot create reprot file");
			}
		}
		return reportFile;
	}

	private static String trimURL(String uRL) {

		String trimmed = uRL;
		if (uRL.length() > 21) {
			trimmed = (String) uRL.subSequence(11, 20);
		}
		trimmed = trimmed.replace("http", "_");
		trimmed = trimmed.replace("www", "_");

		trimmed = trimmed.replace('.', '_');
		trimmed = trimmed.replace('/', '_');
		trimmed = trimmed.replace('~', '_');
		trimmed = trimmed.replace(':', '_');

		return trimmed;
	}

	private static String createTimeBasedString() {
		Date date = new Date();
		return date.toString();
	}

	public static File createFolder(String pathname) {

		File output = new File(pathname);
		if (output.exists()) {

			checkArgument(output.isDirectory(), output + " is not a directory");
			checkArgument(output.canWrite(), "Database directory is not writable");
		} else {
			boolean wasCreated = output.mkdirs();
			checkArgument(wasCreated, output + "cannot be created");
		}
		return output;
	}

	private static CrawljaxConfiguration buildInDatabaseConfiguration(String uRl) {
		CrawljaxConfigurationBuilder builder =
		        buildConfiguration(uRl);
		builder.setGraphType(StateFlowGraphType.SCALABLE);
		CrawljaxConfiguration configuration = builder.build();
		return configuration;
	}

	private static CrawljaxConfiguration buildInMemoryConfiguration(String uRL) {
		CrawljaxConfiguration configuration =
		        buildConfiguration(uRL).build();
		return configuration;
	}

	private static CrawljaxConfigurationBuilder buildConfiguration(String uRL) {

		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(uRL);

		builder.crawlRules().clickDefaultElements();
		builder.crawlRules().click("div");
		builder.crawlRules().click("span");

		int maxStates = 50;
		builder.setMaximumStates(maxStates);

		return builder;
	}
}
