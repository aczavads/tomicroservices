package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

public class FunctionalityPerMicroserviceArchitecture implements MetricPerMicroserviceArchitecture {

	private ConvertValue convertValue;
	
	private int objectiveIndexInProblem;
	
	public FunctionalityPerMicroserviceArchitecture() {}
	
	public FunctionalityPerMicroserviceArchitecture(ConvertValue convertValue) {
		this.convertValue = convertValue;
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(MicroservicesSolution microservicesSolution) {
		double result = 0.0;
		Set<String> functionalitiesPerMicroservice = new TreeSet<String>(); 
		List<Microservice> lMicroservices = microservicesSolution.getMicroservices();
		for (Microservice m: lMicroservices) {
			FunctionalityPerMicroservice metric = new FunctionalityPerMicroservice();
			m.addOrUpdateMetric(metric); //TODO - verificar desempenho!
			result += m.getMetricValue(FunctionalityPerMicroservice.class.getName());
			functionalitiesPerMicroservice.add(metric.getPredominantFunctionalityName());
		}
		double msa = lMicroservices.size();
		double fmsa = functionalitiesPerMicroservice.size();
		result = result + (fmsa/msa);
		System.out.println(result);
		return convert(result);
	}
	
	private double convert(double value) {
		if (this.convertValue != null) {
			return this.convertValue.convert(value);
		}
		return value;
	}

	@Override
	public double printableValue(MicroservicesSolution microservicesSolution) {
		return printableValue(getValue(microservicesSolution));
	}

	@Override
	public double printableValue(double value) {
		return convert(value);
	}

	@Override
	public List<MetricPerMicroservice> getMetricPerMicroservice() {
		// TODO Auto-generated method stub
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
