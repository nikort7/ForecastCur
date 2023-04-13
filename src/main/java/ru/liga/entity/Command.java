package ru.liga.entity;//todo почему entity?

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.liga.enums.*;

import java.util.List;

@Getter
@Setter
public class Command {//todo этот класс очень загружен и не отражает своей реальной сути, лучше не делать структуру с набором сервисов + они не инициализируются и можно словить nullPointerException
    private CommandStart commandStart; // rate//todo комменты в коде - это не очень хорошо
    private List<CurrencyType> currencyTypeList;
    private OperationDate operationDate; // -date, -period
    private TimeRange timeRange;
    private CommandAlgorithm comandAlgorithm; // -alg
    private Algorithms algorithms; // old, last_year, mist, lin_reg
    private CommandOutput commandOutput;
    private OutputType outputType;

    public Command() {//todo лучше использовать @NoArgsConstructor

    }
}
