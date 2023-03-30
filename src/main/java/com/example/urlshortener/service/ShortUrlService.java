package com.example.urlshortener.service;

import com.example.urlshortener.dto.FullUrlDto;
import com.example.urlshortener.dto.UrlResponseDto;
import com.example.urlshortener.exception.InvalidUrlException;
import com.example.urlshortener.exception.ShortUrlNotFoundException;

public interface ShortUrlService {

    /**
     * Génère une URL raccourcie à partir d'une URL complète. Si l'URL complète
     * est invalide une exception {@link InvalidUrlException} est levée.
     *
     * @param fullUrlDto L'URL complète à convertir
     * @param baseUrl    L'URL de base pour générer l'url raccourcie
     * @retour à {@link UrlResponseDto}
     */
    UrlResponseDto convertToShortUrl(FullUrlDto fullUrlDto, String baseUrl);

    /**
     * Recherche un {@link FullUrlDto} à partir d'un shortcode d'URL. Si l'URL raccourcie
     * n'existe pas, une exception {@link ShortUrlNotFoundException} est levée.
     *
     * @param shortUrlCode Le code URL raccourcie à chercher
     * @return un {@link FullUrlDto}
     */
    FullUrlDto getFullUrl(String shortUrlCode);

}
