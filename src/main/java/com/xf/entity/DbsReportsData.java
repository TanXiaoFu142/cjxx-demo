package com.xf.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 李伟高
 * Date: 2020/07/15
 * Time: 17:03
 *
 * BIM周报数据表 
 * BIM周报数据表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BIM周报数据表", description = "BIM周报数据表")
public class DbsReportsData extends MasterEntity<String> {

    private static final long serialVersionUID = 1134L;

    /**
     * BIM周报ID by 李伟高 on 2020/07/15 18:00
     * BIM周报ID
     * BIM周报ID
     */
    @ApiModelProperty(value = "BIM周报ID", example = "1")
    private Long dbsReportId;

    /**
     * 内容 by 李伟高 on 2020/07/15 18:01
     * 内容
     * 内容
     */
    @ApiModelProperty(value = "内容", example = "内容")
    private String content;

    /**
     * 周报数据模板ID by 李伟高 on 2020/07/16 10:26
     * 周报数据模板ID
     * 周报数据模板ID
     */
    @ApiModelProperty(value = "周报数据模板ID", example = "1")
    private Long reportTemplateId;

    /**
     * 上报单位ID by 李伟高 on 2020/07/16 13:57
     * 上报单位ID
     * 上报单位ID
     */
    @ApiModelProperty(value = "上报单位ID", example = "1")
    private Long deployOrgId;

    /**
     * 上报单位 by 李伟高 on 2020/07/16 13:57
     * 上报单位
     * 上报单位
     */
    @ApiModelProperty(value = "上报单位", example = "上报单位")
    private String deployOrg;

    /**
     * 样式 by 李伟高 on 2020/07/16 18:18
     * 样式
     * 样式
     */
    @ApiModelProperty(value = "样式", example = "true")
    private Boolean style;

    /**
     * 开始日期 by 谭俊杰 on 2023/10/16 15:56
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期【开始日期】", example = "yyyy-MM-dd")
    private String startDate;

    /**
     * 结束日期 by 谭俊杰 on 2023/10/16 15:57
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期【结束日期】", example = "yyyy-MM-dd")
    private String endDate;

    @ApiModelProperty(value = "BIM周报数据")
    private List<DbsSituationData> situationDataList;



}