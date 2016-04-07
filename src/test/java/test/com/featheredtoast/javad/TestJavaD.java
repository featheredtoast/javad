package com.featheredtoast.javad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestJavaD {

    private String filePath = "./test.properties";
    private File file;
	
    private JavaD javad;
	
    @Before
    public void setup() throws IOException, InterruptedException {
		
        Logger log = Logger.getRootLogger();
        ConsoleAppender ca = new ConsoleAppender();
        ca.setWriter(new OutputStreamWriter(System.out));
        ca.setLayout(new PatternLayout("%d{yyyy-MM-dd'T'HH:mm:ss,SSSZ} %5p [%t][%x] %c - %m%n"));
        log.addAppender(ca);
		
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
	
    @Test(timeout = 20000)
    public void test() throws IOException, InterruptedException {
        javad = new JavaD("./");
        javad.start();
        assertTrue(javad.getProperties().containsKey("key"));
        assertEquals("value",javad.getProperties().get("key"));
    }
	
    @Test(timeout = 20000)
    public void testLoadDifferentFile() throws IOException, InterruptedException {
        javad = new JavaD("./", 50);
        javad.start();
        assertTrue(javad.getProperties().containsKey("key"));
        file = new File(filePath);
		
        String input = "key = value2\n"
            + "key2 = value2";
		
        FileOutputStream os = new FileOutputStream(file);
        os.write(input.getBytes());
        os.close();
        while(!"value2".equals(javad.getProperties().get("key"))) {
            Thread.sleep(100);
        }
        assertTrue(javad.getProperties().containsKey("key"));
        assertEquals("value2",javad.getProperties().get("key"));
    }
	
    @Test(timeout = 20000)
    public void testLoadDefaults() throws IOException, InterruptedException {
        Properties properties = new Properties();
        properties.put("key", "valuenottheactualone");
        javad = new JavaD("./", properties, 50);
        javad.start();
        assertTrue(javad.getProperties().containsKey("key"));
    }
	
    @Test(timeout = 20000)
    public void testStopWithoutStart() throws IOException {
        javad = new JavaD("./", 50);
        javad.stop();
    }

    @Test(timeout = 20000)
    public void testLoadEnvironment() throws IOException {
        Runtime.getRuntime().exec("define JAVAD_TEST
test1=test1
test2=test2");
        javad = new JavaD("./", 50);
        assertEquals("test1",javad.getProperties().get("test1"));
    }

}
