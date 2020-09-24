package br.pucrio.inf.les.opus.tomicroservices.optimization.ranking;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class EuclideanDistanceRanking implements RankingSolution<List<MicroservicesSolution>>{

	private double[] maxs;
	private double[] mins;
 	
	private void computeMaxsAndMins(List<MicroservicesSolution> solutions) {
		final int firstElement = 0;
		int size = solutions.get(firstElement).getNumberOfObjectives();
		this.maxs = new double[size];
		this.mins = new double[size];
		//Arrays.fill(maxs, 0);
		//Arrays.fill(mins, 0);
		for (MicroservicesSolution solution : solutions) {
			double[] objectives = solution.getObjectives();
			for (int i = 0; i < size; ++i) {
				double value = objectives[i];
				if (maxs[i] < value) {
					maxs[i] = value;
				} if (mins[i] > value) {
					mins[i] = value;
				}
			}
		}
	}
	
	@Override
	public int[] bestSolutions(List<MicroservicesSolution> solutions) {
		int[] result = new int[1];
		if (solutions.size() == 0) {
			return null;
		}
		computeMaxsAndMins(solutions);
		double maxEd = 0;
		int indexEd = 0;
		for (int i = 0; i < solutions.size(); ++i) {
			MicroservicesSolution solution = solutions.get(i);
			double edValue = computeEd(solution.getObjectives());
			if (edValue > maxEd) {
				maxEd = edValue;
				indexEd = i;
			}
		}
		result[0] = indexEd;
		return result;
	}
	
	@Override
	public double[] getMetrics(List<MicroservicesSolution> solutions) {
		if (solutions.size() == 0) {
			return null;
		}
		double results[] = new double[solutions.size()];
		computeMaxsAndMins(solutions);
		int i = 0;
		for (MicroservicesSolution solution : solutions) {
			results[i++] = computeEd(solution.getObjectives());
		}
		return results;
	}

	/**
	 * Computed using max point.
	 * @param objectives objectives
	 * @return euclidean distance (ed)
	 */
	private double computeEd(double[] objectives) {
		double[] normed = norm(objectives);
		double sum = 0;
		for (int i = 0; i < objectives.length; ++i) {
			sum += Math.pow(this.maxs[i] - normed[i], 2);
		}
		return Math.sqrt(sum);
	}

	private double[] norm(double[] objectives) {
		double[] normed = new double[objectives.length];
		for (int i = 0; i < objectives.length; ++i) {
			normed[i] = (objectives[i] - this.mins[i]) / (this.maxs[i] - this.mins[i]);
		}
		return normed;
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}
