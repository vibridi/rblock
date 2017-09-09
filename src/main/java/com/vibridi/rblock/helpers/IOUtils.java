package com.vibridi.rblock.helpers;

import java.io.InputStream;
import java.net.URL;

public class IOUtils {

	public static InputStream getResourceAsStream(String name) {
		return IOUtils.class.getResourceAsStream(name);
	}
	
	public static URL getResource(String name) {
		return IOUtils.class.getResource(name);
	}

}
