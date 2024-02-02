package com.example.monthlySpendingsBackend.dataBaseHandler.dataBaseRecordRepresentations;

public record InteractionRecord(
        String year,
        String month,
        String day,
        String dataBaseName,
        int amount) { }
