package com.magenta.sc.paysafe;
import org.testng.annotations.*;

/**
 * Unit test for PaysafeIntegration micro service class.
 */

public class PaysafeIntegrationTests {

    @BeforeClass
    public void setUp() {
        // code that will be invoked when this test is instantiated
    }

    @Test(groups = { "fast" })
    public void aFastTest() {
        System.out.println("Fast test");
    }

    @Test(groups = { "slow" })
    public void aSlowTest() {
        System.out.println("Slow test");
    }
}
