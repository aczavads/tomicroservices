package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class SizePerMicroserviceArchitecture implements MetricPerMicroserviceArchitecture {

	private int objectiveIndexInProblem;
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(MicroservicesSolution microservicesSolution) {
		List<Microservice> lMicroservice = microservicesSolution.getMicroservices();
		int sizes[] = new int[lMicroservice.size()];
		int totalSize = 0;
		int count = 0;
		for (Microservice microservice : lMicroservice) {
			int size = microservice.getVerticies().size();
			sizes[count++] = size;
			totalSize += size;
		}
		final double thresholdHigh = 0.5;
		final double thresholdLow = 0.1;
		for (int i = 0; i < count; ++i) {
			double reason = ((double)sizes[i]) / ((double)totalSize);
			if (reason > thresholdHigh || reason < thresholdLow) {
				return 1.0;
			}
		}
		return 0.0;
	}

	@Override
	public double printableValue(MicroservicesSolution microservicesSolution) {
		return getValue(microservicesSolution);
	}

	@Override
	public double printableValue(double value) {
		return value;
	}

	@Override
	public List<MetricPerMicroservice> getMetricPerMicroservice() {
		return null;
	}

	@Override
	public void setObjectiveIndex(int index) {
		this.objectiveIndexInProblem = index;
	}

	@Override
	public int getObjectiveIndex() {
		return this.objectiveIndexInProblem;
	}

}
