package com.gec.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gec.model.system.SysMenu;
import com.gec.model.vo.AssginMenuVo;
import com.gec.model.vo.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author hkw
 * @since 2023-10-30
 */
public interface SysMenuService extends IService<SysMenu> {
    // 加载菜单列表
    List<SysMenu> findNodes();

    // 根据id去删除菜单
    void removeMenuById(Long id);


    List<SysMenu> findSysMenuByRoleId(Long roleId);

    void doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> findUserMenuList(Long id);

    List<String> findUserPermsList(Long id);
}
