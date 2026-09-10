package com.example.urlshortener.controller;
import com.example.urlshortener.dto.*; import com.example.urlshortener.service.UrlShortenerService; import jakarta.validation.Valid;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.net.URI;
@RestController
public class UrlController {
 private final UrlShortenerService service; public UrlController(UrlShortenerService service){this.service=service;}
 @PostMapping("/api/v1/urls") public ResponseEntity<CreateUrlResponse> create(@Valid @RequestBody CreateUrlRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));}
 @GetMapping("/{shortCode}") public ResponseEntity<Void> redirect(@PathVariable String shortCode){return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(service.resolve(shortCode))).build();}
 @GetMapping("/api/v1/urls/{shortCode}/analytics") public UrlAnalyticsResponse analytics(@PathVariable String shortCode){return service.analytics(shortCode);}
 @DeleteMapping("/api/v1/urls/{shortCode}") public ResponseEntity<Void> delete(@PathVariable String shortCode){service.delete(shortCode);return ResponseEntity.noContent().build();}
}