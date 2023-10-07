package br.pucrio.inf.les.opus.tomicroservices.metrics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.openjdk.jol.info.ClassLayout;

/**
 * Calculate the size of an object, array, or primitive type
 * @author Luiz Carvalho <lmcarvalho@inf.puc-rio.br>
 *
 */
public class SizeOf {
	
	/**
	 * Receive size (in bytes) and convert to megabyte
	 * @param bytes bytes
	 * @return megabyte
	 */
	public double convertBytesToMegaByte(long bytes) {
		return ( (double)bytes ) / ( (double)1000000 );
	}
	
	/**
	 * Calculate an array size
	 * @param o array
	 * @return size in bytes
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public long sizeOfArray(Object o) throws IllegalArgumentException, IllegalAccessException {
		Class<?> _class = o.getClass();
		Class<?> componentType = _class.getComponentType();
		
		int length = Array.getLength(o);
		if (length == 0) {
			return ClassLayout.parseInstance(o).instanceSize();
		}
	
		long size = length;
		final int firstElementIndex = 0;
		Object firstElement = null;
		while (componentType.isArray()) {
			firstElement = Array.get(o, firstElementIndex);
			size *= Array.getLength(firstElement);
			o = firstElement;
			componentType = componentType.getComponentType();
		}
		firstElement = Array.get(o, firstElementIndex);
		long elementSize = 0;
		if (isPrimitive(firstElement)) {
			elementSize = ClassLayout.parseInstance(firstElement).instanceSize();
		} else {
			elementSize = sizeOf(firstElement);
		}
		return size * elementSize;
	}
	
	/**
	 * Calculate an primitive size
	 * @param o e.g: Character or Number
	 * @return size
	 */
	private long sizeOfPrimtive(Object o) {
		long sum = 0;
		if (o instanceof Character) {
			Character c = (Character) o;
			sum = ClassLayout.parseInstance(c.charValue()).instanceSize();
		} else if (o instanceof Byte) {
			Byte n = (Byte) o;
			sum = ClassLayout.parseInstance(n.byteValue()).instanceSize();
		} else if (o instanceof Short) {
			Short n = (Short) o;
			sum = ClassLayout.parseInstance(n.shortValue()).instanceSize();
		} else if (o instanceof Integer) {
			Integer n = (Integer) o;
			sum = ClassLayout.parseInstance(n.intValue()).instanceSize();
		} else if (o instanceof Long) {
			Long n = (Long) o;
			sum = ClassLayout.parseInstance(n.longValue()).instanceSize();
		} else if (o instanceof Float) {
			Float n = (Float) o;
			sum = ClassLayout.parseInstance(n.floatValue()).instanceSize();
		} else if (o instanceof Double) {
			Double n = (Double) o;
			sum = ClassLayout.parseInstance(n.doubleValue()).instanceSize();
		} else {
			System.err.println(o + " is not a primitive type");
		}
		return sum;
	}
	
	/**
	 * Verify a primitive type
	 * @param o object
	 * @return whether o is primitive type return true. Otherwise, return false.
	 */
	private boolean isPrimitive(Object o) {
		return o instanceof Character || o instanceof Number ||
				o instanceof Boolean;
	}
	
	/**
	 * Calculate an object size
	 * @param o object
	 * @return size in bytes
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public long sizeOf(Object o) throws IllegalArgumentException, IllegalAccessException {
		long sum = 0;
		Class<?> _class = o.getClass();
		if (isPrimitive(o)) {
			sum = sizeOfPrimtive(o);
		} else if (_class.isArray()) {
			sum = sizeOfArray(o);
		} else {
			Field[] fields = _class.getDeclaredFields();
			Method[] methods = _class.getMethods();
			for (Method m: methods) {
				System.out.println(m.toString());
			}
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(o); 
				if (value != null) {
					if (field.getType().isPrimitive()) {
						sum += ClassLayout.parseInstance(value).instanceSize();
					} else {
						sum += sizeOf(value);
					}
				}
			}
		}
		return sum;
	}
	
	/**
	 * Print in file param the log with size of
	 * @param className class name under analyses
	 * @param methodName method name under analyses
	 * @param size sizeOf
	 * @param file path to save log
	 */
	public void sizeOfLog(String className, String methodName, long size, 
			File file) {
		String content = "#metrics.overhead: Class: " + className + " | " +
				"Method: " + methodName + " | sizeOf: " + size;
		BufferedWriter writer;
		try {
			writer = new java.io.BufferedWriter(new FileWriter(file, true));
			writer.write(content);
			writer.newLine();
			writer.close();
		} catch (java.io.IOException e) {
			System.out.println("Problem with sizeOfLog in metrics.overhead module: " + e.getMessage());
		}
	}
	
}
