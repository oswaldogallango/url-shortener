package com.example.urlshortener.controller;

import com.example.urlshortener.dto.FullUrlDto;
import com.example.urlshortener.dto.UrlResponseDto;
import com.example.urlshortener.exception.InvalidUrlException;
import com.example.urlshortener.exception.ShortUrlNotFoundException;
import com.example.urlshortener.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.example.urlshortener.common.Constants.ERROR_INVALID_FULL_URL;
import static com.example.urlshortener.common.Constants.ERROR_SHORT_URL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private ShortUrlService urlService;

    @InjectMocks
    private UrlShortenerController urlShortenerController;

    private static final String BASE_URL = "http://localhost/";
    private static final String FULL_URL = "http://www.google.com";
    private static final String INVALID_FULL_URL = "htp://www.google.com";
    private static final String SHORT_URL_CODE = "abd17Ty";
    private static final String SHORT_URL = BASE_URL + SHORT_URL_CODE;

    private static final String INVALID_SHORT_URL_CODE = "abc";

    @Test
    void shortenUrlWith200Response() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        FullUrlDto fullUrlDto = new FullUrlDto(FULL_URL);
        UrlResponseDto urlResponseDto = new UrlResponseDto(FULL_URL, SHORT_URL);
        when(urlService.convertToShortUrl(fullUrlDto, BASE_URL)).thenReturn(urlResponseDto);

        ResponseEntity<UrlResponseDto> response = urlShortenerController.shortenUrl(fullUrlDto, request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .satisfies(value -> assertThat(value.getFullUrl()).isEqualTo(FULL_URL))
                .satisfies(value -> assertThat(value.getShortUrl()).isEqualTo(SHORT_URL));

    }

    @Test
    void shortenUrlThrowsInvalidUrlException() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        FullUrlDto fullUrlDto = new FullUrlDto(INVALID_FULL_URL);
        when(urlService.convertToShortUrl(fullUrlDto, BASE_URL))
                .thenThrow(new InvalidUrlException(String.format(ERROR_INVALID_FULL_URL, INVALID_FULL_URL)));

        assertThrows(InvalidUrlException.class, () -> urlShortenerController.shortenUrl(fullUrlDto, request));
    }

    @Test
    void getFullUrlWith200Response() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        FullUrlDto fullUrlDto = new FullUrlDto(FULL_URL);
        when(urlService.getFullUrl(SHORT_URL_CODE)).thenReturn(fullUrlDto);

        ResponseEntity<FullUrlDto> response = urlShortenerController.getFullUrl(SHORT_URL_CODE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .satisfies(value -> assertThat(value.getFullUrl()).isEqualTo(FULL_URL));
    }

    @Test
    void getFullUrlThrowsShortUrlNotFoundException() {

        when(urlService.getFullUrl(INVALID_SHORT_URL_CODE))
                .thenThrow(new ShortUrlNotFoundException(String.format(ERROR_SHORT_URL_NOT_FOUND, INVALID_SHORT_URL_CODE)));

        assertThrows(ShortUrlNotFoundException.class, () -> urlShortenerController.getFullUrl(INVALID_SHORT_URL_CODE));
    }
}
