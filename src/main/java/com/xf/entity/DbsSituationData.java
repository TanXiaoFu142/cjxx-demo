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
 * Time: 16:59
 *
 * BIM应用情况数据表 
 * BIM应用情况数据表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BIM应用情况数据表", description = "BIM应用情况数据表")
public class DbsSituationData extends MasterEntity<String> {

    private static final long serialVersionUID = 1133L;



    /**
     * 周报ID by 李伟高 on 2020/07/15 17:49
     * 周报ID
     * 周报ID
     */
    @ApiModelProperty(value = "周报ID", example = "1")
    private Long dbsReportId;

    /**
     * BIM周报数据ID by 李伟高 on 2020/07/15 17:55
     * BIM周报数据ID
     * BIM周报数据ID
     */
    @ApiModelProperty(value = "BIM周报数据ID", example = "1")
    private Long reportsDataId;

    /**
     * 工作内容 by 李伟高 on 2020/07/15 17:57
     * 工作内容
     * 工作内容
     */
    @ApiModelProperty(value = "工作内容", example = "工作内容")
    private String workContent;

    /**
     * 计划提交时间 by 李伟高 on 2020/07/15 17:58
     * 计划提交时间
     * 计划提交时间
     */
    @ApiModelProperty(value = "计划提交时间", example = "2020-01-01")
    private Date sumitDate;

    /**
     * 进展完成情况 by 李伟高 on 2020/07/15 17:59
     * 进展完成情况
     * 进展完成情况
     */
    @ApiModelProperty(value = "进展完成情况", example = "进展完成情况")
    private String completeSituation;

    /**
     * 上报单位ID by 李伟高 on 2020/07/16 14:02
     * 上报单位ID
     * 上报单位ID
     */
    @ApiModelProperty(value = "上报单位ID", example = "1")
    private Long deployOrgId;

    /**
     * 上报单位 by 李伟高 on 2020/07/16 14:02
     * 上报单位
     * 上报单位
     */
    @ApiModelProperty(value = "上报单位", example = "上报单位")
    private String deployOrg;

    /**
     * 责任人 by 谭俊杰 on 2023/10/13 18:03
     * 责任人
     */
    @ApiModelProperty(value = "责任人【责任人】", example = "1")
    private String responsible;

    /**
     * 备注 by 谭俊杰 on 2023/10/13 18:38
     * 备注
     */
    @ApiModelProperty(value = "备注【备注】", example = "1")
    private String remark;

    /**
     * 解决时间要求 by 谭俊杰 on 2023/10/13 18:19
     * 解决时间要求
     */
    @ApiModelProperty(value = "解决时间要求【解决时间要求】", example = "2023-10-13 18:19:25")
    private Date resolutionDate;

    /**
     * 问题内容 by 谭俊杰 on 2023/10/13 18:21
     * 问题内容
     */
    @ApiModelProperty(value = "问题内容【问题内容】", example = "1")
    private String questionContent;

    /**
     * 工作内容(无限制) by 谭俊杰 on 2023/10/13 18:36
     * 工作内容(无限制)
     */
    @ApiModelProperty(value = "工作内容(无限制)【工作内容(无限制)】", example = "1")
    private String workContentUnlimited;

    /**
     * 工作类型 by 谭俊杰 on 2023/10/16 11:53
     * 工作类型
     */
    @ApiModelProperty(value = "工作类型【工作类型】", example = "1")
    private String jobType;

    /**
     * 本月完成工作 by 谭俊杰 on 2023/10/16 11:54
     * 本月完成工作
     */
    @ApiModelProperty(value = "本月完成工作【本月完成工作】", example = "1")
    private String completeWorkMonth;

    /**
     * 完成工作情况 by 谭俊杰 on 2023/10/16 11:55
     * 完成工作情况
     */
    @ApiModelProperty(value = "完成工作情况【完成工作情况】", example = "1")
    private String workCompleted;

    /**
     * 原计划完成时间 by 谭俊杰 on 2023/10/16 11:56
     * 原计划完成时间
     */
    @ApiModelProperty(value = "原计划完成时间【原计划完成时间】", example = "2023-10-16 11:56:45")
    private Date originalPlannedCompletionTime;

    /**
     * 是否调整计划 by 谭俊杰 on 2023/10/16 12:00
     * 是否调整计划
     */
    @ApiModelProperty(value = "是否调整计划【是否调整计划】", example = "1")
    private String adjustPlan;

    /**
     * 下阶段工作计划 by 谭俊杰 on 2023/10/16 11:58
     * 下阶段工作计划
     */
    @ApiModelProperty(value = "下阶段工作计划【下阶段工作计划】", example = "1")
    private String nextStageWorkPlan;

    /**
     * 计划完成时间 by 谭俊杰 on 2023/10/16 11:59
     * 计划完成时间
     */
    @ApiModelProperty(value = "计划完成时间【计划完成时间】", example = "2023-10-16 11:59:11")
    private Date plannedCompletionTime;

    /**
     * 是否调整后期计划 by 谭俊杰 on 2023/10/16 12:00
     * 是否调整后期计划
     */
    @ApiModelProperty(value = "是否调整后期计划【是否调整后期计划】", example = "1")
    private String adjustLaterPlans;

    /**
     * 是否调整后期计划 by 谭俊杰 on 2023/10/16 12:00
     * 是否调整后期计划
     */
    @ApiModelProperty(value = "标段名称", example = "1")
    private String tendersName;

    @ApiModelProperty(value = "BIM周月报基础数据")
    private List<DbsSituationData> situationDataList;
}