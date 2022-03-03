package br.pucrio.inf.les.opus.tomicroservices.optimization.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.uma.jmetal.solution.DoubleSolution;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;

public class MicroservicesSolution implements DoubleSolution {
	
	private List<Microservice> microservices;
	private Map<Integer, Double> objectives;
		
	public List<Microservice> getMicroservices() {
		return this.microservices;
	}
	
	public void setMicroservices(List<Microservice> microservices) {
		this.microservices = microservices;
	}
	
	private void commonConstructor(List<Microservice> microservices) {
		this.objectives = new HashMap<Integer, Double>();
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
		this.objectives.put(index, value);
		/**
		if (index > (this.objectives.size() - 1)) {
			this.objectives.put(index, value);
		} else {
			this.objectives.set(index, value);
		}
		**/
	}

	@Override
	public double getObjective(int index) {
		return this.objectives.get(index);
	}

	@Override
	public double[] getObjectives() {
		/**
		int size = this.objectives.size();
		double[] result = new double[size];
		int i = 0;
		for (Double objective : this.objectives) {
			result[i++] = objective;
		}
		return result;
		**/
		Set<Integer> keys = this.objectives.keySet();
		int size = 0;
		for (Integer key : keys) {
			if (key > size) {
				size = key;
			}
		}
		++size;
		if ((size) != keys.size()) {
			throw new RuntimeException("Empty index in objectives array");
		}
		double[] result = new double[size];
		for (Integer key : keys) {
			result[key] = this.objectives.get(key);
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
	
	public String print() {
		String result = "";
		int count = 0;
		for (Microservice m : this.microservices) {
			result += "microservice" + count + "\n";
			//result += m.print() + "\n";
			result += printMicroservice(m) + "\n";
			++count;
		}
		return result;
	}
	
	private String printMicroservice(Microservice m) {
		String result = "";
		for (Vertex vertex : m.getVerticies()) {
			String name = vertex.getName();
			List<String> funcs = vertex.getFuncitionalities();
			result += name + "!";
			for (String fun : funcs) {
				result += fun + " ";
			}
			result += "! \n";		
			
			for (Edge e : vertex.getOutbound().values()) {
				if (!m.getVerticies().contains(e.getTarget())) {
					result += "==> " + printMicroserviceNameOfVertex(e.getTarget()) + " "+e.getTarget().getName() + "\n";					
				}
			}
						
		}
		return result;
	}
	
	private String printMicroserviceNameOfVertex(Vertex vertex) {
		for (Microservice m : this.microservices) {
			if (m.getVerticies().contains(vertex)) {
				return "[ microservice" + this.microservices.indexOf(m) + "]";
			}
		}
		return "[" + "not found" + "]";
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
