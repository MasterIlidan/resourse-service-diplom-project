package ru.students.resourseservicediplomproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-development.properties")
class ResourseServiceDiplomProjectApplicationTests {

    @Test
    void contextLoads() {
    }

}
