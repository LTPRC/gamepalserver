package com.github.ltprc.gamepal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.ltprc.gamepal.entity.UserOnline;

public interface UserOnlineRepository extends JpaRepository<UserOnline, Long> {

    //public List<UserOnline> queryUserOnlineByUuid();

    //public void deleteByUuid();
}
