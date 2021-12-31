package com.github.ltprc.gamepal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserOnline {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "uuid", nullable = false)
    private String uuid;
    @Column(name = "loginTime", nullable = false)
    private String loginTime;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
