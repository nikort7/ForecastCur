package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.*;
import ru.liga.utils.DateUtils;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


public class ForecastCurrencyService {//todo делать все методы статическими не хорошо
    private static final int CONST_OLD_ALG_VALUE = 7;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;

    /**
     * Дозаплнение списка
     * @param currencyRateDtoList
     * @return
     */
    public static List<CurrencyRateDto> completingList(List<CurrencyRateDto> currencyRateDtoList) {
        String date = currencyRateDtoList.get(ZERO).getDate();

        int differenceDays = DateUtils.getDifferenceDays(date);
        if (differenceDays < ZERO) {
            for (int i = 0; i < Math.abs(differenceDays); i++) {
                currencyRateDtoList.remove(i);
            }
        }
        else {
            ForecastCurrencyService.getForecastOldResult(currencyRateDtoList, differenceDays);
        }

        return currencyRateDtoList;
    }

    private static double getAvgRate(List<CurrencyRateDto> currencyList) {
        double sumRate = currencyList.stream()
                .limit(CONST_OLD_ALG_VALUE)
                .mapToDouble(w -> Double.parseDouble(w.getCurrency()))
                .sum();
        return sumRate / CONST_OLD_ALG_VALUE;
    }

    /**
     * Получение прогноза курса валют
     *
     * @param currencyList Список значений, по которому делам прогноз
     * @param nextDayParam количство дней для заполнения листа
     */
    public static void getForecastOldResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) {
        for (int outLoop = 0; outLoop < nextDayParam; outLoop++) {
            double avgRate = getAvgRate(currencyList);
            try {
                String dateInRus = DateUtils.getDateInRus(currencyList.get(0).getDate());

                currencyList.add(0, new CurrencyRateDto(currencyList.get(ZERO).getNominal(),
                        dateInRus,
                        String.valueOf(avgRate),
                        currencyList.get(ZERO).getCdx()));

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void getForecastLastYearResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) throws ParseException {

        for (int outLoop = 0; outLoop < nextDayParam; outLoop++) {
            String prevYearDate = DateUtils.getNowDateLastYear();
            String dateInRus = DateUtils.getDateInRus(currencyList.get(0).getDate());
            Optional<CurrencyRateDto> currencyRateDto = currencyList.stream()
                    .filter(currency -> currency.getDate().equals(prevYearDate))
                    .findAny();
            if (currencyRateDto.isPresent()) {//todo isPresent. Но лучше использовать Optional красивее через методы map и orElse/orElseGet
                String prevYearRate = currencyRateDto.get().getCurrency();

                currencyList.add(0, new CurrencyRateDto(currencyList.get(ZERO).getNominal(),
                        dateInRus,
                        String.valueOf(prevYearRate),
                        currencyList.get(0).getCdx()));
            }
            else {
                currencyList.add(0, new CurrencyRateDto(currencyList.get(ZERO).getNominal(),
                        dateInRus,
                        currencyList.get(ZERO).getCurrency(),
                        currencyList.get(ZERO).getCdx()));
            }
        }
    }

    public static void getForecastMistResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) throws ParseException {
        for (int outLoop = 0; outLoop < nextDayParam; outLoop++) {
            String dateInRus = DateUtils.getDateInRus(currencyList.get(0).getDate());
            String dateMonth = DateUtils.getDateWithoutYear(dateInRus);
            List<CurrencyRateDto> currencyRateDto = currencyList.stream()
                    .filter(currency -> currency.getDate().contains(dateMonth))
                    .collect(Collectors.toList());

            if (!currencyRateDto.isEmpty()) {//todo isPresent. Но лучше использовать Optional красивее через методы map и orElse/orElseGet
                Random rand = new Random();
                String randomCurrency = currencyRateDto.get(rand.nextInt(currencyRateDto.size())).getCurrency();

                currencyList.add(0, new CurrencyRateDto(currencyList.get(ZERO).getNominal(),
                        dateInRus,
                        String.valueOf(randomCurrency),
                        currencyList.get(ZERO).getCdx()));
            }
            else {
                currencyList.add(0, new CurrencyRateDto(currencyList.get(ZERO).getNominal(),
                        dateInRus,
                        currencyList.get(ZERO).getCurrency(),
                        currencyList.get(ZERO).getCdx()));
            }
        }
    }


    public static void getForecastResult(List<CurrencyRateDto> currencyList,
                                         TimeRange timeRange,
                                         Algorithms algorithms) throws ParseException {
        if (algorithms.getAlgorithmType() == ZERO) {
            ForecastCurrencyService.getForecastOldResult(currencyList, timeRange.getDays());
        } else if (algorithms.getAlgorithmType() == ONE) {
            ForecastCurrencyService.getForecastLastYearResult(currencyList, timeRange.getDays());
        } else if (algorithms.getAlgorithmType() == TWO) {
            ForecastCurrencyService.getForecastMistResult(currencyList, timeRange.getDays());
        } else if (algorithms.getAlgorithmType() == THREE) {
            ForecastCurrencyService.getForecastLinRegResult(currencyList, timeRange.getDays());
        }

    }

    private static void getForecastLinRegResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) throws ParseException {
        for (int outLoop = 0; outLoop < nextDayParam; outLoop++) {
            String dateInRus = DateUtils.getDateInRus(currencyList.get(0).getDate());
            String dateLastMonth = DateUtils.getDateLastMonth(currencyList, currencyList.get(0).getDate());
            List<CurrencyRateDto> currencyRateDtoLastMonthList = new ArrayList<>();

            for (CurrencyRateDto currencyRateDto : currencyList) {
                if (!currencyRateDto.getDate().equals(dateLastMonth)) {
                    currencyRateDtoLastMonthList.add(currencyRateDto);
                } else {
                    break;
                }
            }

            List<Double> rateList = currencyRateDtoLastMonthList.stream()
                    .map(rate -> Double.parseDouble(rate.getCurrency()))
                    .collect(Collectors.toList());

            List<Double> days = currencyRateDtoLastMonthList.stream()
                    .map(rate -> DateUtils.getDayOfMonth(rate.getDate()))
                    .collect(Collectors.toList());

            LinearRegression linearRegression = new LinearRegression(days, rateList);
            currencyList.add(0, new CurrencyRateDto(currencyList.get(0).getNominal(),//todo магическое число
                    dateInRus,
                    String.valueOf(linearRegression.predict(days.get(0))),//todo магическое число
                    currencyList.get(0).getCdx()));
        }
    }
}
