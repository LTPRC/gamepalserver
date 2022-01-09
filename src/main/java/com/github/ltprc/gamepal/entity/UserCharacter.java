package com.github.ltprc.gamepal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserCharacter {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "uuid", nullable = false)
    private String uuid;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String nickname;
    @Column
    private String nameColor;
    @Column
    private String creature;
    @Column
    private String gender;
    @Column
    private String skincolor;
    @Column
    private String hairstyle;
    @Column
    private String hairColor;
    @Column
    private String eyes;
    @Column
    private String faceRatio;
    @Column
    private String faceDecoration;
    @Column
    private String outfit;
    @Column
    private String bodyDecoration;
}
