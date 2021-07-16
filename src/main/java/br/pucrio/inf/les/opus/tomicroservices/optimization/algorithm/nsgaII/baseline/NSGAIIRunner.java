package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaII.baseline;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.TournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesCrossover;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesMutation;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesProblem;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class NSGAIIRunner {
	
	public List<MicroservicesSolution> execute(Graph graph, List<MetricPerMicroserviceArchitecture> metrics,
			List<MetricPerMicroserviceArchitecture> otherMetrics, int numberOfMicroservices,
			PseudoRandomGenerator random, int maxPopulation, int maxIterations, 
			Problem<MicroservicesSolution> problem) {
	    Algorithm<List<MicroservicesSolution>> algorithm;
	    MutationOperator<MicroservicesSolution> mutation;
	    CrossoverOperator<MicroservicesSolution> crossover;
	    SelectionOperator<List<MicroservicesSolution>, MicroservicesSolution> selection;
	    
	    mutation = new MicroservicesMutation(random);
	    selection = new TournamentSelection<MicroservicesSolution>(4);
	    crossover = new MicroservicesCrossover(2, 2, random, 0.9, 0.1);
	    
	    NSGAII<MicroservicesSolution> nsgaII = new NSGAIIBuilder<MicroservicesSolution>(problem, crossover, mutation, maxPopulation)
	    		.setSelectionOperator(selection)
	    		.setMaxEvaluations(maxPopulation * maxIterations)
	    		.build();
	    algorithm = (Algorithm<List<MicroservicesSolution>>) nsgaII;
    	AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
	    List<MicroservicesSolution> population = algorithm.getResult();
	    return population;
	}
	
	public List<MicroservicesSolution> execute(Graph graph, List<MetricPerMicroserviceArchitecture> metrics,
			List<MetricPerMicroserviceArchitecture> otherMetrics, int numberOfMicroservices,
			PseudoRandomGenerator random, int maxPopulation, int maxIterations) {
		Problem<MicroservicesSolution> problem = new MicroservicesProblem(graph, metrics, numberOfMicroservices, random);
		return execute(graph, metrics, otherMetrics, numberOfMicroservices,
				random, maxPopulation, maxIterations, problem);
	}

	public List<MicroservicesSolution> executeWithInitialPopulation(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			List<MetricPerMicroserviceArchitecture> otherMetrics, int numberOfMicroservices, PseudoRandomGenerator random,
			int maxPopulation, int maxIterations, List<MicroservicesSolution> initialPopulation) {
		Problem<MicroservicesSolution> problem = new MicroservicesProblem(graph, metrics, numberOfMicroservices, random, initialPopulation);
		return execute(graph, metrics, otherMetrics, numberOfMicroservices,
				random, maxPopulation, maxIterations, problem);
	}
	
}
