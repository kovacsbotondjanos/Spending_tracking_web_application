package com.example.monthlySpendingsBackend.dataBaseHandler;

import com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseInterActionHandlers.DatabaseHandler;

import java.sql.SQLException;

public class DataBaseWriteHandler {
    DataBaseWriteRecord dbr;
    public DataBaseWriteHandler(DataBaseWriteRecord dbr){
        //TODO: parse values and check for invalid data here
        this.dbr = dbr;
    }
    public void insertIntoDataBase(){
        try{
            (new DatabaseHandler(dbr.dataBaseName())).insertIntoDataBaseByGivenDay(
                    Integer.parseInt(dbr.year()), Integer.parseInt(dbr.month()), Integer.parseInt(dbr.day()), dbr.amount());
        }
        catch(SQLException e){
            System.err.printf(e.getMessage());
        }
        catch(Exception e){
            System.err.printf(e.getMessage());
        }
    }
}
