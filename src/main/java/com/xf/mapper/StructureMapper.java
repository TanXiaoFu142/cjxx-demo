package com.xf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.xf.entity.Structure;
import org.springframework.stereotype.Repository;

//其中继承基本类BaseMapper

@Repository //代表这是持久层
public interface StructureMapper extends BaseMapper<Structure> {
    
    //到这一步已经把简单的CRUD的编写完成了，不在是像之前mybatis一样编写接口和XXXMapper.xml，简化开发！
}

