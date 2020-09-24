package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.randomsearch;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearchBuilder;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesProblem;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class RandomSearchRunner extends AbstractAlgorithmRunner {

	public List<MicroservicesSolution> execute(Graph graph, 
			List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices,
			PseudoRandomGenerator random,
			int maxIterations) {
		Problem<MicroservicesSolution> problem = new MicroservicesProblem(graph, metrics, numberOfMicroservices, random);
		return execute(problem, maxIterations);
	}
	
	private List<MicroservicesSolution> execute(Problem<MicroservicesSolution> problem,
			int maxIterations) {
	    Algorithm<List<MicroservicesSolution>> randomSearch = new RandomSearchBuilder<MicroservicesSolution>(problem)
	    		.setMaxEvaluations(maxIterations)
	    		.build();
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(randomSearch).execute();
		return randomSearch.getResult();
	}
	
}
