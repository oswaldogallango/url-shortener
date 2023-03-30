package com.example.urlshortener.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "short_url_registry")
@Getter
@Setter
@NoArgsConstructor
public class ShortUrlRegistry {

    public ShortUrlRegistry(String fullUrl, String shortUrlCode, String shortUrl) {
        this.fullUrl = fullUrl;
        this.shortUrlCode = shortUrlCode;
        this.shortUrl = shortUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "full_url", nullable = false, unique = true)
    private String fullUrl;

    @NotBlank
    @Column(name = "short_url_code", nullable = false, unique = true)
    private String shortUrlCode;

    @NotBlank
    @Column(name = "short_url", nullable = false)
    private String shortUrl;
}
