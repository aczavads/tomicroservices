package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;

public class NullMetric implements MetricPerMicroservice {

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(Microservice microservice) {
		return 0;
	}

	@Override
	public double getValue(List<Vertex> removedVerticies, List<Vertex> addedVerticies,
			List<Vertex> oldVerticiesInMicroservices, double oldMetricValue) {
		return 0;
	}
	
}
