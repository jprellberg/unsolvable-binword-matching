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

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Thread)
public class UnsolvableBinwordMatcherBenchmark {

	@Param({ "JavaRegexUBM", "PatternUBM" })
	public String implementation;

	@Param({ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20" })
	public int wordLength;

	private UnsolvableBinwordMatcher matcher;
	private List<String> words;

	@Setup(Level.Trial)
	public void setup() {
		switch (implementation) {
		case "JavaRegexUBM":
			matcher = new JavaRegexUBM();
			break;
		case "PatternUBM":
			matcher = new PatternUBM();
			break;
		default:
			throw new RuntimeException("Unhandled parameter value");
		}
		words = new BinaryWordList().getWordsOfLength(wordLength);
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	public void measureIsUnsolvableWord(Blackhole bh) {
		for (String word : words) {
			bh.consume(matcher.isUnsolvableBinaryWord(word));
		}
	}

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
