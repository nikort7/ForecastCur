package ru.liga.service;

import ru.liga.dto.CurrencyDto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class PrognozCurrencyService {

    final static String INPUT_CSV_FILE_USD = "cbr/usd.csv";
    final static String INPUT_CSV_FILE_EUR = "cbr/eur.csv";
    final static String INPUT_CSV_FILE_TRY = "cbr/try.csv";

    private static ArrayList<CurrencyDto> currencyListUsd = new ArrayList<>();
    private static ArrayList<CurrencyDto> currencyListEur = new ArrayList<>();
    private static ArrayList<CurrencyDto> currencyListTry = new ArrayList<>();

    /**
     * Получение файла из папки ресурсов
     *
     * @param fileName
     * @return Входящий поток
     */
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    /**
     * Печать данных из потока
     *
     * @param is Входящий поток, в котором лежат данные из прочтенного файла
     */
    private static void printInputStream(InputStream is) {

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Чтение из файла
     *
     * @param pathToCsv
     * @throws IOException Если есть проблема при чтении файлов
     */
    private static ArrayList<CurrencyDto> readFile(String pathToCsv) throws IOException {
        ArrayList<CurrencyDto> currencyList = new ArrayList<CurrencyDto>();
        CurrencyDto currencyDto = new CurrencyDto();

        PrognozCurrencyService app = new PrognozCurrencyService();
        InputStream is = app.getFileFromResourceAsStream(pathToCsv);
        //printInputStream(is);

        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.replaceAll("\"","").split(";");

                currencyList.add(new CurrencyDto(data));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return currencyList;
    }

    /**
     * Заполнение преременных данными из файлов
     *
     * @throws IOException Если есть проблема при чтении файлов
     */
    private static void initInfo() {
        try {
            currencyListUsd = readFile(INPUT_CSV_FILE_USD);
            currencyListEur = readFile(INPUT_CSV_FILE_EUR);
            currencyListTry = readFile(INPUT_CSV_FILE_TRY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Получение даты, следующей за передаваемой, в русской локали
     *
     * @param dateParam Дата из файла
     * @return Результат пробразования даты
     * @throws ParseException Если есть проблема при преобразовании даты
     */
    public static String getDateInRus(String dateParam) throws ParseException {
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateParam);
        // converting date to LocalDateTime
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(1);

        //Day of week and month in Rus
        Locale rusLocale = new Locale("ru", "RU");
        return localDateTime.format(DateTimeFormatter.ofPattern("EE dd.MM.yyyy", rusLocale));
    }

    /**
     * Получение прогноза курса валют на завтра
     *
     * @param currencyList Список значений, по которому делам прогноз
     * @param remark Ремарка, опрделяющаю вид обрабатываемой валюты
     */
    public static void getPrognozResult(ArrayList<CurrencyDto> currencyList, String remark) {

        ArrayList<CurrencyDto> list = (ArrayList<CurrencyDto>) currencyList.stream().limit(7).collect(Collectors.toList());
        double avgRate = currencyList.stream()
                .limit(7)
                .mapToDouble(w -> Double.parseDouble(w.getCurs()))
                .sum()
                / 7;

        String dateStr = currencyList.get(0).getData();
        try {
            String dateInRus = getDateInRus(currencyList.get(0).getData());
            System.out.println("rate " + remark + " tomorrow " + dateInRus + " " + String.format("%.2f",avgRate));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение прогноза курса валют на неделю
     *
     * @param currencyList Список значений, по которому делам прогноз
     * @param remark Ремарка, опрделяющаю вид обрабатываемой валюты
     * @param flag Флаг выхода из рекурсии
     */
    public static void getPrognozWeekResult(ArrayList<CurrencyDto> currencyList, String remark, Integer flag) {
        ArrayList<CurrencyDto> list = (ArrayList<CurrencyDto>) currencyList.stream().limit(7).collect(Collectors.toList());
        double avgRate = currencyList.stream()
                .limit(7)
                .mapToDouble(w -> Double.parseDouble(w.getCurs()))
                .sum()
                / 7;
        String dateStr = currencyList.get(0).getData();

        try {
            String dateInRus = getDateInRus(currencyList.get(0).getData());
            if (flag == 0) {
                System.out.println("rate " + remark + " week");
            }
            System.out.println(dateInRus.substring(0, 1).toUpperCase() + dateInRus.substring(1) + " " + String.format("%.2f",avgRate));

            currencyList.add(0, new CurrencyDto(currencyList.get(0).getNominal(),
                                                      dateInRus.substring(3),
                                                      String.valueOf(avgRate),
                                                      currencyList.get(0).getCdx()));
            if (flag < 7) {
                getPrognozWeekResult(currencyList, remark, flag + 1);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Получение прогноза
     */
    public static void getPrognozResult() {
        initInfo();
        getPrognozResult(currencyListUsd, "USD");
        getPrognozResult(currencyListEur, "EUR");
        getPrognozResult(currencyListTry, "TRY");

        getPrognozWeekResult(currencyListUsd, "USD", 0);
        getPrognozWeekResult(currencyListEur, "EUR", 0);
        getPrognozWeekResult(currencyListTry, "TRY", 0);

    }

}
