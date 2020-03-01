package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;

public abstract class Monitor {

private List<RankingSolution<List<MicroservicesSolution>>> ranking;
	
	public Monitor(RankingSolution<List<MicroservicesSolution>> ranking) {
		this.ranking = new ArrayList<RankingSolution<List<MicroservicesSolution>>>();
		this.ranking.add(ranking);
	}

	public Monitor(List<RankingSolution<List<MicroservicesSolution>>> ranking) {
		this.ranking = ranking;
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
	
	protected void save(List<MicroservicesSolution> solution, int step, List<MetricPerMicroserviceArchitecture> metrics, 
			File folderToSaveResult) {
		File file = new File(folderToSaveResult, Integer.toString(step));
		MicroservicesToFile toFile = new MicroservicesToFile(file);
		toFile.write(solution, metrics);
		toFile.writeAllMetricsInAFile(solution, metrics, "allMetrics");
		toFile.writeAllRankingInAFile(solution, this.ranking, "ranking");
		computeBestRanking(solution, file);
	}
	
	abstract public void monitor(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, int numberOfMicroservices, 
			PseudoRandomGenerator random, int maxPopulation, int maxIterations, int intervalToMonitor, 
			File saveExecutions) throws Exception;
}
