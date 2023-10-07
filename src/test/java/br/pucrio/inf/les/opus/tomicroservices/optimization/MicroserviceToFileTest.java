package br.pucrio.inf.les.opus.tomicroservices.optimization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.optimization.file.MicroservicesToFile;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;

public class MicroserviceToFileTest {

	List<Microservice> lMicroservice;
	List<Vertex> verticies;
	File solution;
	File microservice1;
	Vertex vertexA, vertexB, vertexC, vertexD;
	
	@BeforeEach
	public void init() {
		this.lMicroservice = new ArrayList<Microservice>();
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
		Microservice microservice = new Microservice(this.verticies);
		this.lMicroservice.add(microservice);
		this.solution = new File(".");
		this.microservice1 = new File(this.solution, "microservice1");
	}
	
	@AfterEach
	public void clear() {
		this.microservice1.delete();
	}
	
	@Test
	public void writeMicroserviceOneFrozenMotive() throws IOException {
		MicroservicesToFile microserviceFile = new MicroservicesToFile(this.solution);
		microserviceFile.writeMicroservice(this.lMicroservice, this.solution);
		String expected = "B\n" + 
				"C\n" + 
				"start#motiveX\n" + 
				"A\n" + 
				"D\n" + 
				"end#motiveX\n";
		String result = FileUtils.readFileToString(new File(this.solution, "microservice1"), "UTF-8");
		assertEquals(expected, result);
	}

	@Test
	public void writeMicroserviceTwoFrozenMotive() throws IOException {
		this.vertexC.setFrozen("motiveK");
		MicroservicesToFile microserviceFile = new MicroservicesToFile(this.solution);
		microserviceFile.writeMicroservice(this.lMicroservice, this.solution);
		String expected = "B\n" + 
				"start#motiveK\n" +
				"C\n" + 
				"end#motiveK\n" +
				"start#motiveX\n" + 
				"A\n" + 
				"D\n" + 
				"end#motiveX\n";
		String result = FileUtils.readFileToString(new File(this.solution, "microservice1"), "UTF-8");
		assertEquals(expected, result);
	}
	
}
