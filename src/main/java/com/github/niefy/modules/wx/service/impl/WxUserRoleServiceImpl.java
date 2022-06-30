package com.github.niefy.modules.wx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.niefy.common.utils.MapUtils;
import com.github.niefy.modules.sys.dao.SysUserRoleDao;
import com.github.niefy.modules.sys.entity.SysUserRoleEntity;
import com.github.niefy.modules.sys.service.SysUserRoleService;
import com.github.niefy.modules.wx.dao.WxUserRoleDao;
import com.github.niefy.modules.wx.entity.WxUserRoleEntity;
import com.github.niefy.modules.wx.service.WxUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户与角色对应关系
 * @author Mark sunlightcs@gmail.com
 */
@Service("wxUserRoleService")
public class WxUserRoleServiceImpl extends ServiceImpl<WxUserRoleDao, WxUserRoleEntity> implements WxUserRoleService {

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除用户与角色关系
        this.removeByMap(new MapUtils().put("user_id", userId));

        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }

        //保存用户与角色关系
        for (Long roleId : roleIdList) {
            WxUserRoleEntity sysUserRoleEntity = new WxUserRoleEntity();
            sysUserRoleEntity.setUserId(userId);
            sysUserRoleEntity.setRoleId(roleId);

            this.save(sysUserRoleEntity);
        }
    }

    @Override
    public void saveOrUpdateUsers(Long roleId, List<Long> userIdList) {

        if (userIdList == null || userIdList.size() == 0) {
            return;
        }

        //保存用户与角色关系
        for (Long userId : userIdList) {
            WxUserRoleEntity sysUserRoleEntity = new WxUserRoleEntity();
            sysUserRoleEntity.setUserId(userId);
            sysUserRoleEntity.setRoleId(roleId);

            this.save(sysUserRoleEntity);
        }
    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }

    @Override
    public List<Long> queryUserIdList(Long roleId) {
        return baseMapper.queryUserIdList(roleId);
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }
}
