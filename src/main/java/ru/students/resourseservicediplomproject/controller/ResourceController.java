package ru.students.resourseservicediplomproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.students.resourseservicediplomproject.service.ResourceService;

import java.io.IOException;

@Slf4j
@Controller
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/resource")
    public ResponseEntity<String> registerNewResource(@RequestParam("image") MultipartFile images) throws IOException {
        log.info("Запрос на добавление ресурсов");
        //String uuidList = resourceService.processFiles(images);
        String uuidList = resourceService.saveResource(images.getResource(), images.getOriginalFilename());
        return new ResponseEntity<>(uuidList, HttpStatus.CREATED);
    }
    @GetMapping("/resource{uuid}")
    public ResponseEntity<String> getResource(@PathVariable String uuid) {
        log.info("Запрос ресурса {}", uuid);
        String base64Image = resourceService.base64Image(uuid);
        if (base64Image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(base64Image, HttpStatus.OK);
    }
    @DeleteMapping("/resource{uuid}")
    public ResponseEntity<String> deleteResource(@PathVariable String uuid){
        log.info("Запрос на удаление ресурса {}", uuid);
        boolean result = resourceService.deleteResource(uuid);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
