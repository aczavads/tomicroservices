package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.TournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.metrics.Metric;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;

public class NSGAIIRunner {

	public void execute(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices,
			PseudoRandomGenerator random,
			File file, List<MetricPerMicroserviceArchitecture> otherMetricsToPrint) {
		Problem<MicroservicesSolution> problem;
	    Algorithm<List<MicroservicesSolution>> algorithm;
	    MutationOperator<MicroservicesSolution> mutation;
	    CrossoverOperator<MicroservicesSolution> crossover;
	    SelectionOperator<List<MicroservicesSolution>, MicroservicesSolution> selection;
	    
	    problem = new MicroservicesProblem(graph, metrics, numberOfMicroservices, random);
	    mutation = new MicroservicesMutation(random);
	    selection = new TournamentSelection<MicroservicesSolution>(4);
	    //crossover = new MicroservicesCrossover(2, 2, random, 0.9, 0.2);
	    crossover = new MicroservicesCrossover(2, 2, random, 0.9, 0.1);
	    
	    NSGAII<MicroservicesSolution> nsgaII = new NSGAIIBuilder<MicroservicesSolution>(problem, crossover, mutation, 50)
	    		.setSelectionOperator(selection)
	    		.setMaxEvaluations(10000*50)
	    		.build();
	    algorithm = (Algorithm<List<MicroservicesSolution>>) nsgaII;
	    try {
	    	long initTime = System.currentTimeMillis();
	    	AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
	    	long time = System.currentTimeMillis() - initTime;
	    	System.out.println("Finished");
		    List<MicroservicesSolution> population = algorithm.getResult();
		    System.out.println("Ranking");
		    Map<String, MicroservicesSolution> solutions = ranking(population, metrics, otherMetricsToPrint);
		    System.out.println("Save Solution");
		    String result = print(solutions, metrics, otherMetricsToPrint);
		    //System.out.println(result);
		    try {
		    	System.out.println(file);
				FileUtils.write(file, result, "UTF-8", false);
				
				FileUtils.write(file, "-------Best Of each fitness-------\n", "UTF-8", true);
				
				FileUtils.write(file, printFront(population, metrics, otherMetricsToPrint), "UTF-8", true);
				
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
	
	public MicroservicesSolution getBestSolution(MicroservicesSolution solution1, 
			MicroservicesSolution solution2,
			int indexMetric, int total, 
			List<MetricPerMicroserviceArchitecture> otherMetricsToPrint, boolean usedMetric) {
		if (solution1 == null && solution2 == null) {
			return null;
		}
		if (solution1 == null) {
			return solution2;
		} else if (solution2 == null) {
			return solution1;
		}
		
		double value1, value2;
		if (!usedMetric) {
			value1 = otherMetricsToPrint.get(indexMetric).getValue(solution1);
			value2 = otherMetricsToPrint.get(indexMetric).getValue(solution2);
		} else {
			value1 = solution1.getObjective(indexMetric);
			value2 = solution2.getObjective(indexMetric);
		}
		if (value1 < value2) {
			return solution1;
		} else if (value2 < value1) {
			return solution2;
		}
		//
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
		for (MetricPerMicroserviceArchitecture otherMetric : otherMetricsToPrint) {
			value1 = otherMetric.getValue(solution1);
			value2 = otherMetric.getValue(solution2);
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
	
	public Map<String, MicroservicesSolution> ranking(List<MicroservicesSolution> population, 
			List<MetricPerMicroserviceArchitecture> metrics, 
			List<MetricPerMicroserviceArchitecture> otherMetricsToPrint) {
		Map<String, MicroservicesSolution> result = new HashMap<String, MicroservicesSolution>();
	    for (MicroservicesSolution solution : population) {
	    	for (MetricPerMicroserviceArchitecture metric : metrics) {
	    		int index = metric.getObjectiveIndex();
	    		double value = solution.getObjective(index);
	    		String name = metric.getName();
	    		MicroservicesSolution currentSolution = result.get(name);
	    		currentSolution = getBestSolution(solution, currentSolution, index, metrics.size(), otherMetricsToPrint, true);
	    		result.put(name, currentSolution);
	    	}
	    	int i = 0;
	       	for (MetricPerMicroserviceArchitecture otherMetric : otherMetricsToPrint) {
	    		String name = otherMetric.getName();
	    		MicroservicesSolution currentSolution = result.get(name);
	    		currentSolution = getBestSolution(solution, currentSolution, i++, metrics.size(), otherMetricsToPrint, false);
	    		result.put(name, currentSolution);
	    	}
	    }
	    return result;
	}
	
	public String print(Map<String, MicroservicesSolution> solutions, 
			List<MetricPerMicroserviceArchitecture> metrics,
			List<MetricPerMicroserviceArchitecture> otherMetricsToPrint) {
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
	    	for (MetricPerMicroserviceArchitecture otherMetric : otherMetricsToPrint) {
	    		double value = otherMetric.printableValue(otherMetric.getValue(solution));
	    		result += otherMetric.getName() + " : " + value + "\n";
	    	}
	    	result += solution.print() + "\n";
		}
		return result;
	}
	
	public String printFront(List<MicroservicesSolution> population, 
			List<MetricPerMicroserviceArchitecture> metrics,
			List<MetricPerMicroserviceArchitecture> otherMetricsToPrint) {
		String result = "";
		for (MicroservicesSolution solution : population) {
			for (MetricPerMicroserviceArchitecture metric : metrics) {
	    		int index = metric.getObjectiveIndex();
	    		double value = metric.printableValue(solution.getObjective(index));
	    		result += metric.getName() + " : " + value + "\n";
	    	}
	    	for (MetricPerMicroserviceArchitecture otherMetric : otherMetricsToPrint) {
	    		double value = otherMetric.printableValue(otherMetric.getValue(solution));
	    		result += otherMetric.getName() + " : " + value + "\n";
	    	}
	    	result += solution.print() + "\n";
		}
		return result;
	}
	
}
