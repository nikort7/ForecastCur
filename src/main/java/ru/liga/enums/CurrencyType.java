package ru.liga.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum CurrencyType {
    INPUT_CSV_FILE_USD("cbr/usd.csv"),
    INPUT_CSV_FILE_EUR("cbr/eur.csv"),
    INPUT_CSV_FILE_TRY("cbr/try.csv");

    private String link;
    CurrencyType(String link) {
        this.link = link;
    }

    public static String checkCurrency(String inputData) {
        List<String> inputList = Arrays.stream(inputData.split(" ")).toList();
        List<String> returnList = new ArrayList<>();
        String returnValue = new String();
        for (String inputValue : inputList) {
            if (inputValue.equals("USD") || inputValue.equals("EUR") || inputValue.equals("TRY")) {
                returnList.add(inputValue);
            }
        }
        if (returnList.size() == 1) {
            returnValue = returnList.get(0);
        }
        return returnValue;
    }
}
