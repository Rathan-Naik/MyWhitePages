package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class MD5Util {
	
	public static String getHashValue(String stringToHash)
	{
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

		}
		messageDigest.update(stringToHash.getBytes());
		byte[] digiest = messageDigest.digest();
		String hashedOutput = DatatypeConverter.printHexBinary(digiest);

		return hashedOutput;
	}
	
	public static boolean doPasswordsMatch(String stringToHash, String md5Hash)
	{
		boolean match = false;
		String hashValue = getHashValue(stringToHash);
		if(hashValue.equals(md5Hash)){
			match = true;
		} 
		return match;
	}
}
