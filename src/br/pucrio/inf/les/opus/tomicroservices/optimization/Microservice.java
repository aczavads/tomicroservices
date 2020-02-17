package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.HasMetric;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;

public class Microservice implements HasMetric<MetricPerMicroservice> {
	
	private List<Vertex> verticies;
	
	private TreeSet<String> verticiesName;
	
	private Map<String, MetricPerMicroservice> metrics;
	
	private Map<String, Double> metricToValue;
		
	public Microservice(Microservice microservice) {
		commonConstructor(microservice.getVerticies());
		this.metrics = microservice.getMetrics();
		this.metricToValue = microservice.getMetricToValue();
	}
	
	public Microservice(List<Vertex> verticies) {
		 commonConstructor(verticies);
	}

	private void commonConstructor(List<Vertex> verticies) {
		this.verticies = new ArrayList<Vertex>();
		this.verticies.addAll(verticies);
		this.verticiesName = new TreeSet<String>();
		for (Vertex vertex : verticies) {
			this.verticiesName.add(vertex.getName());
		}
		this.metrics = new HashMap<String, MetricPerMicroservice>();
		this.metricToValue = new HashMap<String, Double>();
	}
	
	public Map<String, MetricPerMicroservice> getMetrics() {
		return new HashMap<String, MetricPerMicroservice>(this.metrics);
	}
	
	public Map<String, Double> getMetricToValue() {
		return new HashMap<String, Double>(this.metricToValue);
	}
	
	public List<Vertex> getVerticies() {
		return this.verticies;
	}
	
	public void addOrUpdateMetric(MetricPerMicroservice metric) {
		String key = metric.getName();
		this.metrics.put(key, metric);
		double value = metric.getValue(this);
		//System.out.println(value);
		this.metricToValue.put(metric.getName(), new Double(value));
	}
	
	public double getMetricValue(String metricName) {
		return this.metricToValue.get(metricName);
	}
	
	public void removeAndAddVerticies(Vertex remove, Vertex add) {
		List<Vertex> removeVerticies = new ArrayList<Vertex>();
		removeVerticies.add(remove);
		List<Vertex> addVerticies = new ArrayList<Vertex>();
		addVerticies.add(add);
		removeAndAddVerticies(removeVerticies, addVerticies);
	}
	
	public void removeAndAddVerticies(List<Vertex> removeVerticies, 
			List<Vertex> addVerticies) {
		Set<String> keys = metrics.keySet();
		for (String key : keys) {
			MetricPerMicroservice metric = this.metrics.get(key);
			double oldValue = this.metricToValue.get(metric.getName());
			double newValue = metric.getValue(removeVerticies, addVerticies, this.verticies, oldValue);
			removeVerticies(removeVerticies);
			addVerticies(addVerticies);
			this.metricToValue.put(key, newValue);
		}
	}

	private void addVerticies(List<Vertex> verticies) {
		if (verticies == null) return;
		for (Vertex vertex : verticies) {
			if (!verticiesName.contains(vertex.getName())) {
				this.verticiesName.add(vertex.getName());
				this.verticies.add(vertex);
			}
		}
	}

	private void removeVerticies(List<Vertex> verticies) {
		if (verticies == null) return;
		for (Vertex vertex : verticies) {
			this.verticiesName.remove(vertex.getName());
		}
		this.verticies.removeAll(verticies);
	}
	
	public String toString() {
		return this.verticies.toString();
	}

	public String print() {
		String result = "";
		for (Vertex vertex : this.verticies) {
			String name = vertex.getName();
			List<String> funcs = vertex.getFuncitionalities();
			result += name + "!";
			for (String fun : funcs) {
				result += fun + " ";
			}
			result += "! \n";
		}
		return result;
	}
	
}
