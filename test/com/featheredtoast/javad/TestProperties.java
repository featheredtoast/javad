package com.featheredtoast.javad;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class TestProperties {

	@Test
	public void test() throws IOException, InterruptedException {
		JavaD javad = new JavaD("/home/jwong/test/properties/");
		
		Thread.sleep(120000);
		javad.stop();
	}

}
