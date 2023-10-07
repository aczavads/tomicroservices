package src.fitness;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic.DynamicLogAnalyzer;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.Minimize;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CohesionPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.CouplingPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.FunctionalityPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.OverheadMaxPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.ReusePerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.file.MicroservicesFromFile;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesProblem;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

class MicroservicesArchitectureFitness {
	
	@Test
	void test() {
		MicroservicesFromFile microservicesFromFile = new MicroservicesFromFile();
		Graph graph = new Graph();		
		DynamicLogAnalyzer dynamic = new DynamicLogAnalyzer();
		String logDynamic = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log";
		File logDynamicFile = new File(logDynamic);
		String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/_feature.list";
		File featuresGeneralFile = new File(featuresGeneral);
		dynamic.analyze(logDynamicFile, graph, featuresGeneralFile);
		
		List<MetricPerMicroserviceArchitecture> metrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		ConvertValue minimize = new Minimize();
		metrics.add(new CouplingPerMicroserviceArchitecture());
		metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		metrics.add(new OverheadMaxPerMicroserviceArchitecture());
		metrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		metrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
		
		try {
			List<MicroservicesSolution> solutions = microservicesFromFile.read(new File("/tmp/microservice/"), graph);
			for (MicroservicesSolution solution : solutions) {
				MicroservicesProblem problem = new MicroservicesProblem(graph, metrics, 10, new JavaRandomGenerator());
				problem.evaluate(solution);
				double[] objectives = solution.getObjectives();
				for (int i = 0; i < objectives.length; ++i) {
					System.out.println(objectives[i]);
				}
				/**
				List<MetricPerMicroserviceArchitecture> metrics = new ArrayList<MetricPerMicroserviceArchitecture>();
				ConvertValue minimize = new Minimize();
				metrics.add(new CouplingPerMicroserviceArchitecture());
				metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
				metrics.add(new OverheadMaxPerMicroserviceArchitecture());
				metrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
				metrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
				**/
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
