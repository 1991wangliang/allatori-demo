package com.codingapi.allatori.demo.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lorne
 * @since 1.0.0
 */
@Setter
@Getter
public class Demo {

    private int a;
    private int b;

    public int getVal(){
        return a+b;
    }

}
