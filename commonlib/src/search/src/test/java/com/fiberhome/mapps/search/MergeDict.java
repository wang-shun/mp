package com.fiberhome.mapps.search;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import org.springframework.util.StringUtils;

public class MergeDict {
	public static void main(String[] args) throws IOException {
		List<String> dic1 = getDic("data/words.dic");
		List<String> dic2 = getDic("data/words-t-base.dic");
		
		HashSet<String> set = new HashSet<>();
		set.addAll(dic1);
		set.addAll(dic2);
		
		TreeSet<String> ts = new TreeSet(set);
		
		FileOutputStream fos = new FileOutputStream("e:/words.dic");
		
		for(String kw : ts) {
			if (kw != null) {
				fos.write(kw.getBytes("UTF8"));
				fos.write(new byte[]{0x0a});
			}
		}
		
		fos.close();
	}
	
	private static List<String> getDic(String resource) throws IOException {
		ArrayList<String> list = new ArrayList<>();
		
		ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();
		BufferedReader br = new BufferedReader(new InputStreamReader(clzLoader.getResourceAsStream(resource)));
		
		String kw = "";
		do {
			kw = br.readLine();
			if (StringUtils.isEmpty(kw)) {
				break;
			}
			list.add(kw);
		} while (true);
		
		br.close();
		
		return list;
	}
}
