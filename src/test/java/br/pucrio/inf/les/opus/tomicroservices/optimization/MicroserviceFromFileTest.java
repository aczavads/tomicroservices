package br.pucrio.inf.les.opus.tomicroservices.optimization;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.optimization.file.MicroservicesFromFile;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class MicroserviceFromFileTest {

	List<Vertex> verticies;
	Graph graph;
	Vertex vertexA, vertexB, vertexC, vertexD;
	File solution;
	
	@BeforeEach
	public void init() {
		this.vertexA = new Vertex("A");
		vertexA.setFrozen("motiveX");
		this.vertexB = new Vertex("B");
		this.vertexC = new Vertex("C");
		this.vertexD = new Vertex("D");
		vertexD.setFrozen("motiveX");
		this.verticies = new ArrayList<Vertex>();
		this.verticies.add(vertexA);
		this.verticies.add(vertexB);
		this.verticies.add(vertexC);
		this.verticies.add(vertexD);
		this.graph = new Graph();
		for (Vertex vertex : this.verticies) {
			this.graph.insert(vertex);
		}
		this.solution = new File("./___solution1____");
	}
	
	@AfterEach
	public void clear() {
		this.solution.delete();
	}
	
	@Test
	public void readMicroserviceOneFrozenMotive() throws IOException {
		final String content = 
				"B\n" + 
				"C\n" + 
				"start#motiveX\n" + 
				"A\n" + 
				"D\n" + 
				"end#motiveX\n";
		FileUtils.write(new File(this.solution, "microservice1"), content, "UTF-8");
		MicroservicesFromFile fromFile = new MicroservicesFromFile(null);
		MicroservicesSolution microserviceSolution = fromFile.readSolution(this.solution, this.graph.getNameToVertex());
		Microservice microserviceResult = microserviceSolution.getMicroservices().get(0);
		List<Vertex> verticiesResult = microserviceResult.getVerticies();
		assertEquals(this.verticies.size(), verticiesResult.size());
		for (Vertex vertex : this.verticies) {
			assertTrue(verticiesResult.contains(vertex));
		}
		assertEquals("", verticiesResult.get(0).getFrozenMotive());
		assertEquals("", verticiesResult.get(1).getFrozenMotive());
		assertEquals("motiveX", verticiesResult.get(2).getFrozenMotive());
		assertEquals("motiveX", verticiesResult.get(3).getFrozenMotive());
	}
		
	@Test
	public void readMicroserviceTwoFrozenMotive() throws IOException {
		final String content = 
					"B\n" + 
					"start#motiveK\n" +
					"C\n" + 
					"end#motiveK\n" +
					"start#motiveX\n" + 
					"A\n" + 
					"D\n" + 
					"end#motiveX\n";
		FileUtils.write(new File(this.solution, "microservice1"), content, "UTF-8");
		MicroservicesFromFile fromFile = new MicroservicesFromFile(null);
		MicroservicesSolution microserviceSolution = fromFile.readSolution(this.solution, this.graph.getNameToVertex());
		Microservice microserviceResult = microserviceSolution.getMicroservices().get(0);
		List<Vertex> verticiesResult = microserviceResult.getVerticies();
		assertEquals(this.verticies.size(), verticiesResult.size());
		for (Vertex vertex : this.verticies) {
			assertTrue(verticiesResult.contains(vertex));
		}
		assertEquals("", verticiesResult.get(0).getFrozenMotive());
		assertEquals("motiveK", verticiesResult.get(1).getFrozenMotive());
		assertEquals("motiveX", verticiesResult.get(2).getFrozenMotive());
		assertEquals("motiveX", verticiesResult.get(3).getFrozenMotive());
	}
	
}
