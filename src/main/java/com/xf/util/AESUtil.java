package com.xf.util;

import cn.hutool.core.util.IdcardUtil;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

import static cn.hutool.core.codec.Base64.isBase64;

@Slf4j
public class AESUtil {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    // 128位认证标签
    private static final int GCM_TAG_LENGTH = 128;
    // 12字节IV（推荐）
    private static final int IV_LENGTH = 12;
    // 32字节（AES-256）
    private static final String KEY = "jiaxing@tietou?jianguanpingtai!!";

    /**
     * 加密
     * @param data 数据
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encrypt(String data) {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        byte[] iv = new byte[IV_LENGTH];
        // 生成随机IV
        new SecureRandom().nextBytes(iv);

        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            encrypted = cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(concat(iv, encrypted));
    }

    /**
     * 解密
     * @param encryptedData 加密后的数据
     * @return 加密之前的数据
     * @throws Exception
     */
    public static String decrypt(String encryptedData){
        byte[] combined = Base64.getDecoder().decode(encryptedData);
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, IV_LENGTH);

        byte[] encrypted = new byte[combined.length - IV_LENGTH];
        System.arraycopy(combined, IV_LENGTH, encrypted, 0, encrypted.length);

        try {
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            return new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将两个字节数组连接成一个新的字节数组
     *
     * @param a 第一个字节数组，不为null
     * @param b 第二个字节数组，不为null
     * @return 返回一个新的字节数组，包含a和b的所有元素
     */
    private static byte[] concat(byte[] a, byte[] b) {
        // 创建一个新的字节数组，长度为两个输入数组长度之和
        byte[] result = new byte[a.length + b.length];

        // 将第一个字节数组的内容复制到结果数组的前半部分
        System.arraycopy(a, 0, result, 0, a.length);

        // 将第二个字节数组的内容复制到结果数组的后半部分
        System.arraycopy(b, 0, result, a.length, b.length);

        // 返回合并后的字节数组
        return result;
    }

    /**
     * 安全解密方法（结合格式验证）
     * @param input 待解密的数据
     * @return 解密后数据
     */
    public static String decryptSafely(String input) {
        // 1. 判断是否为身份证明文
        if (IdcardUtil.isValidCard(input)) {
            return input;
        }

        // 2. 判断是否为 Base64 编码的密文
        if (!isBase64(input)) {
            return input;
        }

        // 3. 尝试解密
        try {
            return decrypt(input);
        } catch (Exception e) {
            // 记录日志（建议）
            log.error("解密失败，返回原文。输入: " + input);
            return input;
        }
    }

    public static void main(String[] args) {
        String idNumber = "110101199003072516";
        // 加密后的密文
        String encrypted = AESUtil.encrypt(idNumber);
        System.out.println("加密后的密文：" + encrypted);
        // 场景1：输入是密文
        System.out.println("解密密文："+AESUtil.decryptSafely(encrypted));

        // 场景2：输入是明文
        System.out.println("解密明文："+AESUtil.decryptSafely(idNumber));

        // 场景3：输入是无效数据
        System.out.println("解密无效数据"+AESUtil.decryptSafely("invalid_data"));
    }
}