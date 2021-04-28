package com.backbase.kalah;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.UnknownHostException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
public class KalahApplicationTests {

    @Test
    public void contextLoads() throws UnknownHostException {
        KalahApplication.main(new String[0]);
    }
}
