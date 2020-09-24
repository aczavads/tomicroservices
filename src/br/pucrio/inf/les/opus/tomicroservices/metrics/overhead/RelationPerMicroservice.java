package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;

public abstract class RelationPerMicroservice implements MetricPerMicroservice {

	@Override
	abstract public String getName();
	
	abstract protected double valueInEdge(Edge edge);
	
	protected Map<String, Boolean> getNameToBoolean(List<Vertex> verticies) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		for (Vertex vertex : verticies) {
			result.put(vertex.getName(), new Boolean(false));
		}
		return result;
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


	abstract protected boolean toCompute(String vertexName, Map<String, Boolean> nameToBoolean);
	
	
	protected double metricPerVertex(Vertex vertex, Map<String, Boolean> nameToBoolean) {
		double result = 0l;
		result += computeOutbound(vertex, nameToBoolean);
		result += computeInbound(vertex, nameToBoolean);
		return result;
	}

	protected double computeInbound(Vertex vertex, Map<String, Boolean> nameToBoolean) {
		double result = 0;
		Set<String> keys;
		//if (toCompute(vertex.getName(), nameToBoolean)) {
			Map<String, Edge> inbound = vertex.getInbound();
			keys = inbound.keySet();
			for (String key: keys) {
				Edge edge = inbound.get(key);
				String sourceName = edge.getSource().getName();
				if (toCompute(sourceName, nameToBoolean)) {
					result = result + valueInEdge(edge);
				}
				/**
				consider = nameToBoolean.get(sourceName);
				if (consider == null || consider.booleanValue()) {
					//List<Long> lDataTraffic = edge.getDataTraffic();
					//result = result - maxDataTraffic(lDataTraffic);
					result = result + valueInEdge(edge);
				}
				**/
			}
		//}
		return result;
	}

	protected double computeOutbound(Vertex vertex, Map<String, Boolean> nameToBoolean) {
		double result = 0;
		Map<String, Edge> outbound = vertex.getOutbound();
		Set<String> keys = outbound.keySet();
		for (String key: keys) {
			Edge edge = outbound.get(key);
			String targetName = edge.getTarget().getName();
			if (toCompute(targetName, nameToBoolean)) {
				result = result + valueInEdge(edge);
			}
			/**
			Boolean consider = nameToBoolean.get(targetName);
			if (consider == null || consider.booleanValue()) {
				//List<Long> lDataTraffic = edge.getDataTraffic();
				//result = result + maxDataTraffic(lDataTraffic);
				result = result + valueInEdge(edge);
			}
			**/
		}
		return result;
	}
	
	protected Map<String, Boolean> getNameToBoolean(List<Vertex> verticiesTrue, List<Vertex> verticiesTrue2, 
			List<Vertex> verticiesFalse, List<Vertex> verticiesFalse2) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		nameToBoolean(verticiesTrue, result, true);
		nameToBoolean(verticiesTrue2, result, true);
		nameToBoolean(verticiesFalse, result, false);
		nameToBoolean(verticiesFalse2, result, false);
		return result;
	}

	protected void nameToBoolean(List<Vertex> verticiesTrue, Map<String, Boolean> result, boolean value) {
		if (verticiesTrue != null) {
			for (Vertex vertex : verticiesTrue) {
				result.put(vertex.getName(), new Boolean(value));
			}
		}
	}
	
	protected double metricPerVertex(Vertex vertex, List<Vertex> useToCalc, List<Vertex> useToCalc2, 
			List<Vertex> notUseToCalc, List<Vertex> notUseToCalc2) {
		return metricPerVertex(vertex, getNameToBoolean(useToCalc, useToCalc2, notUseToCalc, notUseToCalc2));
	}

	@Override
	public double getValue(List<Vertex> removedVerticies, List<Vertex> addedVerticies,
			List<Vertex> oldVerticiesInMicroservices, double oldMetricValue) {
		double addValue = 0l;
		double removeValue = 0l;
		double newValueMetric = 0l;
		if (removedVerticies != null) {
			for (Vertex vertex: removedVerticies) {
				removeValue += metricPerVertex(vertex, addedVerticies, null, 
						oldVerticiesInMicroservices, removedVerticies);
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
