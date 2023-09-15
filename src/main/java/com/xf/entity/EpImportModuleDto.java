package com.xf.entity;


import com.xf.entity.DtoData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "导入人员模板参数DTO", description = "导入人员模板参数DTO")
public class EpImportModuleDto implements DtoData {

    @ApiModelProperty(value = "单位组织数据", example = "标段数据list")
    public List<OrgDicDto> orgDicDtoList;

    @ApiModelProperty(value = "标段数据list", example = "标段数据list")
    public List<TendersDicDto> tendersDicDtoList;

    @ApiModelProperty(value = "人员类型数据list", example = "人员类型数据list")
    public List<PersonnelTypeDicDto> personnelTypeDicDtoList;

    @ApiModelProperty(value = "管理类型数据list", example = "管理类型数据list")
    public List<ManagerTypeDicDto> managerTypeDicDtoList;

    @ApiModelProperty(value = "岗位类型数据list", example = "岗位类型数据list")
    public List<PostCodeDicDto> postCodeDicDtoList;

    @ApiModelProperty(value = "工作状态数据list", example = "工作状态数据list")
    public List<WorkStateDicDto> workStateDicDtoList;

    @ApiModelProperty(value = "性别数据list", example = "性别数据list")
    public List<GenderDicDto> genderDicDtoList;

    @ApiModelProperty(value = "工种类型数据list", example = "工种类型数据list")
    public List<WorkTypeDicDto> workTypeDicDtoList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrgDicDto implements DtoData{
        @ApiModelProperty(value = "所属单位id")
        private Long orgId;
        @ApiModelProperty(value = "所属单位名称")
        private String orgName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TendersDicDto implements DtoData {
        @ApiModelProperty(value = "标段id")
        private Long tendersId;
        @ApiModelProperty(value = "标段名称")
        private String tendersName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonnelTypeDicDto implements DtoData {
        @ApiModelProperty(value = "人员类型")
        private String personnelType;
        @ApiModelProperty(value = "人员类型名称")
        private String personnelTypeName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ManagerTypeDicDto implements DtoData {
        @ApiModelProperty(value = "管理类型")
        private String managerType;
        @ApiModelProperty(value = "管理类型名称")
        private String managerTypeName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostCodeDicDto implements DtoData{
        @ApiModelProperty(value = "岗位")
        private String postCode;
        @ApiModelProperty(value = "岗位名称")
        private String postName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkStateDicDto implements DtoData {
        @ApiModelProperty(value = "工作状态")
        private String workState;
        @ApiModelProperty(value = "工作状态名称")
        private String workStateName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenderDicDto implements DtoData{
        @ApiModelProperty(value = "性别")
        private String gender;
        @ApiModelProperty(value = "性别名称")
        private String genderName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkTypeDicDto implements DtoData{
        @ApiModelProperty(value = "工种", example = "工种")
        private String workType;
        @ApiModelProperty(value = "工种名称")
        private String workTypeName;
    }

}
