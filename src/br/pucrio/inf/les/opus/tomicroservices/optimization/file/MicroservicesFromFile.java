package br.pucrio.inf.les.opus.tomicroservices.optimization.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class MicroservicesFromFile {
	
	public MicroservicesSolution readSolution(File solution, Map<String, Vertex> verticies) throws IOException {
		System.out.println(solution.getAbsolutePath());
		File[] microservicesFile = solution.listFiles();
		List<Microservice> lMicroservice = new ArrayList<Microservice>();
		for (File microserviceFile : microservicesFile) {
			System.out.println(microserviceFile.getAbsolutePath());
			Microservice microservice;
			List<Vertex> verticiesInMicroservice = new ArrayList<Vertex>();
			List<String> lines = FileUtils.readLines(microserviceFile, "UTF-8");
			boolean isFrozen = false;
			String frozenMotive = "";
			for (String line : lines) {
				System.out.println(line);
				if (line.startsWith("start#")) {
					isFrozen = true;
					final int motiveIndex = 1;
					frozenMotive = line.split("#")[motiveIndex];
				} else if (line.startsWith("end#")) {
					isFrozen = false;
					frozenMotive = "";
				} else {
					Vertex vertex = verticies.get(line);
					if (vertex == null) {
						System.out.println("NULL: " + line);
					}
					if (isFrozen) {
						vertex.setFrozen(frozenMotive);
					}
					verticiesInMicroservice.add(vertex);
				}
			}
			microservice = new Microservice(verticiesInMicroservice);
			lMicroservice.add(microservice);
		}
		return new MicroservicesSolution(lMicroservice);
	} 
	
	public List<MicroservicesSolution> read(File folder, Graph graph) throws IOException {
		List<MicroservicesSolution> microserviceSolutions = new ArrayList<MicroservicesSolution>();
		File[] solutions = folder.listFiles();
		for (File solution : solutions) {
			microserviceSolutions.add(readSolution(solution, graph.getNameToVertex()));
		}
		return microserviceSolutions;
	}
	
}
