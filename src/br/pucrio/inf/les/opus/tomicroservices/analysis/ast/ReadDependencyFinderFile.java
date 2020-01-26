package br.pucrio.inf.les.opus.tomicroservices.analysis.ast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;


/**
 * Enable in Dependency Finder:
 * Packages
 * classes
 * features
 * -->
 * @author luizmatheus
 *
 */
public class ReadDependencyFinderFile {
	
	public void insertInGraphFromFile(File xmlFile, Graph graph, ClassNamePattern pattern) {
		try (BufferedReader reader = new BufferedReader(new FileReader(xmlFile))) {
			String line = reader.readLine();
			long i = 0l;
			long vertexNumber = 0l;
			while (line != null) {
				if (isFeatureLine(line)) {
					line = reader.readLine();
					String name = getName(line);
					if (isField(name)) {
						line = reader.readLine();
						continue;
					}
					line = reader.readLine();
					while (isBound(line)) {
						String bound = getBound(line);
						try {
							if (isField(bound) || !pattern.isAcceptable(name) || !pattern.isAcceptable(bound)) {
								line = reader.readLine();
								continue;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						Vertex vertex1 = new Vertex(name);
						Vertex vertex2 = new Vertex(bound);
						Edge edge;
						if (isInbound(line)) {
							edge = new Edge(vertex2, vertex1, true, 0l, 0l);
						} else {
							edge = new Edge(vertex1, vertex2, true, 0l, 0l);
						}
						Vertex vertexResult1 = graph.insert(vertex1);
						Vertex vertexResult2 = graph.insert(vertex2);
						if (vertexResult1 == vertex1) {
							//System.out.println(vertex1.getName());
							System.out.println(++vertexNumber);
							System.out.println("+ " + vertexResult1);
						} else {
							System.out.println("! " + vertexResult1);
						}
						if (vertexResult2 == vertex2) {
							System.out.println("+ " + vertex2.getName());
							System.out.println(++vertexNumber);
						} else {
							System.out.println("! " + vertexResult2);
						}
						line = reader.readLine();
					}
				} else {
					line = reader.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("FINISHED");
	}

	private boolean isField(String name) {
		return !(name.contains("(") || name.contains(")"));
	}

	private String removeWhiteSpace(String line) {
		return line.replaceAll("\\s+","");
	}
	
	private boolean isFeatureLine(String line) {
		line = removeWhiteSpace(line);
		return line.startsWith("<feature");
	}
	
	private boolean isBound(String line) {
		line = removeWhiteSpace(line);
		return line.startsWith("<inbound") || line.startsWith("<outbound");
	}
	
	private boolean isInbound(String line) {
		line = removeWhiteSpace(line);
		return line.startsWith("<inbound");
	}
	
	private String getBound(String line) {
		line = removeWhiteSpace(line);
		return line.replaceAll("\\<.*?\\>", "");
	}
	
	private String getName(String line) {
		line = removeWhiteSpace(line);
		return line.replaceAll("\\<.*?\\>", "");
	}
	
}
