package com.example.urlshortener.service;

import com.example.urlshortener.common.Utils;
import com.example.urlshortener.dto.FullUrlDto;
import com.example.urlshortener.dto.UrlResponseDto;
import com.example.urlshortener.exception.InvalidUrlException;
import com.example.urlshortener.exception.ShortUrlNotFoundException;
import com.example.urlshortener.model.ShortUrlRegistry;
import com.example.urlshortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import static com.example.urlshortener.common.Constants.*;

@Service
@RequiredArgsConstructor
public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    /**
     * Génère une URL raccourcie à partir d'une URL complète. Si l'URL complète
     * est invalide une exception {@link InvalidUrlException} est levée.
     *
     * @param fullUrlDto L'URL complète à convertir
     * @param baseUrl    L'URL de base pour générer l'url raccourcie
     * @retour à {@link UrlResponseDto}
     */
    public UrlResponseDto convertToShortUrl(FullUrlDto fullUrlDto, String baseUrl) {
        var fullUrl = fullUrlDto.getFullUrl().trim();

        if (!Utils.isValidUrl(fullUrl)) {
            throw new InvalidUrlException(String.format(ERROR_INVALID_FULL_URL, fullUrl));
        }

        var shortUrl = generateShortUrl(fullUrl, baseUrl);

        return new UrlResponseDto(fullUrl, shortUrl);
    }

    /**
     * Recherche un {@link FullUrlDto} à partir d'un shortcode d'URL. Si l'URL raccourcie
     * n'existe pas, une exception {@link ShortUrlNotFoundException} est levée.
     *
     * @param shortUrlCode Le code URL raccourcie à chercher
     * @return un {@link FullUrlDto}
     */
    public FullUrlDto getFullUrl(String shortUrlCode) {
        var shortUrl = getByShortUrlCode(shortUrlCode);
        if (shortUrl == null) {
            throw new ShortUrlNotFoundException(String.format(ERROR_SHORT_URL_NOT_FOUND, shortUrlCode));
        }

        return new FullUrlDto(shortUrl.getFullUrl());
    }

    /**
     * Générer une URL raccourcie pour une URL complète avec une URL de base. Si l'URL complète
     * est déjà associée à une URL raccourcie, renvoyez la valeur existante.
     *
     * @param fullUrl L'URL complète à convertir
     * @param baseUrl L'URL de base pour générer l'url raccourcie
     * @return un URL raccourcie
     */
    private String generateShortUrl(String fullUrl, String baseUrl) {
        var shortUrl = getByFullUrl(fullUrl);
        if (shortUrl == null) {
            var shortUrlCode = RandomStringUtils.randomAlphanumeric(3, 11);
            shortUrl = new ShortUrlRegistry(fullUrl, shortUrlCode, String.format(SHORT_URL_PATTERN, baseUrl, shortUrlCode));
            addShortUrl(shortUrl);
        }
        return shortUrl.getShortUrl();
    }

    /**
     * Recherche un {@link ShortUrlRegistry} a partir d'un URL complète
     *
     * @param fullUrl L'URL complète à chercher
     * @return un {@link ShortUrlRegistry}
     */
    private ShortUrlRegistry getByFullUrl(String fullUrl) {
        return shortUrlRepository.findByFullUrl(fullUrl);
    }

    /**
     * Enregister un object {@link ShortUrlRegistry} dans la base de données
     *
     * @param shortUrlRegistry L'object à enregistrer
     */
    private void addShortUrl(ShortUrlRegistry shortUrlRegistry) {
        shortUrlRepository.save(shortUrlRegistry);
    }

    /**
     * Recherche un {@link ShortUrlRegistry} a partir d'un URL complète
     *
     * @param shortUrlCode Le code URL raccourcie à chercher
     * @return un {@link ShortUrlRegistry}
     */
    private ShortUrlRegistry getByShortUrlCode(String shortUrlCode) {
        return shortUrlRepository.findByShortUrlCode(shortUrlCode);
    }
}
