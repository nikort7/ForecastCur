package ru.liga.dto;

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

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCdx() {
        return cdx;
    }

    public void setCdx(String cdx) {
        this.cdx = cdx;
    }

}
