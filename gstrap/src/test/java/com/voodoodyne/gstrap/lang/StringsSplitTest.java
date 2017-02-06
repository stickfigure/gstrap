package com.voodoodyne.gstrap.lang;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;

/**
 */
class StringsSplitTest {

	@Test
	void combineOneElementWithEscape() {
		final String combined = StringsSplit.combineWithEscape(Arrays.asList("asdf_asdf"), '_');
		assertThat(combined, is("asdf__asdf"));
	}

	@Test
	void combinesEasy() {
		final String combined = StringsSplit.combineWithEscape(Arrays.asList("asdf", "asdf"), '_');
		assertThat(combined, is("asdf_asdf"));
	}

	@Test
	void combinesMixed() {
		final String combined = StringsSplit.combineWithEscape(Arrays.asList("asdf_asdf", "qwer_qwer"), '_');
		assertThat(combined, is("asdf__asdf_qwer__qwer"));
	}

	@Test
	void combinesMixedWithExtraUnderscore() {
		final String combined = StringsSplit.combineWithEscape(Arrays.asList("asdf_asdf_", "qwer_qwer"), '_');
		assertThat(combined, is("asdf__asdf___qwer__qwer"));
	}

	@Test
	void combinesEdgeCaseAtBeginning() {
		final String combined = StringsSplit.combineWithEscape(Arrays.asList("_qwer_qwer"), '_');
		assertThat(combined, is("__qwer__qwer"));
	}

	@Test
	void combinesEdgeCaseAtEnd() {
		final String combined = StringsSplit.combineWithEscape(Arrays.asList("qwer_qwer_"), '_');
		assertThat(combined, is("qwer__qwer__"));
	}


	@Test
	void doesNotSplitUnsplittable() {
		final List<String> splits = StringsSplit.splitWithEscape("asdf__asdf", '_');
		assertThat(splits, contains("asdf_asdf"));
	}

	@Test
	void splitsEasy() {
		final List<String> splits = StringsSplit.splitWithEscape("asdf_qwer", '_');
		assertThat(splits, contains("asdf", "qwer"));
	}

	@Test
	void splitsMixed() {
		final List<String> splits = StringsSplit.splitWithEscape("asdf__asdf_qwer__qwer", '_');
		assertThat(splits, contains("asdf_asdf", "qwer_qwer"));
	}

	@Test
	void splitsMixedWithExtraUnderscore() {
		final List<String> splits = StringsSplit.splitWithEscape("asdf__asdf___qwer__qwer", '_');
		assertThat(splits, contains("asdf_asdf_", "qwer_qwer"));
	}

	@Test
	void splitEdgeCaseAtBeginning() {
		final List<String> splits = StringsSplit.splitWithEscape("_qwer__qwer", '_');
		assertThat(splits, contains("qwer_qwer"));
	}

	@Test
	void splitEdgeCaseAtEnd() {
		final List<String> splits = StringsSplit.splitWithEscape("asdf__asdf_", '_');
		assertThat(splits, contains("asdf_asdf"));
	}
}