package com.xf.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 区块链
 */
public class Blockchain {
 
    private int difficulty;
     
    private List<Block> chain = new ArrayList<>();
     
    public Blockchain() {
        chain.add(createGenesis());
        this.difficulty = 4;
    }
     
    /**
     * 创建创世区块
     * @return
     */
    public Block createGenesis() {
        Block block = new Block(0, System.currentTimeMillis(), "Genesis block", "0");
        return block;
    }
     
    /**
     * 获得最新的区块
     * @return
     */
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }
     
    /**
     * 增加新区块
     * @param newBlock
     */
    public void addBlock(Block newBlock) {
        newBlock.setPreviousHash(getLatestBlock().getHash());
        newBlock.setHash();
        newBlock.mimeBlock(difficulty);
        chain.add(newBlock);
    }
     
    /**
     * 检查区块链的有效性
     * @return
     */
    public boolean checkValid() {
        for (int i=1; i<chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);
            //检查当前区块哈希是否发生变化
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            //检查前一区块哈希是否发生变化
            if (!currentBlock.getPreviousHash().equals(previousBlock.calculateHash())) {
                return false;
            }
        }
        return true;
    }
     
}