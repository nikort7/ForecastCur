package ru.liga.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum CurrencyType {
    USD("usd.csv"),
    EUR("eur.csv"),
    TRY("try.csv");

    private String link;
    CurrencyType(String link) {
        this.link = "cbr/" + link;
    }

    public static CurrencyType checkCurrency(String inputData) {

        List<String> inputList = Arrays.stream(inputData.split(" ")).toList();
        CurrencyType currencyType = CurrencyType.valueOf(inputList.get(1).toUpperCase());

        return currencyType;
    }


}
