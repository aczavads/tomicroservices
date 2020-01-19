package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;
//DoubleSolution
//public class MicroservicesSolution extends DefaultDoubleSolution {
public class MicroservicesSolution implements DoubleSolution {
	
	private List<Microservice> microservices;
	private List<Double> objectives;
	
	public List<Microservice> getMicroservices() {
		return this.microservices;
	}
	
	public void setMicroservices(List<Microservice> microservices) {
		this.microservices = microservices;
	}
	
	private void commonConstructor(List<Microservice> microservices) {
		this.objectives = new ArrayList<Double>();
		this.microservices = new ArrayList<Microservice>();
		for (Microservice microservice : microservices) {
			this.microservices.add(new Microservice(microservice));
		}	
	}
	
	public MicroservicesSolution(List<Microservice> microservices) {
		commonConstructor(microservices);
	}

	
	public MicroservicesSolution(MicroservicesSolution solution) {
		//super(solution);
		commonConstructor(solution.getMicroservices());
	}
	/**
	public MicroservicesSolution(DefaultDoubleSolution solution) {
		super(solution);
	}
	
	public MicroservicesSolution(DoubleProblem solution) {
		super(solution);
	}
	**/
	
	@Override
	public void setObjective(int index, double value) {
		if (index > (this.objectives.size() - 1)) {
			this.objectives.add(index, value);
		} else {
			this.objectives.set(index, value);
		}
	}

	@Override
	public double getObjective(int index) {
		return this.objectives.get(index);
	}

	@Override
	public double[] getObjectives() {
		int size = this.objectives.size();
		double[] result = new double[size];
		int i = 0;
		for (Double objective : this.objectives) {
			result[i++] = objective;
		}
		return result;
	}
	
	@Override
	public MicroservicesSolution copy() {
		MicroservicesSolution newSolution = new MicroservicesSolution(this);
		return newSolution;
	}

	@Override
	public Double getVariableValue(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Double> getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVariableValue(int index, Double value) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getVariableValueString(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfVariables() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfObjectives() {
		return this.objectives.size();
	}

	private Map<Object, Object> atribute = new HashMap<Object, Object>();
	
	@Override
	public void setAttribute(Object id, Object value) {
		this.atribute.put(id, value);
	}

	@Override
	public Object getAttribute(Object id) {
		return this.atribute.get(id);
	}

	@Override
	public Map<Object, Object> getAttributes() {
		return this.atribute;
	}

	@Override
	public Double getLowerBound(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getUpperBound(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		String result = "";
		int count = 0;
		for (Microservice m : this.microservices) {
			result = result + "m" + ++count + " : " + m.toString() + "\n";
		}
		result += "Size: " + this.microservices.size();
		return result;
	}
	
}
