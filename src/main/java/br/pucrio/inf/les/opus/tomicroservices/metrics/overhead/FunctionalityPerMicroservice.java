package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;

public class FunctionalityPerMicroservice implements MetricPerMicroservice {

	private Map<String, Integer> functionalitiesToFrequency = new HashedMap<String, Integer>();
	
	private String predominantFunctionalityName = "";
	
	private int predominantFunctionality = 0;
	
	private int totalFrequencyFunctionalities = 0;
	
	private int totalFunctionalities = 0;
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(Microservice microservice) {
		List<Vertex> verticies = microservice.getVerticies();
		int total = 0;
		int predominantFunctionality = 0;
		String predominantFunctionalityName = "";
		for (Vertex vertex : verticies) {
			List<String> funcs = vertex.getFuncitionalities();
			for (String func : funcs) {
				int frequency = this.functionalitiesToFrequency.merge(func, 1, Integer::sum);
				if (frequency > predominantFunctionality) {
					predominantFunctionality = frequency;
					predominantFunctionalityName = func;
				}
				++total;
			}
		}
		this.predominantFunctionality = predominantFunctionality;
		this.predominantFunctionalityName = predominantFunctionalityName;
		this.totalFunctionalities = this.functionalitiesToFrequency.size();
		this.totalFrequencyFunctionalities = total;
		if (total == 0) {
			return 0;
		}
		return ((double)predominantFunctionality / (double)total);
	}
	
	private void updateFunctionalities(List<Vertex> verticies, boolean increment) {
		int inc;
		if (increment) {
			inc = 1;
		} else {
			inc = -1;
		}
		for (Vertex vertex : verticies) {
			List<String> funcs = vertex.getFuncitionalities();
			for (String func : funcs) {
				int frequency = this.functionalitiesToFrequency.merge(func, inc, Integer::sum);
				if (this.predominantFunctionalityName.equals(func)) {
					this.predominantFunctionality = this.predominantFunctionality + inc;
				}
				if (!increment) { 
					--this.totalFrequencyFunctionalities;
					if (frequency == 0) {
						this.functionalitiesToFrequency.remove(func);
						this.totalFunctionalities = this.totalFunctionalities + inc;
					}
				} else {
					++this.totalFrequencyFunctionalities;
					if (frequency == 1) {
						this.totalFunctionalities = this.totalFunctionalities + inc;
					}
				}
			}
		}
	}
	
	private void updatePredominantFunctionality() {
		Set<String> keys = this.functionalitiesToFrequency.keySet();
		for (String key: keys) {
			int frequencyFunctionality = this.functionalitiesToFrequency.get(key);
			if (frequencyFunctionality > this.predominantFunctionality) {
				this.predominantFunctionality = frequencyFunctionality;
				this.predominantFunctionalityName = key;
			}
		}
	}

	@Override
	public double getValue(List<Vertex> removedVerticies, List<Vertex> addedVerticies,
			List<Vertex> oldVerticiesInMicroservices, double oldMetricValue) {
		if (removedVerticies != null) {
			updateFunctionalities(removedVerticies, false);
		}
		if (addedVerticies != null) {
			updateFunctionalities(addedVerticies, true);
		}
		updatePredominantFunctionality();
		return ((double)this.predominantFunctionality / (double)this.totalFrequencyFunctionalities);
	}

	public String getPredominantFunctionalityName() {
		return predominantFunctionalityName;
	}

	public double getPredominantFunctionalityValue() {
		return predominantFunctionality;
	}

}
