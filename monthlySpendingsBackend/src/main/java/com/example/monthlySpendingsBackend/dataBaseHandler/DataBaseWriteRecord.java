package com.example.monthlySpendingsBackend.dataBaseHandler;

public record DataBaseWriteRecord(
        String year,
        String month,
        String day,
        String dataBaseName,
        int amount) { }
