package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jgrapht.Graph;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic.DynamicLogAnalyzer;

public class SizeOfAnalyzerTest {

	@Test
	public void testAnalyzeSizeOfTwoLineInTheLog() throws IOException {
		File logFile = new File("___logFIleTest");
		DynamicLogAnalyzer analyzer = new DynamicLogAnalyzer();
		FileUtils.writeStringToFile(logFile, "Class:A#Method:a#SizeOf:100\n"
				+ "Class:B#Method:b#SizeOf:200", "UTF-8");
		MethodGraph methodGraph = new MethodGraph();
		analyzer.analyzeSizeOf(logFile, methodGraph);
		Graph<MethodNode, MethodEdgeValue> graph = methodGraph.getGraph();
		assertTrue(graph.containsVertex(new MethodNode("A", "a")));
		assertEquals(new MethodEdgeValue(200L), graph.getEdge(new MethodNode("A", "a"), new MethodNode("B", "b")));
		logFile.deleteOnExit();
	}
	
}
