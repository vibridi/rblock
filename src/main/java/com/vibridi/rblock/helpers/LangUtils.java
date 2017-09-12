package com.vibridi.rblock.helpers;

import java.util.ArrayList;
import java.util.List;

public class LangUtils {

	
	public static boolean isNumeric(CharSequence cs) { // same implementation as apache commons lang
		if (cs == null || cs.length() == 0) {
            return false;
        }
		
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
	}
	
	public static List<String> ngram(String value, int n) {
		List<String> set = new ArrayList<>();
		if(value == null || value.length() == 0)
			return set;
		if(value.length() <= n) {
			set.add(value);
			return set;
		}
		for(int i = 0; i+n <= value.length(); i++)
			set.add(value.substring(i, i+n));

		return set;
	}
	
}
