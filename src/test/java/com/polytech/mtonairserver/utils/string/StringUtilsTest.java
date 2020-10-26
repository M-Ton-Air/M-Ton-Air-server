package com.polytech.mtonairserver.utils.string;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest
{

    @Test
    void upperRemoveDash()
    {
        String case1 = "my-custom-string";
        String expectedResult1 = "My Custom String";
        String realResult1 = StringUtils.upperRemoveDash(case1);
        Assert.assertEquals(expectedResult1, realResult1);

        String case2 = "my-custom-string-----";
        String expectedResult2 = "My Custom String";
        String realResult2 = StringUtils.upperRemoveDash(case2);
        Assert.assertEquals(expectedResult2, realResult2);


        String case3 = null;
        String expectedResult3 = null;
        String realResult3 = StringUtils.upperRemoveDash(case3);
        Assert.assertEquals(expectedResult3, realResult3);

        String case4 = "---";
        String expectedResult4 = "";
        String realResult4 = StringUtils.upperRemoveDash(case4);
        Assert.assertEquals(expectedResult4, realResult4);

        String case5 = "--_--_";
        String expectedResult5 = "_ _";
        String realResult5 = StringUtils.upperRemoveDash(case5);
        Assert.assertEquals(expectedResult5, realResult5);
    }
}