package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;

public class OverheadMaxPerMicroservice implements MetricPerMicroservice {

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	private Map<String, Boolean> getNameToBoolean(List<Vertex> verticies) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		for (Vertex vertex : verticies) {
			result.put(vertex.getName(), new Boolean(false));
		}
		return result;
	}
	
	private Long maxDataTraffic(List<Long> lDataTraffic) {
		Long max = new Long(0l);
		for (Long dataTraffic: lDataTraffic) {
			if (max.compareTo(dataTraffic) < 0) {
				max = dataTraffic;
			}
		}
		return max;
	}
		
	@Override
	public double getValue(Microservice microservice) {
		List<Vertex> verticies = microservice.getVerticies();
		Map<String, Boolean> nameToBoolean = getNameToBoolean(verticies);
		long result = 0l;
		for (Vertex vertex: verticies) {
			result += metricPerVertex(vertex, nameToBoolean);
		}
		return result;
	}

	private long metricPerVertex(Vertex vertex, Map<String, Boolean> nameToBoolean) {
		long result = 0l;
		Map<String, Edge> outbound = vertex.getOutbound();
		Set<String> keys = outbound.keySet();
		for (String key: keys) {
			Edge edge = outbound.get(key);
			String targetName = edge.getTarget().getName();
			Boolean consider = nameToBoolean.get(targetName);
			if (consider == null || consider.booleanValue()) {
				List<Long> lDataTraffic = edge.getDataTraffic();
				result = result + maxDataTraffic(lDataTraffic);
			}
		}
		Boolean consider = nameToBoolean.get(vertex.getName());
		if (consider != null && consider.booleanValue()) {
			Map<String, Edge> inbound = vertex.getInbound();
			keys = inbound.keySet();
			for (String key: keys) {
				Edge edge = inbound.get(key);
				String sourceName = edge.getSource().getName();
				consider = nameToBoolean.get(sourceName);
				if (consider == null || consider.booleanValue()) {
					List<Long> lDataTraffic = edge.getDataTraffic();
					result = result - maxDataTraffic(lDataTraffic);
				}
			}
		}
		return result;
	}
	
	private Map<String, Boolean> getNameToBoolean(List<Vertex> verticiesTrue, List<Vertex> verticiesTrue2, 
			List<Vertex> verticiesFalse, List<Vertex> verticiesFalse2) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		nameToBoolean(verticiesTrue, result, true);
		nameToBoolean(verticiesTrue2, result, true);
		nameToBoolean(verticiesFalse, result, false);
		nameToBoolean(verticiesFalse2, result, false);
		return result;
	}

	private void nameToBoolean(List<Vertex> verticiesTrue, Map<String, Boolean> result, boolean value) {
		if (verticiesTrue != null) {
			for (Vertex vertex : verticiesTrue) {
				result.put(vertex.getName(), new Boolean(value));
			}
		}
	}
	
	private long metricPerVertex(Vertex vertex, List<Vertex> useToCalc, List<Vertex> useToCalc2, 
			List<Vertex> notUseToCalc, List<Vertex> notUseToCalc2) {
		return metricPerVertex(vertex, getNameToBoolean(useToCalc, useToCalc2, notUseToCalc, notUseToCalc2));
	}

	@Override
	public double getValue(List<Vertex> removedVerticies, List<Vertex> addedVerticies,
			List<Vertex> oldVerticiesInMicroservices, double oldMetricValue) {
		double addValue = 0;
		double removeValue = 0;
		double newValueMetric = 0;
		if (removedVerticies != null) {
			for (int i = 0; i < removedVerticies.size(); ++i) {
				Vertex vertex = removedVerticies.get(i);
				//removeValue += metricPerVertex(vertex, addedVerticies, null, oldVerticiesInMicroservices, removedVerticies);
				removeValue += metricPerVertex(vertex, addedVerticies, null, oldVerticiesInMicroservices, 
						new ArrayList<Vertex>(removedVerticies));
			}
		}
		if (addedVerticies != null) {
			for (Vertex vertex: addedVerticies) {
				addValue += metricPerVertex(vertex, oldVerticiesInMicroservices, addedVerticies, 
						removedVerticies, null);
			}
		}
		newValueMetric = oldMetricValue + addValue - removeValue;
		return newValueMetric;
	}

}
