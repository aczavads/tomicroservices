import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.analysis.ast.ClassNamePattern;
import br.pucrio.inf.les.opus.tomicroservices.analysis.ast.ReadDependencyFinderFile;
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

public class MainMGA {

	public static void main(String[] args) {
		/**
		String javaFolder = "/home/luizmatheus/tecgraf/rest-services";
		InjectCode injectCode = new InjectCode();
		boolean result = injectCode.injectCodeInAllFiles(new File(javaFolder), 
				"/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log", 
				"/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/feature");
		**/
		
//		String acceptList = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/accept.list";
//		String rejectList = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/reject.list";
//		String dependency = "/home/luizmatheus/tecgraf/csbaseDependency";
//		String logDynamic = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log";
//		String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/_feature.list";

		String acceptList = "/home/din/toMicroservicesMGA/tomsc/accept.list";
		String rejectList = "/home/din/toMicroservicesMGA/tomsc/reject.list";
		String dependency = "/home/din/toMicroservicesMGA/tomsc/csbaseDependency";
		String logDynamic = "/home/din/toMicroservicesMGA/tomsc/webpublico-log.txt";
		String featuresGeneral = "/home/din/toMicroservicesMGA/tomsc/feature";

		
		File accepListFile = new File(acceptList);
		File rejectListFile = new File(rejectList);
		File staticFile = new File(dependency);
		File logDynamicFile = new File(logDynamic);
		File featuresGeneralFile = new File(featuresGeneral);
		System.out.println("Static");
		ClassNamePattern pattern = new ClassNamePattern(accepListFile, true);
		ClassNamePattern reject = new ClassNamePattern(rejectListFile, false);
		Graph graph = new Graph();
		ReadDependencyFinderFile dependencyFinder = new ReadDependencyFinderFile();
		//remover este comentário para executar!
		dependencyFinder.insertInGraphFromFile(staticFile, graph, pattern, reject);
		System.out.println(graph.getVerticesSize());
		System.out.println("Dynamic");
		DynamicLogAnalyzer dynamic = new DynamicLogAnalyzer();
		dynamic.analyze(logDynamicFile, graph, featuresGeneralFile);
 		System.out.println(graph.getVerticesSize());
		System.out.println("NSGA-III");
		
	
		List<MetricPerMicroserviceArchitecture> metrics;
		metrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		ConvertValue minimize = new Minimize();
		
		//REMOVE IT TO NSGA-II
		//metrics.add(new OverheadMaxPerMicroserviceArchitecture());
		//metrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		//metrics.add(new FunctionalityPerMicroserviceArchitectureV2(minimize));
		//metrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
		
		metrics.add(new CouplingPerMicroserviceArchitecture());
		metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		//metrics.add(new SizePerMicroserviceArchitecture());
		int numberOfMicroservices = 5; 
		double crossoverProbability = 0.9; 
		double crossoverFraction = 0.5;
		PseudoRandomGenerator random = new JavaRandomGenerator();
		
		NSGAIIIRunner runner = new NSGAIIIRunner();

		//br.pucrio.inf.les.opus.tomicroservices.optimization.NSGAIIRunner runner 
		//	= new br.pucrio.inf.les.opus.tomicroservices.optimization.NSGAIIRunner();
		List<MetricPerMicroserviceArchitecture> otherMetrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		otherMetrics.add(new OverheadMaxPerMicroserviceArchitecture());
		otherMetrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		otherMetrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));

		metrics.addAll(otherMetrics);
		final int executions = 5;
		for (int i = 0; i < executions; ++i) {
			File file = new File("/home/din/toMicroservicesMGA/tomsc/results/result-" + (UUID.randomUUID().toString()));
			
			runner._execute(graph, metrics, 
					numberOfMicroservices,
					crossoverProbability,
					crossoverFraction,
					random, file);
			
			//runner.execute(graph, metrics, 
			//		numberOfMicroservices,
			//		random, file, otherMetrics);
		}

	}
	
}