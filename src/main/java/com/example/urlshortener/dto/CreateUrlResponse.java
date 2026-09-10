package com.example.urlshortener.dto;
import java.time.Instant;
public record CreateUrlResponse(String shortCode,String shortUrl,Instant expiresAt){}