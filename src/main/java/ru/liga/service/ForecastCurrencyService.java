package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


public class ForecastCurrencyService {

    final static String INPUT_CSV_FILE_USD = "cbr/usd.csv";
    final static String INPUT_CSV_FILE_EUR = "cbr/eur.csv";
    final static String INPUT_CSV_FILE_TRY = "cbr/try.csv";

    private static FileParseService fileParseService;
    private static DateUtils dateUtils;

    private static InputData inputData;

    private static List<CurrencyRateDto> currencyList = new ArrayList<>();


    /**
     * Заполнение преременных данными из файлов
     *
     * @throws IOException Если есть проблема при чтении файлов
     */
    private static String initInfo(String inputData) {
        String currency = null;
        try {
            if (inputData.contains("USD")) {
                currencyList = fileParseService.readFileCsv(INPUT_CSV_FILE_USD);
                currency = "USD";
            } else if (inputData.contains("EUR")) {
                currencyList = fileParseService.readFileCsv(INPUT_CSV_FILE_EUR);
                currency = "EUR";
            } else if (inputData.contains("TRY")) {
                currencyList = fileParseService.readFileCsv(INPUT_CSV_FILE_TRY);
                currency = "TRY";
            } else {
                System.out.println("Error when entering currency");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currency;
    }

    /**
     * Получение прогноза курса валют на завтра
     *
     * @param currencyList Список значений, по которому делам прогноз
     * @param currencyType Тип обрабатываемой валюты
     */
    public static void getForecastResult(List<CurrencyRateDto> currencyList, String currencyType) {
        ArrayList<CurrencyRateDto> list = (ArrayList<CurrencyRateDto>) currencyList.stream().limit(7).collect(Collectors.toList());
        double avgRate = currencyList.stream()
                .limit(7)
                .mapToDouble(w -> Double.parseDouble(w.getCurrency()))
                .sum()
                / 7;

        String dateStr = currencyList.get(0).getDate();
        try {
            String dateInRus = dateUtils.getDateInRus(currencyList.get(0).getDate());
            System.out.println("rate " + currencyType + " tomorrow " + dateInRus + " " + String.format("%.2f",avgRate));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение прогноза курса валют на неделю
     *
     * @param currencyList Список значений, по которому делам прогноз
     * @param currencyType Вид обрабатываемой валюты
     * @param executeParam Параметр для выхода из рекурсии
     */
    public static void getForecastWeekResult(List<CurrencyRateDto> currencyList, String currencyType, Integer executeParam) {
        ArrayList<CurrencyRateDto> list = (ArrayList<CurrencyRateDto>) currencyList.stream().limit(7).collect(Collectors.toList());
        double avgRate = currencyList.stream()
                .limit(7)
                .mapToDouble(w -> Double.parseDouble(w.getCurrency()))
                .sum()
                / 7;
        String dateStr = currencyList.get(0).getDate();

        try {
            String dateInRus = dateUtils.getDateInRus(currencyList.get(0).getDate());
            if (executeParam == 0) {
                System.out.println("rate " + currencyType + " week");
            }
            System.out.println(dateInRus.substring(0, 1).toUpperCase() + dateInRus.substring(1) + " " + String.format("%.2f",avgRate));

            currencyList.add(0, new CurrencyRateDto(currencyList.get(0).getNominal(),
                                                      dateInRus.substring(3),
                                                      String.valueOf(avgRate),
                                                      currencyList.get(0).getCdx()));
            if (executeParam < 7) {
                getForecastWeekResult(currencyList, currencyType, executeParam + 1);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение прогноза
     */
    public static void getForecastResult() {

        String inputDataFromConsole = inputData.inputFromConsole();

        String currency = initInfo(inputDataFromConsole);
        if (!currency.isEmpty()) {
            if (inputDataFromConsole.contains("tomorrow")) {
                getForecastResult(currencyList, currency);
            } else if (inputDataFromConsole.contains("week")) {
                getForecastWeekResult(currencyList, currency, 0);
            }
            else {
                System.out.println("Input error");
            }
        }

    }

}
