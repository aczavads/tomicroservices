package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaII.baseline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.Monitor;
import br.pucrio.inf.les.opus.tomicroservices.optimization.ranking.RankingSolution;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class NSGAIIMonitor extends Monitor {
	
	public NSGAIIMonitor(List<RankingSolution<List<MicroservicesSolution>>> ranking) {
		super(ranking);
	}
	
	public NSGAIIMonitor(RankingSolution<List<MicroservicesSolution>> ranking) {
		super(ranking);
	}
	
	public NSGAIIMonitor(List<RankingSolution<List<MicroservicesSolution>>> ranking, 
			List<MetricPerMicroserviceArchitecture> otherMetrics) {
		super(ranking, otherMetrics);
	}
	
	public NSGAIIMonitor(RankingSolution<List<MicroservicesSolution>> ranking,
			List<MetricPerMicroserviceArchitecture> otherMetrics) {
		super(ranking, otherMetrics);
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
		allMetrics.addAll(this.additionalMetrics);
		NSGAIIRunner nsgaIIRunner = new NSGAIIRunner();
		int steps = maxIterations / intervalToMonitor;
		/**
		List<MicroservicesSolution> solution = nsgaIIRunner.execute(graph, metrics, this.otherMetrics, 
				numberOfMicroservices, random, maxPopulation, maxIterations);
		super.save(solution, 0, allMetrics, saveExecutions);					
		**/
		List<MicroservicesSolution> solution = nsgaIIRunner.execute(graph, metrics, this.additionalMetrics, 
				numberOfMicroservices, random, maxPopulation, intervalToMonitor);
		final int firstStep = 0;
		super.save(solution, firstStep, allMetrics, saveExecutions);					
		for (int step = 1; step < steps; ++step) {
			System.out.println("Step: " + step);
			solution = nsgaIIRunner.executeWithInitialPopulation(graph, metrics, this.additionalMetrics, numberOfMicroservices, random, 
					maxPopulation, intervalToMonitor, solution);
			super.save(solution, step, allMetrics, saveExecutions);					
		}
	}
	
}
