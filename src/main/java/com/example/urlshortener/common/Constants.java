package com.example.urlshortener.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String ERROR_INVALID_BASE_URL = "L'URL de base introuvable";
    public static final String ERROR_INVALID_FULL_URL = "L'URL complète (%s) est invalide";
    public static final String ERROR_SHORT_URL_NOT_FOUND = "Le code d’URL raccourcie (%s) n'existe pas";
    public static final String SHORT_URL_PATTERN = "%s%s";
}
