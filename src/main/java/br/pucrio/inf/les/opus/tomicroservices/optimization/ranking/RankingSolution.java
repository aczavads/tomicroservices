package br.pucrio.inf.les.opus.tomicroservices.optimization.ranking;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public interface RankingSolution<S> {

	public String getName();
	
	public int[] bestSolutions(S solutions);

	double[] getMetrics(List<MicroservicesSolution> solutions);
		
}
