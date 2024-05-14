package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.contexts.ApplicationContextProvider;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseEvent.Event;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.expenseTables.outgoing.OutgoingService;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.CustomUser;
import com.example.monthlySpendingsBackend.dataBaseHandler.models.users.UserDetailService;
import org.springframework.context.ApplicationContext;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Date;

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
        this.amount = dbr.amount();

        this.date = Date.from(dbr.date().atStartOfDay(ZoneId.systemDefault()).toInstant());

        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        //outgoing service
        this.outgoingService = context.getBean(OutgoingService.class);
        //user detail service
        UserDetailService userDetailService = context.getBean(UserDetailService.class);
        this.user = userDetailService.getUserById(userId);
    }

    private void interactWithDataBase(Event dataBaseInterActionEvent) throws IllegalArgumentException{
        switch(dataBaseInterActionEvent){
            case INSERT -> outgoingService.insertExpenseRecord(date, amount, user, dataBaseName);
            case DELETE -> outgoingService.deleteExpenseRecord(date, amount, user, dataBaseName);
        }
    }
}
