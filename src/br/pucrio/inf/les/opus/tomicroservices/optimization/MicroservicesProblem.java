package br.pucrio.inf.les.opus.tomicroservices.optimization;

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

//public class MicroservicesProblem implements DoubleSolution, HasMetric<MetricPerMicroserviceArchitecture>{
	
	private Map<String, MetricPerMicroserviceArchitecture> metrics;
	
	private int lastObjectiveIndex;
	
	private int numberOfMicroservices;
	
	private Graph graph;

	private PseudoRandomGenerator random;
	
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
	}
	
	@Override
	public void evaluate(MicroservicesSolution solution) {
		Set<String> keys = this.metrics.keySet();
		int index;
		for (String key : keys) {
			MetricPerMicroserviceArchitecture metric = this.metrics.get(key);
			double result = metric.getValue(solution);
			index = metric.getObjectiveIndex();
			solution.setObjective(index, result);
		}
	}

	@Override
	public MicroservicesSolution createSolution() {
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
			last = current;
		}
    	for (Microservice m : lMicroservices) {
    		System.out.println(m.getVerticies());
    	}
		return new MicroservicesSolution(lMicroservices);
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
