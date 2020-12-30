package com.ubb.ppd.utils;

import lombok.NonNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {
    private static MD5Hash instance = null;

    private final MessageDigest messageDigest;

    private MD5Hash() throws NoSuchAlgorithmException {
        this.messageDigest = MessageDigest.getInstance("MD5");
    }

    public static MD5Hash getInstance() {
        if (instance == null) {
            synchronized (MD5Hash.class) {
                if (instance == null) {
                    try {
                        instance = new MD5Hash();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return instance;
    }

    public String getMD5Hash(@NonNull String data) {
        byte[] bytes = this.messageDigest.digest(data.getBytes());
        BigInteger bigInteger = new BigInteger(1, bytes);
        return bigInteger.toString(16);
    }
}
