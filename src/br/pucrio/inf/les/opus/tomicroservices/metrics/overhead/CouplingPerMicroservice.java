package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.Map;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;

public class CouplingPerMicroservice extends RelationPerMicroservice implements MetricPerMicroservice {

	@Override
	public String getName() {
		return this.getClass().getName();
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
	
	@Override
	protected boolean toCompute(String vertexName, Map<String, Boolean> nameToBoolean) {
		Boolean consider = nameToBoolean.get(vertexName);
		return (consider == null || consider.booleanValue());
	}

}
