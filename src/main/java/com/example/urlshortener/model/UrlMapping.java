package com.example.urlshortener.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="url_mappings", indexes=@Index(name="ux_url_short_code", columnList="shortCode", unique=true))
public class UrlMapping {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=12) private String shortCode;
    @Column(nullable=false, length=2048) private String originalUrl;
    @Column(nullable=false) private Instant createdAt;
    private Instant expiresAt;
    @Column(nullable=false) private long clickCount;

    protected UrlMapping() {}
    public UrlMapping(String shortCode, String originalUrl, Instant createdAt, Instant expiresAt) {
        this.shortCode=shortCode; this.originalUrl=originalUrl; this.createdAt=createdAt; this.expiresAt=expiresAt;
    }
    public Long getId(){return id;} public String getShortCode(){return shortCode;} public String getOriginalUrl(){return originalUrl;}
    public Instant getCreatedAt(){return createdAt;} public Instant getExpiresAt(){return expiresAt;} public long getClickCount(){return clickCount;}
    public void incrementClickCount(){clickCount++;}
}