package ru.liga.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.*;



@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    private static final String BOT_NAME = "forecast_currency_bot";//todo понимаю, что так удобнее, но лучше кредсы выносить в переменные окружения
    private static final String BOT_TOKEN = "6295340521:AAFj5wAkbtJ_cJF86WVG7j6oNI9ciel5flw";

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);//todo не обязательно, можно использовать статический метод log, так класс уже помечен аннотацией логирования

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {//todo огромный метод, наверняка тут не все относится к этому сервисы по смыслу, разбей на классы
        Message originalMessage = update.getMessage();
        String inputDataFromConsole = originalMessage.getText();
        logger.debug(inputDataFromConsole);
        SendMessage answer = new SendMessage();
        ////////////////////////////////////////////////////////////////////////////
        //String inputDataFromConsole = InputData.inputFromConsole();
        String outputData = null;
        TimeRange timeRange;
        List<String> inputDataFromConsoleList = Arrays.stream(inputDataFromConsole.split(" ")).toList();

        try {
            CommandStart commandStart = CommandStart.valueOf(inputDataFromConsoleList.get(0).toUpperCase()); // rate

            List<CurrencyType> currencyTypeList = new ArrayList<>();
            for (String currencyTypeStr : inputDataFromConsoleList.get(1).toUpperCase().split(",")) {
                currencyTypeList.add(CurrencyType.valueOf(currencyTypeStr));
            }

            OperationDate operationDate = OperationDate.valueOf(inputDataFromConsoleList.get(2).toUpperCase().substring(1)); // -date, -period
            try {
                timeRange = TimeRange.valueOf(inputDataFromConsoleList.get(3).toUpperCase()); // tomorrow, week, month, anydate
            } catch (IllegalArgumentException e) {
                int p = DateUtils.getDifferenceDays(inputDataFromConsoleList.get(3).toUpperCase());
                timeRange = TimeRange.getAnyDate(p);
            }

            CommandAlgorithm comandAlgorithm = CommandAlgorithm.valueOf(inputDataFromConsoleList.get(4).toUpperCase().substring(1)); // -alg
            Algorithms algorithms = Algorithms.valueOf(inputDataFromConsoleList.get(5).toUpperCase()); // old, lastyear, mist, linreg

            for (CurrencyType currencyType : currencyTypeList) {
                List<CurrencyRateDto> currencyRateDtoList = FileReader.initInfo(currencyType);
                if (!currencyRateDtoList.isEmpty()) {
                    try {
                        ForecastCurrencyService.getForecastResult(currencyRateDtoList, currencyType, operationDate, timeRange, algorithms);
                        //OutputData.printResult(currencyRateDtoList, timeRange.getDays());
                        outputData = OutputData.printResultToTelegram(currencyRateDtoList, timeRange.getDays());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Input error");
                }
            }
            ////////////////////////////////////////////////////////////////////////////
        } catch (IllegalArgumentException e) {
            outputData = "Введена неверная команда";//todo вынеси в константу или enum
            logger.error(String.valueOf(e) + ": " + outputData);// todo используй MessageFormat
        } catch (ArrayIndexOutOfBoundsException e) {
            outputData = "В команде не хватает значений";//todo вынеси в константу или enum
            logger.error(String.valueOf(e) + ": " + outputData);// todo используй MessageFormat
        }

        answer.setChatId(originalMessage.getChatId().toString());
        answer.setText(outputData);
        sendAnswerMessage(answer);
    }

    public void sendAnswerMessage(SendMessage sendMessage) {
        if (sendMessage != null) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error(String.valueOf(e));
            }
        }
    }
}
