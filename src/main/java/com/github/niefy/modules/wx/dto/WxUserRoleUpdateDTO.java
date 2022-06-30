package com.github.niefy.modules.wx.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户与角色对应关系
 * @author Mark sunlightcs@gmail.com
 */
@Data
public class WxUserRoleUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private List<Long> userIds;

    /**
     * 角色ID
     */
    private Long roleId;


}
