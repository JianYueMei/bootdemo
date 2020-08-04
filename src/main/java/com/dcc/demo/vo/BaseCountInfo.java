package com.dcc.demo.vo;

import com.dcc.demo.model.Role;
import com.dcc.demo.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

public class BaseCountInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String baseName;

    private Integer userCount;
    private List<User> userList;

    private Integer roleCount;
    private List<Role> roleList;

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Integer getRoleCount() {
        return roleCount;
    }

    public void setRoleCount(Integer roleCount) {
        this.roleCount = roleCount;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "BaseCountInfo{" +
                "baseName='" + baseName + '\'' +
                ", userCount=" + userCount +
                ", userList=" + userList +
                ", roleCount=" + roleCount +
                ", roleList=" + roleList +
                '}';
    }
}
