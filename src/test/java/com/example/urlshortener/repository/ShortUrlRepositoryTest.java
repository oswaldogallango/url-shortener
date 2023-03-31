package com.example.urlshortener.repository;

import com.example.urlshortener.model.ShortUrlRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ShortUrlRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    private static final String FULL_URL = "http://www.google.com";
    private static final String SHORT_URL_CODE = "a1bCd";
    private static final String SHORT_URL = "http://localhost:8080/" + SHORT_URL_CODE;

    private static final String FULL_URL_2 = "http://www.yahoo.com";
    private static final String SHORT_URL_CODE_2 = "fGHzTy";
    private static final String SHORT_URL_2 = "http://localhost:8080/" + SHORT_URL_CODE_2;


    @Test
    void shouldAddAShortUrlAndGetItByFullUrlAndByShortUrlCode() {
        ShortUrlRegistry shortUrlRegistry = new ShortUrlRegistry(FULL_URL, SHORT_URL_CODE, SHORT_URL);
        shortUrlRepository.save(shortUrlRegistry);

        assertThat(shortUrlRegistry.getId()).isNotNull();

        ShortUrlRegistry shortUrlRegistryFromDb = shortUrlRepository.findByFullUrl(FULL_URL);
        assertThat(shortUrlRegistryFromDb).isNotNull();
        assertThat(shortUrlRegistryFromDb.getFullUrl()).isEqualTo(FULL_URL);
        assertThat(shortUrlRegistryFromDb.getShortUrlCode()).isEqualTo(SHORT_URL_CODE);
        assertThat(shortUrlRegistryFromDb.getShortUrl()).isEqualTo(SHORT_URL);

        shortUrlRegistryFromDb = shortUrlRepository.findByShortUrlCode(SHORT_URL_CODE);
        assertThat(shortUrlRegistryFromDb).isNotNull();
        assertThat(shortUrlRegistryFromDb.getFullUrl()).isEqualTo(FULL_URL);
        assertThat(shortUrlRegistryFromDb.getShortUrlCode()).isEqualTo(SHORT_URL_CODE);
        assertThat(shortUrlRegistryFromDb.getShortUrl()).isEqualTo(SHORT_URL);
    }

    @Test
    void shouldThrowExceptionUniqueFullUrl() {
        ShortUrlRegistry shortUrlRegistry = new ShortUrlRegistry(FULL_URL, SHORT_URL_CODE, SHORT_URL);
        shortUrlRepository.save(shortUrlRegistry);
        assertThat(shortUrlRegistry.getId()).isNotNull();

        ShortUrlRegistry shortUrlRegistry2 = new ShortUrlRegistry(FULL_URL, SHORT_URL_CODE_2, SHORT_URL_2);
        assertThrows(Exception.class, () -> shortUrlRepository.save(shortUrlRegistry2));
    }

    @Test
    void shouldAThrowExceptionUniqueShortUrlCode() {
        ShortUrlRegistry shortUrlRegistry = new ShortUrlRegistry(FULL_URL, SHORT_URL_CODE, SHORT_URL);
        shortUrlRepository.save(shortUrlRegistry);
        assertThat(shortUrlRegistry.getId()).isNotNull();

        ShortUrlRegistry shortUrlRegistry2 = new ShortUrlRegistry(FULL_URL_2, SHORT_URL_CODE, SHORT_URL);
        assertThrows(Exception.class, () -> shortUrlRepository.save(shortUrlRegistry2));
    }
}
