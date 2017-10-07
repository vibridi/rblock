package com.vibridi.rblock.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vibridi.rblock.helpers.PredicateDefinition;

public class PredicateFactory {
	
	protected PredicateFactory() {
	}
	
	@SuppressWarnings("unchecked")
	public static Set<BlockingPredicate<?>> instantiateAll(String path) throws Exception {
	    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(path);
	    String pk = (String) ctx.getBean("pk");
	    List<PredicateDefinition> definitions = (List<PredicateDefinition>) ctx.getBean("definitions");			
		return instantiateAll(pk, definitions);
	}
	
	public static Set<BlockingPredicate<?>> instantiateAll(String idField, List<PredicateDefinition> definitions) throws Exception { 
		Set<BlockingPredicate<?>> set = new HashSet<>();
		ClassLoader cl = PredicateFactory.class.getClassLoader();
		
		for(PredicateDefinition pdef : definitions) {
			List<String> fields = new ArrayList<>(pdef.getFields());
			fields.remove(idField);
			
			List<Class<?>> typeArguments = new ArrayList<>();
			typeArguments.add(String.class);
			typeArguments.add(String.class);				
			typeArguments.addAll(pdef.getClazzes());
			Class<?>[] typeArgumentsArray = typeArguments.toArray(new Class<?>[0]);
			List<Object[]> combinations = allCombinations(pdef.getParameters());
			
			for(String field : fields) {
				if(combinations.size() == 0) {
					BlockingPredicate<?> pred = (BlockingPredicate<?>) cl.loadClass(pdef.getQualifiedName())
							.getDeclaredConstructor(typeArgumentsArray)
							.newInstance(idField, field);	
					set.add(pred);
				} else {
					for(Object[] parameters : combinations) {
						BlockingPredicate<?> pred = (BlockingPredicate<?>) cl.loadClass(pdef.getQualifiedName())
								.getDeclaredConstructor(typeArgumentsArray)
								.newInstance(parameterArray(idField, field, parameters));	
						set.add(pred);
					}
				}
			}
		}
		return set;
	}
	
	private static List<Object[]> allCombinations(List<Object[]> source) {
		List<Object[]> list = new ArrayList<>();
		Map<Integer,Integer> map = new HashMap<>();
		for(int i = 0; i < source.size(); i++)
			map.put(i, 0);
		allCombinations(list, source, 0, map);
		return list;
	}
	
	private static void allCombinations(List<Object[]> acc, List<Object[]> source, int n, Map<Integer,Integer> map) {
		if(n >= source.size()) {
			add(acc, source, map);
			return;
		}
		
		if(map.get(n) < source.get(n).length) {
			allCombinations(acc, source, n+1, map);
			map.put(n, map.get(n) + 1);
			allCombinations(acc, source, n, map);
			
		} else {
			map.put(n, 0);
		}

	}
	
	private static void add(List<Object[]> acc, List<Object[]> source, Map<Integer,Integer> map) {
		List<Object> list = new ArrayList<>();
		for(int i = 0; i < source.size(); i++) {
			list.add(source.get(i)[map.get(i)]);
		}
		acc.add(list.toArray());
	}
	
	private static Object[] parameterArray(String idField, String field, Object[] additional) {
		List<Object> list = new ArrayList<>();
		list.add(idField);
		list.add(field);
		list.addAll(Arrays.asList(additional));
		return list.toArray(new Object[list.size()]);
	}

	
}
