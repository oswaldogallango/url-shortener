package com.example.urlshortener.repository;

import com.example.urlshortener.model.ShortUrlRegistry;
import org.springframework.data.repository.CrudRepository;

public interface ShortUrlRepository extends CrudRepository<ShortUrlRegistry, Long> {

    ShortUrlRegistry findByFullUrl(String fullUrl);

    ShortUrlRegistry findByShortUrlCode(String shortUrlCode);
}
