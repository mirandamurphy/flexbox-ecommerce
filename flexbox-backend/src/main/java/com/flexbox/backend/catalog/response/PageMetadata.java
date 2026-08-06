package com.flexbox.backend.catalog.response;

public record PageMetadata(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
