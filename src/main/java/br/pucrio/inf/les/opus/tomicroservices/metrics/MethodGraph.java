package br.pucrio.inf.les.opus.tomicroservices.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * 
 * @author Luiz Carvalho <lmcarvalho@inf.puc-rio.br>
 * 
 * 
 * 
 */
public class MethodGraph {

	private Map<String, MethodNode> methodToNode;
	
    private Graph<MethodNode, MethodEdgeValue> graph = new SimpleWeightedGraph<>(MethodEdgeValue.class);
	    
	public MethodGraph() {
		this.methodToNode = new HashMap<String, MethodNode>();
	}
	
	private String generateKey(String className, String methodName) {
		return className + "." + methodName;
	}
	
	private MethodNode generateNone(String className, String methodName) {
		String key = generateKey(className, methodName);
		MethodNode node = this.methodToNode.get(key);
		if (node == null) {
			node = new MethodNode(className, methodName); 
			this.methodToNode.put(key, node);
			this.graph.addVertex(node);
		}
		if (!node.getClassName().equals(className) || !node.getMethodName().equals(methodName)) {
			System.err.println("Detected problem - same hashCode to different nodes " + 
					className + "." + methodName + " and " + node.toString() + "(current in the map)");
			return null;
		}
		return node;
	}
	
	/**
	 * Insert nodes (or vertex) in case not exists in graph and insert edge or increase edge value.
	 * @param fromClass 
	 * @param fromMethod 
	 * @param toClass
	 * @param toMethod
	 * @param edgeValue
	 * @return true if insert in graph, otherwise false.
	 */
	public boolean insert(String fromClass, String fromMethod, String toClass, String toMethod, 
			MethodEdgeValue edgeValue) {
		MethodNode fromNode = generateNone(fromClass, fromMethod);
		MethodNode toNode = generateNone(toClass, toMethod);
		MethodEdgeValue fromToEdge = graph.getEdge(fromNode, toNode);
		if (fromToEdge != null) {
			fromToEdge.increaseValue(edgeValue);
			return true;
		} else if (fromNode != null && toNode != null) {
			boolean result = this.graph.addEdge(fromNode, toNode, edgeValue);
			edgeValue.setFromNode(fromNode);
			edgeValue.setToNode(toNode);
			this.graph.setEdgeWeight(edgeValue, (double) edgeValue.getSizeOf());
			return result;
		} else {
			return false;
		}
	}

	public boolean insert(MethodEdgeValue edge) {
		return insert(edge.getFromNode(), edge.getToNode(), edge);
	}
	
	public boolean insert(MethodNode node) {
		String key = generateKey(node.getClassName(), node.getMethodName());
		boolean containKey = this.methodToNode.containsKey(key);
		if (containKey == false) {
			this.methodToNode.put(key, node);
			this.graph.addVertex(node);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean insert(MethodNode fromNode, MethodNode toNode, MethodEdgeValue edgeValeu) {
		return insert(fromNode.getClassName(), fromNode.getMethodName(), toNode.getClassName(),
				toNode.getMethodName(), edgeValeu);
	}
	
	/**
	 * Return the graph used to represent the sizeof parameters between methods in the system under analyze.
	 * The graph is implemented using jgraphT.
	 * @return method graph
	 */
	public Graph<MethodNode, MethodEdgeValue> getGraph() {
		return this.graph;
	}
	
	public List<MethodNode> getNodes() {
		List<MethodNode> nodes = new ArrayList<MethodNode>();
		Set<String> keys = this.methodToNode.keySet();
		for (String key : keys) {
			nodes.add(this.methodToNode.get(key));
		}
		return nodes;
	}

}
