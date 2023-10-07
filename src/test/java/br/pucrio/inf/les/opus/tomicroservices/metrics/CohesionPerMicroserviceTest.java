package br.pucrio.inf.les.opus.tomicroservices.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CohesionPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;

public class CohesionPerMicroserviceTest {

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
	void cohesionWithOneEdgeToOut() {
		double cohesionExpected = 2.0/3.0;
		Vertex vertexO = new Vertex("O");
		new Edge(graph.getVertex("C"), vertexO, true, 1l, 30l);
		graph.insert(vertexO);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new CohesionPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		double cohesionResult = microservice.getMetricValue(metric.getName());
		assertEquals(cohesionExpected, cohesionResult);
	}
	
	@Test
	void cohesionRemoveAndAdd() {
		double cohesionExpected = 1;
		Vertex vertexO = new Vertex("O");
		new Edge(graph.getVertex("C"), vertexO, true, 1l, 1l);
		new Edge(graph.getVertex("C"), graph.getVertex("A"), true, 1l, 1l);
		graph.insert(vertexO);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new CohesionPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		double cohesionResult = microservice.getMetricValue(metric.getName());
		assertEquals(cohesionExpected, cohesionResult);
		cohesionExpected = 1.0/3.0;
		List<Vertex> remove = new ArrayList<Vertex>();
		remove.add(graph.getVertex("A"));
		List<Vertex> add = new ArrayList<Vertex>();
		Vertex vertexX = new Vertex("X");
		graph.insert(vertexX);
		add.add(vertexX);
		microservice.removeAndAddVerticies(remove, add);
		cohesionResult = microservice.getMetricValue(metric.getName());
		assertEquals(Precision.round(cohesionExpected, 2), Precision.round(cohesionResult, 2));
	}
	
	@Test
	public void cohesionOnlyOneVertex() {
		double expected = 0;
		graph = new Graph();
		this.verticiesInAMicroservice = new ArrayList<Vertex>();
		Vertex vertexA = new Vertex("A");
		graph.insert(vertexA);
		this.verticiesInAMicroservice.add(vertexA);
		MetricPerMicroservice metric = new CohesionPerMicroservice();
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		microservice.addOrUpdateMetric(metric);
		double cohesionResult = microservice.getMetricValue(metric.getName());
		assertEquals(expected, cohesionResult);
	}
}
