package com.flexbox.backend.common.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CollectionResponse<T>(
        @JsonProperty("data") List<T> items
) {
}
