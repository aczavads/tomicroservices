package br.pucrio.inf.les.opus.tomicroservices.optimization.algorithm.nsgaIII.toMicroservices;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.TournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesCrossover;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesMutation;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesProblem;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class NSGAIIIRunner extends AbstractAlgorithmRunner {
	
	private long time;
	
	public long timeExecuted() {
		return this.time;
	}
	
	private List<MicroservicesSolution> execute(Problem<MicroservicesSolution> problem,
			int maxIterations,
			int maxPopulation,
			PseudoRandomGenerator random) {
	    Algorithm<List<MicroservicesSolution>> algorithm;
	    MutationOperator<MicroservicesSolution> mutation;
	    CrossoverOperator<MicroservicesSolution> crossover;
	    SelectionOperator<List<MicroservicesSolution>, MicroservicesSolution> selection;
		
	    mutation = new MicroservicesMutation(random);
	    selection = new TournamentSelection<MicroservicesSolution>(4);
	    crossover = new MicroservicesCrossover(2, 2, random, 0.9, 0.1);
	    NSGAIII<MicroservicesSolution> nsgaIII =
	            new NSGAIIIBuilder<>(problem)
	                .setCrossoverOperator(crossover)
	                .setMutationOperator(mutation)
	                .setSelectionOperator(selection)
	                .setMaxIterations(maxIterations)
	                .build();
	    nsgaIII.setMaxPopulationSize(maxPopulation);
	    algorithm = (Algorithm<List<MicroservicesSolution>>) nsgaIII;
	    long initTime = System.currentTimeMillis();
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
		this.time = System.currentTimeMillis() - initTime;
		return algorithm.getResult();
	}
	
	public List<MicroservicesSolution> execute(Graph graph, 
			List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices,
			PseudoRandomGenerator random,
			int maxPopulation,
			int maxIterations) {
		Problem<MicroservicesSolution> problem = new MicroservicesProblem(graph, metrics, numberOfMicroservices, random);
		return execute(problem, maxIterations, maxPopulation, random);
	}
	
	public List<MicroservicesSolution> executeWithInitialPopulation(Graph graph, 
			List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices,
			PseudoRandomGenerator random,
			int maxPopulation,
			int maxIterations,
			List<MicroservicesSolution> initialPopulation) {
		Problem<MicroservicesSolution> problem = new MicroservicesProblem(graph, metrics, numberOfMicroservices, random, initialPopulation);
		return execute(problem, maxIterations, maxPopulation, random);
	}
	
	public void _execute(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices,
			double crossoverProbability, 
			double crossoverFraction,
			PseudoRandomGenerator random,
			File file) {
		Problem<MicroservicesSolution> problem;
	    Algorithm<List<MicroservicesSolution>> algorithm;
	    MutationOperator<MicroservicesSolution> mutation;
	    CrossoverOperator<MicroservicesSolution> crossover;
	    SelectionOperator<List<MicroservicesSolution>, MicroservicesSolution> selection;
	    
	    problem = new MicroservicesProblem(graph, metrics, numberOfMicroservices, random);
	    mutation = new MicroservicesMutation(random);
	    selection = new TournamentSelection<MicroservicesSolution>(4);
	    //crossover = new MicroservicesCrossover(2, 2, random, 0.9, 0.5);
	    //crossover = new MicroservicesCrossover(2, 2, random, 0.4, 0.4);
	    crossover = new MicroservicesCrossover(2, 2, random, crossoverProbability, crossoverFraction);
	    NSGAIII<MicroservicesSolution> nsgaIII =
	            new NSGAIIIBuilder<>(problem)
	                .setCrossoverOperator(crossover)
	                .setMutationOperator(mutation)
	                .setSelectionOperator(selection)
	                .setMaxIterations(2_000)
	                .build();
	    nsgaIII.setMaxPopulationSize(100);
	    algorithm = (Algorithm<List<MicroservicesSolution>>) nsgaIII;
	    try {
	    	long initTime = System.currentTimeMillis();
		    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
	    	long time = System.currentTimeMillis() - initTime;
		    System.out.println("Finished");
		    List<MicroservicesSolution> population = algorithm.getResult();
		    System.out.println("Ranking==>" + population.size());
		    Map<String, MicroservicesSolution> solutions = ranking(population, metrics);
		    System.out.println("Save Solution");
		    String result = print(solutions, metrics);
		    //System.out.println(result);
		    try {
		    	System.out.println(file);
				FileUtils.write(file, result, "UTF-8", false);
				
				FileUtils.write(file, "\n-------Best Of each fitness-------\n", "UTF-8", true);
				
				FileUtils.write(file, printFront(population, metrics), "UTF-8", true);
				
				String timeResult = "\nTIME: " + time;
				FileUtils.write(file, timeResult, "UTF-8", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    } catch (Throwable e) {
	    	System.out.println(e.getStackTrace());
	    }
	    /**
	    int solutionCount = 1;
	    System.out.println("Print Solution");
	    for (MicroservicesSolution solution : population) {
	    	System.out.println("Solution " + solutionCount++);
	    	for (MetricPerMicroserviceArchitecture metric : metrics) {
	    		int index = metric.getObjectiveIndex();
	    		System.out.println(metric.getName() + " : " + 
	    				metric.printableValue(solution.getObjective(index)));
	    		System.out.println(solution);
	    	}
	    }
	    **/
	}
	
	public String printFront(List<MicroservicesSolution> population, 
			List<MetricPerMicroserviceArchitecture> metrics) {
		String result = "";
		for (MicroservicesSolution solution : population) {
			for (MetricPerMicroserviceArchitecture metric : metrics) {
	    		int index = metric.getObjectiveIndex();
	    		double value = metric.printableValue(solution.getObjective(index));
	    		result += metric.getName() + " : " + value + "\n";
	    	}
	    	result += solution.print() + "\n";
		}
		return result;
	}
	
	public MicroservicesSolution getBestSolution(MicroservicesSolution solution1, 
			MicroservicesSolution solution2,
			int indexMetric, int total) {
		if (solution1 == null && solution2 == null) {
			return null;
		}
		if (solution1 == null) {
			return solution2;
		} else if (solution2 == null) {
			return solution1;
		}
		
		double value1 = solution1.getObjective(indexMetric);
		double value2 = solution2.getObjective(indexMetric);
		if (value1 < value2) {
			return solution1;
		} else if (value2 < value1) {
			return solution2;
		} else {
			int count1 = 0;
			int count2 = 0;
			for (int i = 0; i < total; ++i) {
				value1 = solution1.getObjective(i);
				value2 = solution2.getObjective(i);
				if (value1 < value2) {
					++count1;
				} else if (value2 < value1) {
					++count2;
				}
			}
			if (count1 > count2) {
				return solution1;
			} else {
				return solution2;
			}
		}
	}
	
	public Map<String, MicroservicesSolution> ranking(List<MicroservicesSolution> population, 
			List<MetricPerMicroserviceArchitecture> metrics) {
		Map<String, MicroservicesSolution> result = new HashMap<String, MicroservicesSolution>();
	    for (MicroservicesSolution solution : population) {
	    	for (MetricPerMicroserviceArchitecture metric : metrics) {
	    		int index = metric.getObjectiveIndex();
	    		double value = solution.getObjective(index);
	    		String name = metric.getName();
	    		MicroservicesSolution currentSolution = result.get(name);
	    		currentSolution = getBestSolution(solution, currentSolution, index, metrics.size());
	    		result.put(name, currentSolution);
	    	}
	    }
	    return result;
	}
	
	public String print(Map<String, MicroservicesSolution> solutions, 
			List<MetricPerMicroserviceArchitecture> metrics) {
		String result = "";
		Set<String> keys = solutions.keySet();
		for (String key : keys) {
			result += "Best of " + key + "\n";
			MicroservicesSolution solution = solutions.get(key);
			if (solution == null) {
				System.out.println("SOLUTION NULL");
				continue;
			}
	    	for (MetricPerMicroserviceArchitecture metric : metrics) {
	    		int index = metric.getObjectiveIndex();
	    		double value = metric.printableValue(solution.getObjective(index));
	    		result += metric.getName() + " : " + value + "\n";
	    	}
	    	result += solution.print() + "\n";
		}
		return result;
	}	
}
