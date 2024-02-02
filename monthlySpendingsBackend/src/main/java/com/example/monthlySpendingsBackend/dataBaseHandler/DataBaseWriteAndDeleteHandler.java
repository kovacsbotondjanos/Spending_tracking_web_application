package com.example.monthlySpendingsBackend.dataBaseHandler;

import com.example.monthlySpendingsBackend.DataBaseEvent.Event;
import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.DatabaseHandler;

import java.sql.SQLException;

public class DataBaseWriteAndDeleteHandler {
    String dataBaseName;
    private int year;
    private int month;
    private int day;
    private int amount;
    public DataBaseWriteAndDeleteHandler(DataBaseInteractionRecord dbr){
        //TODO: parse values and check for invalid data here
        dataBaseName = dbr.dataBaseName();
        year = Integer.parseInt(dbr.year());
        month = Integer.parseInt(dbr.month());
        day = Integer.parseInt(dbr.day());
        amount = dbr.amount();
    }

    public void interactWithDataBase(Event dataBaseInterActionEvent){
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
