package ru.liga;

import org.apache.commons.lang.WordUtils;
import ru.liga.service.PrognozCurrencyService;

import java.io.IOException;

public class App {
    private static PrognozCurrencyService prognozCurrencyService;

    public static void main( String[] args ) throws IOException {
        prognozCurrencyService.getPrognozResult();
    }
}
