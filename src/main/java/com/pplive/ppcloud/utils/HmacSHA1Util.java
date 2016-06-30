package com.pplive.ppcloud.utils;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA1Util {
	public static final String ALGORITHM = "HmacSHA1";
    
    public static byte[] generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }
     
    public static byte[] generateKey(byte[] seed) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(new SecureRandom(seed));
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }
     
    public static byte[] encode(byte[] content, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(content);
    }
    
    public static byte[] getSignatureBytes(String content, String key) throws Exception {
		return encode(content.getBytes(), key.getBytes());
	}
     
    public static void main(String[] args) throws Exception {
         
        byte[] key = generateKey();
        System.out.println(Arrays.toString(key));
        System.out.println(key.length);
        System.out.println(Arrays.toString(encode("兼容了MD和SHA算法的特性，并在此基础上加上了密钥。".getBytes(), key)));
         
        /*
         * 控制台输出：
         *
         * [49, -56, 99, 31, -61, 125, -108, 27, 82, -1, 95, -88, 17, 11, 39, 59, 24, 11, 97, 83, -126, 62, -69, 63, -40, 90, -61, 73, 68, -9, 81, -6, 43, -8, 69, 113, 63, 79, -122, -64, 36, -32, -17, -121, -15, -83, -76, 17, -75, -74, -97, 104, -95, -9, -107, -30, -76, -68, 24, 111, 124, 21, -108, 53]
         * 64
         * [-7, -11, -3, -91, -118, 102, 60, 5, -92, 105, 70, 69, -1, -99, -10, 38, -80, 20, -112, -1]
         */
    }
}
