package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.OverheadMaxPerMicroserviceArchitecture;

class NSGAIIIRunnerTest {

	@Test
	void test() {
		Graph graph = new Graph();
		Vertex vertexA = new Vertex("A");
		Vertex vertexB = new Vertex("B");
		Vertex vertexC = new Vertex("C");
		Vertex vertexD = new Vertex("D");
		Vertex vertexE = new Vertex("E");
		new Edge(vertexA, vertexB, true, 1, 1);
		graph.insert(vertexA);
		graph.insert(vertexB);
		new Edge(vertexD, vertexE, true, 1, 20);
		graph.insert(vertexC);
		new Edge(vertexC, vertexD, true, 1, 25);
		graph.insert(vertexD);
		graph.insert(vertexE);
		List<MetricPerMicroserviceArchitecture> metrics;
		metrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		metrics.add(new OverheadMaxPerMicroserviceArchitecture());
		int numberOfMicroservices = 2;
		PseudoRandomGenerator random = new JavaRandomGenerator();
		NSGAIIIRunner runner = new NSGAIIIRunner();
		runner.execute(graph, metrics, 
				numberOfMicroservices,
				random);
	}

}
