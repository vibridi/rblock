package com.vibridi.rblock.test;

import com.vibridi.jupper.Jupper;


public class Profiler {
	
	public Profiler() {
	}
	
	public static void main(String[] args) {
		Jupper.defaultClass = "com.vibridi.rblock.test.MainTest";
		Jupper.defaultMethod = "testLearn";
		Jupper.profile();
	}

}
