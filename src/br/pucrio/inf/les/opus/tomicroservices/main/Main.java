package br.pucrio.inf.les.opus.tomicroservices.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic.DynamicLogAnalyzer;
import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.optimization.NSGAIIIStarterFromModifiedCase;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaII.baseline.NSGAIIStarter;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.baseline.NSGAIIIStarterBaseline;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices.NSGAIIIStarter;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.randomsearch.RandomSearchMonitor;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.randomsearch.RandomSearchStarter;
import br.pucrio.inf.les.opus.tomicroservices.optimization.file.MicroservicesFromFile;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class Main {
	
	public static void printCoupling() {
		
	}
	
	public static void subGraphGenerationAndInterface(String[] args) {
		///home/luizmatheus/TSE/usingSubFeatures/selectedCase/lastCase (copy)/new_solution_drive_n_10
		MicroservicesFromFile fromFile = new MicroservicesFromFile();
		
		String logDynamic = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log";
		//String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/_feature.list";
		String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/_feature.list";
		
		File logDynamicFile = new File(logDynamic);
		File featuresGeneralFile = new File(featuresGeneral);
		
		Graph graph = new Graph();		
		System.out.println("Dynamic");
		DynamicLogAnalyzer dynamic = new DynamicLogAnalyzer();
		dynamic.analyze(logDynamicFile, graph, featuresGeneralFile);
		
		File output = new File("/tmp/outputResult.txt");
		
		try {
			List<MicroservicesSolution> solutions = fromFile.read(
					new File("/tmp/microservice/"), 
					graph);
			for (MicroservicesSolution solution : solutions) {
				System.out.println("Solution:");
				List<Microservice> microservices = solution.getMicroservices();
				Map<Integer, List<String>> provToVert = new HashMap<Integer, List<String>>();
				Map<Integer, List<String>> reqToVert = new HashMap<Integer, List<String>>();
				for (int i = 0; i < microservices.size(); ++i) {
					provToVert.put(i + 1, new ArrayList<String>());
					reqToVert.put(i + 1, new ArrayList<String>());
				}
				int i = 0;
				for (Microservice microservice : microservices) {
					for (int j = 0; j < microservices.size(); ++j) {
						int countDeps = 0;
						if (i != j) {
							Microservice outbound = microservices.get(j);
							List<Vertex> inVertices = microservice.getVerticies();
							List<Vertex> outVertices = outbound.getVerticies();
							Map<String, Boolean> mapOut = new HashMap<String, Boolean>();
							for (Vertex outVertex : outVertices) {
								mapOut.put(outVertex.getName(), true);
							}
							for (Vertex inVertex : inVertices) {
								Map<String, Edge> outPossible = inVertex.getOutbound();
								Set<String> keys = outPossible.keySet();
								for (String key : keys) {
									if (mapOut.get(key) != null) {
										reqToVert.get(i + 1).add(key);
										provToVert.get(j + 1).add(inVertex.getName());
										++countDeps;
										String content = "microservice" + (i + 1) + " to " + "microservice" + (j+1) + "  " + inVertex.getName() + " -> " + key;
										FileUtils.write(output, content + "\n", "UTF-8", true);
									}
								}
							}
						}
						String result = "microservice" + (i+1) + " -> " + countDeps + " -> " + "microservice" + (j+1);
						FileUtils.write(output, result + "\n", "UTF-8", true);
					}
					++i;
				}
				Set<Integer> keys = reqToVert.keySet();
				for (Integer key : keys) {
					FileUtils.write(output, "Requerido do microservice" + key + "\n", "UTF-8", true);
					List<String> lReq = reqToVert.get(key);
					for (String req : lReq) {
						String content = req;
						FileUtils.write(output, content + "\n", "UTF-8", true);
					}
					FileUtils.write(output, "Provido do microservice" + key + "\n", "UTF-8", true);
					List<String> lProv = provToVert.get(key);
					for (String prov : lProv) {
						String content = prov;
						FileUtils.write(output, content + "\n", "UTF-8", true);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		subGraphGenerationAndInterface(args);
		
		/*
		int executions = 30;
		for (int i = 0; i < executions; ++i) {
			RandomSearchStarter starter = new RandomSearchStarter(new File("/home/luizmatheus/ICSME/RandomSearch/execution_RandomSearch" + i));
			starter.start();
		}
		*/
		
		/**
		int executions = 30;
		for (int i = 22; i < executions; ++i) {
			NSGAIIStarter starter = new NSGAIIStarter(new File("/home/luizmatheus/ICSME/NSGAIIBaseLine/execution_NSGAIIBaseLine" + i));
			starter.start();
		}
		**/
		
		/**
		final int executions = 2;
		for (int i = 1	; i < executions; ++i) {
			NSGAIIIStarter starter = new NSGAIIIStarter(new File("/tmp/execution_NSGAIII" + i));
			starter.start();
		}
		**/
		
		
		/**
		executions = 41;
		for (int i = 30	; i < executions; ++i) {
			System.out.println("Execution: " + i);
			NSGAIIIStarterBaseline starter = new NSGAIIIStarterBaseline(new File("/home/luizmatheus/ICSME/NSGAIIIBaseLine/execution_NSGAIIIBaseLine" + i));
			starter.start();
		}
		**/
		
		
		///home/luizmatheus/TSE/selectedCase/solution32
		/**
		NSGAIIIStarter starter = new NSGAIIIStarterFromModifiedCase(
				new File("/home/luizmatheus/TSE/usingFeatures/fiveMSA_new/firstCase/case")
				, new File("/home/luizmatheus/TSE/2Execution_NSGAIII_fiveCases/")
				);
		starter.start();
		**/
	}
	
	/**
	public static void main(String[] args) {
		Options options = new Options();
		final String logFileOption = "logfile";
		final String numberOfMicroservicesOption = "numbermicroservices";
		final String outputFileOption = "outputfile";
		options.addOption(logFileOption, true, "Path to log file generated by toMicroservices agent");
		options.addOption(numberOfMicroservicesOption, true, "Number of microservices generated");
		options.addOption(outputFileOption, true, "Path to Output file");
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			boolean fail = false;
			if (!cmd.hasOption(logFileOption)) {
				System.err.println("There is not " + logFileOption);
				fail = true;
			} 
			if (!cmd.hasOption(numberOfMicroservicesOption)) {
				System.err.println("There is not " + numberOfMicroservicesOption);
				fail = true;
			}
			if (!cmd.hasOption(outputFileOption)) {
				System.err.println("There is not " + outputFileOption);
				fail = true; //TODO - remove code duplication
			}
			if (fail) return;
			String logFilePath = cmd.getOptionValue(logFileOption);
			File logFile = new File(logFilePath);
			String numberMicroservicesStr = cmd.getOptionValue(numberOfMicroservicesOption);
			int nMicroservices = Integer.parseInt(numberMicroservicesStr);
			String outputPath = cmd.getOptionValue(outputFileOption);
			File outputFile = new File(outputPath);
			PrintGraph graph = new PrintGraph();
			graph.print(logFile, outputFile, nMicroservices);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	**/
	
}
