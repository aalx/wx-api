package com.github.niefy.modules.wx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.niefy.modules.wx.entity.WxUserRoleEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户与角色对应关系
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
@CacheNamespace(flushInterval = 300000L)//缓存五分钟过期
public interface WxUserRoleDao extends BaseMapper<WxUserRoleEntity> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);


    List<Long> queryUserIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
