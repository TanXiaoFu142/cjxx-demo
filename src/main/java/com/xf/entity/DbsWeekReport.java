package com.xf.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 李伟高
 * Date: 2020/07/15
 * Time: 16:56
 *
 * BIM周报 
 * BIM周报
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BIM周报", description = "BIM周报")
public class DbsWeekReport extends MasterEntity<String> {

    private static final long serialVersionUID = 1132L;



    /**
     * 项目ID by 李伟高 on 2020/07/15 16:59
     * 项目ID
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", example = "1")
    private Long projectId;

    /**
     * 开始时间 by 李伟高 on 2020/07/15 17:06
     * 开始时间
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", example = "2020-01-01")
    private Date startDate;

    /**
     * 结束时间 by 李伟高 on 2020/07/15 17:06
     * 结束时间
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", example = "2020-01-01")
    private Date endDate;

    /**
     * 发布日期 by 李伟高 on 2020/07/15 17:07
     * 发布日期
     * 发布日期
     */
    @ApiModelProperty(value = "发布日期", example = "2020-01-01")
    private Date deployDate;

    /**
     * 发布用户ID by 李伟高 on 2020/07/15 17:09
     * 发布用户ID
     * 发布用户ID
     */
    @ApiModelProperty(value = "发布用户ID", example = "1")
    private Long deployUserId;

    /**
     * 发布用户 by 李伟高 on 2020/07/15 17:09
     * 发布用户
     * 发布用户
     */
    @ApiModelProperty(value = "发布用户", example = "发布用户")
    private String deployUser;

    /**
     * 发布单位ID by 李伟高 on 2020/07/15 17:39
     * 发布单位
     * 发布单位
     */
    @ApiModelProperty(value = "发布单位ID", example = "1")
    private Long deployOrgId;

    /**
     * 发布单位 by 李伟高 on 2020/07/15 17:40
     * 发布单位
     * 发布单位
     */
    @ApiModelProperty(value = "发布单位", example = "发布单位")
    private String deployOrg;

    /**
     * 状态 by 李伟高 on 2020/07/15 17:42
     * 状态
     * 状态
     */
    @ApiModelProperty(value = "状态", example = "ysh")
    private String status;

    /**
     * 附件 by 李伟高 on 2020/07/16 10:16
     * 附件
     * 附件
     */
    @ApiModelProperty(value = "附件", example = "1")
    private Long docId;

    /**
     * 标段ID by 谭俊杰 on 2023/10/13 15:01
     * 标段ID
     */
    @ApiModelProperty(value = "标段ID【标段ID】", example = "1")
    private Long tendersId;

    @ApiModelProperty(value = "BIM周月报标题")
    private List<DbsReportsData> dbsReportsDataList;

}