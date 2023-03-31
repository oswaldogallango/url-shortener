package com.example.urlshortener.service;

import com.example.urlshortener.dto.FullUrlDto;
import com.example.urlshortener.dto.UrlResponseDto;
import com.example.urlshortener.exception.InvalidUrlException;
import com.example.urlshortener.exception.ShortUrlNotFoundException;
import com.example.urlshortener.model.ShortUrlRegistry;
import com.example.urlshortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlServiceImpl shortUrlService;

    private static final String BASE_URL = "http://demo.example.com/";
    private static final String FULL_URL = "https://translate.google.ca/?sl=fr&tl=en&text=Le%20code%20d%E2%80%99URL%20raccourcie%20(%25s)%20n%27existe%20pas&op=translate";
    private static final String SHORT_URL_CODE = "a1Zb8YQ";
    private static final String SHORT_URL = BASE_URL + SHORT_URL_CODE;

    private Random random = new Random();

    @Test
    void getFullUrlSuccessfully() {

        ShortUrlRegistry entity = new ShortUrlRegistry(FULL_URL, SHORT_URL_CODE, SHORT_URL);
        when(shortUrlRepository.findByShortUrlCode(SHORT_URL_CODE)).thenReturn(entity);

        FullUrlDto result = shortUrlService.getFullUrl(SHORT_URL_CODE);
        assertThat(result).isNotNull();
        assertThat(result.getFullUrl()).isEqualTo(FULL_URL);
    }

    @Test
    void getFullUrlWithShortUrlNotFoundException() {
        String shortUrlCode = "a3Zb8DF";
        when(shortUrlRepository.findByShortUrlCode(shortUrlCode)).thenReturn(null);
        assertThrows(ShortUrlNotFoundException.class, () -> shortUrlService.getFullUrl(shortUrlCode));
    }

    @Test
    void convertToShortUrlSuccessfullyWithNonExistingShortUrl() {

        FullUrlDto fullUrlDto = new FullUrlDto(FULL_URL);
        when(shortUrlRepository.findByFullUrl(FULL_URL)).thenReturn(null);

        ShortUrlRegistry entity = new ShortUrlRegistry(FULL_URL, SHORT_URL_CODE, SHORT_URL);
        entity.setId(random.nextLong());
        when(shortUrlRepository.save(any(ShortUrlRegistry.class))).thenReturn(entity);

        UrlResponseDto result = shortUrlService.convertToShortUrl(fullUrlDto, BASE_URL);

        assertThat(result).isNotNull();
        assertThat(result.getFullUrl()).isEqualTo(FULL_URL);
        assertThat(result.getShortUrl()).isNotNull();

    }

    @Test
    void convertToShortUrlSuccessfullyWithExistingShortUrl() {
        FullUrlDto fullUrlDto = new FullUrlDto(FULL_URL);

        ShortUrlRegistry entity = new ShortUrlRegistry(FULL_URL, SHORT_URL_CODE, SHORT_URL);
        entity.setId(random.nextLong());
        when(shortUrlRepository.findByFullUrl(FULL_URL)).thenReturn(entity);

        UrlResponseDto result = shortUrlService.convertToShortUrl(fullUrlDto, BASE_URL);

        assertThat(result).isNotNull();
        assertThat(result.getFullUrl()).isEqualTo(FULL_URL);
        assertThat(result.getShortUrl()).isEqualTo(SHORT_URL);
    }

    @Test
    void convertToShortUrlWithExceptionInvalidFullUrl() {
        FullUrlDto fullUrlDto = new FullUrlDto("htp://demo.example.com/test");

        assertThrows(InvalidUrlException.class, () -> shortUrlService.convertToShortUrl(fullUrlDto, BASE_URL));
    }
}
