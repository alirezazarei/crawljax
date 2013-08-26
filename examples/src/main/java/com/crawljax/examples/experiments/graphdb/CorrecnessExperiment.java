/**
 * 
 */
package com.crawljax.examples.experiments.graphdb;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.crawljax.core.CrawljaxException;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.ExitNotifier;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.InDatabaseStateFlowGraph;
import com.crawljax.core.state.StateFlowGraph;
import com.crawljax.core.state.StateFlowGraph.StateFlowGraphType;
import com.crawljax.core.state.StateVertex;
import com.google.common.collect.ImmutableSet;

/**
 * @author arz
 */
public class CorrecnessExperiment {

	private static final int MAX_TIME = 8 * 60;
	private static int MAX_STATES = 50;
	private static int MAX_DEPTH = 5;

	private static String folder = "/Users/arz/Desktop/graph-db-experiemtns/correctness";

	public static void setFolder(String folder) {
		CorrecnessExperiment.folder = folder;
	}

	public static String getFolder() {
		return folder;
	}

	public static void setMAX_DEPTH(int mAX_DEPTH) {
		MAX_DEPTH = mAX_DEPTH;
	}

	public static void setMaxState(int max) {
		MAX_STATES = max;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://localhost/correctness/c4__CorrectnessObject.htm";
		// correcnessExperimentOn("http://www.ece.ubc.ca/~azarei");
		// correctnessExperimentOn("http://www.google.com");
		// correcnessExperimentOn("file://localhost/Users/arz/localhost/applications/chess/index.html");
		// correcnessExperimentOn("http://demo.crawljax.com");
		// correctnessExperimentOn("http://localhost/applications/phormer331/");
		// correctnessExperimentOn("http://localhost/applications/ajaxfilemanagerv_tinymce1.1/tinymce_test.php");
		// correctnessExperimentOn("http://localhost/correcNess/pluginTestFirst.htm");
		// correctnessExperimentOn("http://localhost/correctness/c4.htm");

		// correctnessExperimentOn("http://demo.crawljax.com");

		correctnessExperimentOn(url);
	}

	private static void correctnessExperimentOn(String uRL) {

		StateFlowGraph inMemorySfg = crawlInMemory(uRL);
		InDatabaseStateFlowGraph inDbSfg = new InDatabaseStateFlowGraph(new ExitNotifier(0));
		InDatabaseStateFlowGraph.saveSfgInDatabase(inMemorySfg, inDbSfg);

		// StateFlowGraph inMemorySfg2 = crawlInMemory(uRL);
		// InDatabaseStateFlowGraph inDbSfg2 = new InDatabaseStateFlowGraph(new ExitNotifier(0));
		// InDatabaseStateFlowGraph.saveSfgInDatabase(inMemorySfg2, inDbSfg2);

		StateFlowGraph inDatabaseSfg = crawlInDb(uRL);

		createExperimentReport(inMemorySfg, inDatabaseSfg, uRL);

	}

	public static StateFlowGraph crawlInDb(String uRl) {

		CrawljaxConfiguration inDatabaseConfiguration = buildInDatabaseConfiguration(uRl);
		CrawljaxRunner inDatabaseCrawljax = new CrawljaxRunner(inDatabaseConfiguration);
		StateFlowGraph inDatabaseSfg = inDatabaseCrawljax.call().getStateFlowGraph();
		return inDatabaseSfg;
	}

	public static StateFlowGraph crawlInMemory(String uRL) {
		CrawljaxConfiguration inMemoryConfiguration = buildInMemoryConfiguration(uRL);
		CrawljaxRunner inMemoryCrawljax = new CrawljaxRunner(inMemoryConfiguration);
		StateFlowGraph inMemorySfg = inMemoryCrawljax.call().getStateFlowGraph();
		return inMemorySfg;
	}

	public static void createExperimentReport(StateFlowGraph inMemorySfg,
	        StateFlowGraph inDatabaseSfg, String uRL, String fold) {
		setFolder(fold);
		createExperimentReport(inMemorySfg, inDatabaseSfg, uRL);
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

		return getWriter(createFile(createFolder(folder), uRL));

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

		ImmutableSet<StateVertex> allStatesFromDb = inDatabaseSfg.getAllStates();
		int allStatesFromDbSize = allStatesFromDb.size();
		report.append("Number os states in Database based crawling: "
		        + allStatesFromDbSize + "\n\n\n");

		String statesComparisonResult = compareStates(inMemorySfg, inDatabaseSfg);
		report.append("Are the states identical in both graphs? "
		        + statesComparisonResult + "\n\n");
		if (statesComparisonResult.equals(new String("Yes, of course!"))) {
			try {
				report.append("Are all the eges and their start and end states identical: "
				        + compareEdges(inMemorySfg, inDatabaseSfg) + "\n");
			} catch (Exception e) {

				report.append(e.getMessage());
			}
		}

		return report.toString();
	}

	public static String compareEdges(StateFlowGraph inMemorySfg, StateFlowGraph inDatabaseSfg) {

		for (StateVertex inDbState : inDatabaseSfg.getAllStates()) {
			for (Eventable inDbEdge : inDatabaseSfg.getOutgoingClickables(inDbState)) {
				int edgeFound = 0;
				for (Eventable inMemEdge : inMemorySfg.getOutgoingClickables(inDbState)) {
					if (inDbEdge.equals(inMemEdge)
					        && inDbEdge.getSourceStateVertex().equals(
					                inMemEdge.getSourceStateVertex())) {
						edgeFound++;
					}
				}
				if (edgeFound == 0) {
					return new String("An edge "
					        + " was found" + edgeFound
					        + " times. This edge must have been between state: "
					        + inDbEdge.getSourceStateVertex().getId() + " and state: "
					        + inDbEdge.getTargetStateVertex().getId()) + ". The edge:"
					        + inDbEdge.toString();
				}
			}
		}
		return new String("Yes, of course!");
	}

	public static String compareStates(StateFlowGraph inMemorySfg, StateFlowGraph inDatabaseSfg) {

		boolean flag = true;
		for (StateVertex inMemState : inMemorySfg.getAllStates()) {
			flag = false;
			String firstStrippedDom = inMemState.getStrippedDom();
			for (StateVertex inDbState : inDatabaseSfg.getAllStates()) {
				String secondStrippedDom = inDbState.getStrippedDom();

				if (firstStrippedDom.equals(secondStrippedDom)) {

					flag = true;
					break;
				} else {
					// for (int i = 0; i < firstStrippedDom.length(); i++) {
					// if (firstStrippedDom.charAt(i) != secondStrippedDom.charAt(i)) {
					// if (i > 10) {
					// // JOptionPane.showMessageDialog(null,
					// // firstStrippedDom.subSequence(i - 10, i));
					// }
					// break;
					// }
					// }
				}

			}
			if (flag == false) {
				return new String("No, this state was not found " + inMemState.getName());
			}
		}

		return new String("Yes, of course!");

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

		builder.crawlRules().insertRandomDataInInputForms(false);
		builder.crawlRules().clickOnce(false);

		int maxStates = MAX_STATES;
		int maxDepth = MAX_DEPTH;
		int MaxTime = MAX_TIME;
		builder.setMaximumStates(maxStates);
		builder.setMaximumDepth(maxDepth);
		builder.setMaximumRunTime(MaxTime, TimeUnit.MINUTES);

		return builder;
	}

}
