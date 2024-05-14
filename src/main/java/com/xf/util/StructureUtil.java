package com.xf.util;

import com.xf.entity.Structure;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 谭俊杰
 * @date 2021/8/2
 * @time 11:44
 */
@Data
public class StructureUtil {

    private static List<Structure> arrayList = new ArrayList<>();

    /**
     * 生成树形结构
     * @param list 子父级关系数据集合
     * @return 树形结构
     */
    public static List<Map<String, Object>> treeStructure(List<Structure> list) {
        //树形结构集合
        List<Map<String, Object>> mapList = new ArrayList();

        StructureUtil.arrayList = list;
        for (Structure structure : list) {
            Map mapArr = new LinkedHashMap();
            if (structure.getParentId() == null ) {
                mapArr.put("id", structure.getId());
                mapArr.put("leaf",structure.getLeaf());
                mapArr.put("name",structure.getName());
                mapArr.put("parentId",structure.getParentId());
                mapArr.put("projectId",structure.getProjectId());
                mapArr.put("recordCreateDate",structure.getRecordCreateDate());
                mapArr.put("recordUpdateDate",structure.getRecordUpdateDate());
                mapArr.put("serialNumber",structure.getSerialNumber());
                mapArr.put("structureType",structure.getStructureType());
                mapArr.put("treeId",structure.getTreeId());
                mapArr.put("treeLevel",structure.getTreeLevel());
                mapArr.put("childrens",treeChild(structure.getId()));
                mapList.add(mapArr);
            }
        }
        return mapList;
    }


    /**
     * 递归查询子级
     * @param id id标识
     * @return
     */
    public static List treeChild(long id) {
        List lists = new ArrayList();
        for (Structure structure : arrayList) {
            Map childArray = new LinkedHashMap();
            if (structure.getParentId() == null) continue;
            if (id == structure.getParentId()) {
                childArray.put("id", structure.getId());
                childArray.put("leaf",structure.getLeaf());
                childArray.put("name",structure.getName());
                childArray.put("parentId",structure.getParentId());
                childArray.put("projectId",structure.getProjectId());
                childArray.put("recordCreateDate",structure.getRecordCreateDate());
                childArray.put("recordUpdateDate",structure.getRecordUpdateDate());
                childArray.put("serialNumber",structure.getSerialNumber());
                childArray.put("structureType",structure.getStructureType());
                childArray.put("treeId",structure.getTreeId());
                childArray.put("treeLevel",structure.getTreeLevel());
                childArray.put("childrens",treeChild(structure.getId()));
                lists.add(childArray);
            }
        }
        return lists;
    }
}
