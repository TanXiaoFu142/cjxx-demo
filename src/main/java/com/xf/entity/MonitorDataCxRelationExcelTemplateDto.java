package com.xf.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName MonitorDataCxRelationExcelTemplateDto
 * @Description 测斜测点基坑关联导入excel模板DTO
 * @Author tanjunjie
 * @Date 2023/09/14 12:34
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "测斜测点基坑关联导入excel模板DTO", description = "测斜测点基坑关联导入excel模板DTO")
public class MonitorDataCxRelationExcelTemplateDto implements DtoData {

    @ApiModelProperty(value = "标段数据list", example = "标段数据list")
    public List<TendersInfo> tendersInfoList;

    @ApiModelProperty(value = "工点数据list", example = "工点数据list")
    public List<WorkPointInfo> workPointInfoList;

    @ApiModelProperty(value = "基坑编码list", example = "工点数据list")
    public List<MonitorManageInfo> monitorManageInfoList;


    @Data/* 标段数据 */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TendersInfo implements DtoData {
        private Long tendersId;
        private String tendersName;
    }

    @Data/* 工点数据 */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkPointInfo implements DtoData {
        private Long workPointTendersId;
        private Long workPointId;
        private String workPointName;
    }

    @Data/* 基坑编码 */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonitorManageInfo implements DtoData {
        private String tendersIdWorkPointMonitorId;
        private String monitorManageName;
    }
}
