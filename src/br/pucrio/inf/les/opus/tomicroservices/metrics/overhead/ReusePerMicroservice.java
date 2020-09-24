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

public class ReusePerMicroservice implements MetricPerMicroservice  {

	private String userVertexName;
	private int threshold;
	
	public ReusePerMicroservice(String userVertexName, int threshold) {
		this.userVertexName = userVertexName;
		this.threshold = threshold;
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}
	
	private boolean isUserVertex(Vertex vertex) {
		return vertex.getName().equals(this.userVertexName);
	}
	
	private void nameToBoolean(List<Vertex> verticies, Map<String, Boolean> result, boolean value) {
		if (verticies != null) {
			for (Vertex vertex : verticies) {
				result.put(vertex.getName(), new Boolean(value));
			}
		}
	}
	
	private Map<String, Boolean> getNameToBoolean(List<Vertex> verticies) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		nameToBoolean(verticies, result, true);
		return result;
	}

	@Override
	public double getValue(Microservice microservice) {
		List<Vertex> verticies = microservice.getVerticies();
		return calcValue(verticies, getNameToBoolean(verticies));
	}

	private double calcValue(List<Vertex> verticies, Map<String, Boolean> nameToBoolean) {
		int result = 0;
		for (Vertex vertex : verticies) {
			if (!nameToBoolean.get(vertex.getName())) {
				continue;
			}
			if (isUserVertex(vertex)) {
				++result;
			} else {
				Map<String, Edge> inbound = vertex.getInbound();
				Set<String> keys = inbound.keySet();
				for (String key : keys) {
					Edge edge = inbound.get(key);
					String targetName = edge.getSource().getName();
					Boolean contains = nameToBoolean.get(targetName);
					if (contains == null || !contains.booleanValue()) {
						++result;
					}
				}
			}
			if (result > threshold) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public double getValue(List<Vertex> removedVerticies, List<Vertex> addedVerticies,
			List<Vertex> oldVerticiesInMicroservices, double oldMetricValue) {
		Map<String, Boolean> nameToBoolean = new HashMap<String, Boolean>();
		nameToBoolean(addedVerticies, nameToBoolean, true);
		nameToBoolean(oldVerticiesInMicroservices, nameToBoolean, true);
		nameToBoolean(removedVerticies, nameToBoolean, false);
		List<Vertex> verticies = new ArrayList<Vertex>();
		if (addedVerticies != null) {
			verticies.addAll(addedVerticies);
		} 
		if (oldVerticiesInMicroservices != null) {
			verticies.addAll(oldVerticiesInMicroservices);
		}
		return calcValue(verticies, nameToBoolean);
	}

}
