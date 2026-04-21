package com.xf.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author tanjunjie
 * @version 1.0
 * @className ScQualityIssueExcelDto
 * @date 2024/12/19 16:47
 * @description 安全问题导出ExcelDto
 */
@Data
@EqualsAndHashCode
public class ScSafetyIssueExcelDto implements DtoData {

    @ExcelProperty("序号")
    @ApiModelProperty(value = "序号", example = "1")
    private Integer serialNumber;

    @ExcelProperty("标段名称")
    @ApiModelProperty(value = "标段名称", example = "1")
    private String tendersName;

    @ExcelProperty("隐患类型")
    @ApiModelProperty(value = "隐患类型", example = "1")
    private String typeName;

    @ExcelProperty("隐患等级")
    @ApiModelProperty(value = "隐患等级", example = "1")
    private String levelName;

    @ExcelProperty("状态")
    @ApiModelProperty(value = "状态", example = "1")
    private String statusName;

    @ExcelProperty("当前节点")
    @ApiModelProperty(value = "当前节点", example = "1")
    private String nodeName;

    @ExcelProperty("节点剩余时间")
    @ApiModelProperty(value = "节点剩余时间", example = "1")
    private String nextDeadlineDay;

    @ExcelProperty("创建人")
    @ApiModelProperty(value = "创建人", example = "1")
    private String createUser;

    @ExcelProperty("检查编号")
    @ApiModelProperty(value = "检查编号", example = "1")
    private String inspectCode;

    @ColumnWidth(20)
    @ExcelProperty("创建时间")
    @ApiModelProperty(value = "创建时间", example = "1")
    private Date recordCreateDate;

    @ColumnWidth(20)
    @ExcelProperty("更新时间")
    @ApiModelProperty(value = "更新时间", example = "1")
    private Date recordUpdateDate;


    @ExcelProperty("照片")
    @ApiModelProperty(value = "照片", example = "1")
    private byte[] photos;



}
