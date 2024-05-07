package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.contexts.ApplicationContextProvider;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseEvent.Event;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.DatabaseHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.bankBalance.BankBalanceService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.Outgoing;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.OutgoingService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.UserDetailService;
import com.example.monthlySpendingsBackend.envVariableHandler.EnvVariableHandlerSingleton;
import org.springframework.context.ApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
//TODO: all the methods from DatabaseHandler and BankBalanceHandler should be here
public class DataBaseWriteAndDeleteHandler {
    private final String dataBaseName;
    private final int amount;
    private final Date date;
    private final OutgoingService outgoingService;
    private final CustomUser user;

    public static void DeleteFromDataBase(InteractionRecord dbDelete, Long userId) throws IllegalArgumentException {
        (new DataBaseWriteAndDeleteHandler(dbDelete, userId)).interactWithDataBase(Event.DELETE);
    }

    public static void InsertIntoDataBase(InteractionRecord dbWrite, Long userId) throws DateTimeException {
        (new DataBaseWriteAndDeleteHandler(dbWrite, userId)).interactWithDataBase(Event.INSERT);
    }

    private DataBaseWriteAndDeleteHandler(InteractionRecord dbr, Long userId) throws IllegalArgumentException, DateTimeException {
        this.dataBaseName = dbr.dataBaseName();
        int year = Integer.parseInt(dbr.year());
        int month = Integer.parseInt(dbr.month());
        int day = Integer.parseInt(dbr.day());

        LocalDate localDate = LocalDate.of(year, month, day);
        this.date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        this.amount = dbr.amount();
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        //outgoing service
        this.outgoingService = context.getBean(OutgoingService.class);
        //user detail service
        UserDetailService userDetailService = context.getBean(UserDetailService.class);
        this.user = userDetailService.getUserById(userId);
    }

    private void interactWithDataBase(Event dataBaseInterActionEvent) throws IllegalArgumentException{
        switch(dataBaseInterActionEvent){
//            case INSERT -> (new DatabaseHandler(dataBaseName, connection, userId)).insertIntoDataBaseByGivenDay(
//                    year, month, day, amount);
            case INSERT -> outgoingService.insertExpenseRecord(date, amount, user, dataBaseName);
//            case DELETE -> (new DatabaseHandler(dataBaseName, connection, userId)).deleteFromDataBaseByGivenDayAndAmount(
//                    year, month, day, amount);
            case DELETE -> outgoingService.deleteExpenseRecord(date, amount, user, dataBaseName);
        }
    }
}
