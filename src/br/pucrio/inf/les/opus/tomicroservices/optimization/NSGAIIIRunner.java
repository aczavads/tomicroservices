package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.operator.impl.selection.TournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;

public class NSGAIIIRunner extends AbstractAlgorithmRunner {
	
	public void execute(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices,
			PseudoRandomGenerator random) {
		Problem<MicroservicesSolution> problem;
	    Algorithm<List<MicroservicesSolution>> algorithm;
	    MutationOperator<MicroservicesSolution> mutation;
	    CrossoverOperator<MicroservicesSolution> crossover;
	    SelectionOperator<List<MicroservicesSolution>, MicroservicesSolution> selection;
	    
	    problem = new MicroservicesProblem(graph, metrics, numberOfMicroservices, random);
	    mutation = new MicroservicesMutation(random);
	    selection = new TournamentSelection<MicroservicesSolution>(4);
	    crossover = new MicroservicesCrossover(2, 2, random, 0.9, 0.5);
	    algorithm =
	            new NSGAIIIBuilder<>(problem)
	                .setCrossoverOperator(crossover)
	                .setMutationOperator(mutation)
	                .setSelectionOperator(selection)
	                .setMaxIterations(100)
	                .build();
	    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
	    List<MicroservicesSolution> population = algorithm.getResult();
	    int solutionCount = 1;
	    for (MicroservicesSolution solution : population) {
	    	for (MetricPerMicroserviceArchitecture metric : metrics) {
	    		System.out.println("Solution " + solutionCount++);
	    		int index = metric.getObjectiveIndex();
	    		System.out.println(metric.getName() + " : " + 
	    				metric.printableValue(solution.getObjective(index)));
	    		System.out.println(solution);
	    	}
	    }
	}
	
}
