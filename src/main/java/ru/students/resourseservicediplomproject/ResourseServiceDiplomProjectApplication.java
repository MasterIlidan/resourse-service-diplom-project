package ru.students.resourseservicediplomproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class ResourseServiceDiplomProjectApplication {

    public static void main(String[] args) throws IOException {
        if (Files.notExists(Path.of("./images"))) {
            Files.createDirectory(Path.of("./images"));
        }
        SpringApplication.run(ResourseServiceDiplomProjectApplication.class, args);
    }

}
