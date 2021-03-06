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

/**
 * Implementation that uses counting and the Knuth-Morris-Pratt failure function
 * for pattern matching.
 */
public class PatternUBM implements UnsolvableBinwordMatcher {

	@Override
	public boolean isUnsolvableBinaryWord(String word) {
		if (word.isEmpty()) {
			return false;
		} else {
			char[] chars = word.toCharArray();
			return step1(chars) || step2(chars);
		}
	}

	/**
	 * Returns true if pattern <code>Ia</code> or pattern <code>Ib</code>
	 * matches the given word.
	 *
	 * <pre>
	 * Ia: ab<sup>x</sup>ab<sup>y</sup>a
	 *
	 * Ib: ba<sup>x</sup>ba<sup>y</sup>b
	 *
	 * where x > y + 2, y ≥ 0
	 *
	 * <pre>
	 *
	 * @param word
	 *                input string that is being searched
	 * @return true if a match is found, i.e. if the word contains an
	 *         unsolvable factor
	 */
	boolean step1(final char[] word) {
		return containsPatternI(word, 'a') || containsPatternI(word, 'b');
	}

	/**
	 * Helper function that checks if the given word contains pattern I with
	 * the given delimiter.
	 *
	 * @param word
	 *                input string that is being searched
	 * @param delim
	 *                character of pattern I that separates the blocks that
	 *                need to be counted
	 * @return true if the input word contains a subsequence of pattern I
	 */
	boolean containsPatternI(final char[] word, final char delim) {
		int delimiterCount = 0;

		int firstDelimIndex = word.length;
		for (int i = 0; i < word.length; i++) {
			if (word[i] == delim) {
				firstDelimIndex = i;
				delimiterCount += 1;
				break;
			}

		}

		int x = 0;
		int y = 0;

		for (int i = firstDelimIndex + 1; i < word.length; i++) {
			if (word[i] == delim) {
				delimiterCount += 1;
				if (x > y + 2 && delimiterCount >= 3) {
					return true;
				} else {
					x = y;
					y = 0;
				}
			} else {
				y = y + 1;
			}
		}

		return false;
	}

	/**
	 * Returns true if pattern <code>IIa</code> or pattern <code>IIb</code>
	 * matches the given word.
	 *
	 * <pre>
	 * IIa: abw(baw)<sup>k</sup>a
	 *
	 * IIb: baw(abw)<sup>k</sup>b
	 *
	 * where k ≥ 1, w ∈ {a, b}*
	 *
	 * <pre>
	 *
	 * @param word
	 *                input string that is being searched
	 * @return true if a match is found, i.e. if the word contains an
	 *         unsolvable factor
	 */
	boolean step2(char[] word) {
		return containsPatternII(word, 'a', 'b') || containsPatternII(word, 'b', 'a');
	}

	/**
	 * Helper function that checks if the given word contains pattern II
	 * with the given characters a and b.
	 *
	 * @param word
	 *                input string that is being searched
	 * @param a
	 *                character 'a'
	 * @param character
	 *                'b'
	 * @return true if the input word contains a subsequence of pattern II
	 */
	boolean containsPatternII(char[] word, final char a, final char b) {
		for (int i = 0; i < word.length - 1; i++) {
			// When v[i..i+1] = ab is found...
			if (word[i] == a && word[i + 1] == b) {
				// Temporarily swap v[i] and v[i+1]
				word[i] = b;
				word[i + 1] = a;

				// Build border table for suffix of v starting
				// at i (v[i..]) with KMP
				int[] f = kmpFailureFunction(word, i);
				// Search for a repeating string s in v[i..j] with v[i..j] = s^n
				for (int j = i + 1; j < f.length - 1; j++) {
					// There is a repetition when the period length divides the string length (j - i + 1)
					if (f[j] > 1 && (j - i + 1) % (j - i + 1 - f[j]) == 0 && word[j + 1] == a) {
						// Reverse temporary swap
						word[i] = a;
						word[i + 1] = b;
						return true;
					}
				}

				// Reverse temporary swap
				word[i] = a;
				word[i + 1] = b;
			}
		}
		return false;
	}

	/**
	 * Returns the Knuth-Morris-Pratt failure function (also called border
	 * table) for the given pattern which starts at the given index. The
	 * returned array contains undefined values for any indices smaller than
	 * pIdx.
	 *
	 * @param p
	 *                char array that contains the pattern as a suffix
	 * @param pIdx
	 *                index at which the pattern starts in the pattern char
	 *                array
	 * @return 0-indexed border table/failure function with length =
	 *         p.length
	 */
	int[] kmpFailureFunction(char[] p, int pIdx) {
		int[] f = new int[p.length];
		f[pIdx] = 0;

		int i = 1;
		int j = 0;

		int patternLength = p.length - pIdx;
		while (i < patternLength) {
			if (p[pIdx + i] == p[pIdx + j]) {
				f[pIdx + i] = j + 1;
				i = i + 1;
				j = j + 1;
			} else if (j == 0) {
				f[pIdx + i] = 0;
				i = i + 1;
			} else {
				j = f[pIdx + j - 1];
			}
		}

		return f;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
