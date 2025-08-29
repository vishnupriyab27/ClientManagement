package com.assignment.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ResourceJsonReader {
    private final ObjectMapper mapper;

    public ResourceJsonReader(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> T readJsonFile(String path, TypeReference<T> typeReference) {
        try (InputStream stream = new ClassPathResource(path).getInputStream()){
            return this.mapper.readValue(stream, typeReference);
        }catch (IOException e){
            throw new RuntimeException("Failed to read the JSON file", e);
        }
    }
}
