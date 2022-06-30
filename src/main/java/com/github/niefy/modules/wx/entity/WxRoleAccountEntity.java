package com.github.niefy.modules.wx.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("wx_role_account")
public class WxRoleAccountEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId
    private Long id;


    /**
     * 角色ID
     */
    private Long roleId;


    private String appid;


}
