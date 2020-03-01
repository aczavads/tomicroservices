package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;

public class NSGAIIMonitor extends Monitor {

	private List<MetricPerMicroserviceArchitecture> otherMetrics;
	
	public NSGAIIMonitor(List<RankingSolution<List<MicroservicesSolution>>> ranking) {
		super(ranking);
	}
	
	public NSGAIIMonitor(RankingSolution<List<MicroservicesSolution>> ranking) {
		super(ranking);
	}
	
	public NSGAIIMonitor(List<RankingSolution<List<MicroservicesSolution>>> ranking, 
			List<MetricPerMicroserviceArchitecture> otherMetrics) {
		super(ranking);
		this.otherMetrics = otherMetrics;
	}
	
	public NSGAIIMonitor(RankingSolution<List<MicroservicesSolution>> ranking,
			List<MetricPerMicroserviceArchitecture> otherMetrics) {
		super(ranking);
		this.otherMetrics = otherMetrics;
	}

	@Override
	public void monitor(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, int numberOfMicroservices, 
			PseudoRandomGenerator random, int maxPopulation, int maxIterations, int intervalToMonitor, 
			File saveExecutions) throws Exception {
		if (maxIterations < intervalToMonitor) {
			throw new Exception("maxIterations < intervalToMonitor");
		}
		List<MetricPerMicroserviceArchitecture> allMetrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		allMetrics.addAll(metrics);
		allMetrics.addAll(this.otherMetrics);
		NSGAIIRunner nsgaIIRunner = new NSGAIIRunner();
		int steps = maxIterations / intervalToMonitor;
		/**
		List<MicroservicesSolution> solution = nsgaIIRunner.execute(graph, metrics, this.otherMetrics, 
				numberOfMicroservices, random, maxPopulation, maxIterations);
		super.save(solution, 0, allMetrics, saveExecutions);					
		**/
		List<MicroservicesSolution> solution = nsgaIIRunner.execute(graph, metrics, this.otherMetrics, 
				numberOfMicroservices, random, maxPopulation, intervalToMonitor);
		final int firstStep = 0;
		super.save(solution, firstStep, allMetrics, saveExecutions);					
		for (int step = 1; step < steps; ++step) {
			System.out.println("Step: " + step);
			solution = nsgaIIRunner.executeWithInitialPopulation(graph, metrics, this.otherMetrics, numberOfMicroservices, random, 
					maxPopulation, intervalToMonitor, solution);
			super.save(solution, step, allMetrics, saveExecutions);					
		}
	}
	
}
