package com.vibridi.rblock.helpers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class DataUtils {
	
	public static List<Map<String,String>> readCSV(String fileName) throws FileNotFoundException, IOException {	
		InputStream in = IOUtils.getResourceAsStream(fileName);
		return CSVFormat.RFC4180.withFirstRecordAsHeader().parse(new InputStreamReader(in))
				.getRecords()
				.stream()
				.map(CSVRecord::toMap)
				.collect(Collectors.toList());
	}
	
	public static List<Map<String,String>> readCSV(String fileName, String[] header) throws FileNotFoundException, IOException {
		InputStream in = IOUtils.getResourceAsStream(fileName);
		return CSVFormat.RFC4180.withHeader(header).parse(new InputStreamReader(in))
				.getRecords()
				.stream()
				.map(CSVRecord::toMap)
				.collect(Collectors.toList());		
	}

}
