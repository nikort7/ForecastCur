package ru.liga;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.InputStart;
import ru.liga.enums.InputType;
import ru.liga.service.ForecastCurrencyService;
import ru.liga.service.InputData;
import ru.liga.service.OutputData;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class App {
    private static ForecastCurrencyService forecastCurrencyService;

    public static void main( String[] args ) throws IOException, ParseException {
        String inputDataFromConsole = InputData.inputFromConsole();
        if (InputStart.checkInputStart(inputDataFromConsole)) {
            List<CurrencyRateDto> currencyRateDtoList = forecastCurrencyService.initInfo(inputDataFromConsole);
            Integer inputType = InputType.getInputTypeFromConsole(inputDataFromConsole);
            if (!currencyRateDtoList.isEmpty()) {
                forecastCurrencyService.getForecastResult(currencyRateDtoList, inputType);
                OutputData.printResult(currencyRateDtoList, inputType);
            } else {
                System.out.println("Input error");
            }
        } else {
            System.out.println("Input start error");
        }
    }
}
