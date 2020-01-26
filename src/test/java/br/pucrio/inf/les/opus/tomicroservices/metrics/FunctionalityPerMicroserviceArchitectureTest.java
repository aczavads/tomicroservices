package br.pucrio.inf.les.opus.tomicroservices.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.FunctionalityPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.MicroservicesSolution;

public class FunctionalityPerMicroserviceArchitectureTest {

	public Graph graph;
	public List<Vertex> verticiesInAMicroservice1;
	public Microservice microservice1;
	public List<Vertex> verticiesInAMicroservice2;
	public Microservice microservice2;
	public List<Microservice> lMicroservice;
	
	@BeforeEach
	public void init() {
		graph = new Graph();
		this.verticiesInAMicroservice1 = new ArrayList<Vertex>();
		this.verticiesInAMicroservice2 = new ArrayList<Vertex>();
		
		Vertex vertexA = new Vertex("A");
		Vertex vertexB = new Vertex("B");
		Vertex vertexC = new Vertex("C");
		new Edge(vertexA, vertexB, true, 1l, 10l);
		graph.insert(vertexA);
		graph.insert(vertexB);
		new Edge(vertexB, vertexC, true, 1l, 10l);
		graph.insert(vertexC);
		this.verticiesInAMicroservice1.add(vertexA);
		this.verticiesInAMicroservice1.add(vertexB);
		this.verticiesInAMicroservice1.add(vertexC);
		this.microservice1 = new Microservice(this.verticiesInAMicroservice1);
		
		Vertex vertexD = new Vertex("D");
		Vertex vertexE = new Vertex("E");
		new Edge(vertexD, vertexE, true, 1l, 10l);
		graph.insert(vertexD);
		graph.insert(vertexE);
		Vertex vertexF = new Vertex("F");
		new Edge(vertexD, vertexF, true, 1l, 10l);
		graph.insert(vertexF);
		this.verticiesInAMicroservice2.add(vertexD);
		this.verticiesInAMicroservice2.add(vertexE);
		this.verticiesInAMicroservice2.add(vertexF);
		this.microservice2 = new Microservice(this.verticiesInAMicroservice2);		
		
		this.lMicroservice = new ArrayList<Microservice>();
		this.lMicroservice.add(microservice1);
		this.lMicroservice.add(microservice2);
	}
	
	@Test
	public void testDifferentPredominantFunctionalityInAllMicroservice() {
		double expected = (12.0/9.0) + 1;
		
		graph.getVertex("A").addFuncitionalities("FA");
		graph.getVertex("B").addFuncitionalities("FA");
		graph.getVertex("C").addFuncitionalities("FX");
		
		graph.getVertex("D").addFuncitionalities("FA");
		graph.getVertex("E").addFuncitionalities("FX");
		graph.getVertex("F").addFuncitionalities("FX");
		
		ConvertValue minimize = new Minimize();
		FunctionalityPerMicroserviceArchitecture funcPerMSa = 
				new FunctionalityPerMicroserviceArchitecture(minimize);
		MicroservicesSolution mSolution = new MicroservicesSolution(this.lMicroservice);
		double result = funcPerMSa.getValue(mSolution);
		assertEquals(1.0/expected, result);
		assertEquals(expected, funcPerMSa.printableValue(result));
	}
	
	@Test
	public void testSamePredominantFunctionalityInAllMicroservice() {
		double expected = 13.0/6.0;
		
		graph.getVertex("A").addFuncitionalities("FA");
		graph.getVertex("B").addFuncitionalities("FA");
		graph.getVertex("C").addFuncitionalities("FX");
		
		graph.getVertex("D").addFuncitionalities("FA");
		graph.getVertex("E").addFuncitionalities("FA");
		graph.getVertex("F").addFuncitionalities("FA");
		
		ConvertValue minimize = new Minimize();
		FunctionalityPerMicroserviceArchitecture funcPerMSa = 
				new FunctionalityPerMicroserviceArchitecture(minimize);
		MicroservicesSolution mSolution = new MicroservicesSolution(this.lMicroservice);
		double result = funcPerMSa.getValue(mSolution);
		assertEquals(1.0/expected, result);
		assertEquals(expected, funcPerMSa.printableValue(result));
	}
}
