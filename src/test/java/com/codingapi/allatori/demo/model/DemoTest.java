package com.codingapi.allatori.demo.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.util.Assert;

/**
 * @author lorne
 * @since 1.0.0
 */
class DemoTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/xxx.csv")
    void getVal(int a,int b,int val){
        Demo demo = new Demo();
        demo.setA(a);
        demo.setB(b);
        int res  = demo.getVal();
        Assert.isTrue(res==val,"加法计算错误。");
    }

}
