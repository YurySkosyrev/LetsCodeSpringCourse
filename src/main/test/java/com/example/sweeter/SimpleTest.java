package com.example.sweeter;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {
    @Test
    public void test(){
        int x = 2;
        int y = 3;

        Assert.assertEquals(6, x*y);
    }
}
