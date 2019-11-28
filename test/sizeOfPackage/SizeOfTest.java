package sizeOfPackage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;


public class SizeOfTest {

	public sizeOfPackage.SizeOf sizeof;
	
	@Test
	public void sizeOfLogTestOneLog() throws FileNotFoundException, IOException {
		SizeOf.sizeOfLog = new File("___sizeOfLog");
		SizeOf.sizeOfLog.deleteOnExit();
		SizeOf.saveSizeOfLog("A", "a", 100L);
		String line = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(SizeOf.sizeOfLog))) {
			line = reader.readLine();
		} finally {
			SizeOf.sizeOfLog.deleteOnExit();
		}
		assertEquals("ClassFrom:A|MethodFrom:a|SizeOf:100", line);
	}
	
	@Test
	public void sizeOfLogTestTwoLog() throws FileNotFoundException, IOException {
		SizeOf.sizeOfLog = new File("___sizeOfLogTwo");
		SizeOf.sizeOfLog.deleteOnExit();
		SizeOf.saveSizeOfLog("A", "a", 100L);
		SizeOf.saveSizeOfLog("B", "b", 200L);
		try (BufferedReader reader = new BufferedReader(new FileReader(SizeOf.sizeOfLog))) {
			String line = reader.readLine();
			assertEquals("ClassFrom:A|MethodFrom:a|SizeOf:100", line);
			line = reader.readLine();
			assertEquals("ClassFrom:B|MethodFrom:b|SizeOf:200", line);
		} finally {
			SizeOf.sizeOfLog.deleteOnExit();
		}
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

//	@Test
	public void testMethod() throws IllegalArgumentException, IllegalAccessException {
		this.sizeof.sizeOf(new SizeOf());
	}
	
}
