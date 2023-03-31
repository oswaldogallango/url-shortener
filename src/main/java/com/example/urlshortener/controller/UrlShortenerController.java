package com.example.urlshortener.controller;

import com.example.urlshortener.common.Utils;
import com.example.urlshortener.dto.FullUrlDto;
import com.example.urlshortener.dto.UrlResponseDto;
import com.example.urlshortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final ShortUrlService shortUrlService;

    @PostMapping(value = "/shorten-url")
    public ResponseEntity<UrlResponseDto> shortenUrl(@Valid @RequestBody FullUrlDto urlRequest,
                                                     HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrl(request.getRequestURL().toString());
        return ResponseEntity.ok(shortUrlService.convertToShortUrl(urlRequest, baseUrl));
    }

    @GetMapping(value = "/{shortUrlCode}")
    public ResponseEntity<FullUrlDto> getFullUrl(@PathVariable String shortUrlCode) {
        return ResponseEntity.ok(shortUrlService.getFullUrl(shortUrlCode));
    }

}
