package br.pucrio.inf.les.opus.tomicroservices.graph;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jgrapht.Graph;

import br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic.DynamicLogAnalyzer;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MethodEdgeValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MethodGraph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MethodNode;

public class PrintGraph {
	/**
	private void printDOT(String result, Graph<MethodNode, MethodEdgeValue> graph, File file) {
		printDOT(result, graph);
		if (file == null) return;
		GenerateDOT generateDOT = new GenerateDOT();
		try {
			FileUtils.writeStringToFile(file, result + "\n", "UTF-8", true);
			FileUtils.writeStringToFile(file, generateDOT.getDOT(graph) + "\n", "UTF-8", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printDOT(String result, Graph<MethodNode, MethodEdgeValue> graph) {
		GenerateDOT generateDOT = new GenerateDOT();
		System.out.println(result);
		System.out.println(generateDOT.getDOT(graph));
	}
	
	public void print(File sizeOflog, File output, int numberOfMicroservices) {
		DynamicLogAnalyzer analyzer = new DynamicLogAnalyzer();
		MethodGraph methodGraph = new MethodGraph();
		analyzer.analyzeSizeOf(sizeOflog, methodGraph);
		printDOT("Method Graph:", methodGraph.getGraph(), output);
		AlgorithmMST algoMST = new AlgorithmMST();
		methodGraph = algoMST.run(methodGraph, numberOfMicroservices);
		printDOT("Clustering Method Graph:", methodGraph.getGraph(), output);
	}
	**/

}
