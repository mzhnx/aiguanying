package com.gec.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gec.model.system.SysRole;
import com.gec.model.vo.SysRoleQueryVo;
import org.apache.ibatis.annotations.Param;

public interface SysRoleMapper extends BaseMapper<SysRole> {
//    IPage<SysRole> page1当作参数，就会默认进行分页查询
    public IPage<SysRole> selectPage(IPage<SysRole> page1, @Param("vo") SysRoleQueryVo roleQueryVo);
}
