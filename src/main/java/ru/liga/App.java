package ru.liga;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.CurrencyType;
import ru.liga.enums.CommandStart;
import ru.liga.enums.TimeRange;
import ru.liga.service.ForecastCurrencyService;
import ru.liga.service.FileReader;
import ru.liga.service.InputData;
import ru.liga.service.OutputData;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main( String[] args ) throws ParseException {
        String inputDataFromConsole = InputData.inputFromConsole();
        List<String> inputDataFromConsoleList = Arrays.stream(inputDataFromConsole.split(" ")).toList();
        CommandStart commandStart = CommandStart.valueOf(inputDataFromConsoleList.get(0).toUpperCase());
        CurrencyType currencyType = CurrencyType.valueOf(inputDataFromConsoleList.get(1).toUpperCase());
        TimeRange timeRange = TimeRange.valueOf(inputDataFromConsoleList.get(2).toUpperCase());

        List<CurrencyRateDto> currencyRateDtoList = FileReader.initInfo(currencyType);
        if (!currencyRateDtoList.isEmpty()) {
            ForecastCurrencyService.getForecastResult(currencyRateDtoList, timeRange.getDays());
            OutputData.printResult(currencyRateDtoList, timeRange.getDays());
        } else {
            System.out.println("Input error");
        }

    }
}
