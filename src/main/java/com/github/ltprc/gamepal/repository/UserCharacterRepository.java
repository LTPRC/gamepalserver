package com.github.ltprc.gamepal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.ltprc.gamepal.entity.UserCharacter;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {

    @Query(value="select new UserCharacter(id, uuid, firstName, lastName, nickname, nameColor, creature, gender, skinColor, hairstyle, hairColor, eyes, faceRatio, faceDecoration, outfit, bodyDecoration, avatar, createTime, updateTime) from UserCharacter where uuid=:uuid")
    public List<UserCharacter> queryUserCharacterByUuid(@Param("uuid") String uuid);

//    @Modifying
//    @Query("update UserCharacter set firstName=:uc.firstName where uuid=:uc.uuid")
//    void updateUserCharacter(@Param("uc") UserCharacter uc);
}
