//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xf;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Utils {
    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);
    protected static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static MessageDigest messagedigest = null;

    public MD5Utils() {
    }

    public static String MD5(String source) {
        return MD5(source.getBytes()).toUpperCase();
    }

    public static String MD5(String... sources) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < sources.length; ++i) {
            sb.append(sources[i]);
        }

        return MD5(sb.toString().getBytes()).toUpperCase();
    }

    public static String MD5(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;

        for(int l = m; l < k; ++l) {
            appendHexPair(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 240) >> 4];
        char c1 = hexDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static boolean compareTo(String source, String md5Str) {
        String s = MD5(source);
        return s.equalsIgnoreCase(md5Str);
    }

    public static void main(String[] args) throws IOException {
        long begin = System.currentTimeMillis();
        String md5 = MD5("D:\\temp\\jre-7u11-linux-i586.tar.gz");
        long end = System.currentTimeMillis();
        System.out.println("md5:" + md5);
        System.out.println("md5_2:" + MD5(begin + ""));
        System.out.println("time:" + (end - begin) / 1000L + "s");
    }

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            logger.error("MD5FileUtil messagedigest初始化失败", var1);
        }

    }
}
