package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.CurrencyType;

import java.io.*;
import java.text.ParseException;
import java.util.*;


public class ForecastCurrencyService {

    /**
     * Заполнение преременных данными из файлов
     *
     * @throws IOException Если есть проблема при чтении файлов
     */
    private static List<CurrencyRateDto> initInfo(String inputData) {
        FileParseService fileParseService = new FileParseService();
        List<CurrencyRateDto> currencyList = new ArrayList<>();
        String currencyValue = CurrencyType.checkCurrency(inputData);
        try {
            if (currencyValue.equals("USD")) {
                currencyList = fileParseService.readFileCsv(CurrencyType.INPUT_CSV_FILE_USD.getLink());
            } else if (currencyValue.equals("EUR")) {
                currencyList = fileParseService.readFileCsv(CurrencyType.INPUT_CSV_FILE_EUR.getLink());
            } else if (currencyValue.equals("TRY")) {
                currencyList = fileParseService.readFileCsv(CurrencyType.INPUT_CSV_FILE_TRY.getLink());
            } else {
                System.out.println("Error when entering currency");
            }

            currencyList = completingList(currencyList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currencyList;
    }

    /**
     * Дозаплнение списка
     * @param currencyRateDtoList
     * @return
     */
    private static List<CurrencyRateDto> completingList(List<CurrencyRateDto> currencyRateDtoList) {
        String date = currencyRateDtoList.get(1).getDate();
        String currentDate = DateUtils.getCurrentDate();
        while (!currentDate.equals(date)) {
            double avgRate = currencyRateDtoList.stream()
                    .limit(7)
                    .mapToDouble(w -> Double.parseDouble(w.getCurrency()))
                    .sum()
                    / 7;

            try {
                String dateInRus = DateUtils.getDateInRus(currencyRateDtoList.get(0).getDate());
                date = dateInRus.substring(3);
                currencyRateDtoList.add(0, new CurrencyRateDto(currencyRateDtoList.get(0).getNominal(),
                        date,
                        String.valueOf(avgRate),
                        currencyRateDtoList.get(0).getCdx()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return currencyRateDtoList;
    }

    /**
     * Получение прогноза курса валют на завтра
     *
     * @param currencyList Список значений, по которому делам прогноз
     */
    public static void getForecastResult(List<CurrencyRateDto> currencyList) {
        double avgRate = currencyList.stream()
                .limit(7)
                .mapToDouble(w -> Double.parseDouble(w.getCurrency()))
                .sum()
                / 7;

        try {
            String dateInRus = DateUtils.getDateInRus(currencyList.get(0).getDate());
            System.out.println(dateInRus + " - " + String.format("%.2f",avgRate));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение прогноза курса валют на неделю
     *
     * @param currencyList Список значений, по которому делам прогноз
     * @param nextDayParam Параметр для выхода из рекурсии
     */
    public static void getForecastWeekResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) {
        double avgRate = currencyList.stream()
                .limit(7)
                .mapToDouble(w -> Double.parseDouble(w.getCurrency()))
                .sum()
                / 7;

        try {
            String dateInRus = DateUtils.getDateInRus(currencyList.get(0).getDate());

            System.out.println(dateInRus + " " + String.format("%.2f",avgRate));

            currencyList.add(0, new CurrencyRateDto(currencyList.get(0).getNominal(),
                                                      dateInRus.substring(3),
                                                      String.valueOf(avgRate),
                                                      currencyList.get(0).getCdx()));
            if (nextDayParam < 7) {
                getForecastWeekResult(currencyList, nextDayParam + 1);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение прогноза
     */
    public static void getForecastResult() {

        String inputDataFromConsole = InputData.inputFromConsole();

        List<CurrencyRateDto> currencyRateDtoList = initInfo(inputDataFromConsole);
        if (!currencyRateDtoList.isEmpty()) {
            if (inputDataFromConsole.matches("(.*)tomorrow")) {
                getForecastResult(currencyRateDtoList);
            } else if (inputDataFromConsole.matches("(.*)week")) {
                getForecastWeekResult(currencyRateDtoList, 0);
            }
            else {
                System.out.println("Input error");
            }
        }

    }

}
