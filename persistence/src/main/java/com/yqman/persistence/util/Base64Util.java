/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yqman.persistence.util;

import java.io.ByteArrayOutputStream;

/**
 * 编码处理类
 */
public class Base64Util {
    /**
     * Base64 编码默认的字符集，码表
     **/
    private static final String[] CHARACTER_SET =
            {"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"};
    private static final int DEFAULT_KEY_INDEX = 0;

    /**
     * 把字符串进行base64编码，变成字符串
     *
     * @param data 需要编码的字符串编码前通过字符串的getBytes进行byte转换，默认编码为系统编码,系统编码与System Property 的"file.encoding"字段相同，一般默认为UTF-8
     */
    public static String encode(String data) {
        if (isEmpty(data)) {
            return null;
        }
        return encode(data.getBytes());
    }

    /**
     * 把byte数组进行base64编码，变成字符串
     *
     * @param data byte数组
     *
     * @return
     */
    public static String encode(byte[] data) {
        if (data == null) {
            return null;
        }
        return encode(data, 0, data.length, null, DEFAULT_KEY_INDEX).toString();
    }

    /**
     * 把byte数组进行base64编码，变成字符串如果结果不是4的整数倍，补齐=号
     *
     * @param data byte数组
     */
    public static String encodeAppendEquals(byte[] data) {
        String result = encode(data, 0, data.length, null, DEFAULT_KEY_INDEX).toString();
        int remainder = result.length() % 4;
        if (remainder > 0) {
            for (int i = 0; i < 4 - remainder; i++) {
                result += "=";
            }
        }
        return result;
    }

    /**
     * 把一个进行base64编码的字符串进行解码还原成byte数组
     *
     * @param src 已经经过base64编码的字符串
     *
     * @return 解码后的byte[]
     */
    public static byte[] decode(String src) throws Exception {
        if (src == null) {
            return null;
        }
        byte[] temp;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            decode(src, bos);
            temp = bos.toByteArray();
            bos.close();
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
        return temp;
    }

    /**
     * 基础编码方法
     *
     * @param data  需要进行编码的byte数组
     * @param start 数组的起始位置
     * @param len   需要编码的长度
     * @param buf   StringBuffer，编码后的结果
     * @param key   码表索引一般用0
     *
     * @return StringBuffer 编码后的结果
     */
    private static StringBuffer encode(byte[] data, int start, int len, StringBuffer buf, int key) {
        char[] charT = CHARACTER_SET[key].toCharArray();

        if (buf == null) {
            buf = new StringBuffer(data.length * 3 / 2);
        }
        int end = len - 3;
        int i = start;
        while (i <= end) {
            int d =
                    ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8)
                            | (((int) data[i + 2]) & 0x0ff);
            buf.append(charT[(d >> 18) & 63]);
            buf.append(charT[(d >> 12) & 63]);
            buf.append(charT[(d >> 6) & 63]);
            buf.append(charT[d & 63]);
            i += 3;
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);
            buf.append(charT[(d >> 18) & 63]);
            buf.append(charT[(d >> 12) & 63]);
            buf.append(charT[(d >> 6) & 63]);
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;
            buf.append(charT[(d >> 18) & 63]);
            buf.append(charT[(d >> 12) & 63]);
        }
        return buf;
    }

    private static boolean isEmpty(String src) {
        return src == null || src.isEmpty();
    }

    /**
     * 基础解码方法
     *
     * @param src 需要解码的字符串
     * @param bos 解码后数据输出
     */
    private static void decode(String src, ByteArrayOutputStream bos) {
        if (isEmpty(src) || bos == null) {
            return;
        }
        int i = 0;
        int len = src.length();
        int key = 0;
        int addNum = (src.length()) % 4;
        if (addNum > 0) {
            addNum = 4 - addNum;
        }
        StringBuilder builder = new StringBuilder(src);
        while (addNum > 0) {
            builder.append('=');
            addNum--;
        }
        src = builder.toString();
        while (true) {
            while (i < len && src.charAt(i) <= ' ') {
                i++;
            }
            if (i == len) {
                break;
            }
            int tri =
                    (decode(src.charAt(i), key) << 18) + (decode(src.charAt(i + 1), key) << 12)
                            + (decode(src.charAt(i + 2), key) << 6) + (decode(src.charAt(i + 3), key));
            bos.write((tri >> 16) & 255);
            if (src.charAt(i + 2) == '=') {
                break;
            }
            bos.write((tri >> 8) & 255);
            if (src.charAt(i + 3) == '=') {
                break;
            }
            bos.write(tri & 255);
            i += 4;
        }
    }

    /**
     * 基础解码查询方法,查询字符在码表中的位置
     *
     * @param c   需要查询的字符
     * @param key 码表的索引
     *
     * @return 字符所在的位置
     */
    private static int decode(char c, int key) {
        char[] charT = CHARACTER_SET[key].toCharArray();
        if (c == '=') {
            return 0;
        }
        for (int i = 0; i < 64; i++) {
            if (charT[i] == c) {
                return i;
            }
        }
        throw new RuntimeException("unexpected code: " + c);
    }

    public static boolean isNeedBase64(String src) {
        if (isEmpty(src)) {
            return false;
        }
        int srcLength = src.length();
        for (int i = srcLength - 1; i >= 0; i--) {
            if (src.charAt(i) == '-') {
                continue;
            }
            if (src.charAt(i) > 0x7A || (src.charAt(i) < 0x61 & src.charAt(i) > 0x5A)
                    || (src.charAt(i) < 0x41 & src.charAt(i) > 0x39) || src.charAt(i) < 0x30) {
                return true;
            }
        }
        return false;
    }

}