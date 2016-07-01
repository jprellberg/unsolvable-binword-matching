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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation that uses Java Regex facilities.
 */
public class JavaRegexUBM implements UnsolvableBinwordMatcher {

	private final Pattern patternA = Pattern.compile("ab([ab]*)b*(ba\\1)+a");
	private final Pattern patternB = Pattern.compile("ba([ba]*)a*(ab\\1)+b");

	@Override
	public boolean isUnsolvableBinaryWord(String word) {
		final Matcher matcherA = patternA.matcher(word);
		final Matcher matcherB = patternB.matcher(word);
		return matcherA.matches() || matcherB.matches();
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
