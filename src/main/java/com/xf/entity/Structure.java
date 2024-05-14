package com.xf.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * structure
 * @author 
 */
@Data
public class Structure implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date recordCreateDate;

    private Date recordUpdateDate;

    private String code;

    private String name;

    private Integer state;

    private Boolean leaf;

    private Long parentId;

    private String treeId;

    private Integer treeLevel;

    private String buildCompany;

    private String completeDate;

    private String componentCode;

    private String coordinate;

    private Date createTime;

    private Long createUser;

    private String direction;

    private Long docId;

    private Integer end;

    private String endMileage;

    private Double endMileageValue;

    private String ex1;

    private String ex2;

    private String ex3;

    private String ex4;

    private String ex5;

    private String ex6;

    private String ex7;

    private String ex8;

    private Long imageDocId;

    private Double latitude;

    private Long level;

    private String location;

    private Double longitude;

    private String modelId;

    private String modelViewScript;

    private String position;

    private Long projectId;

    private Long projectRoadId;

    private String serialNumber;

    private Integer start;

    private String startMileage;

    private Double startMileageValue;

    private String structureType;

    private String type;

    private Date updateTime;

    private Long updateUser;

}