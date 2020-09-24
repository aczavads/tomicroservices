package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm;

import java.io.File;
import java.util.List;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;
import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic.DynamicLogAnalyzer;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices.NSGAIIIMonitor;

public abstract class Starter {

	protected File accepListFile;
	protected File rejectListFile;
	protected File staticFile;
	protected File logDynamicFile;
	protected File featuresGeneralFile;
	
	protected Graph graph;
	
	protected DynamicLogAnalyzer dynamic;
	
	protected NSGAIIIMonitor monitor;
	
	protected List<MetricPerMicroserviceArchitecture> metrics;
	
	protected int numberOfMicroservices = 10;
	protected PseudoRandomGenerator random = new JavaRandomGenerator();
	protected final int maxPopulation = 100;
	protected final int maxIterations = 10000;
	protected final int intervalToMonitor = 200;
	protected File saveExecutions;

	public Starter(File saveExecutions) {
		this.saveExecutions = saveExecutions;
	}
	
	protected void setting() {
		String acceptList = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/accept.list";
		String rejectList = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/reject.list";
		String dependency = "/home/luizmatheus/tecgraf/csbaseDependency";
		String logDynamic = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log";
		//String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/_feature.list";
		String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/feature.list";
		
		this.accepListFile = new File(acceptList);
		this.rejectListFile = new File(rejectList);
		this.staticFile = new File(dependency);
		this.logDynamicFile = new File(logDynamic);
		this.featuresGeneralFile = new File(featuresGeneral);
		
		this.graph = new Graph();		
		System.out.println("Dynamic");
		this.dynamic = new DynamicLogAnalyzer();
		dynamic.analyze(logDynamicFile, graph, featuresGeneralFile);
		
		//System.out.println("Monitor");
		//this.monitor = new NSGAIIIMonitor(new EuclideanDistanceRanking());
		
		//this.saveExecutions = new File("/home/luizmatheus/TSE/execution_first");
	}
	
	abstract public void metrics();
	
	abstract public void start();

}
