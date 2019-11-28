package br.pucrio.inf.les.opus.tomicroservices.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;

import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.MethodEdgeValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.MethodGraph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.MethodNode;

public class AlgorithmMST {

	private MethodEdgeValue maxEdge(List<MethodEdgeValue> edges) {
		final int firstEdge = 0;
		MethodEdgeValue max = edges.get(firstEdge);
		for (MethodEdgeValue edge : edges) {
			if (edge.getSizeOf() > max.getSizeOf()) {
				max = edge;
			}
		}
		return max;
	}
	
	private MethodGraph createGraphFromEdgesInMst(Set<MethodEdgeValue> edgesInMst, 
			List<MethodNode> nodes, int numberToRemoveMaximum) {
		List<MethodEdgeValue> edgesList = new ArrayList<MethodEdgeValue>(edgesInMst);
		for (int i = 0; i < numberToRemoveMaximum; ++i) {
			MethodEdgeValue edgeToRemove = maxEdge(edgesList);
			edgesList.remove(edgeToRemove);
		}
		MethodGraph graph = new MethodGraph();
		for (MethodEdgeValue edge: edgesList) {
			graph.insert(edge);
		}
		for (MethodNode node: nodes) {
			graph.insert(node);
		}
		return graph;
	}
	
	public MethodGraph run(MethodGraph methodGraph, int clusteringNumber) {
		Graph<MethodNode,MethodEdgeValue> graph = methodGraph.getGraph();
		KruskalMinimumSpanningTree mst = new KruskalMinimumSpanningTree<MethodNode,MethodEdgeValue>(graph);
		SpanningTreeAlgorithm.SpanningTree<MethodEdgeValue> tree = mst.getSpanningTree();
		return createGraphFromEdgesInMst(tree.getEdges(), methodGraph.getNodes(), clusteringNumber - 1);
	}

}
