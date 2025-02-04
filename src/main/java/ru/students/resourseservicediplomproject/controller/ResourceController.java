package ru.students.resourseservicediplomproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class ResourceController {

    @PostMapping("/registerNewResource")
    public ResponseEntity<List<String>> registerNewResource(@RequestParam("image") MultipartFile[] images) {
        List<String> stringList = new ArrayList<>(images.length);
        for (MultipartFile file:images) {
            String name = String.valueOf(UUID.randomUUID());
            stringList.add(name);
            log.info("Registered new resource {}", name);
        }

        return new ResponseEntity<>(stringList, HttpStatus.CREATED);
    }

}
