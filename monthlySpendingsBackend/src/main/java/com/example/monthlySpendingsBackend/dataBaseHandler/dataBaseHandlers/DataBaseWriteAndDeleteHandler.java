package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseEvent.Event;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.DatabaseHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;
import com.example.monthlySpendingsBackend.envVariableHandler.EnvVariableHandlerSingleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseWriteAndDeleteHandler {
    private final String dataBaseName;
    private final Long userId;
    private final int year;
    private final int month;
    private final int day;
    private final int amount;
    private final Connection connection;

    public static void DeleteFromDataBase(InteractionRecord dbDelete, Long userId) throws SQLException, Exception{
        (new DataBaseWriteAndDeleteHandler(dbDelete, userId)).interactWithDataBase(Event.DELETE);
    }

    public static void InsertIntoDataBase(InteractionRecord dbWrite, Long userId) throws SQLException, Exception{
        (new DataBaseWriteAndDeleteHandler(dbWrite, userId)).interactWithDataBase(Event.INSERT);
    }

    private DataBaseWriteAndDeleteHandler(InteractionRecord dbr, Long userId) throws SQLException{
        //TODO: parse values and check for invalid data here
        this.userId = userId;

        Properties connectionProps = new Properties();
        connectionProps.put("user", EnvVariableHandlerSingleton.getUsername());
        connectionProps.put("password", EnvVariableHandlerSingleton.getPassword());
        connectionProps.put("serverTimezone", EnvVariableHandlerSingleton.getTimeZone());
        connectionProps.put("sessionTimezone", EnvVariableHandlerSingleton.getTimeZone());
        String dbURL = EnvVariableHandlerSingleton.getDataBaseURL();
        connection = DriverManager.getConnection(dbURL, connectionProps);

        dataBaseName = dbr.dataBaseName();
        year = Integer.parseInt(dbr.year());
        month = Integer.parseInt(dbr.month());
        day = Integer.parseInt(dbr.day());
        amount = dbr.amount();
    }

    private void interactWithDataBase(Event dataBaseInterActionEvent) throws SQLException, Exception{
        switch(dataBaseInterActionEvent){
            case INSERT -> (new DatabaseHandler(dataBaseName, connection, userId)).insertIntoDataBaseByGivenDay(
                    year, month, day, amount);
            case DELETE -> (new DatabaseHandler(dataBaseName, connection, userId)).deleteFromDataBaseByGivenDayAndAmount(
                    year, month, day, amount);
        }
    }
}
