package ru.students.resourseservicediplomproject.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ImagePathExtractor {
    @Value("${text.images.path:default}") private String imagePath;
}
