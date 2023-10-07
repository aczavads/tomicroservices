package br.pucrio.inf.les.opus.tomicroservices.optimization.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.uma.jmetal.problem.impl.AbstractGenericProblem;
import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.HasMetric;
import br.pucrio.inf.les.opus.tomicroservices.metrics.Metric;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;

public class MicroservicesProblem extends AbstractGenericProblem<MicroservicesSolution> 
			implements HasMetric<MetricPerMicroserviceArchitecture> {
	
	private Map<String, MetricPerMicroserviceArchitecture> metrics;
	
	private int lastObjectiveIndex;
	
	private int numberOfMicroservices;
	
	private Graph graph;

	private PseudoRandomGenerator random;
	
	private boolean givenInitialSolution;
	
	private List<MicroservicesSolution> initialSolution;
	
	private int numberOfcreatedSolutions;
	
	public MicroservicesProblem(Graph graph, List<MetricPerMicroserviceArchitecture> metrics, 
			int numberOfMicroservices,
			PseudoRandomGenerator random) {
		this.graph = graph;
		this.metrics = new HashMap<String, MetricPerMicroserviceArchitecture>();
		this.lastObjectiveIndex = -1;
		for (MetricPerMicroserviceArchitecture m : metrics) {
			if (!this.metrics.containsKey(m.getName())) {
				this.metrics.put(m.getName(), m);
				m.setObjectiveIndex(++this.lastObjectiveIndex);
			}
		}
		this.numberOfMicroservices = numberOfMicroservices;
		this.random = random;
		this.setNumberOfObjectives(this.metrics.size());
		this.setNumberOfConstraints(0);
		this.setNumberOfVariables(0);
		this.setName(this.getClass().getName());
		this.givenInitialSolution = false;
		this.numberOfcreatedSolutions = 0;
	}
	
	public MicroservicesProblem(Graph graph, List<MetricPerMicroserviceArchitecture> metrics,
			int numberOfMicroservices, PseudoRandomGenerator random, List<MicroservicesSolution> initialSolution) {
		this(graph, metrics, numberOfMicroservices, random);
		this.givenInitialSolution = true;
		this.initialSolution = initialSolution;
	}

	public static int killed = 0;
	private boolean isKilled(MicroservicesSolution microservicesSolution) {
		List<Microservice> lMicroservice = microservicesSolution.getMicroservices();
		int sizes[] = new int[lMicroservice.size()];
		int totalSize = 0;
		int count = 0;
		for (Microservice microservice : lMicroservice) {
			int size = microservice.getVerticies().size();
			sizes[count++] = size;
			totalSize += size;
		}
		//final double thresholdHigh = 0.16;
		final double thresholdHigh = 0.24;
		//final double thresholdHigh = 0.32;
		final double thresholdLow = 0.04;
		//final double thresholdLow = 0.06;
		for (int i = 0; i < count; ++i) {
			double reason = ((double)sizes[i]) / ((double)totalSize);
			if (reason > thresholdHigh || reason < thresholdLow) {
				System.out.println(++killed);
				System.out.println("killed");
				return true;
			}
		}
		System.out.println("Not killed");
		return false;
	}
	
	@Override
	public void evaluate(MicroservicesSolution solution) {
		//System.out.println("Evaluate");
		//boolean killed = isKilled(solution);
		boolean killed = false;
		Set<String> keys = this.metrics.keySet();
		int index;
		for (String key : keys) {
			MetricPerMicroserviceArchitecture metric = this.metrics.get(key);
			double result;
			if (killed == true) {
				result = Double.MAX_VALUE; //Reavaliar.
			} else {
				result = metric.getValue(solution);
			}
			index = metric.getObjectiveIndex();
			solution.setObjective(index, result);
		}
	}

	public static int valueCreate = 0;
	
	//TO RANDOMSEARCH
	/**
	@Override
	public MicroservicesSolution createSolution() {
		killed = 0;
		++valueCreate;
		List<Vertex> verticies = graph.getVerticies();
		List<Microservice> lMicroservices = new ArrayList<Microservice>();
		int total = verticies.size();
		int jump = total / this.numberOfMicroservices;
		//int current;
		//int last = 0;
		boolean indexFlag[] = new boolean[verticies.size()];
		for (int i = 1; i <= this.numberOfMicroservices; ++i) {
			//current =  (jump * i) + 1;
			List<Vertex> currentVerticies = new ArrayList<Vertex>();
			int addCases = 0;
			for (int j = 0; j < jump; ++j) {
				int index = random.nextInt(0, verticies.size() - 1);
				if (indexFlag[index] == false) {
					indexFlag[index] = true;
					currentVerticies.add(verticies.get(index));
					++addCases;
				}
			}
			for (int j = 0; j < indexFlag.length; ++j) {
				if (addCases < jump) {
					currentVerticies.add(verticies.get(j));
					++addCases;
				} else {
					break;
				}
			}
			Microservice microservice = new Microservice(currentVerticies);
			lMicroservices.add(microservice);
			//last = current;
		}
		//System.out.println(lMicroservices.size());
		MicroservicesSolution solution = new MicroservicesSolution(lMicroservices);
		//System.out.println(solution);
		//System.out.println(valueCreate);
		return solution;
	}
	**/
	
	//NSGA
	@Override
	public MicroservicesSolution createSolution() {
		killed = 0;
		++valueCreate;
		if (this.givenInitialSolution) {
			MicroservicesSolution solution;
			if (this.numberOfcreatedSolutions >= this.initialSolution.size()) {
				int index = random.nextInt(0, this.numberOfcreatedSolutions - 1);
				solution = this.initialSolution.get(index);
				MicroservicesMutation mutant = new MicroservicesMutation(this.random);
					System.out.println("Mutant in createSolution");
					solution = mutant.execute(solution);
					return solution;
				//throw new RuntimeException("Overflow in numberOfcreatedSolutions");
			} else {
				solution = this.initialSolution.get(this.numberOfcreatedSolutions);
				++numberOfcreatedSolutions;
				return solution;
			}
			//return solution;
		}
		//System.out.println("Create solution");
		killed = 0;
		++valueCreate;
		List<Vertex> verticies = graph.getVerticies();
		List<Microservice> lMicroservices = new ArrayList<Microservice>();
		int total = verticies.size();
		int jump = total / this.numberOfMicroservices;
		int current;
		int last = 0;
		for (int i = 1; i <= this.numberOfMicroservices; ++i) {
			current =  (jump * i) + 1;
			Microservice microservice = new Microservice(verticies.subList(last, current));
			lMicroservices.add(microservice);
			//System.out.println(microservice);
			last = current;
		}
		//System.out.println(lMicroservices.size());
		MicroservicesSolution solution = new MicroservicesSolution(lMicroservices);
		//System.out.println(solution);
		//System.out.println(valueCreate);
		return solution;
		//return new MicroservicesSolution(lMicroservices);
	}

	@Override
	public void addOrUpdateMetric(MetricPerMicroserviceArchitecture metric) {
		this.metrics.put(metric.getName(), metric);
		metric.setObjectiveIndex(++this.lastObjectiveIndex);
		this.setNumberOfObjectives(this.metrics.size());
	}
	
	public Map<String, MetricPerMicroserviceArchitecture> getMetrics() {
		return this.metrics;
	}

}
