package com.polytech.mtonairserver.utils.string;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Class that tests all the StringUtils methods.
 */
class StringUtilsTest
{

    /**
     * Testing the {@link StringUtils#upperRemoveDashAndSlashes(String)} method.
     * @see StringUtils
     */
    @Test
    void upperRemoveDash()
    {
        String case1 = "my-custom-string";
        String expectedResult1 = "My Custom String";
        String realResult1 = StringUtils.upperRemoveDashAndSlashes(case1);
        Assert.assertEquals(expectedResult1, realResult1);

        String case2 = "my-custom-string-----";
        String expectedResult2 = "My Custom String";
        String realResult2 = StringUtils.upperRemoveDashAndSlashes(case2);
        Assert.assertEquals(expectedResult2, realResult2);


        String case3 = null;
        String expectedResult3 = null;
        String realResult3 = StringUtils.upperRemoveDashAndSlashes(case3);
        Assert.assertEquals(expectedResult3, realResult3);

        String case4 = "---";
        String expectedResult4 = "";
        String realResult4 = StringUtils.upperRemoveDashAndSlashes(case4);
        Assert.assertEquals(expectedResult4, realResult4);

        String case5 = "--_--_";
        String expectedResult5 = "_ _";
        String realResult5 = StringUtils.upperRemoveDashAndSlashes(case5);
        Assert.assertEquals(expectedResult5, realResult5);

        String case6 = "/otay-mesa-donovan-correctional-facility/";
        String expectedResult6= "Otay Mesa Donovan Correctional Facility";
        String realResult6 = StringUtils.upperRemoveDashAndSlashes(case6);
        Assert.assertEquals(expectedResult6, realResult6);


    }
}