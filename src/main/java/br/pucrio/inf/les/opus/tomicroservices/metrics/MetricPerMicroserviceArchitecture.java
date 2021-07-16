package br.pucrio.inf.les.opus.tomicroservices.metrics;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public interface MetricPerMicroserviceArchitecture extends Metric {

	public double getValue(MicroservicesSolution microservicesSolution);
	
	public double printableValue(MicroservicesSolution microservicesSolution);

	public double printableValue(double value);

	List<MetricPerMicroservice> getMetricPerMicroservice();
		
	public void setObjectiveIndex(int index);

	public int getObjectiveIndex();
	
}
