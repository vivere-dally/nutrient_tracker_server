package com.ubb.ppd.utils;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MD5HashTest {

    @Test
    void getMD5Hash() throws NoSuchAlgorithmException {
        String data = "11,21,31,41,51";
        String hash = MD5Hash.getInstance().getMD5Hash(data);
        assertEquals(hash, MD5Hash.getInstance().getMD5Hash(data));
    }

    @Test
    void getMD5Hash_() throws NoSuchAlgorithmException {
        int N = 100;
        String[] strings = new String[N], hashes = new String[N], hashes2 = new String[N];
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            byte[] array = new byte[N];
            random.nextBytes(array);
            strings[i] = new String(array, StandardCharsets.UTF_8);
            hashes[i] = MD5Hash.getInstance().getMD5Hash(strings[i]);
        }

        for (int i = 0; i < N; i++) {
            hashes2[i] = MD5Hash.getInstance().getMD5Hash(strings[i]);
        }

        assertArrayEquals(hashes, hashes2);
    }
}