package src.fitness;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic.DynamicLogAnalyzer;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.optimization.file.MicroservicesFromFile;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.Microservice;
import br.pucrio.inf.les.opus.tomicroservices.optimization.search.MicroservicesSolution;

class Features {

	//5 && 7 && 10
	@Test
	void featureTanglingAndScattering() {
		Graph graph = new Graph();		
		DynamicLogAnalyzer dynamic = new DynamicLogAnalyzer();
		String logDynamic = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log";
		File logDynamicFile = new File(logDynamic);
		String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/_feature.list";
		File featuresGeneralFile = new File(featuresGeneral);
		dynamic.analyze(logDynamicFile, graph, featuresGeneralFile);
		
		MicroservicesFromFile microservicesFromFile = new MicroservicesFromFile();
		List<MicroservicesSolution> solutions;
		Set<String> allFeatures;
		try {
			solutions = microservicesFromFile.read(new File("/tmp/microservice/"), graph);
			for (MicroservicesSolution solution : solutions) {
				List<Microservice> microservices = solution.getMicroservices();
				System.out.println("Solution with " + microservices.size() + " microservices");
				for (Microservice microservice : microservices) {
					System.out.println("A vertex: " + microservice.getVerticies().get(0));
					System.out.println("Tangling");
					computeFeatureTangling(microservice.getVerticies());
					System.out.println("Scattering");
					allFeatures = getAllFeatures(microservice.getVerticies());
					computeFeatureScattering(microservice.getVerticies(), allFeatures);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Solution to all verticies in the graph");
		System.out.println("Tangling");
		computeFeatureTangling(graph.getVerticies());
		System.out.println("Scattering");
		allFeatures = getAllFeatures(graph.getVerticies());
		computeFeatureScattering(graph.getVerticies(), allFeatures);
	}
	
	private Set<String> getAllFeatures(List<Vertex> verticies) {
		Set<String> features = new TreeSet<String>();
		for (Vertex vertex : verticies) {
			features.addAll(vertex.getFuncitionalities());
		}
		return features;
	}
	
	private void computeFeatureScatteringClassLevel(List<Vertex> verticies, Set<String> allFeatures) {
		System.out.println("Class level:");
		Map<String, Set<String>> classNameToFeatures = new HashMap<String, Set<String>>();
		for (Vertex vertex : verticies) {
			String methodName = vertex.getName();
			List<String> funcs = vertex.getFuncitionalities();
			Set<String> setFuncs = new HashSet<String>();
			setFuncs.addAll(funcs);
			int lastIndex = methodName.lastIndexOf(".");
			String className = methodName.substring(0, lastIndex);
			Set<String> currentFeatures = classNameToFeatures.get(className);
			if (currentFeatures != null) {
				setFuncs.addAll(currentFeatures);
			}
			classNameToFeatures.put(className, setFuncs);
		}
		Map<String, Integer> featureCount = new HashMap<String, Integer>();
		for (String feature : allFeatures) {
			featureCount.put(feature, 0);
		}
		Set<String> keys = classNameToFeatures.keySet();
		for (String key : keys) {
			Set<String> features = classNameToFeatures.get(key);
			for (String feature : features) {
				int count = featureCount.get(feature);
				featureCount.put(feature, count + 1);
			}
		}
		System.out.println(featureCount);
	}
	
	private void computeFeatureScattering(List<Vertex> verticies, Set<String> allFeatures) {
		Map<String, Integer> featureCount = new HashMap<String, Integer>();
		for (String feature : allFeatures) {
			featureCount.put(feature, 0);
		}
		System.out.println("Method level:");
		for (Vertex vertex : verticies) {
			List<String> features = vertex.getFuncitionalities();
			for (String feature : features) {
				int count = featureCount.get(feature);
				featureCount.put(feature, count + 1);
			}
		}
		System.out.println(featureCount);
		computeFeatureScatteringClassLevel(verticies, allFeatures);
	}
	
	private void computeFeatureTangling(List<Vertex> verticies) {
		Map<String, Set<String>> classNameToFeatures = new HashMap<String, Set<String>>();
		Map<String, Boolean> methodNameToFeatures = new HashMap<String, Boolean>();
		for (Vertex vertex : verticies) {
			String methodName = vertex.getName();
			List<String> funcs = vertex.getFuncitionalities();
			Set<String> setFuncs = new HashSet<String>();
			setFuncs.addAll(funcs);
			boolean flag = funcs.size() > 1;
			methodNameToFeatures.put(methodName, flag);
			int lastIndex = methodName.lastIndexOf(".");
			String className = methodName.substring(0, lastIndex);
			Set<String> currentFeatures = classNameToFeatures.get(className);
			if (currentFeatures != null) {
				setFuncs.addAll(currentFeatures);
			}
			classNameToFeatures.put(className, setFuncs);
		}
		Set<String> classNames = classNameToFeatures.keySet();
		int total = 0;
		int moreThenOneFeature = 0;
		for (String className : classNames) {
			Set<String> features = classNameToFeatures.get(className);
			++total;
			if (features.size() > 1) {
				++moreThenOneFeature;
			}
		}
		System.out.println("Total class: " + total);
		System.out.println("More then one feature: " + moreThenOneFeature);
		System.out.println("moreThenOneFeature/total: " + ((float)moreThenOneFeature/(float)total));
		
		int methodTotal = 0;
		int moreThenOneFeaturePerMethod = 0;
		Set<String> methodNames = methodNameToFeatures.keySet();
		for (String methodName : methodNames) {
			++methodTotal;
			if (methodNameToFeatures.get(methodName)) {
				++moreThenOneFeaturePerMethod;
			}
		}
		System.out.println("Total method: " + methodTotal);
		System.out.println("More then one feature: " + moreThenOneFeaturePerMethod);
		System.out.println("moreThenOneFeature/total: " + ((float)moreThenOneFeaturePerMethod/(float)methodTotal));
	}
	
	//Feature tangling to all
	//@Test
	void featureTanglingAll() {
		Graph graph = new Graph();		
		DynamicLogAnalyzer dynamic = new DynamicLogAnalyzer();
		String logDynamic = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/log";
		File logDynamicFile = new File(logDynamic);
		String featuresGeneral = "/home/luizmatheus/tecgraf/csgrid/csgrid-server/agent/feature.list";
		File featuresGeneralFile = new File(featuresGeneral);
		dynamic.analyze(logDynamicFile, graph, featuresGeneralFile);
		List<Vertex> verticies = graph.getVerticies();
		Map<String, Set<String>> classNameToFeatures = new HashMap<String, Set<String>>();
		Map<String, Boolean> methodNameToFeatures = new HashMap<String, Boolean>();
		for (Vertex vertex : verticies) {
			String methodName = vertex.getName();
			List<String> funcs = vertex.getFuncitionalities();
			Set<String> setFuncs = new HashSet<String>();
			setFuncs.addAll(funcs);
			boolean flag = funcs.size() > 1;
			methodNameToFeatures.put(methodName, flag);
			int lastIndex = methodName.lastIndexOf(".");
			String className = methodName.substring(0, lastIndex);
			Set<String> currentFeatures = classNameToFeatures.get(className);
			if (currentFeatures != null) {
				setFuncs.addAll(currentFeatures);
			}
			classNameToFeatures.put(className, setFuncs);
		}
		Set<String> classNames = classNameToFeatures.keySet();
		int total = 0;
		int moreThenOneFeature = 0;
		for (String className : classNames) {
			Set<String> features = classNameToFeatures.get(className);
			++total;
			if (features.size() > 1) {
				++moreThenOneFeature;
			}
		}
		System.out.println("Total class: " + total);
		System.out.println("More then one feature: " + moreThenOneFeature);
		System.out.println("moreThenOneFeature/total: " + ((float)moreThenOneFeature/(float)total));
		
		int methodTotal = 0;
		int moreThenOneFeaturePerMethod = 0;
		Set<String> methodNames = methodNameToFeatures.keySet();
		for (String methodName : methodNames) {
			++methodTotal;
			if (methodNameToFeatures.get(methodName)) {
				++moreThenOneFeaturePerMethod;
			}
		}
		System.out.println("Total method: " + methodTotal);
		System.out.println("More then one feature: " + moreThenOneFeaturePerMethod);
		System.out.println("moreThenOneFeature/total: " + ((float)moreThenOneFeaturePerMethod/(float)methodTotal));
	}

}
