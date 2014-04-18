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
	
	public JavaD(String directoryPath) throws IOException {
		this.directoryPath = directoryPath;
		properties = new Properties();
		loadPropertiesInDirectory();
		startJavaD();
	}
	
	public JavaD(String directoryPath, Properties defaults) throws IOException {
		this.directoryPath = directoryPath;
		properties = new Properties(defaults);
		loadPropertiesInDirectory();
		startJavaD();
	}
	
	public synchronized void stop() {
		timer.stop();
	}
	
	private void startJavaD() {
		ActionListener al = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					loadPropertiesInDirectory();
				} catch (IOException e1) {
					log.warn("error loading properties", e1);
				}
			}
		};
		log.debug("properties javad starting");
		timer = new Timer(60000, al);
		timer.setRepeats(true);
		timer.start();
	}
	
	private synchronized void loadPropertiesInDirectory() throws IOException {
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
	}
	
	private void loadProperteisInFile(File propertyFile) throws IOException {
		log.debug("loading properties: " + propertyFile.getName());
		InputStream is = new FileInputStream(propertyFile);
		properties.load(is);
		is.close();
	}
	
}