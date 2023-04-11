package ru.liga.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import ru.liga.dto.CurrencyRateDto;
import ru.liga.entity.Command;
import ru.liga.enums.*;
import ru.liga.utils.DateUtils;


@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    private static final String BOT_NAME = "forecast_currency_bot";//todo понимаю, что так удобнее, но лучше кредсы выносить в переменные окружения
    private static final String BOT_TOKEN = "6295340521:AAFj5wAkbtJ_cJF86WVG7j6oNI9ciel5flw";

    private static final String TEXT_FOR_ERROR_LOG = "{0}: {1}";
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
        log.debug(inputDataFromConsole);

        MessageFormat messageFormat = new MessageFormat(TEXT_FOR_ERROR_LOG);
        Object[] messageArgs;

        String outputData = null;

        try {
            Command command = CommandService.getCommand(inputDataFromConsole);

            for (CurrencyType currencyType : command.getCurrencyTypeList()) {
                List<CurrencyRateDto> currencyRateDtoList = FileReader.initInfo(currencyType);
                if (!currencyRateDtoList.isEmpty()) {
                    try {
                        ForecastCurrencyService.getForecastResult(currencyRateDtoList, command.getTimeRange(), command.getAlgorithms());

                        if (command.getCommandOutput() != null && command.getOutputType() != null) {
                            if (command.getOutputType().getOutputTypeId() == 1) {
                                outputData = "Сохранен рисунок";
                                sendPhotoMessage(originalMessage, OutputData.getResultAsByteArray(currencyRateDtoList, command.getTimeRange().getDays()));
                            }
                            else {
                                outputData = OutputData.printResultToTelegram(currencyRateDtoList, command.getTimeRange().getDays());
                                sendTextMessage(originalMessage, (outputData != null ? outputData : null));
                            }
                        }
                        else {
                            outputData = OutputData.printResultToTelegram(currencyRateDtoList, command.getTimeRange().getDays());
                            sendTextMessage(originalMessage, (outputData != null ? outputData : null));
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    outputData = OutputErrors.INPUT_ERROR.getErrorMessege();
                    log.error(outputData);
                }
            }
        } catch (IllegalArgumentException e) {
            outputData = OutputErrors.COMMAND_ERROR.getErrorMessege();
            messageArgs = new String[]{String.valueOf(e), outputData};
            log.error(messageFormat.format(messageArgs));
        } catch (ArrayIndexOutOfBoundsException e) {
            outputData = OutputErrors.MISSING_VALUES.getErrorMessege();
            messageArgs = new String[]{String.valueOf(e), outputData};
            log.error(messageFormat.format(messageArgs));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void sendTextMessage(Message originalMessage, String outputData) {
        SendMessage sendText = SendMessage.builder()
                .chatId(originalMessage.getChatId().toString())
                .text(outputData != null ? outputData : null)
                .build();
        try {
            execute(sendText);
        } catch (TelegramApiException e) {
            log.error(String.valueOf(e));
        }
    }

    public void sendPhotoMessage(Message originalMessage, ByteArrayOutputStream byteArrayOutputStream) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(originalMessage.getChatId().toString())
                .photo(new InputFile(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), "graph"))
                .build();
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(String.valueOf(e));
        }
    }
}
