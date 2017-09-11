package com.vibridi.rblock.helpers;

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
	
}
