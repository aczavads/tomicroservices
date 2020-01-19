package br.pucrio.inf.les.opus.tomicroservices.graph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jgrapht.Graph;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.metrics.MethodEdgeValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MethodGraph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MethodNode;

public class AlgorithMSTTest {

	@Test
	public void testRunTwoEdgesTwoMicroservices() {
		AlgorithmMST mst = new AlgorithmMST();
		MethodGraph methodGraph = new MethodGraph();
		methodGraph.insert("A", "a", "B", "b", new MethodEdgeValue(100L, 1));
		methodGraph.insert("B", "b", "C", "c", new MethodEdgeValue(300L, 2));
		Graph<MethodNode, MethodEdgeValue> graph = mst.run(methodGraph, 2).getGraph();
		assertTrue(graph.containsEdge(new MethodNode("A", "a"), new MethodNode("B", "b")));
		assertFalse(graph.containsEdge(new MethodNode("B", "b"), new MethodNode("C", "c")));
	}
	
	@Test
	public void testRunFourEdgesTwoMicroservices() {
		AlgorithmMST mst = new AlgorithmMST();
		MethodGraph methodGraph = new MethodGraph();
		methodGraph.insert("A", "a", "B", "b", new MethodEdgeValue(100L, 1));
		methodGraph.insert("B", "b", "C", "c", new MethodEdgeValue(300L, 2));
		methodGraph.insert("C", "c", "D", "d", new MethodEdgeValue(500L, 3));
		methodGraph.insert("D", "d", "A", "a", new MethodEdgeValue(600L, 4));
		methodGraph.insert("A", "a", "C", "c", new MethodEdgeValue(200L, 5));
		Graph<MethodNode, MethodEdgeValue> graph = mst.run(methodGraph, 2).getGraph();
		assertTrue(graph.containsEdge(new MethodEdgeValue(100L, 1)));
		assertTrue(graph.containsEdge(new MethodEdgeValue(200L, 5)));
	}
	
}
