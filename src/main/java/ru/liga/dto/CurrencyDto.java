package ru.liga.dto;

public class CurrencyDto {
    private String nominal;
    private String data;
    private String curs;
    private String cdx;

    public CurrencyDto(String nominal, String data, String curs, String cdx) {
        this.nominal = nominal;
        this.data = data;
        this.curs = curs;
        this.cdx = cdx;
    }

    public CurrencyDto(String[] arr) {
        this.nominal = arr[0];
        this.data = arr[1];
        this.curs = arr[2].replace(",",".");
        this.cdx = arr[3];
    }

    public CurrencyDto() {

    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCurs() {
        return curs;
    }

    public void setCurs(String curs) {
        this.curs = curs;
    }

    public String getCdx() {
        return cdx;
    }

    public void setCdx(String cdx) {
        this.cdx = cdx;
    }

}
