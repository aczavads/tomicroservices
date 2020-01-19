package br.pucrio.inf.les.opus.tomicroservices.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CouplingPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.OverheadMaxPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.Microservice;

class CouplingPerMicroserviceTest {

	public Graph graph;
	public List<Vertex> verticiesInAMicroservice;
	
	@BeforeEach
	void init() {
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
	void microserviceWithOneEdgeToOut() {
		double couplingExpected = 1;
		Vertex vertexO = new Vertex("O");
		new Edge(graph.getVertex("C"), vertexO, true, 1l, 30l);
		graph.insert(vertexO);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new CouplingPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		double couplingResult = microservice.getMetricValue(metric.getName());
		assertEquals(couplingExpected, couplingResult);
	}
	
	@Test
	void microserviceWithTwoEdgeToOutAndRemoveAndAdd() {
		long couplingExpected = 2;
		Vertex vertexO = new Vertex("O");
		new Edge(graph.getVertex("C"), vertexO, true, 1l, 1l);
		new Edge(graph.getVertex("A"), vertexO, true, 1l, 1l);
		graph.insert(vertexO);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new CouplingPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		double coupling = microservice.getMetricValue(metric.getName());
		assertEquals(couplingExpected, coupling);
		List<Vertex> remove = new ArrayList<Vertex>();
		remove.add(graph.getVertex("A"));
		List<Vertex> add = new ArrayList<Vertex>();
		Vertex vertexX = new Vertex("X");
		graph.insert(vertexX);
		add.add(vertexX);
		microservice.removeAndAddVerticies(remove, add);
		coupling = microservice.getMetricValue(metric.getName());
		assertEquals(couplingExpected - 1, 1);
	}
	

}
