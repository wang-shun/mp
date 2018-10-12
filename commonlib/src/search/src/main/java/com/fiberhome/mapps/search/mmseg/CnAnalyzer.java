package com.fiberhome.mapps.search.mmseg;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Tokenizer;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

public class CnAnalyzer extends MMSegAnalyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Reader reader = new BufferedReader(new StringReader(fieldName));
		Tokenizer tokenizer = new MMSegTokenizer(newSeg(), reader);
		return new TokenStreamComponents(tokenizer);
	}

}
