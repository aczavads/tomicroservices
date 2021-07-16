package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;

public class CohesionPerMicroservice extends RelationPerMicroservice implements MetricPerMicroservice  {

	@Override
	public String getName() {
		return this.getClass().getName();
	}
	
	@Override
	public double getValue(Microservice microservice) {
		int size = microservice.getVerticies().size();
		if (size == 1) {
			return 0;
		}
		double result = super.getValue(microservice);
		result = (2 * result) / (size * (size - 1));
		return result;
	}
	
	@Override
	protected Map<String, Boolean> getNameToBoolean(List<Vertex> verticies) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		for (Vertex vertex : verticies) {
			result.put(vertex.getName(), new Boolean(true));
		}
		return result;
	}
	
	@Override
	protected double metricPerVertex(Vertex vertex, Map<String, Boolean> nameToBoolean) {
		return super.computeOutbound(vertex, nameToBoolean);
	}
	
	@Override
	protected Map<String, Boolean> getNameToBoolean(List<Vertex> verticiesTrue, List<Vertex> verticiesTrue2, 
			List<Vertex> verticiesFalse, List<Vertex> verticiesFalse2) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		super.nameToBoolean(verticiesTrue, result, true);
		super.nameToBoolean(verticiesTrue2, result, true);
		super.nameToBoolean(verticiesFalse, result, true);
		super.nameToBoolean(verticiesFalse2, result, false);
		return result;
	}
	
	private double metricPerVertexToAddAndRemove(Vertex vertex, Map<String, Boolean> nameToBoolean) {
		return super.computeOutbound(vertex, nameToBoolean) + super.computeInbound(vertex, nameToBoolean);
	}
	
	@Override
	protected double metricPerVertex(Vertex vertex, List<Vertex> useToCalc, List<Vertex> useToCalc2, 
			List<Vertex> notUseToCalc, List<Vertex> notUseToCalc2) {
		return metricPerVertexToAddAndRemove(vertex, getNameToBoolean(useToCalc, useToCalc2, notUseToCalc, notUseToCalc2));
	}
	
	private double calcCohesion(double inValue, double size) {
		return (2.0 * inValue) / (size * (size - 1));
	}
	
	@Override
	public double getValue(List<Vertex> removedVerticies, List<Vertex> addedVerticies,
			List<Vertex> oldVerticiesInMicroservices, double oldMetricValue) {
		double addValue = 0l;
		double removeValue = 0l;
		double newValueMetric = 0l;
		double addedVerticiesSize = 0.0;
		double removededVerticiesSize = 0.0;
		if (removedVerticies != null) {
			removededVerticiesSize = removedVerticies.size();
			for (Vertex vertex: removedVerticies) {
				removeValue += metricPerVertex(vertex, addedVerticies, null, 
						oldVerticiesInMicroservices, removedVerticies);
			}
		}
		if (addedVerticies != null) {
			addedVerticiesSize = addedVerticies.size();
			for (Vertex vertex: addedVerticies) {
				addValue += metricPerVertex(vertex, oldVerticiesInMicroservices, addedVerticies, 
						removedVerticies, null);
			}
		}
		double size = oldVerticiesInMicroservices.size() + addedVerticiesSize - removededVerticiesSize;
		newValueMetric = oldMetricValue + calcCohesion(addValue, size) - calcCohesion(removeValue, size);
		return newValueMetric;
	}
	
	@Override
	protected boolean toCompute(String vertexName, Map<String, Boolean> nameToBoolean) {
		Boolean consider = nameToBoolean.get(vertexName);
		return (consider != null && consider.booleanValue());
	}
	
	@Override
	protected double valueInEdge(Edge edge) {
		boolean exists = edge.getStaticCall();
		if (exists) {
			return 1;
		} else {
			return 0;
		}
	}
}
