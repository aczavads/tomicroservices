import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class InjectCode {
	
	private int point;
	private int lastModified;
	private char[] contentFile;
	private String result;
	private String logFile;
	private String featureFile;
	
	private int ignoreAnotationWithPoint(int point) {
		if (contentFile[point] == '@') {
			++point;
		}
		char end = ' ';
		while (point < contentFile.length) {
			if (contentFile[point] == '@') {
				point = ignoreAnotationWithPoint(point);
			} else if (contentFile[point] == '/' ) {
				point = ignoreCommentWithPoint(point);
			} else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				point = ignoreStringWithPoint(point);
			} else if (contentFile[point] == '(') {
				end = ')';
				++point;
			} else if (contentFile[point] == end) {
				//++point;
				return point;
			} else {
				++point;
			}
		}
		return point;
	}
	
	private void ignoreAnotation() {
		point = ignoreAnotationWithPoint(point);
	}
	
	public boolean injectCodeInAllFiles(File file, String logPath, String featurePath) {
		if (file.exists()) {
			String diName = file.getName();
			if (diName.equals("test") || diName.equals("testcases") || 
					diName.equals("testutils")) {
				return false;
			}
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					injectCodeInAllFiles(f, logPath, featurePath);
				}
				return true;
			} else {
				String name = file.getName();
				if (name.endsWith(".java")) {
					if (name.endsWith("Test.java")) { return false; }
					char[] contentFile;
					try {
						contentFile = FileUtils.readFileToString(file, "UTF-8").toCharArray();
						System.out.println("Inject in " + name);
						String newContent = injectCodeMethods(contentFile, logPath, featurePath);
						FileUtils.write(file, newContent, "UTF-8", false);
						return true;
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public String injectCodeMethods(char[] contentFile, String logFile, String featureFile) {
		this.contentFile = contentFile.clone();
		this.logFile = logFile;
		this.featureFile = featureFile;
		result = "";
		point = 0;
		lastModified = 0;
		for (; point < contentFile.length; ++point) {
			if (contentFile[point] == '@') {
				ignoreAnotation();
			} else if (contentFile[point] == '/' ) {
				ignoreComment();
			} else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				ignoreString();
			} else if (contentFile[point] == '(') {
				int openStart = point;
				if (isMethod()) {
					int startBlock = point;
					List<String> parameters = getParameters(openStart, startBlock);
					injectCodeMethod(parameters);
					injectCodeInBodyAndEndMethod();
				}
			}
		}
		if (lastModified == 0) {
			return new String(contentFile);
		}
		return result + new String(contentFile, lastModified, point - lastModified - 1);
	}

	private boolean isReturn(int point) {
		final int returnWordSize = 6;
		String possibleReturn = new String(contentFile, point, returnWordSize);
		return possibleReturn.equals("return");
	}
	
	/**
	private void injectCodeInReturn() {
		while (point < contentFile.length) {
			if (contentFile[point] == '@') {
				ignoreAnotation();
			} else if (contentFile[point] == '/' ) {
				ignoreComment();
			} else if (contentFile[point] == 'r') {
				if (isReturn(point)) {
					String inject = "sizeOfPackage.SizeOf.decreaseDeep();";
					result += new String(contentFile, lastModified, point - lastModified);
					result += inject;
					lastModified = point + 1;
				}
				++point;
			}
		}
	}
	**/

	private void injectCodeInBodyAndEndMethod() {
		int countOpen = 0;
		while (point < contentFile.length) {
			if (contentFile[point] == '@') {
				ignoreAnotation();
			} else if (contentFile[point] == '/') {
				ignoreComment();
			}  else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				ignoreString();
			} /**else if (contentFile[point] == 'r') {
				if (isReturn(point)) {
					String inject = "sizeOfPackage.SizeOf.decreaseDeep();";
					result += new String(contentFile, lastModified, point - lastModified);
					result += inject;
					lastModified = point;
				}
				++point;
			} **/ 
			else if (contentFile[point] == '}') {
				--countOpen;
				if (countOpen == 0) {
					String inject = "} catch (Throwable eeeeeeee_e) {\n" + 
							"			throw eeeeeeee_e;\n" + 
							"		} finally {\n" + 
							"			sizeOfPackage.SizeOf.decreaseDeep();\n" + 
							"		} ";
					result += new String(contentFile, lastModified, point - lastModified);
					result += inject;
					lastModified = point;
					++point;
					return;
				}
				++point;
			} else if (contentFile[point] == '{') {
				++countOpen;
				++point;
			} else {
				++point;
			}
		}
	}
	
	private void ignoreComment() {
		point = ignoreCommentWithPoint(point);
	}
	
	private int ignoreCommentWithPoint(int point) {
		char first = contentFile[point];
		char second = contentFile[point + 1];
		char firstEnd;
		char secondEnd;
		boolean onlyOne = false;
		if (first == '/' && second == '/') {
			firstEnd = '\n';
			secondEnd = ' ';
			onlyOne = true;
		} else if (first == '/' && second == '*') {
			firstEnd = '*';
			secondEnd = '/';
		} else {
			return ++point;
		}
		point += 2;
		while (point < contentFile.length - 1) {
			if (onlyOne) {
				if (contentFile[point] == firstEnd) {
					return point + 1;
				}
			} else {
				if (contentFile[point] == firstEnd && contentFile[point + 1] == secondEnd) {
					point += 2;
					return point;
				}
			}
			++point;
		}
		return point;
	}
	
	private void ignoreString() {
		point = ignoreStringWithPoint(point);
	}
	
	private int ignoreStringWithPoint(int point) {
		char end;
		if (contentFile[point] == '\'') {
			end = '\'';
			++point;
		} else if (contentFile[point] == '\"') {
			++point;
			end = '\"';
		} else {
			return point;
		}
		while (point < contentFile.length) {
			if (contentFile[point] == '\\' && contentFile[point + 1] == '\\') {
				point += 2;
			} else if (contentFile[point] == '\\' && contentFile[point + 1] == end) {
				point += 2;
			} else if (contentFile[point] == end) {
				return point + 1;
			} else {
				++point;
			}
		}
		return point;
	}

	private void injectCodeMethod(List<String> parameters) {
		String callMethod = "sizeOfPackage.SizeOf.sizeOfLog = new java.io.File(\"" + logFile + "\"); ";
		callMethod += "sizeOfPackage.SizeOf.featureInfo = new java.io.File(\"" + featureFile + "\"); ";
		callMethod += "try { sizeOfPackage.SizeOf.saveSizeOfLog(";
		//callMethod += "new Object().getClass().getEnclosingClass().getName(), ";
		//callMethod += "new Object().getClass().getEnclosingMethod().getName(), ";
		callMethod += "new java.lang.Throwable().getStackTrace()[0].getClassName(), ";
		callMethod += "new java.lang.Throwable().getStackTrace()[0].getMethodName(), ";
		if (parameters.isEmpty()) {
			callMethod += "0l";
		} else {
			boolean first = true;
			for (String p : parameters) {
				if (first == true) {
					first = false;
					callMethod += "sizeOfPackage.SizeOf.sizeOf(" + p + ")";
				} else {
					callMethod +=  "+ sizeOfPackage.SizeOf.sizeOf(" + p + ")";
				}
			}
		}
		callMethod += "); ";
		result += new String(contentFile, lastModified, point - lastModified + 1);
		result += callMethod;
		lastModified = point + 1; 
	}

	private List<String> getParameters(int openStart, int startBlock) {
		List<String> parameters = new ArrayList<String>();
		String str = "";
		for (int i = openStart ; i < startBlock; ++i) {
			if (contentFile[i] == '@') {
				i = ignoreAnotationWithPoint(i);
				if (i >= startBlock) {
					break;
				}
			} else if (contentFile[point] == '/' ) {
				i = ignoreCommentWithPoint(i);
				if (i >= startBlock) {
					break;
				}
			} else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				i = ignoreStringWithPoint(i);
				if (i >= startBlock) {
					break;
				}
			}
			str += contentFile[i];
		}
		str = str.replace(",final ", ", ");
		str = str.replace("(final ", "( ");
		str = str.replace(" final ", "( ");
		int index = str.indexOf(")");
		str = str.substring(0, index);
		str = str.replace("(", "");
		str = str.replace("\n", " ");
		str = str.replace("\t", " ");
		str = str.replaceAll("<.*?>", "");
		str = str.replace("[]", "");
		String typeAndParameters[] = str.split(",");
		for (int i = 0; i < typeAndParameters.length; i = i + 1) {
			char typeAndParameter[] = typeAndParameters[i].toCharArray();
			String parameter = "";
			boolean endFirstString = false;
			boolean startFirstString = false;
			boolean startSecondString = false;
			for (int j = 0; j < typeAndParameter.length; ++j) {
				if (typeAndParameter[j] != ' ') {
					startFirstString = true;
					if (endFirstString) {
						startFirstString = false;
						startSecondString = true;
						parameter += typeAndParameter[j];
					}
				} else if (startFirstString) {
					endFirstString = true;
				} else if (startSecondString) {
					break;
				}
			}
			if (!parameter.equals("")) {
				parameters.add(parameter);
			}
		}
		return parameters;
	}

	private boolean isKeyWord(String word) {
		final String keys[] = {"this", "super", "if", "switch", "while", "for", 
				"synchronized", "catch"};
		for (int i = 0; i < keys.length; ++i) {
			if (keys[i].equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isModifier(String word) {
		final String mod[] = {"public", "protected", "private"};
		for (int i = 0; i < mod.length; ++i) {
			if (mod[i].equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isMethod() {
		String lastWord = previousWord(point, 1);
		if (isKeyWord(lastWord) || lastWord.contains(")")) {
			return false;
		}
		lastWord = previousWord(point, 2);
		if (isModifier(lastWord) || lastWord.equals("new") || lastWord.contains(";") || 
				lastWord.contains("}") || lastWord.contains("/") || lastWord.contains("*/") || 
				lastWord.contains(",")) {
			return false;
		}
		lastWord = previousWord(point, 3);
		if (lastWord.equals("new")) {
			return false;
		}
		while (point < contentFile.length) {
			if (contentFile[point] == '@') {
				ignoreAnotation();
			} else if (contentFile[point] == '/' ) {
				ignoreComment();
			} else if (contentFile[point] == '\'' || contentFile[point] == '\"') {
				ignoreString();
			} else if (contentFile[point] == ';') {
				return false;
			} else if (isNewKeyWord()) {
				return false;
			} else if (contentFile[point] == '{') {
				return true;
			} else {
				++point;
			}
		}
		return false;
	}

	private boolean isNewKeyWord() {
		return contentFile[point] == 'n' && contentFile[point+1] == 'e' && contentFile[point+2] == 'w' 
				&& contentFile[point+3] == ' ';
	}

	private int skipSpaceBreakAndTab(int point, int move) {
		char current = contentFile[point];
		while (current == ' ' || current == '\n' || current == '\t') {
			point = point + move;
			current = contentFile[point];
		}
		return point;
	}
	
	private String previousWord(int point, int previousOrder) {
		--previousOrder;
		int ret = point - 1;
		boolean findWord = false;
		String lastWord = "";
		ret = skipSpaceBreakAndTab(ret, -1);
		while (ret >= 0) {
			if (contentFile[ret] != ' ' && contentFile[ret] != '\n' 
					&& contentFile[ret] != '\t') {
				if (previousOrder == 0) {
					findWord = true;
					lastWord = contentFile[ret] + lastWord;
				}
			} else {
				if (findWord) {
					break;
				} else {
					ret = skipSpaceBreakAndTab(ret, -1) + 1;
					--previousOrder;
				}
			}
			--ret;
		}
		return lastWord;
	}

}
