package com.rest.marketplace.infrastructure.rest.advice.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErrorResponse(Integer code,
							String detail,
                            String message,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                            LocalDateTime timestamp,
                            String path) {
}
