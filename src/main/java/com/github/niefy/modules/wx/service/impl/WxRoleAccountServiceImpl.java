package com.github.niefy.modules.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.niefy.common.utils.MapUtils;
import com.github.niefy.modules.sys.dao.SysUserRoleDao;
import com.github.niefy.modules.sys.entity.SysRoleMenuEntity;
import com.github.niefy.modules.sys.entity.SysUserRoleEntity;
import com.github.niefy.modules.sys.service.SysUserRoleService;
import com.github.niefy.modules.wx.dao.WxRoleAccountDao;
import com.github.niefy.modules.wx.entity.WxRoleAccountEntity;
import com.github.niefy.modules.wx.service.WxRoleAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 用户与角色对应关系
 * @author Mark sunlightcs@gmail.com
 */
@Service("wxRoleAccountService")
public class WxRoleAccountServiceImpl extends ServiceImpl<WxRoleAccountDao, WxRoleAccountEntity> implements WxRoleAccountService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<String> appIdList) {
        //先删除角色与菜单关系
        deleteBatch(new Long[]{roleId});

        if (appIdList.size() == 0) {
            return;
        }

        //保存角色与菜单关系
        for (String appId : appIdList) {
            WxRoleAccountEntity sysRoleMenuEntity = new WxRoleAccountEntity();
            sysRoleMenuEntity.setAppid(appId);
            sysRoleMenuEntity.setRoleId(roleId);

            this.save(sysRoleMenuEntity);
        }
    }

    @Override
    public List<String> queryAppIdList(Long roleId) {
        return baseMapper.queryAppIdList(roleId);
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }

    @Override
    public List<String> queryAllAppId(Long userId) {
        return baseMapper.queryAllAppId(userId);
    }
}
