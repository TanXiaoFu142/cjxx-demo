package com.xf.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.runtime.CodeStore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.javassist.SerialVersionUID;

import java.nio.charset.CoderResult;
import java.util.Date;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谭俊杰
 * Date: 2023/08/15
 * Time: 13:56
 *
 * 履约单位
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "PuPerformanceUnit", description = "")
public class PuPerformanceUnit extends SerialVersionUID {

    private static final long serialVersionUID = 3525L;

    public static final String ENTITY_TABLE = "pu_performance_unit";
    public static final String ENTITY_CODE = "PuPerformanceUnit";

    public static final String COLUMN_UNIT_TYPE = "unit_type";
    public static final String COLUMN_CREDIT_CODE = "credit_code";
    public static final String COLUMN_REGISTRATION_AMOUNT = "registration_amount";
    public static final String COLUMN_LEGAL_PERSON_NAME = "legal_person_name";
    public static final String COLUMN_ID_CARD = "id_card";
    public static final String COLUMN_BUSINESS_LICENSE_DOC_ID = "business_license_doc_id";
    public static final String COLUMN_CERTIFICATIONS_DOC_ID = "certifications_doc_id";
    public static final String COLUMN_OTHER_DOC_ID = "other_doc_id";
    public static final String COLUMN_SYSTEM_RANKING = "system_ranking";
    public static final String COLUMN_DELETE_FLAG = "delete_flag";

    public static final String ATTRIBUTE_UNIT_TYPE = "unitType";
    public static final String ATTRIBUTE_CREDIT_CODE = "creditCode";
    public static final String ATTRIBUTE_REGISTRATION_AMOUNT = "registrationAmount";
    public static final String ATTRIBUTE_LEGAL_PERSON_NAME = "legalPersonName";
    public static final String ATTRIBUTE_ID_CARD = "idCard";
    public static final String ATTRIBUTE_BUSINESS_LICENSE_DOC_ID = "businessLicenseDocId";
    public static final String ATTRIBUTE_CERTIFICATIONS_DOC_ID = "certificationsDocId";
    public static final String ATTRIBUTE_OTHER_DOC_ID = "otherDocId";
    public static final String ATTRIBUTE_SYSTEM_RANKING = "systemRanking";
    public static final String ATTRIBUTE_DELETE_FLAG = "deleteFlag";

    //流程模版编码
    public static final String PROCESS_TEMPLATE_CODE = "fbrk";

    //审核状态
    public static final Integer STATE_SHZ = 1;
    public static final Integer STATE_DZG = 2;
    public static final Integer STATE_TG = 3;

    //数据显示维度 单位，标段
    public static final String UNIT = "unit";
    public static final String TENDERS = "tenders";

    /**
     * 单位类型 by 谭俊杰 on 2023/08/14 15:25
     * 单位类型
     */
    @ApiModelProperty(value = "单位类型【必填，字典】", example = "1")
    private String unitType;

    /**
     * 单位统一社会信用代码 by 谭俊杰 on 2023/08/14 15:25
     * 单位统一社会信用代码
     */
    private String creditCode;

    /**
     * 注册金额 by 谭俊杰 on 2023/08/14 15:27
     * 注册金额
     */
    private String registrationAmount;

    /**
     * 法定代表人姓名 by 谭俊杰 on 2023/08/14 15:27
     * 法定代表人姓名
     */
    private String legalPersonName;

    /**
     * 法定代表人公民身份证 by 谭俊杰 on 2023/08/14 15:28
     * 法定代表人公民身份证
     */
    private String idCard;

    /**
     * 履约单位营业执照附件 by 谭俊杰 on 2023/08/14 15:28
     * 履约单位营业执照附件
     */
    private Long businessLicenseDocId;

    /**
     * 履约单位资质证书 by 谭俊杰 on 2023/08/14 15:29
     * 履约单位资质证书
     */
    private Long certificationsDocId;

    /**
     * 其他材料 by 谭俊杰 on 2023/08/14 15:30
     * 其他材料
     */
    private Long otherDocId;

    /**
     * 系统排名 by 谭俊杰 on 2023/08/14 15:30
     * 系统排名
     */
    private Integer systemRanking;

    /**
     * 逻辑删除标识 by 谭俊杰 on 2023/08/15 13:56
     * 逻辑删除标识
     */
    private Integer deleteFlag = 0;

    /*****************************************************************/

    /**
     * 流程模板ID by 田文渊 on 2019/08/14 15:05
     * 流程模板ID
     */
    private Long processTemplateId;

    /**
     * 流程体ID by 田文渊 on 2019/08/14 15:07
     * 流程体ID
     */
    private Long processBodyId;

    /**
     * 是否使用流程 by 田文渊 on 2019/08/14 15:10
     * 是否使用流程
     */
    private Boolean useProcess;

    /**
     * 发起人ID by 田文渊 on 2019/08/14 15:15
     * 发起人ID
     */
    private Long applyUserId;

    /**
     * 项目ID by 田文渊 on 2019/08/14 15:16
     * 项目ID
     */
    private Long projectId;

    /**
     * 组织ID by 田文渊 on 2019/08/14 15:17
     * 组织ID
     */
    private Long orgId;

    /**
     * 流程ID by 田文渊 on 2019/08/14 15:18
     * 流程ID
     */
    private String processId;

    /**
     * 大字符串 by 田文渊 on 2019/08/14 15:19
     * 大字符串
     */
    private String metadata;

    /**
     * 业务表单类型 by 田文渊 on 2019/09/23 10:58
     * 业务表单类型
     */
    private String businessType;

    /**
     * 发起时间 by 田文渊 on 2019/10/21 18:27
     * 发起时间
     */
    private Date applyDate;

}
