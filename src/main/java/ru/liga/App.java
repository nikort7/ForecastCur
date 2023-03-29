package ru.liga;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.CurrencyType;
import ru.liga.enums.InputStart;
import ru.liga.enums.InputType;
import ru.liga.service.ForecastCurrencyService;
import ru.liga.service.InitInformation;
import ru.liga.service.InputData;
import ru.liga.service.OutputData;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main( String[] args ) throws ParseException {
        String inputDataFromConsole = InputData.inputFromConsole();
        List<String> inputDataFromConsoleList = Arrays.stream(inputDataFromConsole.split(" ")).toList();
        InputStart inputStart = InputStart.valueOf(inputDataFromConsoleList.get(0).toUpperCase());
        CurrencyType currencyType = CurrencyType.valueOf(inputDataFromConsoleList.get(1).toUpperCase());
        InputType inputType = InputType.valueOf(inputDataFromConsoleList.get(2).toUpperCase());

        List<CurrencyRateDto> currencyRateDtoList = InitInformation.initInfo(inputDataFromConsole);
        if (!currencyRateDtoList.isEmpty()) {
            ForecastCurrencyService.getForecastResult(currencyRateDtoList, inputType.getDays());
            OutputData.printResult(currencyRateDtoList, inputType.getDays());
        } else {
            System.out.println("Input error");
        }

    }
}
