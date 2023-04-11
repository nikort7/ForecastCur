package ru.liga.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.liga.enums.*;

import java.util.List;

@Getter
@Setter
public class Command {
    private CommandStart commandStart; // rate
    private List<CurrencyType> currencyTypeList;
    private OperationDate operationDate; // -date, -period
    private TimeRange timeRange;
    private CommandAlgorithm comandAlgorithm; // -alg
    private Algorithms algorithms; // old, last_year, mist, lin_reg
    private CommandOutput commandOutput;
    private OutputType outputType;

    public Command() {

    }
}
