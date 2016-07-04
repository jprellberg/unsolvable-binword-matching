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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
		testAll(matcher);
	}

	@Test
	public void patternUBMTest() {
		UnsolvableBinwordMatcher matcher = new PatternUBM();
		assertFalse(matcher.isUnsolvableBinaryWord("abbba"));
		testAll(matcher);
	}

	@Test
	public void letterCountingUBMTest() {
		UnsolvableBinwordMatcher matcher = new LetterCountingUBM();
		testAll(matcher);
	}

	private void testAll(UnsolvableBinwordMatcher matcher) {
		testUnsolvableWords(matcher);
		testUnsolvableSubWord(matcher);
		testSimpleSolvableWords(matcher);
		testSolvableWords(matcher);
	}

	private void testUnsolvableWords(UnsolvableBinwordMatcher matcher) {
		for (String word : unsolvableWords) {
			assertTrue("The unsolvable word '" + word + "' was incorrectly classified as solvable.",
					matcher.isUnsolvableBinaryWord(word));
		}
	}

	private void testUnsolvableSubWord(UnsolvableBinwordMatcher matcher) {
		String unsolvableWord = "a" + "abbaa";
		assertTrue("The word 'aabbaa' containing the unsolvable factor 'abbaa' was incorrectly classified as solvable.",
				matcher.isUnsolvableBinaryWord(unsolvableWord));
	}

	private void testSimpleSolvableWords(UnsolvableBinwordMatcher matcher) {
		assertFalse(matcher.isUnsolvableBinaryWord("aaaaaaaa"));
		assertFalse(matcher.isUnsolvableBinaryWord("bbbbbbbb"));
		assertFalse(matcher.isUnsolvableBinaryWord("aaaabbbb"));
	}

	private void testSolvableWords(UnsolvableBinwordMatcher matcher) {
		BinaryWordList wordList = new BinaryWordList();
		List<String> unsolvableWords = wordList.getUnsolvableWords();
		for (String minUnsolvableWord : unsolvableWords) {
			String prefix = minUnsolvableWord.substring(0, minUnsolvableWord.length() - 1);
			String suffix = minUnsolvableWord.substring(1);
			assertFalse("The word '" + prefix + "' was incorrectly classified as unsolvable.",
					matcher.isUnsolvableBinaryWord(prefix));
			assertFalse("The word '" + suffix + "' was incorrectly classified as unsolvable.",
					matcher.isUnsolvableBinaryWord(suffix));
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
