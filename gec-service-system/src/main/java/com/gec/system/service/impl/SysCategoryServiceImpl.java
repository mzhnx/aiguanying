package com.gec.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec.model.system.SysCategory;
import com.gec.model.vo.SysCategoryQueryVo;
import com.gec.system.mapper.SysCategoryMapper;
import com.gec.system.service.SysCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper,SysCategory> implements SysCategoryService {
    @Override
    public IPage<SysCategory> selectPage(IPage<SysCategory> p1, SysCategoryQueryVo sysCategoryQueryVo) {
        return this.baseMapper.selectPage(p1,sysCategoryQueryVo);
    }
}
