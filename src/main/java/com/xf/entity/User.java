package com.xf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author tanjunjie
 * @version 1.0
 * @className User
 * @date 2024/9/12 17:37
 * @description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    private String name;

    private String sex;

    private Integer age;
}
