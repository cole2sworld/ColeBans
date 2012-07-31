/**
 * 
 */
package com.cole2sworld.misc;

import java.util.HashMap;
import java.util.Map;

/**
 * simple variable substitution system
 * 
 * @author cole2
 * 
 */
public class VariableSystem {
	/**
	 * styles of a variable system
	 * 
	 * @author cole2
	 * 
	 */
	public enum Style {
		/**
		 * ${EXAMPLE}
		 */
		DOLLAR_SIGN_WITH_BRACKETS,
		/**
		 * {$EXAMPLE}
		 */
		DOLLAR_SIGN_INSIDE_BRACKETS,
		/**
		 * $EXAMPLE
		 */
		DOLLAR_SIGN,
		/**
		 * %EXAMPLE%
		 */
		PERCENT_SIGNS,
		/**
		 * {EXAMPLE}
		 */
		CURLY_BRACKETS,
		/**
		 * [EXAMPLE]
		 */
		SQUARE_BRACKETS,
		/**
		 * &lt;EXAMPLE&gt;
		 */
		ANGLE_BRACKETS;
	}
	
	private final Map<String, VariableValueCalculator>	variables	= new HashMap<String, VariableValueCalculator>();
	private final Style									sty;
	
	public VariableSystem(final Style style) {
		sty = style;
	}
	
}
