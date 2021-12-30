package com.github.ltprc.gamepal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.ltprc.gamepal.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    @Query(value="select id, uuid, username, password, createTime, updateTime from UserInfo where id=:id")
    public List<UserInfo> queryUserInfoById(@Param("id") Long id);

    @Query(value="select id, uuid, username, password, createTime, updateTime from UserInfo where uuid=:uuid")
    public List<UserInfo> queryUserInfoByUuid(@Param("uuid") String uuid);

    @Query(value="select id, uuid, username, password, createTime, updateTime from UserInfo where username=:username")
    public List<UserInfo> queryUserInfoByUsername(@Param("username") String username);
}
