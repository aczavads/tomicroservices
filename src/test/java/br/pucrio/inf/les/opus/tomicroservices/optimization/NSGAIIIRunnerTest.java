package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.Minimize;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CohesionPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CouplingPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.FunctionalityPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.OverheadMaxPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.ReusePerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.ReusePerMicroserviceArchitecture;

class NSGAIIIRunnerTest {

	@Test
	void test() {
		Graph graph = new Graph();
		Vertex vertexA = new Vertex("A");
		vertexA.addFuncitionalities("FX");
		Vertex vertexB = new Vertex("B");
		vertexB.addFuncitionalities("FX");
		Vertex vertexC = new Vertex("C");
		vertexC.addFuncitionalities("FY");
		Vertex vertexD = new Vertex("D");
		vertexD.addFuncitionalities("FY");
		Vertex vertexE = new Vertex("E");
		vertexE.addFuncitionalities("FY");
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
		ConvertValue minimize = new Minimize();
		metrics.add(new OverheadMaxPerMicroserviceArchitecture());
		metrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		metrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
		metrics.add(new CouplingPerMicroserviceArchitecture());
		metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		int numberOfMicroservices = 2;
		PseudoRandomGenerator random = new JavaRandomGenerator();
		NSGAIIIRunner runner = new NSGAIIIRunner();
		runner.execute(graph, metrics, 
				numberOfMicroservices,
				random);
	}

}
