package com.featheredtoast.javad;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestProperties {

	private String filePath = "./test.properties";
	private File file;
	
	private JavaD javad;
	
	@Before
	public void setup() throws IOException, InterruptedException {
		file = new File(filePath);
		
		String input = "key = value\n"
				+ "key2 = value2";
		
		FileOutputStream os = new FileOutputStream(file);
		os.write(input.getBytes());
		os.close();
	}
	
	@After
	public void cleanup() {
		file.delete();
		javad.stop();
	}
	
	@Test
	public void test() throws IOException, InterruptedException {
		javad = new JavaD("./");
		assertTrue(javad.getProperties().containsKey("key"));
	}

}
