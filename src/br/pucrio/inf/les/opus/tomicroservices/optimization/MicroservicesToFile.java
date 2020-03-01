package br.pucrio.inf.les.opus.tomicroservices.optimization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;
import br.pucrio.inf.les.opus.tomicroservices.metrics.MetricPerMicroserviceArchitecture;

public class MicroservicesToFile {

	private File folder;
	
	public MicroservicesToFile(File folder) {
		this.folder = folder;
	}
	
	private void sort(Microservice microservice) {
		Collections.sort(microservice.getVerticies(), new Comparator<Vertex>() {
			@Override
			public int compare(Vertex o1, Vertex o2) {
				return o1.getFrozenMotive().compareTo(o2.getFrozenMotive());
			}
		} );
	}
	
	private void writeMicroservicesSolutionMetrics(MicroservicesSolution microserviceSolution, 
			List<MetricPerMicroserviceArchitecture> metrics, 
			File solutionFolder) throws IOException {
		File result = new File(solutionFolder, "metrics");
		for (MetricPerMicroserviceArchitecture metric : metrics) {
			FileUtils.write(result, metric.getName() + ": " + 
					metric.printableValue(metric.getValue(microserviceSolution)) + "\n", "UTF-8", true);
		}
	}
	
	public void writeAllRankingInAFile(List<MicroservicesSolution> solution,
			List<RankingSolution<List<MicroservicesSolution>>> ranking, String fileName) {
		try {
			for (RankingSolution<List<MicroservicesSolution>> r : ranking) {
				String fileNameRanking = fileName + r.getName();
				File file = new File(this.folder.getAbsoluteFile(), fileNameRanking);
				double[] metrics = r.getMetrics(solution);
				for (double metric : metrics) {
					FileUtils.write(file, metric + "\n", "UTF-8", true);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void writeAllMetricsInAFile(List<MicroservicesSolution> solution,
			List<MetricPerMicroserviceArchitecture> metrics, String fileName) {
		try {
			String content = "";
			File fileToSave = new File(this.folder, fileName);
			boolean firstLoop = true;
			for (MetricPerMicroserviceArchitecture metric : metrics) {
				if (firstLoop) {
					firstLoop = false;
				} else {
					content += " ";
				}
				content += metric.getName();
			}
			FileUtils.write(fileToSave, content + "\n", "UTF-8");
			for (MicroservicesSolution s : solution) {
				content = "";
				firstLoop = true;
				for (MetricPerMicroserviceArchitecture metric : metrics) {
					if (firstLoop) {
						firstLoop = false;
					} else {
						content += " ";
					}
					content += metric.getValue(s);
				}
				FileUtils.write(fileToSave, content + "\n" , "UTF-8", true);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void write(List<MicroservicesSolution> microservicesSolution, List<MetricPerMicroserviceArchitecture> metrics) {
		int solutionCount = 0;
		for (MicroservicesSolution solution : microservicesSolution) {
			++solutionCount;
			File solutionFolder = new File(this.folder, "solution" + solutionCount);
			try {
				writeMicroservice(solution.getMicroservices(), solutionFolder);
				writeMicroservicesSolutionMetrics(solution, metrics, solutionFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void printMotive(Vertex vertex, File microserviceFile, boolean start) throws IOException {
		String flag = "";
		if (start) {
			flag = "start#";
		} else {
			flag = "end#";
		}
		FileUtils.write(microserviceFile, flag + vertex.getFrozenMotive() + "\n", "UTF-8", true);
	}
	
	private void printVertex(Vertex vertex, File microserviceFile) throws IOException {
		String features = "";
		//vertex.getFuncitionalities().forEach(f -> features += " " + f);
		for (String func : vertex.getFuncitionalities()) {
			features += func + " ";
		}
		FileUtils.write(microserviceFile, vertex.getName() 
				+ "| " + features + " |"
				+ "\n", "UTF-8", true);
	}
	
	public void writeMicroserviceMetrics(Microservice microservice, File fileToSave) throws IOException {
		Map<String, Double> metrics = microservice.getMetricToValue();
		Set<String> keys = metrics.keySet();
		List<String> lKeys = new ArrayList<String>(keys); 
		Collections.sort(lKeys);
		FileUtils.write(fileToSave, "###\n", "UTF-8", true);
		for (String key : lKeys) {
			FileUtils.write(fileToSave, key + ": " + metrics.get(key) + "\n", "UTF-8", true);
		}
		FileUtils.write(fileToSave, "###\n", "UTF-8", true);
	}
	
	public void writeMicroservice(List<Microservice> lMicroservice, File solution) throws IOException {
		int microserviceCount = 0;
		for (Microservice microservice : lMicroservice) {
			++microserviceCount;
			File microserviceFile = new File(solution, "microservice" + microserviceCount);
			writeMicroserviceMetrics(microservice, microserviceFile);
			sort(microservice);
			List<Vertex> verticies = microservice.getVerticies();
			Vertex lastVertexFrozen = null;
			boolean isLastVertexFrozen = false;
			for (Vertex vertex : verticies) {
				if (lastVertexFrozen != null) {
					if (vertex.isFrozen()) {
						if (isLastVertexFrozen) {
							if (!vertex.getFrozenMotive().equals(lastVertexFrozen.getFrozenMotive())) {
								printMotive(lastVertexFrozen, microserviceFile, false);
								printMotive(vertex, microserviceFile, true);
							}
						} else {
							printMotive(vertex, microserviceFile, true);
						}
						lastVertexFrozen = vertex;
					} else {
						if (isLastVertexFrozen) {
							printMotive(lastVertexFrozen, microserviceFile, false);
						}
						isLastVertexFrozen = false;
					}
				} else {
					if (vertex.isFrozen()) {
						lastVertexFrozen = vertex;
						isLastVertexFrozen = true;
						printMotive(vertex, microserviceFile, true);
					} 
				}
				printVertex(vertex, microserviceFile);
			}
			if (isLastVertexFrozen) {
				printMotive(lastVertexFrozen, microserviceFile, false);
			}
		}
	}
	
}
