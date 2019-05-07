package br.pucrio.inf.les.opus.tomicroservices.metrics.overhead;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SizeOfTest {

	public SizeOf sizeof;

	@Before
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

}
