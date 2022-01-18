package com.github.ltprc.gamepal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "uuid", nullable = false)
    private String uuid;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "nameColor")
    private String nameColor;
    @Column(name = "creature")
    private String creature;
    @Column(name = "gender")
    private String gender;
    @Column(name = "skinColor")
    private String skinColor;
    @Column(name = "hairstyle")
    private String hairstyle;
    @Column(name = "hairColor")
    private String hairColor;
    @Column(name = "eyes")
    private String eyes;
    @Column(name = "faceRatio")
    private String faceRatio;
    @Column(name = "faceDecoration")
    private String faceDecoration;
    @Column(name = "outfit")
    private String outfit;
    @Column(name = "bodyDecoration")
    private String bodyDecoration;
    @Column(name = "avatar")
    private int avatar;
    @Column(name = "createTime", nullable = false)
    private String createTime;
    @Column(name = "updateTime", nullable = false)
    private String updateTime;

    public UserCharacter() {}

    public UserCharacter(Long id, String uuid, String firstName, String lastName, String nickname, String nameColor,
            String creature, String gender, String skinColor, String hairstyle, String hairColor, String eyes,
            String faceRatio, String faceDecoration, String outfit, String bodyDecoration, int avatar, String createTime,
            String updateTime) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.nameColor = nameColor;
        this.creature = creature;
        this.gender = gender;
        this.skinColor = skinColor;
        this.hairstyle = hairstyle;
        this.hairColor = hairColor;
        this.eyes = eyes;
        this.faceRatio = faceRatio;
        this.faceDecoration = faceDecoration;
        this.outfit = outfit;
        this.bodyDecoration = bodyDecoration;
        this.avatar = avatar;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getCreature() {
        return creature;
    }

    public void setCreature(String creature) {
        this.creature = creature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    public String getHairstyle() {
        return hairstyle;
    }

    public void setHairstyle(String hairstyle) {
        this.hairstyle = hairstyle;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getFaceRatio() {
        return faceRatio;
    }

    public void setFaceRatio(String faceRatio) {
        this.faceRatio = faceRatio;
    }

    public String getFaceDecoration() {
        return faceDecoration;
    }

    public void setFaceDecoration(String faceDecoration) {
        this.faceDecoration = faceDecoration;
    }

    public String getOutfit() {
        return outfit;
    }

    public void setOutfit(String outfit) {
        this.outfit = outfit;
    }

    public String getBodyDecoration() {
        return bodyDecoration;
    }

    public void setBodyDecoration(String bodyDecoration) {
        this.bodyDecoration = bodyDecoration;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
