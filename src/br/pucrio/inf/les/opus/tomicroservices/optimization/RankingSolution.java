package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.util.List;

public interface RankingSolution<S> {

	public String getName();
	
	public int[] bestSolutions(S solutions);

	double[] getMetrics(List<MicroservicesSolution> solutions);
		
}
