package com.github.ltprc.gamepal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.ltprc.gamepal.entity.UserCharacter;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
}
