package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;

import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.MicroservicesSolution;

public class ReusePerMicroserviceArchitecture implements MetricPerMicroserviceArchitecture {

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(MicroservicesSolution microservicesSolution) {
		return 0;
	}

	@Override
	public double printableValue(MicroservicesSolution microservicesSolution) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double printableValue(double value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MetricPerMicroservice> getMetricPerMicroservice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObjectiveIndex(int index) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getObjectiveIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

}
