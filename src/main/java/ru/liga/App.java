package ru.liga;

import ru.liga.service.ForecastCurrencyService;

import java.io.IOException;

public class App {
    private static ForecastCurrencyService forecastCurrencyService;

    public static void main( String[] args ) throws IOException {
        forecastCurrencyService.getForecastResult();
    }
}
