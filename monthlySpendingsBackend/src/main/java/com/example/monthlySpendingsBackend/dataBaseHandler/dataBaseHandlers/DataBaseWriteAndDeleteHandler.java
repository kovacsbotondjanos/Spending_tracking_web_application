package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseHandlers;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseEvent.Event;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.DatabaseHandler;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations.InteractionRecord;

import java.sql.SQLException;

public class DataBaseWriteAndDeleteHandler {
    private final String dataBaseName;
    private final int year;
    private final int month;
    private final int day;
    private final int amount;

    public static void DeleteFromDataBase(InteractionRecord dbDelete){
        (new DataBaseWriteAndDeleteHandler(dbDelete)).interactWithDataBase(Event.DELETE);
    }

    public static void InsertIntoDataBase(InteractionRecord dbWrite){
        (new DataBaseWriteAndDeleteHandler(dbWrite)).interactWithDataBase(Event.INSERT);
    }

    private DataBaseWriteAndDeleteHandler(InteractionRecord dbr){
        //TODO: parse values and check for invalid data here
        dataBaseName = dbr.dataBaseName();
        year = Integer.parseInt(dbr.year());
        month = Integer.parseInt(dbr.month());
        day = Integer.parseInt(dbr.day());
        amount = dbr.amount();
    }

    private void interactWithDataBase(Event dataBaseInterActionEvent){
        try{
            switch(dataBaseInterActionEvent){
                case INSERT -> (new DatabaseHandler(dataBaseName)).insertIntoDataBaseByGivenDay(
                        year, month, day, amount);
                case DELETE -> (new DatabaseHandler(dataBaseName)).deleteFromDataBaseByGivenDayAndAmount(
                        year, month, day, amount);
            }
        }
        catch(SQLException e){
            System.err.printf(e.getMessage());
        }
        catch(Exception e){
            System.err.printf(e.getMessage());
        }
    }
}
