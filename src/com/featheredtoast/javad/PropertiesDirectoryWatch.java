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

public class PropertiesDirectoryWatch {

	private String directoryPath;
	
	private Properties properties;
	
	private Timer timer;
	
	public PropertiesDirectoryWatch(String directoryPath) throws IOException {
		this.directoryPath = directoryPath;
		properties = new Properties();
		loadPropertiesInDirectory();
		startPropertiesDirectoryWatch();
	}
	
	public PropertiesDirectoryWatch(String directoryPath, Properties defaults) throws IOException {
		this.directoryPath = directoryPath;
		properties = new Properties(defaults);
		loadPropertiesInDirectory();
		startPropertiesDirectoryWatch();
	}
	
	public synchronized void stop() {
		timer.stop();
	}
	
	private void startPropertiesDirectoryWatch() {
		ActionListener al = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("loading files...");
					loadPropertiesInDirectory();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};
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
		System.out.println("loading file: " + propertyFile.getName());
		InputStream is = new FileInputStream(propertyFile);
		properties.load(is);
		is.close();
	}
	
}
