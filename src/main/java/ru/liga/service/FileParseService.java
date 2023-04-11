package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
            throw new IllegalArgumentException("file not found! " + fileName);// todo вынести в константу
        } else {
            return inputStream;
        }

    }

    /**
     * Печать данных из потока
     *
     * @param is Входящий поток, в котором лежат данные из прочтенного файла
     */
    private static void printInputStream(InputStream is) {//todo не используется

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
    private List<CurrencyRateDto> readFile(String pathToCsv) throws IOException {
        List<CurrencyRateDto> currencyList = new ArrayList<CurrencyRateDto>();
        CurrencyRateDto currencyRateDto = new CurrencyRateDto();

        InputStream is = getFileFromResourceAsStream(pathToCsv);

        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader)) {

            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.replaceAll("\"","").split(";");// todo вынести в константы

                currencyList.add(new CurrencyRateDto(data));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return currencyList;
    }

    public List<CurrencyRateDto> readFileCsv(String pathToCsv) throws IOException {//todo парсеру лучше не давать читать файл, лучше сделать FileReader и все, что он прочитал отдавать FileParserService
        return readFile(pathToCsv);
    }
}
