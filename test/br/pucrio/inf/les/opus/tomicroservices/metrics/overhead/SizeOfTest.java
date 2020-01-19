package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.pucrio.inf.les.opus.tomicroservices.metrics.SizeOf;


public class SizeOfTest {

	public SizeOf sizeof;

	@BeforeEach
	public void init() {
		this.sizeof = new SizeOf();
	}

	@Test
	public void testArrayChar() throws IllegalArgumentException, IllegalAccessException {
		char[] array = {'a', 'b', 'c', 'd'};
		assertEquals(64, this.sizeof.sizeOfArray(array));
	}
	
	@Test
	public void testArrayCharMultiDimensional() throws IllegalArgumentException, IllegalAccessException {
		char[][][] array = new char[1][2][20];
		int charSize = (int) this.sizeof.sizeOf('a');
		assertEquals((20*2*1)*charSize, this.sizeof.sizeOfArray(array));
	}
	
	@Test
	public void testArrayString() throws IllegalArgumentException, IllegalAccessException {
		String[] array = {"abc", "abc", "abc", "a"};
		int strASize = (int) this.sizeof.sizeOf("abc");
		assertEquals(array.length*strASize, this.sizeof.sizeOfArray(array));
	}

	@Test
	public void testMethod() throws IllegalArgumentException, IllegalAccessException {
		this.sizeof.sizeOf(new SizeOf());
	}
	
	@Test
	public void testSizeOfLog() throws IOException {
		File logFile = new File("_____logFromSizeOf__");
		logFile.deleteOnExit();
		this.sizeof.sizeOfLog("className", "methodName", 100, logFile);
		this.sizeof.sizeOfLog("className", "methodName", 200, logFile);
		this.sizeof.sizeOfLog("className", "methodName", 300, logFile);
		try (BufferedReader reader = new BufferedReader(new FileReader(logFile))){
			String line = reader.readLine();
			for (int i = 100; line != null; i += 100) {
				String result = "#metrics.overhead: Class: " + "className" + " | " +
				"Method: " + "methodName" + " | sizeOf: " + Integer.toString(i);
				assertEquals(result, line);
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			logFile.deleteOnExit();
			fail(e.getMessage());
		}
	}
	
}
