package com.github.ltprc.gamepal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.ltprc.gamepal.entity.UserOnline;

public interface UserOnlineRepository extends JpaRepository<UserOnline, Long> {

    @Query(value="select new UserOnline(id, uuid, loginTime) from UserOnline where uuid=:uuid")
    public List<UserOnline> queryUserOnlineByUuid(@Param("uuid") String uuid);

    //public void deleteByUuid();
}
