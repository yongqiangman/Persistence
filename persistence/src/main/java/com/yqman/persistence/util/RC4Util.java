package com.yqman.persistence.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 */
public class RC4Util {

    private static final String CRYPT_NAME = "RC4";
    private static final String DEFAULT_STRING_ENCODE = "UTF-8";

    private static String mKey = "";

    private static String getRC4Key() {
        if (isEmpty(mKey)) {
            mKey = "manyongqiang_rc_4_util";
        }
        return mKey;
    }

    private static boolean isEmpty(String src) {
        return src == null || src.isEmpty();
    }

    /**
     * 通过系统默认key加密
     *
     * @param data 加密数据byte[]
     *
     * @return 加密后的数据
     */
    public static byte[] encrypt(byte[] data) {
        return handle(data, getRC4Key());
    }

    /**
     * 通过系统默认key解密
     *
     * @param date 加密后的数据
     *
     * @return 解密后的数据
     */
    public static byte[] decrypt(byte[] date) {
        return handle(date, RC4Util.getRC4Key());
    }

    public static byte[] makeRc4WithCharset(String aInput, String charsetName) {
        return handle(aInput, RC4Util.getRC4Key(), charsetName);
    }

    /**
     * 基础加解密方法
     */
    public static byte[] handle(byte[] iInputChar, String key) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i = 0; i < 256; i++) {
            iS[i] = i;
        }

        int j = 1;

        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) key.charAt((i % key.length()));
        }

        j = 0;

        for (int i = 0; i < 256; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }

        int i = 0;
        j = 0;
        byte[] iOutputByte = new byte[iInputChar.length];
        for (int x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputByte[x] = (byte) (iInputChar[x] ^ iCY);
        }

        return iOutputByte;
    }

    public static byte[] handle(String aInput, String key, String charsetName) {
        byte[] iInputChar = null;
        try {
            iInputChar = aInput.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            iInputChar = aInput.getBytes();
        }
        return handle(iInputChar, key);
    }

    public static byte[] makeRc4(String aInput) {
        return makeRc4(aInput, RC4Util.getRC4Key());
    }

    public static byte[] makeRc4(String aInput, String key) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i = 0; i < 256; i++) {
            iS[i] = i;
        }

        int j = 1;

        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) key.charAt((i % key.length()));
        }

        j = 0;

        for (int i = 0; i < 256; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }

        int i = 0;
        j = 0;
        byte[] iInputChar = aInput.getBytes();
        byte[] iOutputByte = new byte[iInputChar.length];
        for (int x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputByte[x] = (byte) (iInputChar[x] ^ iCY);
        }

        return iOutputByte;
    }

    /**
     * 只在闪电互传中使用
     */
    public static byte[] makeP2PShareRc4(String aInput, String key) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i = 0; i < 256; i++) {
            iS[i] = i;
        }

        int j = 1;

        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) key.charAt((i % key.length()));
        }

        j = 0;

        for (int i = 0; i < 256; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }

        int i = 0;
        j = 0;
        // 和Handle方法的不同，byte和char
        char[] iInputChar = aInput.toCharArray();
        byte[] iOutputByte = new byte[iInputChar.length];
        for (int x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputByte[x] = (byte) (iInputChar[x] ^ iCY);
        }

        return iOutputByte;
    }

    public static String encryptRC4EWithSystem(String data, byte[] keyData) throws Exception {
        Cipher cipher = Cipher.getInstance(CRYPT_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyData, CRYPT_NAME));
        byte[] cdata = cipher.update(data.getBytes(DEFAULT_STRING_ENCODE));
        return Base64Util.encodeAppendEquals(cdata);
    }

    public static String decryptRC4WithSystem(String data, byte[] keyData) throws Exception {
        Cipher cipher = Cipher.getInstance(CRYPT_NAME);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyData, CRYPT_NAME));
        return new String(cipher.update(Base64Util.decode(data)), DEFAULT_STRING_ENCODE);
    }
}
