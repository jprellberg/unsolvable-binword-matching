/*-
 * Unsolvable Binary Word Matching
 * Copyright (C) 2016 Jonas Prellberg
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package uniol.matcher;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class UnsolvableBinwordMatcherTest {

	private List<String> unsolvableWords;

	@Before
	public void setup() {
		unsolvableWords = new BinaryWordList().getUnsolvableWords();
	}

	@Test
	public void javaRegexUBMTest() {
		UnsolvableBinwordMatcher matcher = new JavaRegexUBM();
		testUnsolvableWords(matcher);
		testUnsolvableSubWord(matcher);
	}

	@Test
	public void patternUBMTest() {
		UnsolvableBinwordMatcher matcher = new PatternUBM();
		testUnsolvableWords(matcher);
		testUnsolvableSubWord(matcher);
	}

	private void testUnsolvableWords(UnsolvableBinwordMatcher matcher) {
		for (String word : unsolvableWords) {
			if (!matcher.isUnsolvableBinaryWord(word)) {
				fail("The unsolvable word '" + word + "' was incorrectly classified as solvable.");
			}
		}
	}

	private void testUnsolvableSubWord(UnsolvableBinwordMatcher matcher) {
		String unsolvableWord = "a" + "abbaa";
		assertTrue("The word 'aabbaa' containing the unsolvable factor 'abbaa' was incorrectly classified as solvable.",
				matcher.isUnsolvableBinaryWord(unsolvableWord));
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
