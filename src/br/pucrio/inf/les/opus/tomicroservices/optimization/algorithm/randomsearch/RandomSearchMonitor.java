package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.randomsearch;

import java.io.File;
import java.util.List;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.Monitor;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices.NSGAIIIRunner;
import br.pucrio.inf.les.opus.tomicroservices.optimization.ranking.RankingSolution;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class RandomSearchMonitor extends Monitor {
	
	private List<RankingSolution<List<MicroservicesSolution>>> ranking;
	
	public RandomSearchMonitor(List<RankingSolution<List<MicroservicesSolution>>> ranking) {
		super(ranking);
	}
	
	public RandomSearchMonitor(RankingSolution<List<MicroservicesSolution>> ranking) {
		super(ranking);
	}
	
	public RandomSearchMonitor(List<RankingSolution<List<MicroservicesSolution>>> ranking, 
			List<MetricPerMicroserviceArchitecture> otherMetrics) {
		super(ranking, otherMetrics);
	}
	
	public RandomSearchMonitor(RankingSolution<List<MicroservicesSolution>> ranking,
			List<MetricPerMicroserviceArchitecture> otherMetrics) {
		super(ranking, otherMetrics);
	}

	@Override
	public void monitor(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices, PseudoRandomGenerator random, int maxPopulation, 
			int maxIterations, int intervalToMonitor, File saveExecutions) throws Exception {
		if (intervalToMonitor != 1) {
			throw new Exception("intervalToMonitor shoud be one because only the last result is saved");
		}
		RandomSearchRunner rs = new RandomSearchRunner();
		List<MicroservicesSolution> solution = rs.execute(graph, metrics, numberOfMicroservices, 
				random, maxIterations);
		final int onylOneMonitor = 0;
		super.save(solution, onylOneMonitor, this.additionalMetrics, saveExecutions);
	}
	
	

}
