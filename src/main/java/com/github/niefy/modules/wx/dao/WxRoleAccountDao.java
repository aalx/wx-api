package com.github.niefy.modules.wx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.niefy.modules.wx.entity.WxRoleAccountEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色与菜单对应关系
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
@CacheNamespace(flushInterval = 300000L)//缓存五分钟过期
public interface WxRoleAccountDao extends BaseMapper<WxRoleAccountEntity> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<String> queryAppIdList(Long roleId);



    int deleteBatch(Long[] roleIds);

    List<String> queryAllAppId(Long userId);
}
