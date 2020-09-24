import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;
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
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices.NSGAIIIRunner;

public class Main2 {

	public static void main(String[] args) {
		String acceptList = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/accept.list";
		String dependency = "/home/luizmatheus/tecgraf/csbaseDependency";
		String logDynamic = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log_auth";
		String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/feature.list";

		File accepListFile = new File(acceptList);
		File staticFile = new File(dependency);
		File logDynamicFile = new File(logDynamic);
		File featuresGeneralFile = new File(featuresGeneral);
		//System.out.println("Static!");
		//ClassNamePattern pattern = new ClassNamePattern(accepListFile, true);
		Graph graph = new Graph();
		//ReadDependencyFinderFile dependencyFinder = new ReadDependencyFinderFile();
		//dependencyFinder.insertInGraphFromFile(staticFile, graph, pattern);
		//System.out.println(graph.getVerticesSize());
		System.out.println("Dynamic");
		DynamicLogAnalyzer dynamic = new DynamicLogAnalyzer();
		dynamic.analyze(logDynamicFile, graph, featuresGeneralFile);
		System.out.println(graph.getVerticesSize());
		System.out.println("NSGA-III");
		List<MetricPerMicroserviceArchitecture> metrics;
		metrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		ConvertValue minimize = new Minimize();
		metrics.add(new OverheadMaxPerMicroserviceArchitecture());
		metrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		metrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
		metrics.add(new CouplingPerMicroserviceArchitecture());
		metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		int numberOfMicroservices = 13;
		PseudoRandomGenerator random = new JavaRandomGenerator();
		NSGAIIIRunner runner = new NSGAIIIRunner();
		File file = new File("/home/result");
		runner.execute(graph, metrics, 
				numberOfMicroservices,
				random, file);
	}
	
}
