package com.vibridi.rblock.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vibridi.rblock.core.BlockingPredicate;

public class PredicateDefinition {

	public static PredicateDefinition defineFor(List<String> fields, Class<? extends BlockingPredicate<?>> clazz, Object[]... parameters) {
		return new PredicateDefinition(fields, clazz.getName(), 
				Stream.of(parameters).filter(a -> a.length > 0).map(a -> a[0].getClass()).collect(Collectors.toList()), 
				parameters == null ? new ArrayList<>() : Arrays.asList(parameters));
	}
	
	private List<String> fields;
	private String qualifiedName;
	private List<Class<?>> clazzes;
	private List<Object[]> parameters;
	
	private PredicateDefinition(List<String> fields, String qualifiedName, List<Class<?>> clazzes, List<Object[]> parameters) {
		this.fields = fields;
		this.qualifiedName = qualifiedName;
		this.clazzes = clazzes;
		this.parameters = parameters;
	}

	public List<String> getFields() {
		return fields;
	}
	
	public String getQualifiedName() {
		return qualifiedName;
	}

	public List<Class<?>> getClazzes() {
		return clazzes;
	}

	public List<Object[]> getParameters() {
		return parameters;
	}
	
	

}
