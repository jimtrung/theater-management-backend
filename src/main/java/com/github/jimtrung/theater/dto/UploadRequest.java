package com.github.jimtrung.theater.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UploadRequest(MultipartFile file, String type, UUID id) {}
