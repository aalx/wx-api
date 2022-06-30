package com.github.niefy.modules.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.niefy.modules.sys.entity.SysRoleMenuEntity;
import com.github.niefy.modules.wx.entity.WxRoleAccountEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 微信应用与角色对应关系
 * @author Mark sunlightcs@gmail.com
 */
public interface WxRoleAccountService extends IService<WxRoleAccountEntity> {

    void saveOrUpdate(Long roleId, List<String> appIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<String> queryAppIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

    /**
     * 查询用户的所有菜单ID
     */
    List<String> queryAllAppId(Long userId);
}
