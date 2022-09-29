package advent.y2016;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day14 {
	static final String SALT = "zpqevtbw";
    static final char[] digits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    static final char NO_TRIPLE = 'X';

	static MessageDigest digester;

	private static class Candidate {
		final char tri;
		final int candidateIndex;
		final String value;

		public Candidate(char tripledLetter, int index, String md5){
			tri = tripledLetter;
			candidateIndex = index;
			value = md5;
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		digester = MessageDigest.getInstance("MD5");
		int index = 0;

		List<Candidate> candidates = new ArrayList<>();
	    List<Candidate> keys = new ArrayList<>();

	    while (keys.size() < 64) {
	    	String candidate = md5(SALT + index);

	    	List<Candidate> expired = new ArrayList<>();
	    	for (Candidate c : candidates) {
	    		if (c.candidateIndex + 1000 < index) {
	    			expired.add(c);
	    			continue;
	    		}
	    		String quint = new String(new char[5]).replace('\0', c.tri);
	    		if (candidate.contains(quint)) {
	    			keys.add(c);
	    			expired.add(c);
	    			Util.log("found key at %d; quint at %d", c.candidateIndex, index);
	    		}
	    	}
	    	candidates.removeAll(expired);

	    	char triple = findTriple(candidate);
	    	if (triple != NO_TRIPLE) {
	    		candidates.add(new Candidate(triple, index, candidate));
	    	}

	    	++index;
	    }

	    Util.log("found %d", keys.size());
	}

	private static char findTriple(String candidate) {
		for (char c : digits) {
			String triple = new String(new char[3]).replace('\0', c);
			if (candidate.contains(triple)) {
				return c;
			}
		}

		return NO_TRIPLE;
	}

	private static String md5(String string) {
		byte[] md5 = digester.digest(string.getBytes());
		return Util.getHex(md5).toLowerCase();
	}
}
