package com.github.niefy.modules.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.niefy.modules.sys.entity.SysUserRoleEntity;
import com.github.niefy.modules.wx.entity.WxUserRoleEntity;

import java.util.List;


/**
 * 用户与角色对应关系
 * @author Mark sunlightcs@gmail.com
 */
public interface WxUserRoleService extends IService<WxUserRoleEntity> {

    void saveOrUpdate(Long userId, List<Long> roleIdList);
    void saveOrUpdateUsers(Long roleId, List<Long> userIdList);

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);
    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryUserIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
