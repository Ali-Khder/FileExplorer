package com.FileExplorer.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public record FilesProperties(int maxUpload) {
}
