package com.voodoodyne.gstrap.test;

import org.joda.time.DateTimeUtils;

/**
 * Some tools for manipulating the system clock
 */
public class SystemClock {

	/** Reset it back to normal */
	public static void reset() {
		DateTimeUtils.setCurrentMillisSystem();
	}
}
