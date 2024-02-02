package com.example.monthlySpendingsBackend.dataBaseHandler;

public record DataBaseInteractionRecord(
        String year,
        String month,
        String day,
        String dataBaseName,
        int amount) { }
