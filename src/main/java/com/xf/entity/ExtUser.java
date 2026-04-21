package com.xf.entity;

import lombok.Data;

/**
 * @author tanjunjie
 * @version 1.0
 * @className ExtUser
 * @date 2025/8/22 15:34
 * @description TODO
 */
@Data
public class ExtUser extends User {

    private String extName;
    private Integer extAge;
    private String extAddress;

    public ExtUser() {
    }

    public ExtUser(String name) {
        this.setName(name);
    }
}
