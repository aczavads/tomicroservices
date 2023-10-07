package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import br.pucrio.inf.les.opus.tomicroservices.metrics.ConvertValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroservice;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;

public class FunctionalityPerMicroserviceArchitectureV2 implements MetricPerMicroserviceArchitecture {

	private ConvertValue convertValue;
	
	private int objectiveIndexInProblem;
	
	public FunctionalityPerMicroserviceArchitectureV2() {}
	
	public FunctionalityPerMicroserviceArchitectureV2(ConvertValue convertValue) {
		this.convertValue = convertValue;
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public double getValue(MicroservicesSolution microservicesSolution) {
		//System.out.println(microservicesSolution.toString());
		double result = 0.0;
		int quantidadeDeMétodos = microservicesSolution.getMicroservices().stream().map(m -> m.getVerticies().size()).reduce(0, (v1,v2) -> v1+v2);
    	int linhas = microservicesSolution.getMicroservices().stream().flatMap(m -> m.getVerticies().stream().map(v -> v.getFuncitionalities())).collect(Collectors.toSet()).size();
		int colunas = microservicesSolution.getMicroservices().size();
		int[][] distribuiçãoFuncionalidadesPorMS = new int[linhas][colunas];
				
		List funcionalidades = new ArrayList<>(microservicesSolution.getMicroservices().stream().flatMap(m -> m.getVerticies().stream().map(v -> v.getFuncitionalities())).collect(Collectors.toSet()));

		for (int m = 0; m < microservicesSolution.getMicroservices().size(); m++) {
			//FunctionalityPerMicroservice metric = new FunctionalityPerMicroservice();
			MetricPerMicroservice metric = new NullMetric();
			microservicesSolution.getMicroservices().get(m).addOrUpdateMetric(metric); //TODO - verificar desempenho!

			for (Vertex v: microservicesSolution.getMicroservices().get(m).getVerticies()) {
				for (String f : v.getFuncitionalities()) {
		    		distribuiçãoFuncionalidadesPorMS[funcionalidades.indexOf(v.getFuncitionalities())][m]++;
				}
			}			
		}
		
    	result = calcularAptidãoPelaDistribuiçãoDeFuncionalidades(linhas, colunas, distribuiçãoFuncionalidadesPorMS);
    	result -= descontarAptidãoPorMicroserviçosSemFuncionalidades(linhas, colunas, distribuiçãoFuncionalidadesPorMS, quantidadeDeMétodos, colunas);
		System.out.println(microservicesSolution.hashCode() +  "==>"+ result + ", convert(result) ==>  " + convert(result));
//    	System.out.println(microservicesSolution.toString());
		return convert(result);
	}

	private int descontarAptidãoPorMicroserviçosSemFuncionalidades(int linhas, int colunas,
			int[][] distribuiçãoFuncionalidadesPorMS, int contagemDeMétodosDoMonolito, int quantidadeDeMicroserviços  ) {
		int quantidadeMsSemFuncionalidades = 0;
		for (int coluna = 0; coluna < colunas; coluna++) {
			int quantidadeFuncionalidades = 0;
			for (int linha = 0; linha < linhas; linha++) {
				quantidadeFuncionalidades += distribuiçãoFuncionalidadesPorMS[linha][coluna];
			}
			if (quantidadeFuncionalidades == 0) {
				quantidadeMsSemFuncionalidades++;
			}
		}
		int médiaDeMétodosEsperadaPorMS = contagemDeMétodosDoMonolito / quantidadeDeMicroserviços;
		return médiaDeMétodosEsperadaPorMS * quantidadeMsSemFuncionalidades;
	}

	private int calcularAptidãoPelaDistribuiçãoDeFuncionalidades(int linhas, int colunas,
			int[][] distribuiçãoFuncionalidadesPorMS) {
		int aptidao = 0;
    	for (int i = 0; i < linhas; i++) {
    		//System.out.print("Funcionalidade: " +  i + "  ");
        	int maiorDistribuiçãoPorFuncionalidade = 0;
    		for (int j = 0; j < colunas; j++) {
    			if (distribuiçãoFuncionalidadesPorMS[i][j] > maiorDistribuiçãoPorFuncionalidade) {
    				maiorDistribuiçãoPorFuncionalidade = distribuiçãoFuncionalidadesPorMS[i][j]; 
    			}
    			//System.out.print(distribuiçãoFuncionalidadesPorMS[i][j] + " "); 				
			}
    		aptidao += maiorDistribuiçãoPorFuncionalidade;
		}
    	return aptidao;
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
