package com.xf.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName PersonnelExcelDto
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/09/04 11:34
 * @Version 1.0
 */
@Data
@HeadRowHeight(20)
@ColumnWidth(25)
public class PersonnelExcelDto {

    @ExcelProperty("姓名")
    @ApiModelProperty(value = "测量人员名称",example = "1")
    private String name;

    @ExcelProperty("所属单位")
    @ApiModelProperty(value = "项目组织名称",example = "1")
    private String projectOrgName;

    @ExcelProperty("所属标段")
    @ApiModelProperty(value = "标段名称",example = "1")
    private String tendersName;

    @ExcelProperty("进场日期")
    @ApiModelProperty(value = "进场日期【进场日期】", example = "2023-08-29")
    private Date entryDate;

    @ExcelProperty("出场日期")
    @ApiModelProperty(value = "出场日期【出场日期】", example = "2023-08-29")
    private Date exitDate;

    @ExcelProperty("审核状态")
    @ApiModelProperty(value = "审核状态【审核状态】", example = "1")
    private String approvalStatus;

    @ExcelProperty("人员状态")
    @ApiModelProperty(value = "进出场状态【进出场状态1进0出】", example = "1")
    private String status;
}
