package ru.liga.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CurrencyType {
    USD("cbr/usd.csv"),
    EUR("cbr/eur.csv"),
    TRY("cbr/try.csv"),
    AMD("cbr/amd.csv"),
    BGN("cbr/bgn.csv");

    private String link;//todo сделай final
    CurrencyType(String link) {
        this.link = link;
    }//todo лучше использовать @AllArgsConstructor

    public static CurrencyType checkCurrency(String inputData) {//todo не используется

        List<String> inputList = Arrays.stream(inputData.split(" ")).toList();
        CurrencyType currencyType = CurrencyType.valueOf(inputList.get(1).toUpperCase());

        return currencyType;
    }


}
