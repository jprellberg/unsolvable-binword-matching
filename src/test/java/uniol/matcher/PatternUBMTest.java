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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;

import java.util.stream.IntStream;

import org.junit.Test;

public class PatternUBMTest {

	private PatternUBM matcher = new PatternUBM();

	@Test
	public void testPatternIa1() {
		String word = "abbbbaba";
		assertThat(matcher.containsPatternI(word.toCharArray(), 'a'), is(true));
	}

	@Test
	public void testPatternIa2() {
		// word has one too few 'b' in the first block
		String word = "abbbaba";
		assertThat(matcher.containsPatternI(word.toCharArray(), 'a'), is(false));
	}

	@Test
	public void testPatternIa3() {
		// word is missing final 'a'
		String word = "abbbbab";
		assertThat(matcher.containsPatternI(word.toCharArray(), 'a'), is(false));
	}

	@Test
	public void testPatternIa4() {
		String word = "abbabbbaa";
		assertThat(matcher.containsPatternI(word.toCharArray(), 'a'), is(true));
	}

	@Test
	public void testPatternIIa1() {
		String word = "abbaa";
		assertThat(matcher.containsPatternII(word.toCharArray(), 'a', 'b'), is(true));
	}

	@Test
	public void testPatternIIa2() {
		// w = bbb, k = 1
		String word = "abbbbbabbba";
		assertThat(matcher.containsPatternII(word.toCharArray(), 'a', 'b'), is(true));
	}

	@Test
	public void testPatternIIa3() {
		// w = bab, k = 2
		String word = "abbabbababbaba";
		assertThat(matcher.containsPatternII(word.toCharArray(), 'a', 'b'), is(true));
	}

	@Test
	public void testPatternIIa4() {
		// prefix = aaaaaaa, w = bab, k = 2
		String word = "aaaaaaaabbabbababbaba";
		assertThat(matcher.containsPatternII(word.toCharArray(), 'a', 'b'), is(true));
	}

	@Test
	public void testPatternIIa5() {
		String word = "abaaa";
		assertThat(matcher.containsPatternII(word.toCharArray(), 'a', 'b'), is(true));
	}

	@Test
	public void testKmpFailureFunction1() {
		char[] pattern = "ACACAGT".toCharArray();
		int[] borderTable = matcher.kmpFailureFunction(pattern, 0);
		assertThat(box(borderTable), arrayContaining(0, 0, 1, 2, 3, 0, 0));
	}

	@Test
	public void testKmpFailureFunction2() {
		char[] pattern = "012ACACAGT".toCharArray();
		int[] borderTable = matcher.kmpFailureFunction(pattern, 3);
		assertThat(box(borderTable), arrayContaining(0, 0, 1, 2, 3, 0, 0));
	}

	@Test
	public void testKmpFailureFunction3() {
		char[] pattern = "PARTICIPATE IN PARACHUTE".toCharArray();
		int[] borderTable = matcher.kmpFailureFunction(pattern, 0);
		assertThat(box(borderTable), arrayContaining(0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 1, 2, 3, 0, 0,
				0, 0, 0, 0));
	}

	private Integer[] box(int[] array) {
		return IntStream.of(array).boxed().toArray(Integer[]::new);
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
