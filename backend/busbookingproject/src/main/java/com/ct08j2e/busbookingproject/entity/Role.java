package com.ct08j2e.busbookingproject.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;
    
    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;
    
    @OneToMany(mappedBy = "role")
    private Set<User> users;
    
    public Role() {
    }
    
    public Role(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
    
    public Integer getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public Set<User> getUsers() {
        return users;
    }
    
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
