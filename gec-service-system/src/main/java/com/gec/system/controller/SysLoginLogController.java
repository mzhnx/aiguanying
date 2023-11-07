package com.gec.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gec.model.system.SysLoginLog;
import com.gec.model.vo.SysLoginLogQueryVo;
import com.gec.system.service.SysLoginLogService;
import com.gec.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("登录日志控制器")
@RestController
@RequestMapping("/admin/system/sysLoginLog")
public class SysLoginLogController {
    @Autowired
    private SysLoginLogService sysLoginLogService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result QueryLoginLog(
            @PathVariable Long page,
            @PathVariable Long limit,
            SysLoginLogQueryVo sysLoginLogQueryVo) {

        IPage<SysLoginLog> iPage = sysLoginLogService.selectPage(page, limit, sysLoginLogQueryVo);
        return Result.ok(iPage);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysLoginLog sysLoginLog = sysLoginLogService.getById(id);
        return Result.ok(sysLoginLog);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("del/{id}")
    public Result del(@PathVariable Long id) {
        boolean flag = sysLoginLogService.deleteById(id);
        if (flag)
        {
            return Result.ok();
        }
        else
        {
            return Result.fail();
        }
    }
}
