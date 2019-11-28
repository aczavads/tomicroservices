import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Open file responsible by containing regex with the class name to be acceptd.
 * Return regex contained in the file (one per line).
 * 
 * @author Luiz Carvalho <lmcarvalho@inf.puc-rio.br>
 *
 */
public class ClassNamePattern {

	private File file;
	
	private boolean openedFile = false;
	
	/**
	 * Contains all regex found in provided file (one per line)
	 */
	private List<String> regex;
	
	private boolean accept;
	
	public ClassNamePattern(File file, boolean accept) {
		this.accept = accept;
		this.file = file;
	}
	
	/**
	 * Open file and save his content (regex - one per line)
	 * @throws IOException in case the file does not exist or another problem in open the file
	 */
	private void openFile() throws IOException {
		if (this.openedFile) {
			return;
		} else {
			this.regex = new ArrayList<String>();
		}
		if (this.file.exists()) {
			try (BufferedReader reader = new BufferedReader(
					new FileReader(this.file))) {
				String line = reader.readLine();
				while (line != null) {
					this.regex.add(line);
					line = reader.readLine();
				}
				this.openedFile = true;
			}
		} else {
			throw new FileNotFoundException("File accept did not find");
		}
	}
	
	/**
	 * Return a list with all regex
	 * @return a list with regex
	 * @throws Exception in case the file does not exist or another problem in open the file
	 */
	public List<String> getRegex() throws Exception {
		openFile();
		return new ArrayList<String>(this.regex);
	}
	
	/**
	 * Verify if class name is acceptable 
	 * @param className name to verify
	 * @return true if class name is acceptable, otherwise false 
	 * @throws Exception in case the file does not exist or another problem in open the file
	 */
	public boolean isAcceptable(String className) throws Exception {
		openFile();
		for (String rgx : this.regex) {
			Pattern pattern = Pattern.compile(rgx);
			Matcher matcher = pattern.matcher(className);
			if (matcher.find()) {
				return this.accept;
			}
		}
		return !this.accept;
	}
	
	public void setAccept(boolean accept) {
		this.accept = accept;
	}
	
}
