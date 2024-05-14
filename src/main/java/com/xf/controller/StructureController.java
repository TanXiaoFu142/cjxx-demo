package com.xf.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xf.entity.Structure;
import com.xf.mapper.StructureMapper;
import com.xf.util.StructureUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName StructureController
 * @Description TODO
 * @Author tanjunjie
 * @Date 2024/5/14 15:09
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/structure", method = RequestMethod.POST)
public class StructureController {

    @Autowired
    private StructureMapper structureMapper;

    @ApiOperation("结构模糊查询")
    @RequestMapping("/queryStructureByName")
    public Object queryStructureByName(@RequestBody JSONObject jsonRequestBody) {
        //查询结果
        List<Structure> queryResult = new ArrayList<>();
        //最终返回结果
        List<Map<String, Object>> result = null;
        try {
            QueryWrapper<Structure> wrapper = new QueryWrapper<>();
            wrapper.like("name", jsonRequestBody.getString("name"));//例：华园泵、24944827
            List<Structure> structureList = structureMapper.selectList(wrapper);

            if (!ObjectUtils.isEmpty(structureList)) {
                for (int i = 0; i < structureList.size(); i++) {
                    Structure s = structureList.get(i);
                    queryResult.add(s);
                    Long parentId = s.getParentId();//获取父id
                    boolean flag = true;
                    //查询子节点
                    while (flag) {
                        if (StringUtils.isEmpty(parentId)) {//父id为空
                            flag = false;
                        } else {
                            Structure s1 = structureMapper.selectById(parentId.toString());
                            parentId = s1.getParentId();
                            queryResult.add(s1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "查询失败";
        }
        //根据getId去重
        queryResult = queryResult.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Structure::getId))), ArrayList::new));
        //生成结构树
        result = StructureUtil.treeStructure(queryResult);
        return result;
    }
}
