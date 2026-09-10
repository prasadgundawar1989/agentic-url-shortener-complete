package com.example.urlshortener.service;

import com.example.urlshortener.dto.*;
import com.example.urlshortener.exception.*;
import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.Instant;

@Service
public class UrlShortenerService {
    private static final String ALPHABET="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final SecureRandom random=new SecureRandom(); private final UrlMappingRepository repository; private final String baseUrl;
    public UrlShortenerService(UrlMappingRepository repository,@Value("${app.base-url:http://localhost:8080}") String baseUrl){this.repository=repository;this.baseUrl=baseUrl;}

    @Transactional
    public CreateUrlResponse create(CreateUrlRequest request){
        String code; do{code=code(7);}while(repository.findByShortCode(code).isPresent());
        Instant now=Instant.now(); Instant expiry=request.expirationMinutes()==null?null:now.plusSeconds(request.expirationMinutes()*60);
        UrlMapping saved=repository.save(new UrlMapping(code,request.url(),now,expiry));
        return new CreateUrlResponse(saved.getShortCode(),baseUrl+"/"+saved.getShortCode(),saved.getExpiresAt());
    }
    @Transactional
    public String resolve(String code){
        UrlMapping m=find(code); if(m.getExpiresAt()!=null && Instant.now().isAfter(m.getExpiresAt())) throw new ExpiredUrlException("Short URL has expired");
        m.incrementClickCount(); return m.getOriginalUrl();
    }
    @Transactional(readOnly=true)
    public UrlAnalyticsResponse analytics(String code){UrlMapping m=find(code);return new UrlAnalyticsResponse(m.getShortCode(),m.getOriginalUrl(),m.getClickCount(),m.getCreatedAt(),m.getExpiresAt());}
    @Transactional
    public void delete(String code){UrlMapping m=find(code);repository.delete(m);}
    private UrlMapping find(String code){return repository.findByShortCode(code).orElseThrow(()->new NotFoundException("Short URL not found: "+code));}
    private String code(int n){StringBuilder b=new StringBuilder(n);for(int i=0;i<n;i++)b.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));return b.toString();}
}