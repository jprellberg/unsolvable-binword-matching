/*-
 * Unsolvable Binary Word Matching
 * Copyright (C) 2016 Jonas Prellberg
 * Copyright (C) 2015 Harro Wimmel
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
 * Letter counting algorithm based on an implementation by Harro Wimmel.
 */
public class LetterCountingUBM implements UnsolvableBinwordMatcher {

	/**
	 * This class is for saving the computed intervals since they will be
	 * needed later to construct places that are common to several
	 * separation points.
	 */
	public static class Interval {
		int minNum;
		int minDen;
		int maxNum;
		int maxDen;

		public Interval() {
		}

		public void set(int num1, int den1, int num2, int den2) {
			this.minNum = num1;
			this.minDen = den1;
			this.maxNum = num2;
			this.maxDen = den2;
		}

		public boolean isInfinite() {
			return (minDen == 0);
		}

		public boolean isUnlimited() {
			return (maxDen == 0);
		}

		public float leftQuot() {
			return ((float) minNum / (float) minDen);
		}

		public float rightQuot() {
			if (maxDen == 0)
				return 0;
			return ((float) maxNum / (float) maxDen);
		}

		public int getMinNum() {
			return minNum;
		}

		public int getMinDen() {
			return minDen;
		}

		public int getMaxNum() {
			return maxNum;
		}

		public int getMaxDen() {
			if (maxDen == 0 && maxNum == 0)
				return 1; // replace 0/0 by 0/1
			return maxDen;
		}
	}

	@Override
	public boolean isUnsolvableBinaryWord(String word) {
		// word length
		int len = word.length();
		// position of the separation point in our word (n = before
		// position n)
		int seppt;

		// split off the last letter ("the letter to append")
		char inLetter = word.charAt(len - 1);
		--len;

		// create space to save the intervals in which the arc weights
		// have to reside
		Interval[] intervals = new Interval[len + 1];
		for (int i = 0; i < len + 1; ++i) {
			intervals[i] = new Interval();
		}

		// letter at the separation point
		char sepLetter = word.charAt(0);

		// temporary (de)nominators
		int tmpMinNum, tmpMinDen, tmpMaxNum, tmpMaxDen;
		// number of a's/b's in prefix or postfix at the separation
		// point
		int numa, numb;

		// for all possible separation points
		for (seppt = 1; seppt < len; ++seppt) {
			// letter allowed at the separation point
			sepLetter = word.charAt(seppt);

			// compute the interval boundary for the infixes ending
			// at the separation point this will be the upper
			// boundary of the interval
			numa = 0;
			numb = 0;
			tmpMinNum = 0;
			tmpMinDen = 0;
			for (int prefix = seppt - 1; prefix >= 0; --prefix) {
				if (word.charAt(prefix) == 'a') {
					++numa;
				} else {
					++numb;
				}
				if (sepLetter == word.charAt(prefix)) {
					continue;
				}
				if (sepLetter == 'a') {
					if (tmpMinNum == 0 || (tmpMinNum * numa > tmpMinDen * numb)) {
						tmpMinNum = numb;
						tmpMinDen = numa;
					}
				} else if (tmpMinNum == 0 || (tmpMinNum * numb > tmpMinDen * numa)) {
					tmpMinNum = numa;
					tmpMinDen = numb;
				}
			}

			// compute the interval boundary for the infixes
			// starting at the separation point
			// this will be the lower boundary of the interval
			numa = 0;
			numb = 0;
			tmpMaxNum = 0;
			tmpMaxDen = 0;
			for (int postfix = seppt; postfix < len; ++postfix) {
				if (word.charAt(postfix) == 'a') {
					++numa;
				} else {
					++numb;
				}
				if (postfix < len - 1 && sepLetter == word.charAt(postfix + 1)) {
					continue;
				}
				if (postfix == len - 1 && sepLetter == inLetter) {
					continue;
				}
				if (sepLetter == 'a') {
					if (tmpMaxDen == 0 || (tmpMaxNum * numa < tmpMaxDen * numb)) {
						tmpMaxNum = numb;
						tmpMaxDen = numa;
					}
				} else if (tmpMaxDen == 0 || (tmpMaxNum * numb < tmpMaxDen * numa)) {
					tmpMaxNum = numa;
					tmpMaxDen = numb;
				}
			}

			// save the interval
			intervals[seppt].set(tmpMinNum, tmpMinDen, tmpMaxNum, tmpMaxDen);
		}

		// write down which intervals have been handled
		// 0=not yet handled, 1=in process, 2=already done
		char[] check = new char[len];
		for (seppt = 0; seppt < len; ++seppt) {
			check[seppt] = 0;
		}

		// check for separation failures
		for (seppt = 1; seppt < len; ++seppt) {
			sepLetter = word.charAt(seppt);
			if (!intervals[seppt].isInfinite() && !intervals[seppt].isUnlimited()
					&& intervals[seppt].leftQuot() <= intervals[seppt].rightQuot()) {
				check[seppt] = 2;
				return true;
			}
		}

		return false;
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
