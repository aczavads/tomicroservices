package br.pucrio.inf.les.opus.tomicroservices.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.metrics.MethodEdgeValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MethodGraph;

public class GenerateDOTTest {
	
	@Test
	public void gnerateDotTest() {
		GenerateDOT dot = new GenerateDOT();
		MethodGraph methodGraph = new MethodGraph();
		methodGraph.insert("A", "a", "B", "b", new MethodEdgeValue(100L, 1));
		methodGraph.insert("B", "b", "C", "c", new MethodEdgeValue(300L, 2));
		String graph = dot.getDOT(methodGraph.getGraph());
		assertEquals("strict graph G {\n" + 
				"  Aa [ label=\"A.a\" ];\n" + 
				"  Bb [ label=\"B.b\" ];\n" + 
				"  Cc [ label=\"C.c\" ];\n" + 
				"  Aa -- Bb [ label=\"100\" ];\n" + 
				"  Bb -- Cc [ label=\"300\" ];\n" + 
				"}\n" + 
				"", graph);
	}
	
}
