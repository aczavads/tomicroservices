package br.pucrio.inf.les.opus.tomicroservices.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

	Map<String, Vertex> vertices; 
	
	public Graph() {
		this.vertices = new HashMap();
	}
	
	/**
	 * Insert vertex in the graph
	 * @param vertex vertex
	 * @return true if does not exists this label, otherwise false
	 */
	public boolean insert(Vertex vertex) {
		if (!containsVertex(vertex)) {
			this.vertices.put(vertex.getName(), vertex);
			return true;
		} else {
			Vertex currentVertex = this.vertices.get(vertex.getName());
			assert(currentVertex.getName().equals(vertex.getName()));
			currentVertex.addOrUpdateNewData(vertex);
		}
		return false;
	}
	
	/**
	 * Verify if contains vertex in the graph
	 * @param vertex vertex
	 * @return true if contains the vertex
	 */
	public boolean containsVertex(Vertex vertex) {
		return containsVertexByName(vertex.getName());
	}
	
	public boolean containsVertexByName(String name) {
		return this.vertices.containsKey(name);
	}
	
	public void print() {
		System.out.println(toString());
	}
	
	public long getVerticesSize() {
		return this.vertices.size();
	}
	
	public String toString() {
		Set<String> keys = this.vertices.keySet();
		String result = "";
		for (String key: keys) {
			Vertex vertex = this.vertices.get(key);
			//TODO
		}
		return result;
	}
	
}
