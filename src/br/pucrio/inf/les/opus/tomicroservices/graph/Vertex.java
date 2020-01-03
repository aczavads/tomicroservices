package br.pucrio.inf.les.opus.tomicroservices.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Vertex {
	
	private String name;
	private List<String> funcitionalities;
	private Map<String, Edge> outbound;
	private Map<String, Edge> intbound;

	private void startCommonFields(String name) {
		this.name = name;
		this.outbound = new HashMap<String, Edge>();
		this.intbound = new HashMap<String, Edge>();
	}
	
	public Vertex(String name) {
		startCommonFields(name);
		this.funcitionalities = new ArrayList<String>();
	}
	
	public Vertex(String name, List<String> functionalities) {
		startCommonFields(name);
		this.funcitionalities = functionalities;
	}
	
	public void addOutbound(Edge edge) {
		AddOrUpdateEdge(this.outbound, edge);
	}
	
	/**
	 * Add or update the current edge in the map with new informations
	 * @param bound bound
	 * @param edge edge with new informations
	 */
	private void AddOrUpdateEdge(Map<String, Edge> bound, Edge edge) {
		Vertex newVertex = edge.getTarget();
		String label = newVertex.getName();
		Edge currentEdge = bound.get(label);
		if (currentEdge != null) {
			currentEdge.incrementNewData(edge);
		} else {
			bound.put(label, edge);
		}
	}
	
	public void addInBound(Edge edge) {
		AddOrUpdateEdge(this.intbound, edge);
	}
	
	public Map<String, Edge> getOutbound() {
		return this.outbound;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return this.name;
	}

	public List<String> getFuncitionalities() {
		return funcitionalities;
	}

	public void addFuncitionalities(List<String> funcitionalities) {
		this.funcitionalities.addAll(funcitionalities);
	}

	private void addBound(Map<String, Edge> bound, boolean inbound) {
		Set<String> keys = bound.keySet();
		for (String key: keys) {
			Edge edge = bound.get(key);
			if (inbound) {
				addInBound(edge);
			} else {
				addOutbound(edge);
			}
		}
	}
	
	public void addOrUpdateNewData(Vertex vertex) {
		addFuncitionalities(vertex.getFuncitionalities());
		addBound(vertex.getOutbound(), false);
		addBound(vertex.getInbound(), true);
	}

	private Map<String, Edge> getInbound() {
		return this.intbound;
	}
	
}
