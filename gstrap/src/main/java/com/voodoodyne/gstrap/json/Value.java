package com.voodoodyne.gstrap.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pretty much just to make it easy to create single valued JSON objects
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Value<T> {
	T value;
}