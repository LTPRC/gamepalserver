package com.github.ltprc.entity.subject;

import org.springframework.stereotype.Component;

@Component
public class LasVegas extends Subject {

    public LasVegas() {
        super();
        setName("Las Vegas");
        setMinPlayerNum(1);
        setMaxPlayerNum(8);
    }
}
