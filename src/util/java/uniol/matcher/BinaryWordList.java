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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryWordList {

	public List<String> getWordsOfLength(int length) {
		int count = (int) Math.pow(2, length);
		List<String> words = new ArrayList<>(count);

		for (int i = 0; i < count; i++) {
			String word = "";
			for (int j = 0; j < length; j++) {
				if ((i & (1 << j)) == 0) {
					word += "a";
				} else {
					word += "b";
				}
			}
			words.add(word);
		}

		return words;
	}

	public List<String> getUnsolvableWords() {
		try {
			return readWordFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getUnsolvableWordsOfLength(int length) {
		List<String> allWords = getUnsolvableWords();
		return allWords.stream().filter(word -> word.length() == length).collect(Collectors.toList());
	}

	private List<String> readWordFile() throws IOException {
		InputStream is = BinaryWordList.class.getResourceAsStream("/binary-nonsyntetizable-words");
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

}

// vim: ft=java:noet:sw=8:sts=8:ts=8:tw=120
