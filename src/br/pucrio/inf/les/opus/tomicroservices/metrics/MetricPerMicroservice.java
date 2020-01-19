package br.pucrio.inf.les.opus.tomicroservices.metrics;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.optimization.Microservice;

public interface MetricPerMicroservice extends Metric {
	
	public double getValue(Microservice microservice);	
	
	public double getValue(List<Vertex> removedVerticies, List<Vertex> addedVerticies, 
			List<Vertex> oldVerticiesInMicroservices, double oldMetricValue);

}
