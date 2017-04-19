package com.voodoodyne.gstrap.gae;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.voodoodyne.gstrap.lang.Strings2;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Locale;

/**
 * Google's obsession with builders sucks. This is prettier.
 */
public class DocumentBuilder<E extends Enum<E>> {

	private final Document.Builder builder = Document.newBuilder();

	public DocumentBuilder(final String documentId) {
		builder.setId(documentId);
		builder.setLocale(Locale.US);
	}

	public DocumentBuilder<E> addAtom(final E field, final String value) {
		if (StringUtils.isNotBlank(value))
			builder.addField(fld(field).setAtom(value));

		return this;
	}

	public DocumentBuilder<E> addText(final E field, final String value) {
		if (StringUtils.isNotBlank(value))
			builder.addField(fld(field).setText(value));

		return this;
	}

	public DocumentBuilder<E> addText(final E field, final Iterable<String> values) {
		for (final String value : values) {
			addText(field, value);
		}

		return this;
	}

	public DocumentBuilder<E> addFragments(final E field, final String string) {
		for (final String fragment : Strings2.fragment(string)) {
			builder.addField(fld(field).setText(fragment));
		}

		return this;
	}

	public DocumentBuilder<E> addFragments(final E field, final Iterable<String> strings) {
		for (final String str : strings) {
			addFragments(field, str);
		}

		return this;
	}

	public DocumentBuilder<E> addNumber(final E field, final double value) {
		builder.addField(fld(field).setNumber(value));
		return this;
	}

	/** Date only to be used for sorting; actually stored as a number */
	public DocumentBuilder<E> addSortDate(final E field, final DateTime value) {
		final double asNumber = (double)value.getMillis() / (double)Long.MAX_VALUE;
		return addNumber(field, asNumber);
	}

	public Document build() {
		return builder.build();
	}

	private Field.Builder fld(final E field) {
		return fld(field.name());
	}

	private Field.Builder fld(final String name) {
		return Field.newBuilder().setName(name);
	}
}
