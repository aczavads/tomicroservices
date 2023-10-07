package br.pucrio.inf.les.opus.tomicroservices.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Vertex {
	
	private String name;
	private Set<String> funcitionalities;
	private Map<String, Edge> outbound;
	private Map<String, Edge> intbound;
	private String frozenMotived;
	private boolean frozen;

	private void startCommonFields(String name) {
		this.name = name;
		this.outbound = new HashMap<String, Edge>();
		this.intbound = new HashMap<String, Edge>();
		this.funcitionalities = new HashSet<String>();
		this.frozenMotived = "";
		this.frozen = false;
	}
	
	public Vertex(String name) {
		startCommonFields(name);
	}
	
	public Vertex(String name, List<String> functionalities) {
		startCommonFields(name);
		this.funcitionalities.addAll(functionalities);
	}
	
	public void addOutbound(Edge edge) {
		addOrUpdateEdge(this.outbound, edge);
	}
	
	/**
	 * Add or update the current edge in the map with new informations
	 * @param bound bound
	 * @param edge edge with new informations
	 */
	private void addOrUpdateEdge(Map<String, Edge> bound, Edge edge) {
		Vertex newVertex;
		if (bound == this.intbound) {
			newVertex = edge.getSource();
		} else {
			newVertex = edge.getTarget();
		}
		String label = newVertex.getName();
		Edge currentEdge = bound.get(label);
		if (currentEdge != null) {
			currentEdge.incrementNewData(edge);
		} else {
			bound.put(label, edge);
		}
	}
	
	public void addInBound(Edge edge) {
		addOrUpdateEdge(this.intbound, edge);
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
		List<String> result = new ArrayList<String>();
		result.addAll(this.funcitionalities);
		return result;
	}

	public void addFuncitionalities(List<String> funcitionalities) {
		this.funcitionalities.addAll(funcitionalities);
	}
	
	public void addFuncitionalities(String functionality) {
		this.funcitionalities.add(functionality);
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

	public Map<String, Edge> getInbound() {
		return this.intbound;
	}
	
	public boolean isFrozen() {
		return this.frozen;
	}
	
	public void setFrozen(String motive) {
		this.frozen = true;
		this.frozenMotived = motive;
	}
	
	public void setNotFrozen() {
		this.frozen = false;
		this.frozenMotived = "";
	}
	
	public String getFrozenMotive() {
		return this.frozenMotived;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		return Objects.equals(name, other.name);
	}
	
	
}
