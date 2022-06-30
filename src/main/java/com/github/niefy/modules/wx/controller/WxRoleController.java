package com.github.niefy.modules.wx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.niefy.common.annotation.SysLog;
import com.github.niefy.common.utils.Constant;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;
import com.github.niefy.common.validator.ValidatorUtils;
import com.github.niefy.modules.sys.controller.AbstractController;
import com.github.niefy.modules.sys.entity.SysRoleEntity;
import com.github.niefy.modules.sys.entity.SysUserEntity;
import com.github.niefy.modules.sys.service.SysRoleMenuService;
import com.github.niefy.modules.sys.service.SysRoleService;
import com.github.niefy.modules.sys.service.SysUserService;
import com.github.niefy.modules.wx.dto.WxUserRoleUpdateDTO;
import com.github.niefy.modules.wx.entity.WxRoleEntity;
import com.github.niefy.modules.wx.entity.WxUserRoleEntity;
import com.github.niefy.modules.wx.service.WxRoleAccountService;
import com.github.niefy.modules.wx.service.WxRoleService;
import com.github.niefy.modules.wx.service.WxUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/wx/role")
@Api(tags = {"微信公众号角色"})
public class WxRoleController extends AbstractController {
    @Autowired
    private WxRoleService wxRolxeService;
    @Autowired
    private WxRoleAccountService wxRoleAccountService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private WxUserRoleService wxUserRoleService;

    /**
     * 角色列表
     */
    @GetMapping("/list")
    @RequiresPermissions("wx:role:list")
    @ApiOperation(value = "角色列表",notes = "")
    public R list(@RequestParam Map<String, Object> params) {
        //如果不是超级管理员，则只查询自己创建的角色列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }

        PageUtils page = wxRolxeService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 角色列表
     */
    @GetMapping("/select")
    @RequiresPermissions("wx:role:select")
    @ApiOperation(value = "拥有的角色列表",notes = "")
    public R select() {
        Map<String, Object> map = new HashMap<>(4);

        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            map.put("create_user_id", getUserId());
        }
        List<WxRoleEntity> list = wxRolxeService.listByMap(map);

        return R.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("wx:role:info")
    @ApiOperation(value = "角色详情",notes = "")
    public R info(@PathVariable("roleId") Long roleId) {
        WxRoleEntity role = wxRolxeService.getById(roleId);

        //查询角色对应的app
        List<String> appIdList = wxRoleAccountService.queryAppIdList(roleId);
        role.setAppIdList(appIdList);

        return R.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @RequiresPermissions("wx:role:save")
    @ApiOperation(value = "保存角色",notes = "")
    public R save(@RequestBody WxRoleEntity role) {
        ValidatorUtils.validateEntity(role);
        role.setCreateUserId(getUserId());
        wxRolxeService.saveRole(role);

        return R.ok();
    }

    /**
     * 修改角色
     */
    @SysLog("修改角色")
    @PostMapping("/update")
    @RequiresPermissions("wx:role:update")
    @ApiOperation(value = "修改角色",notes = "")
    public R update(@RequestBody WxRoleEntity role) {
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        wxRolxeService.update(role);

        return R.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("wx:role:delete")
    @ApiOperation(value = "删除角色",notes = "")
    public R delete(@RequestBody Long[] roleIds) {
        wxRolxeService.deleteBatch(roleIds);

        return R.ok();
    }


    /**
     * 查看角色用户
     */
    @SysLog("查看角色用户")
    @GetMapping("user/list/{roleId}")
    @RequiresPermissions("wx:user_role:list")
    @ApiOperation(value = "查看角色用户",notes = "")
    public R listUser(@PathVariable("roleId") Long roleId) {
        List<Long> userIdList= wxUserRoleService.queryUserIdList(roleId);

        return R.ok().put("list", userIdList);
    }


    /**
     * 更新角色用户
     */
    @SysLog("更新角色用户")
    @PostMapping("user/update/{roleId}")
    @RequiresPermissions("wx:user_role:update")
    @ApiOperation(value = "查看角色用户",notes = "")
    public R updateUser(@PathVariable("roleId") Long roleId,@RequestBody WxUserRoleUpdateDTO wxUserRoleUpdateDTO) {
        wxUserRoleService.deleteBatch(new Long[]{roleId});
        wxUserRoleService.saveOrUpdateUsers(roleId,wxUserRoleUpdateDTO.getUserIds());
        return R.ok();
    }
}
