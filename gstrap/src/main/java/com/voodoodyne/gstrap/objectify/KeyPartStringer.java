package com.voodoodyne.gstrap.objectify;

import lombok.Data;

/**
 * Maps between key prefixes and kinds
 */
public interface KeyPartStringer {
	/** */
	@Data
	class Part {
		private final String kind;
		private final String identifier;
	}

	/** Assemble the components into a single string */
	String assemble(final Part part);

	/** Break the assembled version into component parts */
	Part disassemble(final String assembled);
}
