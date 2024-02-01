package com.example.monthlySpendingsBackend.dataBaseHandler;

public record DataBaseDeletionRecord(
        String year,
        String month,
        String day,
        String type,
        int amount) {}
