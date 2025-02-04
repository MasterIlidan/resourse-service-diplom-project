package ru.students.resourseservicediplomproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ResourceController {

    @GetMapping("/registerNewResource")
    public ResponseEntity<String> registerNewResource() {
        log.info("Registered new resource");
        return new ResponseEntity<>("register new resource", HttpStatus.CREATED);
    }
}
