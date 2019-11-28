package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import static org.junit.jupiter.api.Assertions.*;

import org.jgrapht.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MethodGraphTest {

	private MethodGraph methodGraph;
	
	@BeforeEach
	public void setup() {
		this.methodGraph = new MethodGraph();
	}
	
	@Test
	void insertTest() {
		boolean rInsert = methodGraph.insert("A", "a", "B", "b", new MethodEdgeValue(100L));
		assertTrue(rInsert);
		Graph<MethodNode, MethodEdgeValue> graph = methodGraph.getGraph();
		MethodNode m1 = new MethodNode("A", "a");
		MethodNode m2 = new MethodNode("B", "b");
		MethodNode other = new MethodNode("C", "c");
		assertTrue(graph.containsVertex(m1));
		assertTrue(graph.containsVertex(m2));
		assertFalse(graph.containsVertex(other));
	}
	
	@Test
	void insertTwiceTest() {
		boolean rInsert = methodGraph.insert("A", "a", "B", "b", new MethodEdgeValue(100L));
		assertTrue(rInsert);
		rInsert = methodGraph.insert("B", "b", "C", "c", new MethodEdgeValue(200L));
		assertTrue(rInsert);
		rInsert = methodGraph.insert("C", "c", "A", "a", new MethodEdgeValue(100L));
		assertTrue(rInsert);
		rInsert = methodGraph.insert("A", "a", "B", "b", new MethodEdgeValue(300L));
		assertTrue(rInsert);
		MethodEdgeValue value = methodGraph.getGraph().getEdge(new MethodNode("A", "a"), new MethodNode("B", "b"));
		assertEquals(400, value.getSizeOf());
	}
	
}
