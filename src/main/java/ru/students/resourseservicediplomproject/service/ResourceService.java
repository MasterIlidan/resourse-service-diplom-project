package ru.students.resourseservicediplomproject.service;

public interface ResourceService {

    //String processFiles(Resource fileResource, String fileName);

    String saveResource(org.springframework.core.io.Resource fileResource, String fileName);

    String base64Image(String uuid);

    boolean deleteResource(String uuid);
}
