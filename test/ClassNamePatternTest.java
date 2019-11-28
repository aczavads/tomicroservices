import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class ClassNamePatternTest {
	
	public File file;
	
	@BeforeEach
	void setup() {
		this.file = new File("___someFile__");
	}
	
	@AfterEach
	void tearDown() {
		this.file.deleteOnExit();
	}
	
	@Test
	void testAcceptAll() {
		try {
			FileUtils.writeStringToFile(this.file, ".", "UTF-8");
			ClassNamePattern pattern = new ClassNamePattern(this.file, true);
			assertEquals(true, pattern.isAcceptable("someName"));
			pattern.setAccept(false);
			assertEquals(false, pattern.isAcceptable("someName"));
			List<String> lRegex = pattern.getRegex();
			assertEquals(1, lRegex.size());
			assertEquals(".", lRegex.get(0));
		} catch (Exception e) {
			fail("Exception");
		}
	}
	
	@Test
	void testAcceptSpecificName() {
		try {
			FileUtils.writeStringToFile(file, "name", "UTF-8");
			ClassNamePattern pattern = new ClassNamePattern(file, true);
			assertEquals(false, pattern.isAcceptable("otherName"));
			pattern.setAccept(false);
			assertEquals(true, pattern.isAcceptable("otherName"));
		} catch (Exception e) {
			fail("Exception");
		}
	}
	
	@Test
	void testAcceptRealName() {
		try {
			FileUtils.writeStringToFile(file, "toCheck.main.Main", "UTF-8");
			ClassNamePattern pattern = new ClassNamePattern(file, true);
			assertEquals(true, pattern.isAcceptable("toCheck.main.Main"));
			pattern.setAccept(false);
			assertEquals(false, pattern.isAcceptable("toCheck.main.Main"));
		} catch (Exception e) {
			fail("Exception");
		}
	}
	
	@Test
	void testIgnoreRealName() {
		try {
			FileUtils.writeStringToFile(file, "toCheck.main.Main", "UTF-8");
			ClassNamePattern pattern = new ClassNamePattern(file, true);
			assertEquals(false, pattern.isAcceptable("jdk.jfr.internal.EventWriter"));
			pattern.setAccept(false);
			assertEquals(true, pattern.isAcceptable("jdk.jfr.internal.EventWriter"));
		} catch (Exception e) {
			fail("Exception");
		}
	}	
	
	@Test
	void testWithoutContent() {
		try {
			FileUtils.writeStringToFile(file, "", "UTF-8");
			ClassNamePattern pattern = new ClassNamePattern(file, true);
			assertEquals(false, pattern.isAcceptable("some"));
			pattern.setAccept(false);
			assertEquals(true, pattern.isAcceptable("some"));
		} catch (Exception e) {
			fail("Exception");
		}
	}
	
	@Test
	void testWithoutFile() {
		try {
			file = mock(File.class);
			when(file.exists()).thenReturn(false);
			ClassNamePattern pattern = new ClassNamePattern(file, true);
			pattern.getRegex();
			fail("Do not get FileNotFound");
		} catch (Exception e) {
			
		}
	}

}
