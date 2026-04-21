package com.xf.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 区块对象
 */
public class Block {
 
    /**
     * 区块索引值
     */
    private Object index;
     
    /**
     * 时间戳
     */
    private Object timestamp;
     
    /**
     * 区块交易数据
     */
    private Object data;
 
    /**
     * 前一区块的哈希
     */
    private String previousHash;
 
    /**
     * 区块哈希
     */
    private String hash;
     
    private long nonce = 0;
     
    public Block(Object index, Object timestamp, Object data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
        this.nonce = 0;
    }
 
    public Block(Object index, Object timestamp, Object data) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
    }
     
    /**
     * 计算区块哈希
     * @return
     */
    public String calculateHash() {
        try {
            return this.SHA256(this.index + this.previousHash + this.timestamp + this.data + this.nonce);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
     
    /**
     * 挖矿-计算一个特殊的哈希值
     * @param difficulty 难度值
     */
    public void mimeBlock(int difficulty) {
        //每次循环会从0索引到给定的难度值，取哈希值的字符串，如果该子字符串不等于给定哈希值0的个数，则继续进行，nonce值加1，如果相等则挖矿成功。
        String str = "";
        for (int i=0; i<difficulty; i++) {
            str = str + "0";
        }
//      System.out.println(str);
        while (!this.hash.substring(0, difficulty).equals(str)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined:" + this.hash);
    }
     
    private String SHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append("0");
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
     
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
     
    public String getPreviousHash() {
        return previousHash;
    }
     
    public void setHash() {
        this.hash = calculateHash();
    }
     
    public String getHash() {
        return hash;
    }
     
}