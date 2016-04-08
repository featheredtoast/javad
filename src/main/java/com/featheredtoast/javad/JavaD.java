package com.featheredtoast.javad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JavaD {

    private Log log = LogFactory.getLog(this.getClass());
	
    private String directoryPath;
	
    private Properties properties;
	
    private Timer timer;
	
    private int interval = 60000;

    private String systemVar = null;
	
    public JavaD(String directoryPath) throws IOException {
        this.directoryPath = directoryPath;
        properties = new Properties();
        loadExternalProperties();
    }
	
    public JavaD(String directoryPath, int interval) throws IOException {
        this.directoryPath = directoryPath;
        this.interval = interval;
        properties = new Properties();
        loadExternalProperties();
    }
	
    public JavaD(String directoryPath, Properties defaults) throws IOException {
        this.directoryPath = directoryPath;
        properties = new Properties(defaults);
        loadExternalProperties();
    }
	
    public JavaD(String directoryPath, Properties defaults, int interval) throws IOException {
        this.directoryPath = directoryPath;
        this.interval = interval;
        properties = new Properties(defaults);
        loadExternalProperties();
    }

    public void addLoadFromSystemProperty(String envVar) throws IOException {
        log.debug("adding envvar: " + envVar);
        this.systemVar = envVar;
        loadExternalProperties();
    }
	
    public synchronized void start() {
        startJavaD();
    }
	
    public synchronized void stop() {
        if(timer != null) {
            timer.stop();
        }
    }
	
    public Properties getProperties() {
        return properties;
    }
	
    public void resetProperties(Properties properties) {
        this.properties = new Properties(properties);
    }
	
    private void startJavaD() {
        ActionListener al = new ActionListener() {
			
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        loadExternalProperties();
                    } catch (IOException e1) {
                        log.warn("error loading properties", e1);
                    }
                }
            };
        log.debug("properties javad starting");
        timer = new Timer(interval, al);
        timer.setRepeats(true);
        timer.start();
    }
	
    private synchronized void loadExternalProperties() throws IOException {
        File dir = new File(directoryPath);
        if(dir.isDirectory()) {
            File[] propertyFiles = dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File arg0, String arg1) {
                        return arg1.endsWith(".properties");
                    }
                });
            for(File file : propertyFiles) {
                loadProperteisInFile(file);
            }
        }
        loadEnvironmentProperty();
    }
	
    private void loadProperteisInFile(File propertyFile) throws IOException {
        log.debug("loading properties: " + propertyFile.getName());
        InputStream is = new FileInputStream(propertyFile);
        Properties newFileProperties = new Properties();
        newFileProperties.load(is);
        is.close();
        properties.putAll(newFileProperties);
    }

    private void loadEnvironmentProperty() {
        Properties newEnvironmentProperties = new Properties();
        if(systemVar != null) {
            log.debug(systemVar);
            String environmentPropertyString = System.getenv(systemVar);
            if(environmentPropertyString == null ||
               "".equals(environmentPropertyString)) {
            	environmentPropertyString = System.getProperty(systemVar);
            }
            log.debug(environmentPropertyString);
            if(environmentPropertyString != null) {
                newEnvironmentProperties = parseEnvironmentProperty(environmentPropertyString);
            }
        }
        properties.putAll(newEnvironmentProperties);
    }

    private Properties parseEnvironmentProperty(String propertyString) {
        Properties newEnvironmentProperties = new Properties();
        String[] propertyKeyValues = propertyString.split(";");
        for(String keyValue : propertyKeyValues) {
            String[] keyValueArray = keyValue.trim().split(" ");
            if(keyValueArray.length >= 2) {
                String key = keyValueArray[0].trim();
                String value = keyValueArray[1].trim();
                log.debug("setting system property " + key + " " + value);
                newEnvironmentProperties.setProperty(key, value);
            }
        }
        return newEnvironmentProperties;
    }
}
