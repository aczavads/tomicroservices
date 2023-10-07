package br.pucrio.inf.les.opus.tomicroservices.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.FunctionalityPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;

public class FunctionalityPerMicroserviceTest {

	public Graph graph;
	public List<Vertex> verticiesInAMicroservice;
	
	@BeforeEach
	public void init() {
		graph = new Graph();
		this.verticiesInAMicroservice = new ArrayList<Vertex>();
		Vertex vertexA = new Vertex("A");
		Vertex vertexB = new Vertex("B");
		Vertex vertexC = new Vertex("C");
		new Edge(vertexA, vertexB, true, 1l, 10l);
		graph.insert(vertexA);
		graph.insert(vertexB);
		new Edge(vertexB, vertexC, true, 1l, 10l);
		graph.insert(vertexC);
		this.verticiesInAMicroservice.add(vertexA);
		this.verticiesInAMicroservice.add(vertexB);
		this.verticiesInAMicroservice.add(vertexC);
	}
	
	@Test
	public void testThreeDifferentFuncs() {
		this.graph.getVertex("A").addFuncitionalities("FA");
		this.graph.getVertex("B").addFuncitionalities("FB");
		this.graph.getVertex("C").addFuncitionalities("FC");

		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		FunctionalityPerMicroservice metric = new FunctionalityPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		double result = microservice.getMetricValue(metric.getName());
		assertEquals(1.0/3.0, result);
		assertEquals(1.0, metric.getPredominantFunctionalityValue());
	}
	
	@Test
	public void testTheSameFuncs() {
		this.graph.getVertex("A").addFuncitionalities("FA");
		this.graph.getVertex("B").addFuncitionalities("FA");
		this.graph.getVertex("C").addFuncitionalities("FA");

		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		FunctionalityPerMicroservice metric = new FunctionalityPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		assertEquals(1.0, microservice.getMetricValue(metric.getName()));
		assertEquals(3.0, metric.getPredominantFunctionalityValue());
	}
	
	@Test
	public void testAddAndRemoveVertex() {
		this.graph.getVertex("A").addFuncitionalities("FA");
		this.graph.getVertex("B").addFuncitionalities("FA");
		this.graph.getVertex("C").addFuncitionalities("FC");
		
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		FunctionalityPerMicroservice metric = new FunctionalityPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		assertEquals(2.0/3.0, microservice.getMetricValue(metric.getName()));
		assertEquals(2.0, metric.getPredominantFunctionalityValue());
		
		Vertex vertexO = new Vertex("O");
		vertexO.addFuncitionalities("FA");
		microservice.removeAndAddVerticies(this.graph.getVertex("C"), vertexO);
		assertEquals(1.0, microservice.getMetricValue(metric.getName()));
		assertEquals(3.0, metric.getPredominantFunctionalityValue());
		assertEquals("FA", metric.getPredominantFunctionalityName());
	}
	
	@Test
	public void testAddAndRemoveTwoVertex() {
		this.graph.getVertex("A").addFuncitionalities("FA");
		this.graph.getVertex("B").addFuncitionalities("FB");
		this.graph.getVertex("C").addFuncitionalities("FA");
		Vertex vertexD = new Vertex("D");
		vertexD.addFuncitionalities("FB");
		this.graph.insert(vertexD);
		this.verticiesInAMicroservice.add(vertexD);
		
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		FunctionalityPerMicroservice metric = new FunctionalityPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		assertEquals(2.0/4.0, microservice.getMetricValue(metric.getName()));
		assertEquals(2.0, metric.getPredominantFunctionalityValue());
		
		Vertex vertexO = new Vertex("O");
		vertexO.addFuncitionalities("FA");
		Vertex vertexY = new Vertex("Y");
		vertexY.addFuncitionalities("FY");
		List<Vertex> add = new ArrayList<Vertex>();
		add.add(vertexO);
		add.add(vertexY);
		List<Vertex> remove = new ArrayList<Vertex>();
		remove.add(this.graph.getVertex("B"));
		remove.add(this.graph.getVertex("D"));

		microservice.removeAndAddVerticies(remove, add);
		assertEquals(3.0/4.0, microservice.getMetricValue(metric.getName()));
		assertEquals(3.0, metric.getPredominantFunctionalityValue());
		assertEquals("FA", metric.getPredominantFunctionalityName());
	}
}
