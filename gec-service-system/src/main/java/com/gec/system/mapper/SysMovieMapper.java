package com.gec.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gec.model.system.SysMovie;
import com.gec.model.vo.SysMovieQueryVo;
import org.apache.ibatis.annotations.Param;

public interface SysMovieMapper extends BaseMapper<SysMovie> {
    public IPage<SysMovie> selectPage(IPage<SysMovie> p1, @Param("vo") SysMovieQueryVo sysMovieQueryVo);
}
