package br.pucrio.inf.les.opus.tomicroservices.graph;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import org.jgrapht.Graph;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;

import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.MethodEdgeValue;
import br.pucrio.inf.les.opus.tomicroservices.metrics.overhead.MethodNode;

public class GenerateDOT {
	
	class EdgeName implements ComponentNameProvider {
		@Override
		public String getName(Object component) {
			MethodEdgeValue edge = (MethodEdgeValue) component;
			return edge.toString();
		}
	}

	class VertexName implements ComponentNameProvider {
		@Override
		public String getName(Object component) {
			MethodNode node = (MethodNode) component;
			return node.toString();
		}
	}

	class VertexId implements ComponentNameProvider {
		@Override
		public String getName(Object component) {
			MethodNode node = (MethodNode) component;
			return node.getClassName() + node.getMethodName();
		}
	}
	
	public String getDOT(Graph<MethodNode, MethodEdgeValue> graph) {
		DOTExporter<MethodNode, MethodEdgeValue> exporter = 
				new DOTExporter<>(new VertexId(), new VertexName(), new EdgeName());
		Writer writer = new StringWriter();
		exporter.exportGraph(graph, writer);
		return writer.toString();
	}
	
}
