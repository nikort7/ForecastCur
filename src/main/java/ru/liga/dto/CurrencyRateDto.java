package ru.liga.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyRateDto {
    private String nominal;
    private String date;
    private String currency;
    private String cdx;

    public CurrencyRateDto(String nominal, String date, String currency, String cdx) {
        this.nominal = nominal;
        this.date = date;
        this.currency = currency;
        this.cdx = cdx;
    }

    public CurrencyRateDto(String[] arr) {
        this.nominal = arr[0];
        this.date = arr[1];
        this.currency = arr[2].replace(",",".");
        this.cdx = arr[3];
    }

    public CurrencyRateDto() {

    }

}
