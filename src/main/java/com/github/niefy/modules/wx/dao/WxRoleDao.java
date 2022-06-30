package com.github.niefy.modules.wx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.niefy.modules.sys.entity.SysRoleEntity;
import com.github.niefy.modules.wx.entity.WxRoleEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色管理
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
@CacheNamespace(flushInterval = 300000L)//缓存五分钟过期
public interface WxRoleDao extends BaseMapper<WxRoleEntity> {

    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);
}
