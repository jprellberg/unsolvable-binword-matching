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

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class UnsolvableBinwordMatcherTest {

	private List<String> unsolvableWords;

	@Before
	public void setup() throws IOException {
		unsolvableWords = readWordFile();
	}

	private List<String> readWordFile() throws IOException {
		InputStream is = UnsolvableBinwordMatcherTest.class
				.getResourceAsStream("/binary-nonsyntetizable-words");
		BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

		List<String> words = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			if (!line.startsWith("#")) {
				words.add(line);
			}
		}
		return words;
	}

	@Test
	public void javaRegexUBMTest() {
		test(new JavaRegexUBM());
	}

	private void test(UnsolvableBinwordMatcher matcher) {
		for (String word : unsolvableWords) {
			if (!matcher.isUnsolvableBinaryWord(word)) {
				fail("The unsolvable word '" + word + "' was incorrectly classified as solvable.");
			}
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
