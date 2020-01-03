package br.pucrio.inf.les.opus.tomicroservices.analysis.dynamic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.graph.Graph;

class DynamicLogAnalyzerTest {

	@Test
	void testOnlyOneSF() {
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

}
