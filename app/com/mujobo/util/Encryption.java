package com.mujobo.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: eguller
 * Date: 2/24/13
 * Time: 1:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class Encryption {
    private static final BASE64Encoder BASE64ENCODE = new BASE64Encoder();
    public static String md5(String input){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(BASE64ENCODE.encode(md.digest(input.getBytes())));
    }
}
