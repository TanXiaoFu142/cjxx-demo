package com.xf.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础类模型
 * 
 * @author joe.xie
 *
 * @param <CODE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MasterEntity<CODE extends Serializable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2859718235308439898L;

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_STATE = "state";

	public static final String ATTRIBUTE_NAME = "name";
	public static final String ATTRIBUTE_STATE = "state";

	@ApiModelProperty(value = "通用字段：名称", example = "entity name")
	private String name;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "通用字段：数据状态，1用作逻辑删除", example = "0")
	private Integer state;

}
