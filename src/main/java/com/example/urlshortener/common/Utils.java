package com.example.urlshortener.common;

import com.example.urlshortener.exception.InvalidUrlException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static com.example.urlshortener.common.Constants.ERROR_INVALID_BASE_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Utils {

    /**
     * Validation simple de l'URL avec le constructor du java.net.URL
     *
     * @param url L'URL a valider
     * @return true si est l'URL es valide , faux sinon
     */
    public static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (URISyntaxException | MalformedURLException ex) {
            log.warn(ex.getMessage());
            return false;
        }
    }

    /**
     * Retourne l'URL base à partir du string du request URL
     *
     * @param requestBaseUrl Le request URL
     * @return l'URL base
     */
    public static String getBaseUrl(String requestBaseUrl) {
        try {
            URL url = new URL(requestBaseUrl);
            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();

            if (port == -1) {
                return String.format("%s://%s/", protocol, host);
            } else {
                return String.format("%s://%s:%d/", protocol, host, port);
            }
        } catch (MalformedURLException ex) {
            log.warn(ex.getMessage(), ex);
            throw new InvalidUrlException(String.format(ERROR_INVALID_BASE_URL, requestBaseUrl));
        }

    }
}
