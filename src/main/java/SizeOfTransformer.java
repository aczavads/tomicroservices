import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.MethodParametersAttribute;

/**
 * 
 *  Insert SizeOf in system under analyze
 *  
 *  Warning: SizeOf class in sizeOfPackage, so change SizeOfTransformer in case renaming sizeOfPackage or class name.
 *  
 *  @author Luiz Carvalho <lmcarvalho@inf.puc-rio.br>
 *  
 * Based on: http://appcrawler.com/wordpress/2013/01/02/simple-byte-code-injection-example-with-javassist/
 * https://stackoverflow.com/questions/20316965/get-a-name-of-a-method-parameter-using-javassist
 * https://stackoverflow.com/questions/43027872/how-to-print-out-all-methods-called-during-runtime-in-java-using-instrumentation
 * http://www.javassist.org/tutorial/tutorial2.html (Add method and use variable)
 * https://www.programcreek.com/java-api-examples/?api=javassist.bytecode.LocalVariableAttribute
*/
public class SizeOfTransformer implements ClassFileTransformer {
	
	private ClassNamePattern pattern;
	
	public SizeOfTransformer(ClassNamePattern pattern, File logFile, File featureFile) {
		super();
		this.pattern = pattern;
		sizeOfPackage.SizeOf.sizeOfLog = logFile;		
		sizeOfPackage.SizeOf.featureInfo = featureFile;
	}
	
	/**
	 * Get all methods. Including private, protected and public.
	 */
	private CtBehavior[] getMethods(ClassPool pool, CtClass cl) {
		String oldName = cl.getName();
		String newName = "com.xx.yy" + "." + oldName;
		cl.replaceClassName(oldName, newName);
		try {
			Class _class = cl.toClass();
			Method[] methods = _class.getDeclaredMethods();
			System.out.println(_class.getName());
			System.out.println(methods.length);
			CtMethod[] ctMethod = new CtMethod[methods.length];
			cl.defrost();
			cl.replaceClassName(newName, oldName);
			cl.defrost();
			int i = 0;
			System.out.println(methods.length);
			Set<String> names = new TreeSet<String>();
			for (Method m : methods) {
				String name = m.getName();
				if (names.contains(name)) {
					System.out.println("Continue");
					continue;
				}
				System.out.println(name);
				try {
					cl.getDeclaredBehaviors();
					
					CtMethod[] ctMethods = cl.getDeclaredMethods();
				} catch (Exception e) {
					e.printStackTrace();
				}
				/**
				try {
					System.out.println("Something");
					CtMethod[] ctMethods = cl.getDeclaredMethods(name);
					System.out.println("Declared Methods");
					for (CtMethod cm : ctMethods) {
						System.out.println(i);
						ctMethod[i] = cm;
						ctMethod[i].setModifiers(Modifier.PUBLIC);
						++i;
					}
					names.add(name);
				} catch (Exception e) {
					System.out.println("Exception");
					e.printStackTrace();
				}
				**/
				System.out.println(methods.length);
			}
			_class = null;
			//cl.defrost();
			//cl.replaceClassName(newName, oldName);
			//cl.defrost();
			System.out.println("EXIT");
			return ctMethod;
		} catch (CannotCompileException e) {
			e.printStackTrace();
			cl.replaceClassName(newName, oldName);
		}
		cl.replaceClassName(newName, oldName);
		return null;
	}
	
	public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain,
			byte[] bytes) throws IllegalClassFormatException {
		//ClassPool pool = ClassPool.getDefault();
		ClassPool pool = new ClassPool();
		pool.appendClassPath(new LoaderClassPath(loader));
		pool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
		try {
			pool.appendClassPath("/home/luizmatheus/tecgraf/someJars/*");
		} catch (NotFoundException e1) {
			System.err.println("Can not include external jars.");
			e1.printStackTrace();
		}
		CtClass cl = null;
		System.out.println("ClassLoader: " + loader);
		System.out.println(Thread.currentThread().getContextClassLoader());
		System.out.println("ClassName: " + className);
		try {
			cl = pool.makeClass(new java.io.ByteArrayInputStream(bytes));
			cl.defrost();
			String clName = cl.getName();
			if (pattern.isAcceptable(clName) && !cl.isInterface()) {
				System.out.println("Inject in " + clName.toString());
				//CtBehavior[] methods = cl.getDeclaredBehaviors(); //TODO - verficar declared ou apenas methods
				//CtBehavior[] methods = getMethods(pool, cl);
				CtBehavior[] methods = cl.getDeclaredMethods(); //TODO - verficar declared ou apenas methods
				addSizeOfMethods(cl);	
				for (int i = 0; i < methods.length; i++) {
					//System.out.println(methods[i].getName());
					if (methods[i] == null) {
						continue;
					} else if (methods[i].isEmpty() == false) {
			        	changeMethod(methods[i], clName);
			        }
			    }
			}
		    bytes = cl.toBytecode();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cl != null) {
				cl.detach();
		    }
		}
		return bytes;
	}

	private void addSizeOfMethod(String method, CtClass cl) throws CannotCompileException {
		CtMethod m;
		m = CtNewMethod.make(method, cl);
		cl.addMethod(m);
	}
	
	/**
	 * Add method in original class to call sizeOf
	 * @param ctClass cl class to be transformed
	 */	
	private void addSizeOfMethods(CtClass cl) {
		CtMethod m;
		try {
			String str = "";
			addSizeOfMethod("public static long ____sizeOf(Object o) {return sizeOfPackage.SizeOf.sizeOf(o);}", 
					cl);			
			addSizeOfMethod("public static long ____sizeOf(int pt) {return sizeOfPackage.SizeOf.sizeOf(pt);}",
					cl);
			addSizeOfMethod("public static long ____sizeOf(char pt) {return sizeOfPackage.SizeOf.sizeOf(pt);}",
					cl);
			addSizeOfMethod("public static long ____sizeOf(byte pt) {return sizeOfPackage.SizeOf.sizeOf(pt);}",
					cl);
			addSizeOfMethod("public static long ____sizeOf(short pt) {return sizeOfPackage.SizeOf.sizeOf(pt);}",
					cl);
			addSizeOfMethod("public static long ____sizeOf(long pt) {return sizeOfPackage.SizeOf.sizeOf(pt);}",
					cl);
			addSizeOfMethod("public static long ____sizeOf(float pt) {return sizeOfPackage.SizeOf.sizeOf(pt);}",
					cl);
			addSizeOfMethod("public static long ____sizeOf(double pt) {return sizeOfPackage.SizeOf.sizeOf(pt);}",
					cl);
			addSizeOfMethod("public static long ____sizeOf(boolean pt) {return sizeOfPackage.SizeOf.sizeOf(pt);}",
					cl);
		} catch (CannotCompileException e) {
			System.err.println("Can not compile sizeOf method - maybe you forgot to add sizeOf in classpath");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Change method to insert a sizeOf call in start
	 * @param method method to change
	 */
	private void changeMethod(CtBehavior method, String className) {
		if (!Modifier.isNative(method.getModifiers())) { 
			List<String> methodParameters = methodParameterName(method);
			if (methodParameters.isEmpty()) { return; }
			try {
				String decreaseDeep = "sizeOfPackage.SizeOf.decreaseDeep();";
				method.insertAfter(decreaseDeep, false);
				//String sizeOfCall = "System.out.println(";
				final String quote = "\"";
				String sizeOfCall = "sizeOfPackage.SizeOf.saveSizeOfLog(";
				sizeOfCall += quote + className + quote + "," + quote + method.getName() + quote 
						+ ",";
				boolean first = true;
				for (String parameterName: methodParameters) {
					if (first) {
						first = false;
					} else {
						sizeOfCall += " + ";
					}
					sizeOfCall += "____sizeOf(" + parameterName + ")";
					String printParameterName = "System.out.println(" + parameterName + ");";
					method.insertBefore(printParameterName);
					String sizePerParameter = "System.out.println("+ "____sizeOf(" + parameterName + ")" + ");";
					//System.out.println(sizePerParameter);
					method.insertBefore(sizePerParameter);
				}
				sizeOfCall += ");";
				//System.out.println(sizeOfCall);
				method.insertBefore(sizeOfCall);
				/**
				try {
					String decreaseDeep = "sizeOfPackage.SizeOf.decreaseDeep();";
					method.insertAfter(decreaseDeep, true);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				**/
				//if (!className.endsWith(method.getName()) || method.getModifiers() != Modifier.STATIC) {
					//String decreaseDeep = "sizeOfPackage.SizeOf.decreaseDeep();";
					//method.insertAfter(decreaseDeep, true);
				//} 
			} catch (CannotCompileException e) {
				System.err.println("Can not insert ____sizeOf");
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Return parameters names from a method
	 * E.g: methodX(int a, int b) => (a, b)
	 * @param method method
	 * @return parameters names
	 */
	public List<String> methodParameterName(CtBehavior method) {
		List<String> result = new ArrayList<String>();
		MethodInfo methodInfo = method.getMethodInfo();
    	LocalVariableAttribute table = (LocalVariableAttribute) methodInfo.getCodeAttribute().
    			getAttribute(javassist.bytecode.LocalVariableAttribute.tag);
		//MethodParametersAttribute table = (MethodParametersAttribute) methodInfo.getCodeAttribute().
		//   		getAttribute(javassist.bytecode.MethodParametersAttribute.tag);
		int parametersSize;
		try {
			parametersSize = method.getParameterTypes().length;
			//System.out.println("Parameter size: " + parametersSize);
			//String parameterName = null;
	    	//int frame;
	    	//boolean containThisParam = false;
	    	for (int i = 0; i < parametersSize; ++i) {
	    		/**
	        	frame = table.nameIndex(i); 
	        	//frame = table.name(i);
	    		parameterName = methodInfo.getConstPool().getUtf8Info(frame);
	    		System.out.println("Parameter name:" + parameterName);
	        	if (parameterName.equals("this")) {
	        		containThisParam = true;
	        		++parametersSize; //It don't count 'this' like parameter
	        	} else {
	        		if (containThisParam) {
		        		result.add("$" + (i));
	        		} else {
		        		result.add("$" + (i + 1));
	        		}
	        		//result.add(parameterName);
	        	}
	        	**/
        		result.add("$" + (i + 1)); //skip this parameter.
	        	/**
	        	 * 	    		if (i == 0 && parameterName.equals("this")) {
	    			continue;
	    		} else {
	        		result.add("$" + i);
	    		}
	        	 */
	    	}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
    	return result;
	}
	
}
	