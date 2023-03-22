package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileParseService {

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
    private static ArrayList<CurrencyRateDto> readFile(String pathToCsv) throws IOException {
        ArrayList<CurrencyRateDto> currencyList = new ArrayList<CurrencyRateDto>();
        CurrencyRateDto currencyRateDto = new CurrencyRateDto();

        FileParseService app = new FileParseService();
        InputStream is = app.getFileFromResourceAsStream(pathToCsv);
        //printInputStream(is);

        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.replaceAll("\"","").split(";");

                currencyList.add(new CurrencyRateDto(data));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return currencyList;
    }

    public static ArrayList<CurrencyRateDto> readFileCsv(String pathToCsv) throws IOException {
        return readFile(pathToCsv);
    }
}
