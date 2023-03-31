package com.example.urlshortener.common;

import com.example.urlshortener.exception.InvalidUrlException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsTest {

    private static final String VALID_URL = "https://translate.google.ca/?sl=fr&tl=es&text=Ici%20on%20mange%20bien&op=translate";
    private static final String INVALID_URL = "www.mypage";
    private static final String URL_REQUEST_1 = "http://demo.example.com:8080/url-shortener";
    private static final String BASER_URL_1 = "http://demo.example.com:8080/";

    private static final String URL_REQUEST_2 = "http://demo.example.com/url-shortener";
    private static final String BASER_URL_2 = "http://demo.example.com/";

    private static final String INVALID_URL_REQUEST = "www.mypage/url-shortener";
    private static final String INVALID_URL_REQUEST_2 = "htp://www.mypage/url-shortener";

    @Test
    void shouldBeAValidUrl() {
        assertThat(Utils.isValidUrl(VALID_URL)).isTrue();
    }

    @Test
    void shouldBeAnInValidUrl() {
        assertThat(Utils.isValidUrl(INVALID_URL)).isFalse();
    }

    @Test
    void getBaseUrlWithResult() {
        assertThat(Utils.getBaseUrl(URL_REQUEST_1)).isEqualTo(BASER_URL_1);
        assertThat(Utils.getBaseUrl(URL_REQUEST_2)).isEqualTo(BASER_URL_2);
    }

    @Test
    void getBaseUrlThrowsException() {
        assertThrows(InvalidUrlException.class, () -> Utils.getBaseUrl(INVALID_URL_REQUEST));
        assertThrows(InvalidUrlException.class, () -> Utils.getBaseUrl(INVALID_URL_REQUEST_2));
    }
}
