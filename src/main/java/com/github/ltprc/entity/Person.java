package com.github.ltprc.entity;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@ConfigurationProperties(prefix = "person")
@Component
public class Person {

    @Value("${person.name}")
    private String name;
    @Value("${person.age}")
    private Integer age;
    private String sex;
    private List<String> likes;
    
    public Person() {
        
    }

    public Person(String name, Integer age, String sex, List<String> likes) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.likes = likes;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public List<String> getLikes() {
        return likes;
    }
    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + ", sex=" + sex + ", likes=" + likes + "]";
    }
}
