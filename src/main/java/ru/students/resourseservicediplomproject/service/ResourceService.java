package ru.students.resourseservicediplomproject.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {
    List<String> processFiles(MultipartFile[] files);

    void saveResource(String uuid, String name);

    String base64Image(String uuid);
}
