package br.pucrio.inf.les.opus.tomicroservices.optimization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.OverheadMaxPerMicroservice;

class MicroserviceTest {

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
		long overheadExpected = 120l;
		Vertex vertexO = new Vertex("O");
		new Edge(graph.getVertex("C"), vertexO, true, 1l, overheadExpected);
		graph.insert(vertexO);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new OverheadMaxPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		double overheadResult = microservice.getMetricValue(metric.getName());
		assertEquals(overheadExpected, overheadResult);
	}
	
	@Test
	void microserviceWithTwoEdgeToOut() {
		long overheadExpected = 240l;
		Vertex vertexO = new Vertex("O");
		new Edge(graph.getVertex("C"), vertexO, true, 1l, overheadExpected/2);
		new Edge(graph.getVertex("A"), vertexO, false, 1l, overheadExpected/2);
		graph.insert(vertexO);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new OverheadMaxPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		long overheadResult = microservice.getMetricValue(metric.getName());
		assertEquals(overheadExpected, overheadResult);
		List<Vertex> remove = new ArrayList<Vertex>();
		remove.add(graph.getVertex("A"));
		List<Vertex> add = new ArrayList<Vertex>();
		Vertex vertexX = new Vertex("X");
		graph.insert(vertexX);
		add.add(vertexX);
		microservice.removeAndAddVerticies(remove, add);
		overheadResult = microservice.getMetricValue(metric.getName());
		assertEquals(overheadExpected/2, overheadResult);
	}
	
	@Test
	void microserviceAddAndRemoveVerticiesWithOverhead() {
		long overheadExpected = 100l;
		Vertex vertexO = new Vertex("O");
		new Edge(graph.getVertex("B"), vertexO, true, 1l, overheadExpected/2);
		graph.insert(vertexO);
		Vertex vertexX = new Vertex("X");
		new Edge(graph.getVertex("C"), vertexX, true, 1l, overheadExpected/2);
		graph.insert(vertexX);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new OverheadMaxPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		long overheadResult = microservice.getMetricValue(metric.getName());
		assertEquals(overheadExpected, overheadResult);
		List<Vertex> remove = new ArrayList<Vertex>();
		remove.add(graph.getVertex("B"));
		List<Vertex> add = new ArrayList<Vertex>();
		add.add(vertexX);
		microservice.removeAndAddVerticies(remove, add);
		overheadResult = microservice.getMetricValue(metric.getName());
		assertEquals(0, overheadResult);
	}
	
	@Test
	void microservicesAddTwoEqualVerticies() {
		Vertex vertexA = graph.getVertex("A");
		Vertex vertexB = graph.getVertex("B");
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new OverheadMaxPerMicroservice();
		microservice.addOrUpdateMetric(metric);
		long overheadResult = microservice.getMetricValue(metric.getName());
		assertEquals(0, overheadResult);
		List<Vertex> remove = new ArrayList<Vertex>();
		remove.add(vertexB);
		List<Vertex> add = new ArrayList<Vertex>();
		add.add(vertexA);
		microservice.removeAndAddVerticies(remove, add);
		List<Vertex> verticies = microservice.getVerticies();
		assertEquals(2, verticies.size());
	}

}
