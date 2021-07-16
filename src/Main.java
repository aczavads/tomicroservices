import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.runner.multiobjective.NSGAIIRunner;
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
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.SizePerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices.NSGAIIIRunner;

public class Main {

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

		String acceptList = "/home/arthur/Documents/doutorado/tomsc/accept.list";
		String rejectList = "/home/arthur/Documents/doutorado/tomsc/reject.list";
		String dependency = "/home/arthur/Documents/doutorado/tomsc/csbaseDependency";
		String logDynamic = "/home/arthur/Documents/doutorado/tomsc/log";
		String featuresGeneral = "/home/arthur/Documents/doutorado/tomsc/feature";

		
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
		//metrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
		
		metrics.add(new CouplingPerMicroserviceArchitecture());
		metrics.add(new CohesionPerMicroserviceArchitecture(minimize));
		//metrics.add(new SizePerMicroserviceArchitecture());
		//int numberOfMicroservices = 13;
		int numberOfMicroservices = 2;
		PseudoRandomGenerator random = new JavaRandomGenerator();
		
		NSGAIIIRunner runner = new NSGAIIIRunner();

		//br.pucrio.inf.les.opus.tomicroservices.optimization.NSGAIIRunner runner 
		//	= new br.pucrio.inf.les.opus.tomicroservices.optimization.NSGAIIRunner();
		List<MetricPerMicroserviceArchitecture> otherMetrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		otherMetrics.add(new OverheadMaxPerMicroserviceArchitecture());
		otherMetrics.add(new FunctionalityPerMicroserviceArchitecture(minimize));
		otherMetrics.add(new ReusePerMicroserviceArchitecture("start", 1, minimize));
		
		final int executions = 7;
		for (int i = 0; i < executions; ++i) {
			File file = new File("/home/arthur/Documents/doutorado/tomsc/result" + i);
			
			runner._execute(graph, metrics, 
					numberOfMicroservices,
					random, file);
			
			//runner.execute(graph, metrics, 
			//		numberOfMicroservices,
			//		random, file, otherMetrics);
		}

	}
	
}
