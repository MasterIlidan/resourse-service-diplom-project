package ru.students.resourseservicediplomproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.students.resourseservicediplomproject.service.ResourceService;

import java.util.List;

@Slf4j
@Controller
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/registerNewResource")
    public ResponseEntity<List<String>> registerNewResource(@RequestParam("image") MultipartFile[] images) {
        List<String> uuidList = resourceService.processFiles(images);
        return new ResponseEntity<>(uuidList, HttpStatus.CREATED);
    }

}
