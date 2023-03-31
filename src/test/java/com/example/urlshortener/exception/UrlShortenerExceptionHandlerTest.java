package com.example.urlshortener.exception;

import com.example.urlshortener.controller.UrlShortenerController;
import com.example.urlshortener.dto.FullUrlDto;
import com.example.urlshortener.service.ShortUrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.urlshortener.common.Constants.ERROR_INVALID_FULL_URL;
import static com.example.urlshortener.common.Constants.ERROR_SHORT_URL_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShortUrlService shortUrlService;

    @Test
    void notFoundExceptionHandling() throws Exception {
        String shortUrlCode = "abc";
        when(shortUrlService.getFullUrl(shortUrlCode))
                .thenThrow(new ShortUrlNotFoundException(String
                        .format(ERROR_SHORT_URL_NOT_FOUND, shortUrlCode)));

        mvc.perform(MockMvcRequestBuilders
                        .get("/abc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidUrlExceptionHandling() throws Exception {
        String fullUrl = "www.google.com/test/a";

        //FullUrlDto fullUrlDto = new FullUrlDto(fullUrl);

        when(shortUrlService.convertToShortUrl(any(FullUrlDto.class), anyString()))
                .thenThrow(new InvalidUrlException(String.
                        format(ERROR_INVALID_FULL_URL, fullUrl)));

        mvc.perform(MockMvcRequestBuilders
                        .post("/shorten-url")
                        .content(String.format("{ \"fullUrl\": \"%s\"}", fullUrl))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
