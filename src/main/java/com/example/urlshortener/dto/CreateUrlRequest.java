package com.example.urlshortener.dto;
import jakarta.validation.constraints.*;
public record CreateUrlRequest(@NotBlank @Pattern(regexp="https?://.+",message="url must start with http:// or https://") String url,
                               @Min(value=1,message="expirationMinutes must be positive") Long expirationMinutes){}