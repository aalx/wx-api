package com.github.niefy.modules.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.niefy.common.exception.RRException;
import com.github.niefy.common.utils.Constant;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.Query;
import com.github.niefy.modules.sys.dao.SysRoleDao;
import com.github.niefy.modules.sys.entity.SysRoleEntity;
import com.github.niefy.modules.sys.service.SysRoleMenuService;
import com.github.niefy.modules.sys.service.SysRoleService;
import com.github.niefy.modules.sys.service.SysUserRoleService;
import com.github.niefy.modules.wx.dao.WxRoleDao;
import com.github.niefy.modules.wx.entity.WxRoleEntity;
import com.github.niefy.modules.wx.service.WxRoleAccountService;
import com.github.niefy.modules.wx.service.WxRoleService;
import com.github.niefy.modules.wx.service.WxUserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色
 * @author Mark sunlightcs@gmail.com
 */
@Service("wxRoleService")
public class WxRoleServiceImpl extends ServiceImpl<WxRoleDao, WxRoleEntity> implements WxRoleService {
    @Autowired
    private WxRoleAccountService wxRoleAccountService;
    @Autowired
    private WxUserRoleService wxUserRoleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        Long createUserId = (Long) params.get("createUserId");

        IPage<WxRoleEntity> page = this.page(
            new Query<WxRoleEntity>().getPage(params),
            new QueryWrapper<WxRoleEntity>()
                .like(StringUtils.isNotBlank(roleName), "role_name", roleName)
                .eq(createUserId != null, "create_user_id", createUserId)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(WxRoleEntity role) {
        role.setCreateTime(new Date());
        this.save(role);

        //检查权限是否越权
        checkPrems(role);

        //保存角色与菜单关系
        wxRoleAccountService.saveOrUpdate(role.getRoleId(), role.getAppIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WxRoleEntity role) {
        this.updateById(role);

        //检查权限是否越权
        checkPrems(role);

        //更新角色与菜单关系
        wxRoleAccountService.saveOrUpdate(role.getRoleId(), role.getAppIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        wxRoleAccountService.deleteBatch(roleIds);

        //删除角色与用户关联
        wxUserRoleService.deleteBatch(roleIds);
    }


    @Override
    public List<Long> queryRoleIdList(Long createUserId) {
        return baseMapper.queryRoleIdList(createUserId);
    }

    /**
     * 检查权限是否越权
     */
    private void checkPrems(WxRoleEntity role) {
        //如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
        if (role.getCreateUserId() == Constant.SUPER_ADMIN) {
            return;
        }

        //查询用户所拥有的菜单列表
        List<String> appIdList = wxRoleAccountService.queryAppIdList(role.getCreateUserId());

        //判断是否越权
        if (!appIdList.containsAll(role.getAppIdList())) {
            throw new RRException("新增角色的权限，已超出你的权限范围");
        }
    }
}
