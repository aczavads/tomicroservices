package br.pucrio.inf.les.opus.tomicroservices.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.ReusePerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.Microservice;

public class ReusePerMicroserviceTest {

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
	public void reuseZeroWithTwoMicroservice() {
		double reuseExpected = 0;
		Vertex vertexO = new Vertex("O");
		new Edge(vertexO, graph.getVertex("C"), true, 1l, 30l);
		graph.insert(vertexO);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new ReusePerMicroservice("start.user", 1);
		microservice.addOrUpdateMetric(metric);
		double reuseResult = microservice.getMetricValue(metric.getName());
		assertEquals(reuseExpected, reuseResult);
	}
	
	@Test
	public void reuseOneWithTwoMicroserviceAndAddAndRemoveVertex() {
		double reuseExpected = 1;
		Vertex vertexO = new Vertex("O");
		new Edge(vertexO, graph.getVertex("C"), true, 1l, 30l);
		Vertex vertexU = new Vertex("U");
		graph.insert(vertexU);
		new Edge(vertexU, graph.getVertex("A"), true, 1l, 30l);
		graph.insert(vertexO);
		Microservice microservice = new Microservice(this.verticiesInAMicroservice);
		MetricPerMicroservice metric = new ReusePerMicroservice("start.user", 1);
		microservice.addOrUpdateMetric(metric);
		double reuseResult = microservice.getMetricValue(metric.getName());
		assertEquals(reuseExpected, reuseResult);
		
		List<Vertex> remove = new ArrayList<Vertex>();
		remove.add(graph.getVertex("C"));
		List<Vertex> add = new ArrayList<Vertex>();
		Vertex vertexX = new Vertex("X");
		graph.insert(vertexX);
		add.add(vertexX);
		microservice.removeAndAddVerticies(remove, add);
		reuseExpected = 0;
		reuseResult = microservice.getMetricValue(metric.getName());
		assertEquals(reuseExpected, reuseResult);
		
		remove = new ArrayList<Vertex>();
		remove.add(graph.getVertex("A"));
		add = new ArrayList<Vertex>();
		add.add(graph.getVertex("C"));
		microservice.removeAndAddVerticies(remove, add);
		reuseExpected = 1;
		reuseResult = microservice.getMetricValue(metric.getName());
		assertEquals(reuseExpected, reuseResult);
	}

}
