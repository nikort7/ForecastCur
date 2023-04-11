package ru.liga.service;

import ru.liga.entity.Command;
import ru.liga.enums.*;
import ru.liga.utils.DateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandService {
    private static Command command;

    public static Command getCommand(String inputDataFromConsole) {
        command = new Command();
        List<String> inputDataFromConsoleList = Arrays.stream(inputDataFromConsole.split(" ")).toList();

        command.setCommandStart(CommandStart.valueOf(inputDataFromConsoleList.get(0).toUpperCase())); // rate

        List<CurrencyType> tempCurrencyTypeList = new ArrayList<>();
        for (String currencyTypeStr : inputDataFromConsoleList.get(1).toUpperCase().split(",")) {
            tempCurrencyTypeList.add(CurrencyType.valueOf(currencyTypeStr));
        }
        command.setCurrencyTypeList(tempCurrencyTypeList);

        command.setOperationDate(OperationDate.valueOf(inputDataFromConsoleList.get(2).toUpperCase().substring(1))); // -date, -period
        try {
            command.setTimeRange(TimeRange.valueOf(inputDataFromConsoleList.get(3).toUpperCase())); // tomorrow, week, month, anydate
        } catch (IllegalArgumentException e) {
            int p = DateUtils.getDifferenceDays(inputDataFromConsoleList.get(3).toUpperCase());
            command.setTimeRange(TimeRange.instalAnyDate(p));
        }

        command.setComandAlgorithm(CommandAlgorithm.valueOf(inputDataFromConsoleList.get(4).toUpperCase().substring(1))); // -alg
        command.setAlgorithms(Algorithms.valueOf(inputDataFromConsoleList.get(5).toUpperCase())); // old, last_year, mist, lin_reg

        if (inputDataFromConsoleList.size() == 8) {
            command.setCommandOutput(CommandOutput.valueOf(inputDataFromConsoleList.get(6).toUpperCase().substring(1))); // -output
            command.setOutputType(OutputType.valueOf(inputDataFromConsoleList.get(7).toUpperCase())); // list, graph
        }
        return command;
    }
}
