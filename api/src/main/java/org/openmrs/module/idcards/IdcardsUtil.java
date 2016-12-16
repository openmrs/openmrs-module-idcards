package org.openmrs.module.idcards;

import org.openmrs.api.context.Context;

public class IdcardsUtil {
	
	public static int getCheckDigit(String id) {
		String withCheckDigit = Context.getPatientService().getDefaultIdentifierValidator().getValidIdentifier(String.valueOf(id));
		char checkDigitChar = withCheckDigit.charAt(withCheckDigit.length() - 1);
		return getCheckDigit(checkDigitChar);
	}
	
	private static int getCheckDigit(char checkDigitChar) {
		if (Character.isDigit(checkDigitChar)) {
			return Integer.parseInt("" + checkDigitChar);
		} else {
			switch (checkDigitChar) {
				case 'A':
				case 'a':
					return 0;
				case 'B':
				case 'b':
					return 1;
				case 'C':
				case 'c':
					return 2;
				case 'D':
				case 'd':
					return 3;
				case 'E':
				case 'e':
					return 4;
				case 'F':
				case 'f':
					return 5;
				case 'G':
				case 'g':
					return 6;
				case 'H':
				case 'h':
					return 7;
				case 'I':
				case 'i':
					return 8;
				case 'J':
				case 'j':
					return 9;
				default:
					return 10;
			}
		}
	}
}
