package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import org.jgrapht.graph.DefaultEdge;

public class MethodEdgeValue extends DefaultEdge{

	private long sizeOf;
	
	private MethodNode toNode;
	
	private MethodNode fromNode;
	
	private int id;
	
	public MethodEdgeValue(long sizeOf) {
		this.sizeOf = sizeOf; //TODO
	}
	
	public MethodEdgeValue(long sizeOf, int id) {
		this.sizeOf = sizeOf;
		this.id = id;
	}
	
	public long getSizeOf() {
		return this.sizeOf;
	}
	
	public void increaseValue(MethodEdgeValue value) {
		this.sizeOf += value.getSizeOf();
	}
	
	public int hashCode() {
		return this.id;
	}
	
	public boolean equals(Object object) {
		if (object instanceof MethodEdgeValue) {
			MethodEdgeValue value = (MethodEdgeValue) object;
			return this.id == value.hashCode() && this.sizeOf == value.getSizeOf();
		}
		return false;
	}
	
	public String toString() {
		return String.valueOf(this.sizeOf);
	}

	public void setFromNode(MethodNode fromNode) {
		this.fromNode = fromNode;
	}
	
	public MethodNode getFromNode() {
		return this.fromNode;
	}
	
	public void setToNode(MethodNode toNode) {
		this.toNode = toNode;
	}

	public MethodNode getToNode() {
		return this.toNode;
	}
	
}
