package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.Monitor;
import br.pucrio.inf.les.opus.tomicroservices.optimization.file.MicroservicesToFile;
import br.pucrio.inf.les.opus.tomicroservices.optimization.ranking.RankingSolution;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class NSGAIIIMonitor extends Monitor{
	
	private List<RankingSolution<List<MicroservicesSolution>>> ranking;
		
	public NSGAIIIMonitor(List<RankingSolution<List<MicroservicesSolution>>> ranking) {
		super(ranking);
	}
	
	public NSGAIIIMonitor(RankingSolution<List<MicroservicesSolution>> ranking) {
		super(ranking);
	}
	
	public NSGAIIIMonitor(List<RankingSolution<List<MicroservicesSolution>>> ranking, 
			List<MetricPerMicroserviceArchitecture> otherMetrics) {
		super(ranking, otherMetrics);
	}
	
	public NSGAIIIMonitor(RankingSolution<List<MicroservicesSolution>> ranking,
			List<MetricPerMicroserviceArchitecture> otherMetrics) {
		super(ranking, otherMetrics);
	}
	
	/**
	public NSGAIIIMonitor(RankingSolution<List<MicroservicesSolution>> ranking) {
		this.ranking = new ArrayList<RankingSolution<List<MicroservicesSolution>>>();
		this.ranking.add(ranking);
	}
	
	private void computeBestRanking(List<MicroservicesSolution> solution, File saveRanking) {
		for (RankingSolution<List<MicroservicesSolution>> ranking : this.ranking) {
			int bestsIndex[] = ranking.bestSolutions(solution);
			String name = ranking.getName();
			String content = name + ":";
			for (int i = 0; i < bestsIndex.length; ++i) {
				content += "solution" + (bestsIndex[i] + 1) + " ";
			}
			try {
				FileUtils.write(new File(saveRanking, "ranking"), content, "UTF-8", true);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	private void save(List<MicroservicesSolution> solution, int step, List<MetricPerMicroserviceArchitecture> metrics, 
			File folderToSaveResult) {
		File file = new File(folderToSaveResult, Integer.toString(step));
		MicroservicesToFile toFile = new MicroservicesToFile(file);
		toFile.write(solution, metrics);
		toFile.writeAllMetricsInAFile(solution, metrics, "allMetrics");
		toFile.writeAllRankingInAFile(solution, this.ranking, "ranking");
		computeBestRanking(solution, file);
	}
	**/
	
	public void monitor(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices, PseudoRandomGenerator random, int maxPopulation, 
			int maxIterations, int intervalToMonitor, File saveExecutions) throws Exception {
		if (maxIterations < intervalToMonitor) {
			throw new Exception("maxIterations < intervalToMonitor");
		}
		NSGAIIIRunner nsgaIIIRunner = new NSGAIIIRunner();
		int steps = maxIterations / intervalToMonitor;
		List<MicroservicesSolution> solution = nsgaIIIRunner.execute(graph, metrics, numberOfMicroservices, 
				random, maxPopulation, intervalToMonitor);
		secondUntilLastMonitor(graph, metrics, numberOfMicroservices, random, solution, maxPopulation, maxIterations, 
				intervalToMonitor, saveExecutions, solution, steps, nsgaIIIRunner);
	}

	public void monitor(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices, PseudoRandomGenerator random, 
			int maxPopulation, int maxIterations, 
			int intervalToMonitor, List<MicroservicesSolution> initialPopulation, File saveExecutions) throws Exception {
		if (maxIterations < intervalToMonitor) {
			throw new Exception("maxIterations < intervalToMonitor");
		}
		NSGAIIIRunner nsgaIIIRunner = new NSGAIIIRunner();
		int steps = maxIterations / intervalToMonitor;
		List<MicroservicesSolution> solution = nsgaIIIRunner.executeWithInitialPopulation(graph, metrics, numberOfMicroservices, random, 
				maxPopulation, intervalToMonitor, initialPopulation);		
		secondUntilLastMonitor(graph, metrics, numberOfMicroservices, random, solution, maxPopulation, maxIterations, 
				intervalToMonitor, saveExecutions, solution, steps, nsgaIIIRunner);
	}
	
	private void secondUntilLastMonitor(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices, PseudoRandomGenerator random, 
			List<MicroservicesSolution> initialPopulation, int maxPopulation, int maxIterations, 
			int intervalToMonitor, File saveExecutions, 
			List<MicroservicesSolution> solution, int steps, NSGAIIIRunner nsgaIIIRunner) {
		System.out.println("First");
		final int firstStep = 0;
		List<MetricPerMicroserviceArchitecture> allMetrics = new ArrayList<MetricPerMicroserviceArchitecture>();
		allMetrics.addAll(metrics);
		if (this.additionalMetrics != null && !this.additionalMetrics.isEmpty()) {
			allMetrics.addAll(this.additionalMetrics);
		}
		save(solution, firstStep, allMetrics, saveExecutions);		
		//save(solution, firstStep, metrics, saveExecutions);		
		for (int step = 1; step < steps; ++step) {
			System.out.println("Step: " + step);
			solution = nsgaIIIRunner.executeWithInitialPopulation(graph, metrics, numberOfMicroservices, random, 
					maxPopulation, intervalToMonitor, initialPopulation);
			save(solution, step, allMetrics, saveExecutions);
			//save(solution, step, metrics, saveExecutions);					
		}
	}
	
}
