package br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Edge;
import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;
import br.pucrio.inf.les.opus.tomicroservices.graph.Vertex;

class DynamicLogAnalyzerTest {

	//@Test
	void testOneSF() {
		DynamicLogAnalyzer logAnalyzer = new DynamicLogAnalyzer();
		File log = new File("_______logAnalyzer_______");
		String data = "SF:A#B#C#D\n" + 
					"Class:ibase.rest.api.authentication.v1.AuthenticationApi#Method:authenticationPost#SizeOf:304#Deep:1\n" + 
					"Class:ibase.common.ServiceUtil#Method:getLocale#SizeOf:0#Deep:2\n";
		try {
			FileUtils.write(log, data, "UTF-8");
			Graph graph = new Graph();
			logAnalyzer.analyze(log, graph, null);
			assertTrue(graph.containsVertexByName("ibase.rest.api.authentication.v1.AuthenticationApi.authenticationPost"));
			assertTrue(graph.containsVertexByName("ibase.common.ServiceUtil.getLocale"));
			assertFalse(graph.containsVertexByName("notFound"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testTwoSFSameEdge() {
		DynamicLogAnalyzer logAnalyzer = new DynamicLogAnalyzer();
		File log = new File("_______logAnalyzer_______");
		String data = "SF:A#B#C#D\n" + 
					"Class:ibase.rest.api.authentication.v1.AuthenticationApi#Method:authenticationPost#SizeOf:304#Deep:1\n" + 
					"Class:ibase.common.ServiceUtil#Method:getLocale#SizeOf:20#Deep:2\n" +
					"SF:X#Y#Z\n" +
					"Class:ibase.rest.api.authentication.v1.AuthenticationApi#Method:authenticationPost#SizeOf:304#Deep:1\n" +
					"Class:ibase.common.ServiceUtil#Method:getLocale#SizeOf:10#Deep:2\n";
		try {
			FileUtils.write(log, data, "UTF-8");
			Graph graph = new Graph();
			logAnalyzer.analyze(log, graph, null);
			Vertex sourceVertex = graph.getVertex("ibase.rest.api.authentication.v1.AuthenticationApi.authenticationPost");
			Edge edge = sourceVertex.getOutbound().get("ibase.common.ServiceUtil.getLocale");
			assertNotNull(edge);
			List<Long> lDataTraffic = edge.getDataTraffic();
			assertEquals(2, lDataTraffic.size());
			assertEquals(20, lDataTraffic.get(0));
			assertEquals(10, lDataTraffic.get(1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	void testWithFunctionalitiesInGeneralFile() {
		DynamicLogAnalyzer logAnalyzer = new DynamicLogAnalyzer();
		File log = new File("_______logAnalyzer_______");
		File generalFuncs = new File("_______generalFunctionalities_______");
		String dataGeneralFuncs = "SF:FuncA<A.a>#FuncB<B.b>#Y";
							//"FunB<B.b>\n" + 
							//"Y";
		String dataLog = "SF:X\n" + 
					"Class:A#Method:a#SizeOf:304#Deep:1\n" +
					"Class:A#Method:e#SizeOf:20#Deep:2\n";
		try {
			FileUtils.write(log, dataLog, "UTF-8");
			FileUtils.write(generalFuncs, dataGeneralFuncs, "UTF-8");
			Graph graph = new Graph();
			logAnalyzer.analyze(log, graph, generalFuncs);
			Vertex sourceVertex = graph.getVertex("A.a");
			assertEquals("A.a", sourceVertex.getName());
			List<String> funcs = sourceVertex.getFuncitionalities();
			assertTrue(funcs.contains("FuncA"));
			assertFalse(funcs.contains("X"));
			assertFalse(funcs.contains("FuncB"));
			Edge edge = sourceVertex.getOutbound().get("A.e");
			assertNotNull(edge);
			List<Long> lDataTraffic = edge.getDataTraffic();
			assertEquals(20, lDataTraffic.get(0));
			Vertex targetVertex = graph.getVertex("A.a");
			funcs = targetVertex.getFuncitionalities();
			assertTrue(funcs.contains("FuncA"));
			assertFalse(funcs.contains("X"));
			assertFalse(funcs.contains("FuncB"));
			targetVertex = graph.getVertex("A.e");
			funcs = targetVertex.getFuncitionalities();
			assertTrue(funcs.contains("FuncA"));
			assertFalse(funcs.contains("X"));
			assertFalse(funcs.contains("FuncB"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void verifyFuncs(List<String> resultFuncs, List<String> expectedFuncs, 
			List<String> notExpectedFuncs) {
		assertTrue(resultFuncs.size() == expectedFuncs.size());
		for (String expectedFunc : expectedFuncs) {
			assertTrue(resultFuncs.contains(expectedFunc));
		}
		for (String notExpectedFunc : notExpectedFuncs) {
			assertFalse(resultFuncs.contains(notExpectedFunc));
		}
		expectedFuncs.clear();
		notExpectedFuncs.clear();
	}
	
	//@Test
	public void testWithTwoEntryPoints() {
		DynamicLogAnalyzer logAnalyzer = new DynamicLogAnalyzer();
		File log = new File("_______logAnalyzer_______");
		File generalFuncs = new File("_______generalFunctionalities_______");
		String dataGeneralFuncs = "SF:FuncA<A.a>#FuncB<B.b>#Y";
							//"FunB<B.b>\n" + 
							//"Y";
		String dataLog = "SF:X\n" + 
					"Class:A#Method:a#SizeOf:304#Deep:1\n" +
					"Class:A#Method:e#SizeOf:20#Deep:2\n" + 
					"Class:B#Method:b#SizeOf:20#Deep:3\n" + 
					"Class:B#Method:e#SizeOf:20#Deep:4\n" + 
					"Class:A#Method:f#SizeOf:20#Deep:2\n";
		try {
			FileUtils.write(log, dataLog, "UTF-8");
			FileUtils.write(generalFuncs, dataGeneralFuncs, "UTF-8");
			Graph graph = new Graph();
			logAnalyzer.analyze(log, graph, generalFuncs);
			Vertex sourceVertex = graph.getVertex("A.a");
			assertEquals("A.a", sourceVertex.getName());
			List<String> funcs = sourceVertex.getFuncitionalities();
			assertTrue(funcs.contains("FuncA"));
			assertFalse(funcs.contains("X"));
			assertFalse(funcs.contains("FuncB"));
			Edge edge = sourceVertex.getOutbound().get("A.e");
			assertNotNull(edge);
			List<Long> lDataTraffic = edge.getDataTraffic();
			assertEquals(20, lDataTraffic.get(0));
			
			
			Vertex targetVertex = graph.getVertex("A.a");
			funcs = targetVertex.getFuncitionalities();
			List<String> expectedFuncs = new ArrayList<String>();
			List<String> notExpectedFuncs = new ArrayList<String>();
			expectedFuncs.add("FuncA");
			notExpectedFuncs.add("X");
			notExpectedFuncs.add("FuncB");
			verifyFuncs(funcs, expectedFuncs, notExpectedFuncs);
			
			targetVertex = graph.getVertex("B.b");
			funcs = targetVertex.getFuncitionalities();
			expectedFuncs.add("FuncB");
			notExpectedFuncs.add("X");
			notExpectedFuncs.add("FuncA");
			verifyFuncs(funcs, expectedFuncs, notExpectedFuncs);
			
			targetVertex = graph.getVertex("B.e");
			funcs = targetVertex.getFuncitionalities();
			expectedFuncs.add("FuncB");
			notExpectedFuncs.add("X");
			notExpectedFuncs.add("FuncA");
			verifyFuncs(funcs, expectedFuncs, notExpectedFuncs);
			
			targetVertex = graph.getVertex("A.f");
			funcs = targetVertex.getFuncitionalities();
			expectedFuncs.add("FuncA");
			notExpectedFuncs.add("X");
			notExpectedFuncs.add("FuncB");
			verifyFuncs(funcs, expectedFuncs, notExpectedFuncs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
